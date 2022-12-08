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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Name des Zahlers.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class PayerName
{
  public String given_name;
  public String surname;
  public String alternate_full_name;
}
