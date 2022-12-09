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
 * Ein einzelner Saldo.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class BalanceDetail
{
  public String currency;
  public Boolean primary;
  public Money total_balance;
  public Money available_balance;
  public Money withheld_balance;
  
  
}
