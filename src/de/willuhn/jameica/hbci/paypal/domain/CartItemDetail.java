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
 * Informationen zu einem einzelnen Warenkorb-Element.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class CartItemDetail
{
  public Money adjustment_amount;
  public Money basic_shipping_amount;
  public List<CheckoutOption> checkout_options;
  public Money discount_amount;
  public Money extra_shipping_amount;
  public Money gift_wrap_amount;
  public Money handling_amount;
  public Money insurance_amount;
  public String invoice_number;
  public Money item_amount;
  public String item_code;
  public String item_description;
  public String item_name;
  public String item_options;
  public String item_quantity;
  public Money item_unit_price;
  public List<TaxAmount> tax_amounts;
  public String tax_percentage;
  public Money total_item_amount;
}
