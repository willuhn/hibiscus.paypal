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

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.willuhn.jameica.hbci.paypal.transport.TransportService;

/**
 * Eine einzelne Transaktion.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class TransactionInfo
{
  public String annual_percentage_rate;
  public Money available_balance;
  public String bank_reference_id;
  public Money credit_promotional_fee;
  public String credit_term;
  public Money credit_transactional_fee;
  public String custom_field;
  public Money discount_amount;
  public Money ending_balance;
  public Money fee_amount;
  public String instrument_sub_type;
  public String instrument_type;
  public Money insurance_amount;
  public String invoice_id;
  public Money other_amount;
  public String payment_method_type;
  public String payment_tracking_id;
  public String paypal_account_id;
  public String paypal_reference_id;
  public String paypal_reference_id_type;
  public String protection_eligibility;
  public Money sales_tax_amount;
  public Money shipping_amount;
  public Money shipping_discount_amount;
  public Money shipping_tax_amount;
  public Money tip_amount;
  public Money transaction_amount;
  
  /**
   * Transaktionsart - siehe https://developer.paypal.com/docs/transaction-search/transaction-event-codes/
   */
  public String transaction_event_code;
  
  public String transaction_id;

  @JsonFormat(shape=JsonFormat.Shape.STRING,pattern=TransportService.DF_ISO8601)
  public Date transaction_initiation_date;

  public String transaction_note;
  
  /**
   * D: PayPal or merchant rules denied the transaction.
   * P: The transaction is pending. The transaction was created but waits for another payment process to complete, such as an ACH transaction, before the status changes to S.
   * S: The transaction successfully completed without a denial and after any pending statuses.
   * V: A successful transaction was fully reversed and funds were refunded to the original sender.
   */
  public String transaction_status;

  public String transaction_subject;

  @JsonFormat(shape=JsonFormat.Shape.STRING,pattern=TransportService.DF_ISO8601)
  public Date transaction_updated_date;
  
}


