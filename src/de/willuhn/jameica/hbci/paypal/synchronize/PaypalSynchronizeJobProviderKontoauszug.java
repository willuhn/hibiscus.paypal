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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import de.willuhn.annotation.Lifecycle;
import de.willuhn.annotation.Lifecycle.Type;
import de.willuhn.jameica.hbci.SynchronizeOptions;
import de.willuhn.jameica.hbci.paypal.Plugin;
import de.willuhn.jameica.hbci.rmi.Konto;
import de.willuhn.jameica.hbci.synchronize.jobs.SynchronizeJob;
import de.willuhn.jameica.hbci.synchronize.jobs.SynchronizeJobKontoauszug;
import de.willuhn.logging.Logger;

/**
 * Implementierung eines Job-Providers fuer das Abrufen von Saldo und/oder Kontoauszug.
 */
@Lifecycle(Type.CONTEXT)
public class PaypalSynchronizeJobProviderKontoauszug implements PaypalSynchronizeJobProvider
{
  @Resource
  private PaypalSynchronizeBackend backend = null;
  
  private final static List<Class<? extends SynchronizeJob>> JOBS = new ArrayList<Class<? extends SynchronizeJob>>()
  {{
    add(PaypalSynchronizeJobKontoauszug.class);
  }};

  /**
   * @see de.willuhn.jameica.hbci.synchronize.SynchronizeJobProvider#getSynchronizeJobs(de.willuhn.jameica.hbci.rmi.Konto)
   */
  public List<SynchronizeJob> getSynchronizeJobs(Konto k)
  {
    Class<SynchronizeJobKontoauszug> type = SynchronizeJobKontoauszug.class;
    
    List<SynchronizeJob> jobs = new LinkedList<SynchronizeJob>();
    for (Konto kt:backend.getSynchronizeKonten(k))
    {
      try
      {
        if (!Plugin.getStatus(kt).checkSyncProvider())
          continue;

        // Wird der Geschaeftsvorfall unterstuetzt?
        if (!backend.supports(type,k))
          continue;

        final SynchronizeOptions options = new SynchronizeOptions(kt);

        // Weder Saldo- noch Kontoauszug aktiv.
        // Also nichts zu tun.
        if (!options.getSyncKontoauszuege() && !options.getSyncSaldo())
          continue;
        
        SynchronizeJobKontoauszug job = backend.create(type,kt);
        job.setContext(SynchronizeJob.CTX_ENTITY,kt);
        jobs.add(job);
      }
      catch (Exception e)
      {
        Logger.error("unable to load synchronize jobs",e);
      }
    }

    return jobs;
  }
  
  /**
   * @see de.willuhn.jameica.hbci.synchronize.SynchronizeJobProvider#supports(java.lang.Class, de.willuhn.jameica.hbci.rmi.Konto)
   */
  @Override
  public boolean supports(Class<? extends SynchronizeJob> type, Konto k)
  {
    return true;
  }
  
  /**
   * @see de.willuhn.jameica.hbci.synchronize.SynchronizeJobProvider#getJobTypes()
   */
  public List<Class<? extends SynchronizeJob>> getJobTypes()
  {
    return JOBS;
  }
  
  /**
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  public int compareTo(Object o)
  {
    // Umsaetze und Salden werden zum Schluss ausgefuehrt,
    // damit die oben gesendeten Ueberweisungen gleich mit
    // erscheinen, insofern die Bank das unterstuetzt.
    return 1;
  }
}
