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
 * JSON-Objekt für eine Checkout-Option.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class CheckoutOption
{
  public String checkout_option_name;
  public String checkout_option_value;

}


