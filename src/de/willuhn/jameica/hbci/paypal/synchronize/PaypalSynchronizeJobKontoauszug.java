/**********************************************************************
 *
 * Copyright (c) 2022 Olaf Willuhn
 * All rights reserved.
 * 
 * This software is copyrighted work licensed under the terms of the
 * Jameica License.  Please consult the file "LICENSE" for details. 
 *
 **********************************************************************/

package de.willuhn.jameica.hbci.paypal.synchronize;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.hbci.HBCI;
import de.willuhn.jameica.hbci.HBCIProperties;
import de.willuhn.jameica.hbci.SynchronizeOptions;
import de.willuhn.jameica.hbci.messaging.ImportMessage;
import de.willuhn.jameica.hbci.messaging.SaldoMessage;
import de.willuhn.jameica.hbci.paypal.Plugin;
import de.willuhn.jameica.hbci.paypal.domain.ApiAuth;
import de.willuhn.jameica.hbci.paypal.domain.BalanceDetail;
import de.willuhn.jameica.hbci.paypal.domain.BalanceResult;
import de.willuhn.jameica.hbci.paypal.domain.CartItemDetail;
import de.willuhn.jameica.hbci.paypal.domain.Money;
import de.willuhn.jameica.hbci.paypal.domain.PayerInfo;
import de.willuhn.jameica.hbci.paypal.domain.PayerName;
import de.willuhn.jameica.hbci.paypal.domain.TransactionDetails;
import de.willuhn.jameica.hbci.paypal.domain.TransactionInfo;
import de.willuhn.jameica.hbci.paypal.transport.ApiException;
import de.willuhn.jameica.hbci.paypal.transport.TransportService;
import de.willuhn.jameica.hbci.rmi.HibiscusAddress;
import de.willuhn.jameica.hbci.rmi.Konto;
import de.willuhn.jameica.hbci.rmi.Protokoll;
import de.willuhn.jameica.hbci.rmi.Umsatz;
import de.willuhn.jameica.hbci.server.VerwendungszweckUtil;
import de.willuhn.jameica.hbci.synchronize.jobs.SynchronizeJobKontoauszug;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.OperationCanceledException;
import de.willuhn.jameica.system.Settings;
import de.willuhn.jameica.util.DateUtil;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.I18N;

/**
 * Implementierung des Jobs fuer den Abruf der Kontoauszuege per Paypal.
 */
public class PaypalSynchronizeJobKontoauszug extends SynchronizeJobKontoauszug implements PaypalSynchronizeJob
{
  private final static I18N i18n = Application.getPluginLoader().getPlugin(Plugin.class).getResources().getI18N();
  private final static Settings settings = Application.getPluginLoader().getPlugin(HBCI.class).getResources().getSettings();
  
  @Resource private TransportService transportService;

