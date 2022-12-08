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
 * Informationen zum Shipping.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ShippingInfo
{
  public ShippingAddress address;
  public String method;
  public String name;
  public ShippingAddress secondary_shipping_address;
}
