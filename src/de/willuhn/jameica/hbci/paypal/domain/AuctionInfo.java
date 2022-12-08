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
 * Informationen zur Auktion.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class AuctionInfo
{
  public String auction_buyer_id;

  @JsonFormat(shape=JsonFormat.Shape.STRING,pattern=TransportService.DF_ISO8601)
  public Date auction_closing_date;
  
  public String auction_item_site;
  
  public String auction_site;
  
  
}
