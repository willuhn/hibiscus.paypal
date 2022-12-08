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
 * Informationen zum Shipping-Adresse.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ShippingAddress
{
  public String line1;
  public String line2;
  public String city;
  public String country_code;
  public String postal_code;
}
