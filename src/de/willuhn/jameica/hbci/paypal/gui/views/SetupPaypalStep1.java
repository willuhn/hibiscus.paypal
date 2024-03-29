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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ProgressBar;

import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.InfoPanel;
import de.willuhn.jameica.gui.util.Container;
import de.willuhn.jameica.gui.util.SimpleContainer;
import de.willuhn.jameica.hbci.paypal.Plugin;
import de.willuhn.jameica.hbci.paypal.SupportStatus;
import de.willuhn.jameica.hbci.paypal.gui.action.SetupPaypalStep2;
import de.willuhn.jameica.hbci.rmi.Konto;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.system.Application;

/**
 * View zum Einrichten eines Paypal-Kontos.
 */
public class SetupPaypalStep1 extends AbstractSetupPaypal
{
  /**
   * @see de.willuhn.jameica.gui.AbstractView#bind()
   */
  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(i18n.tr("Paypal-Konto - Schritt 1 von 3: BIC"));

    final SupportStatus status = (SupportStatus) this.getCurrentObject();
    final Konto k = status.getKonto();

    final Container c = new SimpleContainer(this.getParent(),false,1);

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
          final ProgressBar bar = createProgressBar(comp);
          bar.setSelection(10);
          return comp;
        }
        return super.extend(state, comp, context);
      }
    };
    info.setTitle(i18n.tr("Schritt 1 von 3: BIC"));
    info.setIcon("paypal-large.png");
    
    if (!status.checkBic())
    {
      k.setBic(Plugin.BIC_PAYPAL);
      k.store();
      final String text = i18n.tr("Die BIC des Kontos wurde korrigiert auf \"{0}\".",Plugin.BIC_PAYPAL);
      Application.getMessagingFactory().sendMessage(new StatusBarMessage(text, StatusBarMessage.TYPE_SUCCESS));
      info.setText(text);
    }
    else
    {
      info.setText(i18n.tr("Die BIC des Kontos ist korrekt."));
    }
    info.setComment(i18n.tr("Kontoinhaber: {0}.\n\nF�r die Pr�fung der API Client-ID klicken Sie bitte auf \"Weiter\".",k.getName()));
    
    info.addButton(new Button(i18n.tr("Weiter"),new SetupPaypalStep2(),status,false,"go-next.png"));
    c.addPart(info);
  }

}
