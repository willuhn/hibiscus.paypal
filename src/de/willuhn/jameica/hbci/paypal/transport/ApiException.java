/**********************************************************************
 *
 * Copyright (c) 2022 Olaf Willuhn
 * All rights reserved.
 * 
 * This software is copyrighted work licensed under the terms of the
 * Jameica License.  Please consult the file "LICENSE" for details. 
 *
 **********************************************************************/

package de.willuhn.jameica.hbci.paypal.transport;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.willuhn.jameica.hbci.paypal.domain.ApiErrorDetails;

/**
 * JSON-Mapping für die Fehler.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ApiException extends Exception
{
  /**
   * Der HTTP-Status.
   */
  public int httpStatus;
  
  /**
   * Die HTTP-Message.
   */
  public String httpMessage;

  /**
   * Name des Fehlers.
   */
  public String name;
  
  /**
   * Fehlerbeschreibung.
   */
  public String message;

  /**
   * Die Debug-ID.
   */
  public String debug_id;

  /**
   * Liste mit den Fehlerdetails.
   */
  public List<ApiErrorDetails> details;

  // Bei Identity-Fehlern kommen stattdessen die folgenden beiden Attribute.
  
  /**
   * Der Fehlercode.
   */
  public String error;
  
  /**
   * Die Fehlerbeschreibung.
   */
  public String error_description;
  
}