  /**
   * @see de.willuhn.jameica.hbci.paypal.synchronize.PaypalSynchronizeJob#exeute()
   */
  @Override
  public void exeute() throws ApplicationException
  {
    try
    {
      final Konto k = (Konto) this.getContext(CTX_ENTITY);
      Boolean forceSaldo   = (Boolean) this.getContext(CTX_FORCE_SALDO);
      Boolean forceUmsatz  = (Boolean) this.getContext(CTX_FORCE_UMSATZ);
      
      SynchronizeOptions o = new SynchronizeOptions(k);
      boolean syncSaldo  = (o.getSyncSaldo() || (forceSaldo != null && forceSaldo.booleanValue()));
      boolean syncUmsatz = (o.getSyncKontoauszuege() || (forceUmsatz != null && forceUmsatz.booleanValue()));
      
      if (!syncSaldo && !syncUmsatz)
      {
        Logger.info("no synchronize options activated");
        return;
      }

      final Date startDate = this.getStartDate(k);
      
      final ApiAuth auth = this.transportService.login(k);

      if (syncUmsatz)
      {
        final List<TransactionDetails> result = this.transportService.getTransactions(auth,startDate);

      	int created = 0;
      	int skipped = 0;
      
      	if (!result.isEmpty())
      	{
      	  final Date mergeWindow = this.getMergeWindow(startDate, result);
      	  final DBIterator existing = k.getUmsaetze(mergeWindow, null);
      
      	  Logger.info("applying entries");
      
      	  for (TransactionDetails t : result)
      	  {
      	    final List<Umsatz> umsaetze = convert(t);
      	    if (umsaetze == null || umsaetze.isEmpty())
      	      continue;
      
      	    for (Umsatz umsatz : umsaetze)
      	    {
      	      umsatz.setKonto(k);
      
      	      boolean found = false;
      
      	      /////////////////////////////////////////
      	      // Checken, ob wir den Umsatz schon haben
      	      existing.begin();
      	      for (int i = 0; i < existing.size(); i++)
      	      {
            		GenericObject dbObject = existing.next();
            		found = dbObject.equals(umsatz);
            		if (found)
            		{
            		  skipped++; // Haben wir schon
            		  break;
            		}
      	      }
      	      /////////////////////////////////////////
      
      	      // Umsatz neu anlegen
      	      if (!found)
      	      {
            		try
            		{
            		  umsatz.store(); // den Umsatz haben wir noch nicht, speichern!
            		  Application.getMessagingFactory().sendMessage(new ImportMessage(umsatz));
            		  created++;
            		}
            		catch (Exception e2)
            		{
            		  Application.getMessagingFactory().sendMessage(new StatusBarMessage(i18n.tr("Nicht alle empfangenen Umsätze konnten gespeichert werden. Bitte prüfen Sie das System-Protokoll"),StatusBarMessage.TYPE_ERROR));
            		  Logger.error("error while adding umsatz, skipping this one", e2);
            		}
	            }
	          }
	        }
        }
	      Logger.info("done. new entries: " + created + ", skipped entries (already in database): " + skipped);
	      k.addToProtokoll(i18n.tr("Umsätze abgerufen"), Protokoll.TYP_SUCCESS);
      }
      
      if (syncSaldo)
      {
        // Jetzt noch den Saldo abrufen
        this.applySaldo(k,this.transportService.getBalances(auth));
      }
    }
    catch (ApiException ae)
    {
      String detail = ae.message;
      if (StringUtils.trimToNull(detail) == null)
        detail = ae.error + " - " + ae.error_description;
      throw new ApplicationException(i18n.tr("Abruf fehlgeschlagen: " + detail));
    }
    catch (ApplicationException ae2)
    {
      throw ae2;
    }
    catch (OperationCanceledException oce)
    {
      throw oce;
    }
    catch (Exception e)
    {
      Logger.error("error",e);
      throw new ApplicationException(i18n.tr("Fehler beim Abrufen der Kontoauszüge"),e);
    }
  }
  
  /**
   * Übernimmt den Saldo.
   * @param k das Konto.
   * @param br der Saldo.
   * @throws ApplicationException wenn das Übernehmen fehlschlägt.
   * @throws RemoteException wenn das Übernehmen fehlschlägt.
   */
  private void applySaldo(Konto k, BalanceResult br) throws ApplicationException, RemoteException
  {
    if (br == null || br.balances == null || br.balances.isEmpty())
    {
      Logger.warn("no balances received");
      return;
    }

    BalanceDetail b = null;
    for (BalanceDetail d:br.balances)
    {
      if (Objects.equals(HBCIProperties.CURRENCY_DEFAULT_DE,d.currency))
      {
        b = d;
        break;
      }
      Logger.info("ignoring balance - wrong currency " + d.currency);
    }
    
    if (b == null)
    {
      Logger.warn("no balance found for " + HBCIProperties.CURRENCY_DEFAULT_DE);
      return;
    }

    boolean set = false;
    
    if (b.total_balance != null)
    {
      k.setSaldo(b.total_balance.doubleValue());
      set = true;
    }
    if (b.available_balance != null)
    {
      k.setSaldoAvailable(b.available_balance.doubleValue());
      set = true;
    }
    
    if (!set)
    {
      Logger.warn("no balance received found for " + HBCIProperties.CURRENCY_DEFAULT_DE);
      return;
    }
    
    k.store();
    k.addToProtokoll(i18n.tr("Saldo abgerufen"),Protokoll.TYP_SUCCESS);
    Application.getMessagingFactory().sendMessage(new SaldoMessage(k));
  }
  
