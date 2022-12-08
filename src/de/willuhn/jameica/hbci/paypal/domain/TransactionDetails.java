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
 * Eine einzelne Transaktion.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class TransactionDetails
{
  /**
   * Die Infos zur Auktion.
   */
  public AuctionInfo auction_info;

  /**
   * Die Infos zum Warenkorb.
   */
  public CartInfo cart_info;
  
  /**
   * Die Infos zu den Incentives.
   */
  public IncentiveInfo incentive_info;

  /**
   * Die Infos zum Zahler.
   */
  public PayerInfo payer_info;

  /**
   * Die Infos zum Shipping.
   */
  public ShippingInfo shipping_info;

  /**
   * Die Infos zum Store.
   */
  public StoreInfo store_info;

  /**
   * Die Infos zur Transaktion.
   */
  public TransactionInfo transaction_info;

}


