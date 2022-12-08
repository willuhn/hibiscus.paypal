/**********************************************************************
 *
 * Copyright (c) 2022 Olaf Willuhn
 * All rights reserved.
 * 
 * This software is copyrighted work licensed under the terms of the
 * Jameica License.  Please consult the file "LICENSE" for details. 
 *
 **********************************************************************/

package de.willuhn.jameica.hbci.paypal.synchronize;

import de.willuhn.jameica.hbci.synchronize.jobs.SynchronizeJob;
import de.willuhn.util.ApplicationException;

/**
 * Basis-Interface fuer die Synchronize-Jobs von Paypal.
 */
public interface PaypalSynchronizeJob extends SynchronizeJob
{
  /**
   * Fuehrt den Auftrag aus.
   * @throws ApplicationException
   */
  void exeute() throws ApplicationException;

}


