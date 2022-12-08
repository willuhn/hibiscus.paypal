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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Informationen zum Warenkorb.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class CartInfo
{
  public List<CartItemDetail> item_details;
  
  public String paypal_invoice_id;
}
