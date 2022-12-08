/**********************************************************************
 *
 * Copyright (c) 2022 Olaf Willuhn
 * All rights reserved.
 * 
 * This software is copyrighted work licensed under the terms of the
 * Jameica License.  Please consult the file "LICENSE" for details. 
 *
 **********************************************************************/

package de.willuhn.jameica.hbci.paypal.domain;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.willuhn.logging.Logger;

/**
 * Betrag der Transaktion.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Money
{
  public String currency_code;
  public String value;
  
  /**
   * Liefert den Betrag als double.
   * @return der Betrag als double. Double.NaN, wenn kein Wert ermittelbar ist.
   */
  public double doubleValue()
  {
    final String s = StringUtils.trimToNull(this.value);
    
    if (s == null)
      return Double.NaN;
    
    try
    {
      return Double.parseDouble(s);
    }
    catch (Exception e)
    {
      Logger.error("unable to parse as double: " + s,e);
    }
    return Double.NaN;
  }
}
