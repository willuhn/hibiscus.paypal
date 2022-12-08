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
 * JSON-Mapping.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ApiErrorDetails
{
  /**
   * Betroffenes Feld.
   */
  public String field;
  
  /**
   * Betroffener Wert.
   */
  public String value;
  
  /**
   * Betroffene Stelle.
   */
  public String location;
  
  /**
   * Art des Fehlers.
   */
  public String issue;
  
  /**
   * Problembeschreibung.
   */
  public String description;
}