  /**
   * Liefert das Startdatum fuer den Abgleich mit den existierenden Umsaetzen.
   * @param startDate das von uns gesendete Startdatum.
   * @param tr die Umsatzbuchungen.
   * @return das Startdatum. Kann NULL sein.
   */
  private Date getMergeWindow(final Date startDate, final List<TransactionDetails> tr)
  {
    // Das Datum der ältesten empfangenen Transaktion
    Date d = null;
    String basedOn = null;

    if (tr != null && !tr.isEmpty())
    {
      for (TransactionDetails td:tr)
      {
        final TransactionInfo ti = td.transaction_info;
        if (ti == null || ti.transaction_updated_date == null)
          continue;

        Date nd = ti.transaction_updated_date;
        if (d == null || nd.before(d))
        {
          d = nd;
          basedOn = "received data";
        }
      }
    }

    // Wir haben keine Umsätze erhalten oder kein Datum aus den Umsätzen ermitteln können
    if (d == null && startDate != null)
    {
      Calendar cal = Calendar.getInstance();
      cal.setTime(startDate);
      cal.add(Calendar.DATE,settings.getInt("umsatz.mergewindow.offset",-30));
      d = cal.getTime();
      basedOn = "last sync";
    }
    
    if (d == null)
      Logger.info("merge window: not set");
    else
      Logger.info("merge window: " + d + " - now (based on " + basedOn + ")");
    
    return d;
  }
  
