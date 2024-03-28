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

import de.willuhn.jameica.hbci.rmi.Konto;
import de.willuhn.jameica.plugin.AbstractPlugin;

/**
 * Plugin-Klasse.
 */
public class Plugin extends AbstractPlugin
{
  /**
   * Die BIC von Paypal.
   */
  public final static String BIC_PAYPAL = "PPLXLULLXXX";

  /**
   * Meta-Parameter mit der API Client-ID.
   */
  public final static String META_PARAM_API_CLIENTID = "API Client-ID";

  /**
   * Meta-Parameter mit dem API Secret.
   */
  public final static String META_PARAM_API_SECRET = "API Secret";

  /**
   * Meta-Parameter für Import-Auswahl
   */
  public final static String META_PARAM_IMPORT_AUTHORIZATIONS = "Authorisierungsbuchungen importieren (true/false)";
  
  /**
   * Liefert den Support-Status des Kontos.
   * @param konto das zu pruefende Konto.
   * @return der Support-Status.
   */
  public static SupportStatus getStatus(Konto konto)
  {
    return new SupportStatus(konto);
  }
}
