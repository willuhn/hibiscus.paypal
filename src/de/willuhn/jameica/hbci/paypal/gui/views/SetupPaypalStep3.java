/**********************************************************************
 *
 * Copyright (c) 2022 Olaf Willuhn
 * All rights reserved.
 * 
 * This software is copyrighted work licensed under the terms of the
 * Jameica License.  Please consult the file "LICENSE" for details. 
 *
 **********************************************************************/

package de.willuhn.jameica.hbci.paypal.gui.views;

import java.rmi.RemoteException;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;

import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.InfoPanel;
import de.willuhn.jameica.gui.util.Container;
import de.willuhn.jameica.gui.util.SimpleContainer;
import de.willuhn.jameica.hbci.paypal.Plugin;
import de.willuhn.jameica.hbci.paypal.SupportStatus;
import de.willuhn.jameica.hbci.rmi.Konto;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * View zum Einrichten eines Paypal-Kontos.
 */
public class SetupPaypalStep3 extends AbstractSetupPaypal
{
  private Konto konto = null;
  private SupportStatus status = null;
  private Button next = null;
  private TextInput apiSecret = null;
  private Listener listener = null;
  private ProgressBar bar = null;
  
  /**
   * @see de.willuhn.jameica.gui.AbstractView#bind()
   */
  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(i18n.tr("Paypal-Konto - Schritt 3 von 3: API Secret"));

    this.status = (SupportStatus) this.getCurrentObject();
    this.konto = status.getKonto();

    final Container c = new SimpleContainer(this.getParent());

    final InfoPanel info = new InfoPanel()
    {
      /**
       * @see de.willuhn.jameica.gui.parts.InfoPanel#extend(de.willuhn.jameica.gui.parts.InfoPanel.DrawState, org.eclipse.swt.widgets.Composite, java.lang.Object)
       */
      @Override
      public Composite extend(DrawState state, Composite comp, Object context)
      {
        if (state == DrawState.TITLE_AFTER)
        {
          bar = createProgressBar(comp);
          return comp;
        }
        if (state == DrawState.COMMENT_BEFORE)
        {
          final Composite newComp = new Composite(comp,SWT.NONE);
          newComp.setBackground(comp.getBackground());
          newComp.setBackgroundMode(SWT.INHERIT_FORCE);
          newComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
          
          final GridLayout gl = new GridLayout(1,false);
          gl.marginWidth = 0;
          gl.horizontalSpacing = 0;
          newComp.setLayout(gl);
          
          try
          {
            final TextInput input = getApiSecret();
            input.paint(newComp);
            input.getControl().addKeyListener(new KeyAdapter() {
              /**
               * @see org.eclipse.swt.events.KeyAdapter#keyReleased(org.eclipse.swt.events.KeyEvent)
               */
              @Override
              public void keyReleased(KeyEvent e)
              {
                getListener().handleEvent(null);
              }
            });
          }
          catch (RemoteException re)
          {
            Logger.error("unable to show input for api secret",re);
          }
          return newComp;
        }
        return super.extend(state, comp, context);
        
      }
    };
    info.setTitle(i18n.tr("Schritt 3 von 3: API Secret"));
    info.setIcon("paypal-large.png");
    
    final Button b = this.getNext();
    
    if (!status.checkApiSecret())
    {
      info.setText(i18n.tr("Folgen Sie bitte den Anweisungen auf der Webseite, um ein API Secret zu erstellen.\n" +
                           "Geben Sie das API Secret anschließend hier ein."));
      b.setEnabled(false);
    }
    else
    {
      info.setText(i18n.tr("Das API Secret des Kontos ist korrekt konfiguriert.\n" +
                           "Prüfen Sie bitte ggf. die Korrektheit des API Secret."));
    }
    info.setUrl("https://www.willuhn.de/wiki/doku.php?id=support:hibiscus.paypal");
    info.setComment(i18n.tr("Kontoinhaber: {0}.\n\nSie können das API Secret später jederzeit in den Synchronisierungsoptionen des Kontos ändern.\nKlicken Sie bitte auf \"Fertigstellen\", um den Assistenten zu beenden.",this.konto.getName()));
    
    info.addButton(new Button(i18n.tr("Zurück"),new de.willuhn.jameica.hbci.paypal.gui.action.SetupPaypalStep2(),status,false,"go-previous.png"));
    info.addButton(b);
    c.addPart(info);
    
    getListener().handleEvent(null);
  }
  
  /**
   * Liefert den Next-Button.
   * @return der Next-Button.
   */
  private Button getNext()
  {
    if (this.next != null)
      return this.next;
    
    this.next = new Button(i18n.tr("Fertigstellen"),new Action() {
      
      @Override
      public void handleAction(Object context) throws ApplicationException
      {
        new de.willuhn.jameica.hbci.paypal.gui.action.SetupPaypalStep4().handleAction(context);
      }
    },this.getCurrentObject(),false,"ok.png");
    return this.next;
  }
  
  /**
   * Liefert das Eingabefeld fuer das API Secret.
   * @return das Eingabefeld fuer das API Secret.
   * @throws RemoteException
   */
  private TextInput getApiSecret() throws RemoteException
  {
    if (this.apiSecret != null)
      return this.apiSecret;
    
    this.apiSecret = new TextInput(this.konto.getMeta(Plugin.META_PARAM_API_SECRET,null));
    this.apiSecret.setHint(i18n.tr("Bitte geben Sie hier das API Secret ein."));
    this.apiSecret.setMaxLength(90);
    this.apiSecret.setInvalidChars(" \t\n\r");
    this.apiSecret.addListener(this.getListener());
    return this.apiSecret;
  }
  
  /**
   * Liefert den Listener zum Speichern des API Secret.
   * @return der Listener.
   */
  private Listener getListener()
  {
    if (this.listener != null)
      return this.listener;

    this.listener = new Listener() {
      
      @Override
      public void handleEvent(Event event)
      {
        try
        {
          final String s = StringUtils.trimToNull((String) getApiSecret().getValue());
          int len = s != null ? s.length() : 0;
          final boolean haveSecret = s != null && len > 50;
          
          if (haveSecret)
          {
            String current = konto.getMeta(Plugin.META_PARAM_API_SECRET,null);
            konto.setMeta(Plugin.META_PARAM_API_SECRET,s);
            if (!Objects.equals(s,current))
              Application.getMessagingFactory().sendMessage(new StatusBarMessage(i18n.tr("API Secret gespeichert"),StatusBarMessage.TYPE_SUCCESS));
          }
          else if (len < 50 && len > 0)
          {
            Application.getMessagingFactory().sendMessage(new StatusBarMessage(i18n.tr("Das eingegebene API Secret ist zu kurz"),StatusBarMessage.TYPE_INFO));
          }
          
          bar.setSelection(haveSecret ? 80 : 40);
          getNext().setEnabled(haveSecret);
        }
        catch (RemoteException re)
        {
          Logger.error("unable to apply api secret",re);
        }
      }
    };
    return this.listener;
    
  }
}