  /**
   * Konvertiert die Buchung in einen Hibiscus-Datensatz.
   * @param t die Buchung.
   * @return der Hibiscus-Datensatz.
   * @throws Exception
   */
  private List<Umsatz> convert(TransactionDetails t) throws Exception
  {
    final TransactionInfo ti = t.transaction_info;
    if (ti == null)
    {
      Logger.warn("received transaction-details w/o transaction-info - skipping");
      return null;
    }
    
    final List<Umsatz> result = new ArrayList<>();

    final String status = ti.transaction_status;
    final String ec = ti.transaction_event_code;
    String feeZweck = i18n.tr("Gebühren für Transaktion {0}",ti.transaction_id);
    
    // Wir übernehmen Transaktionen nur, wenn sie den Status "S" oder "P" haben oder gar keinen
    if (status != null && !Objects.equals("S",status) && !Objects.equals("P",status))
    {
      Logger.info("skipping denied/reversed transaction id " + ti.transaction_id + " (status: " + status + ")");
      return null;
    }
    
    Umsatz umsatz = (Umsatz) de.willuhn.jameica.hbci.Settings.getDBService().createObject(Umsatz.class,null);
    result.add(umsatz);
    umsatz.setTransactionId(ti.transaction_id);
    umsatz.setEndToEndId(ti.transaction_id);
    umsatz.setArt(clean(ec));
    umsatz.setCustomerRef(t.payer_info != null ? t.payer_info.account_id : null);
    umsatz.setGvCode(status);
    
    Money saldo = ti.ending_balance;
    if (saldo != null)
      umsatz.setSaldo(saldo.doubleValue());

    Money value = ti.transaction_amount;
    if (value != null)
      umsatz.setBetrag(value.doubleValue());
    
    umsatz.setDatum(ti.transaction_updated_date);
    umsatz.setValuta(ti.transaction_updated_date);

    final List<String> usages = new ArrayList<>();
    if (StringUtils.trimToNull(ti.transaction_subject) != null)
      usages.add(ti.transaction_subject);
    if (StringUtils.trimToNull(ti.transaction_note) != null && (StringUtils.trimToNull(ti.transaction_subject) == null || !ti.transaction_note.equals(ti.transaction_subject)))
      usages.add(ti.transaction_note);
    if (t.cart_info != null && t.cart_info.item_details != null && !t.cart_info.item_details.isEmpty())
    {
      for (CartItemDetail cd:t.cart_info.item_details)
      {
        if (StringUtils.trimToNull(cd.item_name) != null)
          usages.add(cd.item_name);
      }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Gegenkonto
    final PayerInfo pi = t.payer_info;
    HibiscusAddress e = (HibiscusAddress) de.willuhn.jameica.hbci.Settings.getDBService().createObject(HibiscusAddress.class,null);
    
    final String email = pi != null ? pi.email_address : null;
    if (email != null)
      e.setIban(email.substring(0, Math.min(40, email.length())));
    
    final PayerName pn = pi != null ? pi.payer_name : null;
    if (pn != null)
    {
      String name = StringUtils.trimToNull(pn.alternate_full_name);
      
      if (name == null)
      {
        StringBuilder sb = new StringBuilder();
        if (pn.given_name != null)
          sb.append(pn.given_name);
        
        if (pn.surname != null)
        {
          if (sb.length() > 0)
            sb.append(" ");
          sb.append(pn.surname);
        }
        
        name = sb.toString();
      }
      
      if (name != null && name.length() > HBCIProperties.HBCI_TRANSFER_NAME_MAXLENGTH)
        name = StringUtils.trimToEmpty(name.substring(0,HBCIProperties.HBCI_TRANSFER_NAME_MAXLENGTH));
      e.setName(name);
    }
    
    if (ec != null)
    {
      if (ec.startsWith("T04"))
      {
        e.setName(i18n.tr("Bankkonto"));
        
        if (ec.equals("T0400"))
        {
          usages.clear();
          usages.add(i18n.tr("Abbuchung auf Bankkonto"));
        }
        else if (ec.equals("T0401"))
        {
          usages.clear();
          usages.add(i18n.tr("Automatische Abbuchung auf Bankkonto"));
        }
      }
      else if (ec.startsWith("T03"))
      {
        e.setName(i18n.tr("Bankkonto"));
        
        if (ec.equals("T0300"))
        {
          usages.clear();
          usages.add(i18n.tr("Einzahlung vom Bankkonto"));
        }
      }
      else if (ec.startsWith("T11"))
      {
        if (ec.equals("T1107"))
        {
          if (StringUtils.trimToNull(ti.transaction_subject) != null)
            umsatz.setKommentar(usages.remove(0));
          
          if (!usages.isEmpty())
            usages.add(0, i18n.tr("Rückzahlung ") + usages.remove(0));
          else 
            usages.add(0, i18n.tr("Rückzahlung "));
          
          feeZweck = i18n.tr("Widerrufene ") + feeZweck;
        }
      }
    }
    
    
    umsatz.setGegenkonto(e);
    //
    ////////////////////////////////////////////////////////////////////////////
    
    if (!usages.isEmpty())
      VerwendungszweckUtil.applyCamt(umsatz,usages);
    
    
    // Die Gebühren, falls vorhanden
    if (ti.fee_amount != null)
    {
      e = (HibiscusAddress) de.willuhn.jameica.hbci.Settings.getDBService().createObject(HibiscusAddress.class,null);
      e.setName("Paypal");
      
      Umsatz umsatz2 = (Umsatz) de.willuhn.jameica.hbci.Settings.getDBService().createObject(Umsatz.class,null);
      result.add(umsatz2);
      umsatz2.setTransactionId(ti.transaction_id + "-fee");
      umsatz2.setCustomerRef(t.payer_info != null ? t.payer_info.account_id : null);
      umsatz2.setBetrag(ti.fee_amount.doubleValue());
      umsatz2.setZweck(feeZweck);
      umsatz2.setGegenkonto(e);
      
      Money saldo2 = ti.ending_balance;
      if (saldo2 != null)
        umsatz2.setSaldo(saldo2.doubleValue() + ti.fee_amount.doubleValue()); // Fee ist schon eine negative Zahl, daher sinkt der Saldo

      umsatz2.setDatum(ti.transaction_updated_date);
      umsatz2.setValuta(ti.transaction_updated_date);
    }

    return result;
  }
  
  /**
   * Entfernt Zeichen, die in den Strings nicht enthalten sein sollten.
   * Typischerweise Zeilenumbrueche.
   * @param s der String.
   * @return der bereinigte String.
   */
  private static String clean(String s)
  {
    return HBCIProperties.replace(s,HBCIProperties.TEXT_REPLACEMENTS_UMSATZ);
  }

  /**
   * Liefert das zu verwendende Saldo-Datum.
   * @param k das Konto.
   * @return das Saldo-Datum.
   */
  private Date getStartDate(Konto k) throws RemoteException
  {
    Date start = k.getSaldoDatum();
    if (start != null)
    {
      // Checken, ob das Datum vielleicht in der Zukunft liegt. Das ist nicht zulaessig
      if (start.after(new Date()))
      {
        Logger.warn("future start date " + start + " given. this is not allowed");
        start = null;
      }
    }
    
    if (start == null)
    {
      // Wir nehmen die letzten 360 Tage
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DATE,-360);
      start = cal.getTime();
    }
    else
    {
      // Umsaetze vom Vortrag erneut abrufen
      start = new Date(start.getTime() - 24 * 60 * 60 * 1000L);
    }
    
    start = DateUtil.startOfDay(start);
    Logger.info("startdate: " + HBCI.LONGDATEFORMAT.format(start));
    return start;
  }
  
}
