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
 * Informationen zum Zahler.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class PayerInfo
{
  public String account_id;
  public String email_address;
  public String address_status;
  public String payer_status;
  public PayerName payer_name;
  public String country_code;
}
