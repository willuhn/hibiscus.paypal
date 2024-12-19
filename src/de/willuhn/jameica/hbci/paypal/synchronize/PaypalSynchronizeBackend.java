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

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import de.willuhn.annotation.Lifecycle;
import de.willuhn.annotation.Lifecycle.Type;
import de.willuhn.jameica.hbci.paypal.Plugin;
import de.willuhn.jameica.hbci.rmi.Konto;
import de.willuhn.jameica.hbci.synchronize.AbstractSynchronizeBackend;
import de.willuhn.jameica.hbci.synchronize.SynchronizeEngine;
import de.willuhn.jameica.hbci.synchronize.SynchronizeSession;
import de.willuhn.jameica.hbci.synchronize.jobs.SynchronizeJob;
import de.willuhn.jameica.system.OperationCanceledException;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

/**
 * Backend, welches Konten via Paypal anbindet.
 */
@Lifecycle(Type.CONTEXT)
public class PaypalSynchronizeBackend extends AbstractSynchronizeBackend<PaypalSynchronizeJobProvider>
{
  @Resource
  private SynchronizeEngine engine = null;

  /**
   * @see de.willuhn.jameica.hbci.synchronize.SynchronizeBackend#getName()
   */
  public String getName()
  {
    return "Paypal";
  }

  /**
   * @see de.willuhn.jameica.hbci.synchronize.AbstractSynchronizeBackend#getJobProviderInterface()
   */
  protected Class<PaypalSynchronizeJobProvider> getJobProviderInterface()
  {
    return PaypalSynchronizeJobProvider.class;
  }

  /**
   * @see de.willuhn.jameica.hbci.synchronize.AbstractSynchronizeBackend#createJobGroup(de.willuhn.jameica.hbci.rmi.Konto)
   */
  protected de.willuhn.jameica.hbci.synchronize.AbstractSynchronizeBackend.JobGroup createJobGroup(Konto k)
  {
    return new PaypalJobGroup(k);
  }

  /**
   * @see de.willuhn.jameica.hbci.synchronize.AbstractSynchronizeBackend#getSynchronizeKonten(de.willuhn.jameica.hbci.rmi.Konto)
   */
  public List<Konto> getSynchronizeKonten(Konto k)
  {
    List<Konto> list = super.getSynchronizeKonten(k);
    List<Konto> result = new ArrayList<Konto>();
    
    for (Konto konto:list)
    {
      if (Plugin.getStatus(konto).checkSyncProvider())
        result.add(konto);
    }
    
    return result;
  }
  
  /**
   * @see de.willuhn.jameica.hbci.synchronize.AbstractSynchronizeBackend#create(java.lang.Class, de.willuhn.jameica.hbci.rmi.Konto)
   */
  public <T> T create(Class<? extends SynchronizeJob> type, Konto konto) throws ApplicationException
  {
    if (!this.supports(type,konto))
      throw new ApplicationException(i18n.tr("Der Gesch�ftsvorfall wird nicht unterst�tzt"));
    
    return(T) super.create(type,konto);
  }

  /**
   * @see de.willuhn.jameica.hbci.synchronize.AbstractSynchronizeBackend#supports(java.lang.Class, de.willuhn.jameica.hbci.rmi.Konto)
   */
  public boolean supports(Class<? extends SynchronizeJob> type, Konto konto)
  {
    if (!Plugin.getStatus(konto).checkSyncProvider())
      return false;

    return super.supports(type,konto);
  }
  
  /**
   * @see de.willuhn.jameica.hbci.synchronize.AbstractSynchronizeBackend#execute(java.util.List)
   */
  public synchronized SynchronizeSession execute(List<SynchronizeJob> jobs) throws ApplicationException, OperationCanceledException
  {
    try
    {
      // Wir checken extra noch, ob es wirklich alles Offline-Konten sind oder ob bei denen das Scripting ausgewaehlt wurde
      for (SynchronizeJob job:jobs)
      {
        Konto konto = job.getKonto();
        if (!Plugin.getStatus(konto).checkSyncProvider())
          throw new ApplicationException(i18n.tr("Das Zugangsverfahren {0} unterst�tzt das Konto {1} nicht",this.getName(),konto.getLongName()));
      }
    }
    catch (RemoteException re)
    {
      Logger.error("error while performing synchronization",re);
      throw new ApplicationException(i18n.tr("Synchronisierung fehlgeschlagen: {0}",re.getMessage()));
    }

    return super.execute(jobs);
  }
  
  /**
   * @see de.willuhn.jameica.hbci.synchronize.AbstractSynchronizeBackend#getPropertyNames(de.willuhn.jameica.hbci.rmi.Konto)
   */
  public List<String> getPropertyNames(Konto konto)
  {
    if (!Plugin.getStatus(konto).checkSyncProvider())
      return null;
    
    return Arrays.asList(Plugin.META_PARAM_API_CLIENTID, Plugin.META_PARAM_API_SECRET, Plugin.META_PARAM_IMPORT_AUTHORIZATIONS+"(true/false)");
  }

  /**
   * Unsere Implementierung.
   */
  protected class PaypalJobGroup extends JobGroup
  {
    /**
     * ct.
     * @param k das Konto.
     */
    protected PaypalJobGroup(Konto k)
    {
      super(k);
    }

    /**
     * @see de.willuhn.jameica.hbci.synchronize.AbstractSynchronizeBackend.JobGroup#sync()
     */
    protected void sync() throws Exception
    {
      ////////////////////////////////////////////////////////////////////
      // lokale Variablen
      ProgressMonitor monitor = worker.getMonitor();
      
      double chunk  = 100d / (worker.getSynchronization().size()) * (this.jobs.size());
      double window = chunk - 6d;
      getCurrentSession().setProgressWindow(window);
      ////////////////////////////////////////////////////////////////////

      this.checkInterrupted();

      monitor.log(" ");
      monitor.log(i18n.tr("Synchronisiere Konto: {0}",this.getKonto().getLongName()));

      Logger.info("processing jobs");
      for (SynchronizeJob job:this.jobs)
      {
        this.checkInterrupted();
        monitor.setStatusText(i18n.tr("F�hre Gesch�ftsvorfall aus: \"{0}\"",job.getName()));
        ((PaypalSynchronizeJob)job).exeute();
      }
    }
  }
}
