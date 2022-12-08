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
 * Informationen zu den Incentive-Details.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class IncentiveDetails
{
  public Money incentive_amount;
  public String incentive_code;
  public String incentive_program_code;
  public String incentive_type;
}
