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
 * Ergebnis-Container mit den empfangenen Salden.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class BalanceResult
{
  /**
   * Die Account-ID.
   */
  public String account_id;
  
  /**
   * Das Datum der letzten Aktualisierung.
   */
  @JsonFormat(shape=JsonFormat.Shape.STRING,pattern=TransportService.DF_ISO8601_SALDO)
  public Date last_refreshed_time;

  /**
   * Das Saldo-Datum.
   */
  @JsonFormat(shape=JsonFormat.Shape.STRING,pattern=TransportService.DF_ISO8601_SALDO)
  public Date as_of_time;
  
  /**
   * Die Salden.
   */
  public List<BalanceDetail> balances;

}


