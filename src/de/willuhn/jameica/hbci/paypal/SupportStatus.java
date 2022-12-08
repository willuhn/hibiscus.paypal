/**********************************************************************
 *
 * Copyright (c) 2022 Olaf Willuhn
 * All rights reserved.
 * 
 * This software is copyrighted work licensed under the terms of the
 * Jameica License.  Please consult the file "LICENSE" for details. 
 *
 **********************************************************************/

package de.willuhn.jameica.hbci.paypal;

import java.util.Objects;

import org.apache.commons.lang.StringUtils;

import de.willuhn.jameica.hbci.paypal.synchronize.PaypalSynchronizeBackend;
import de.willuhn.jameica.hbci.rmi.Konto;
import de.willuhn.logging.Logger;

/**
 * Fuehrt verschiedenste Pruefungen durch, um herauszufinden, inwiefern ein Konto alle Anforderungen fuer die
 * Nutzung via TransferWise erfuellt.
 */
public class SupportStatus
{
  private Konto konto = null;

  /**
   * ct.
   * @param k das Konto.
   */
  SupportStatus(Konto k)
  {
    this.konto = k;
  }
  
  /**
   * Liefert das zugehoerige Konto.
   * @return das zugehoerige Konto.
   */
  public Konto getKonto()
  {
    return this.konto;
  }
  
  /**
   * Prueft, ob die korrekte BIC hinterlegt ist.
   * @return true, wenn die korrekte BIC hinterlegt ist.
   */
  public boolean checkBic()
  {
    try
    {
      String bic = StringUtils.trimToNull(this.konto.getBic());
      if (bic == null)
        return false;
      
      bic = bic.replace(" ","");
      return bic.equalsIgnoreCase(Plugin.BIC_PAYPAL);
    }
    catch (Exception e)
    {
      Logger.error("unable to check bic for account",e);
      return false;
    }
  }
  
  /**
   * Prueft, ob das korrekte Backend konfiguriert ist.
   * @return true, wenn das korrekte Backend konfiguriert ist.
   */
  public boolean checkBackend()
  {
    try
    {
      final String backend = StringUtils.trimToNull(this.konto.getBackendClass());
      return Objects.equals(backend,PaypalSynchronizeBackend.class.getName());
    }
    catch (Exception e)
    {
      Logger.error("unable to check backend for account",e);
      return false;
    }
  }
  
  /**
   * Prueft, ob die API Client-ID vorhanden ist.
   * @return true, wenn die API Client-ID hinterlegt ist.
   */
  public boolean checkApiClientId()
  {
    try
    {
      return StringUtils.trimToNull(this.konto.getMeta(Plugin.META_PARAM_API_CLIENTID,null)) != null;
    }
    catch (Exception e)
    {
      Logger.error("unable to check api client-id for account",e);
      return false;
    }
  }
  
  /**
   * Prueft, ob das API Secret vorhanden ist.
   * @return true, wenn das API Secret hinterlegt ist.
   */
  public boolean checkApiSecret()
  {
    try
    {
      return StringUtils.trimToNull(this.konto.getMeta(Plugin.META_PARAM_API_SECRET,null)) != null;
    }
    catch (Exception e)
    {
      Logger.error("unable to check api secret for account",e);
      return false;
    }
  }
  
  /**
   * Prueft alle Anforderungen des Kontos.
   * @return true, wenn alle Anforderungen erfuellt sind.
   */
  public boolean checkAll()
  {
    return this.checkBic() && 
           this.checkBackend() &&
           this.checkApiClientId() &&
           this.checkApiSecret();
  }

  /**
   * Prueft die Minimal-Anforderungen des Kontos, um es im Sync-Backend zu verwenden.
   * @return true, wenn alle Anforderungen erfuellt sind.
   */
  public boolean checkSyncProvider()
  {
    return this.checkBic() && 
           this.checkBackend();
  }

  /**
   * Prueft die Minimal-Anforderungen des Kontos, damit es grundsaetzlich ueberhaupt als Transferwise-Konto erkannt wird.
   * @return true, wenn alle Anforderungen erfuellt sind.
   */
  public boolean checkInitial()
  {
    return this.checkBackend();
  }

}
