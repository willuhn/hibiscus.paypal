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
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.willuhn.jameica.hbci.paypal.transport.TransportService;

/**
 * Ergebnis-Container mit den empfangenen Umsatzbuchungen.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class TransactionResult
{
  /**
   * Die Kontonummer.
   */
  public String account_number;
  
  /**
   * Das Datum der letzten Aktualisierung.
   */
  @JsonFormat(shape=JsonFormat.Shape.STRING,pattern=TransportService.DF_ISO8601)
  public Date last_refreshed_datetime;
  
  /**
   * Die Gesamt-Anzahl der Transaktionen.
   */
  public Integer total_items;
  
  /**
   * Die Seiten-Nummer.
   */
  public Integer page;
  
  /**
   * Die Gesamt-Anzahl der Seiten.
   */
  public Integer total_pages;
  
  /**
   * Die einzelnen Transaktionen.
   */
  public List<TransactionDetails> transaction_details;


}


