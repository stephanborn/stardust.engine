/*******************************************************************************
 * Copyright (c) 2011 SunGard CSA LLC and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    SunGard CSA LLC - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.stardust.engine.api.ejb2.beans;

import java.lang.reflect.Proxy;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBContext;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.TimedObject;
import javax.ejb.Timer;
import javax.ejb.TimerService;

import org.eclipse.stardust.common.Action;
import org.eclipse.stardust.common.CollectionUtils;
import org.eclipse.stardust.common.config.Parameters;
import org.eclipse.stardust.common.error.PublicException;
import org.eclipse.stardust.common.log.LogManager;
import org.eclipse.stardust.common.log.Logger;
import org.eclipse.stardust.common.utils.ejb.J2eeContainerType;
import org.eclipse.stardust.engine.api.ejb2.WorkflowException;
import org.eclipse.stardust.engine.api.ejb2.beans.interceptors.CMTSessionInterceptor;
import org.eclipse.stardust.engine.api.ejb2.beans.interceptors.ContainerConfigurationInterceptor;
import org.eclipse.stardust.engine.api.ejb2.beans.interceptors.SessionBeanExceptionHandler;
import org.eclipse.stardust.engine.core.persistence.jdbc.SessionProperties;
import org.eclipse.stardust.engine.core.runtime.beans.ActionRunner;
import org.eclipse.stardust.engine.core.runtime.beans.ForkingService;
import org.eclipse.stardust.engine.core.runtime.beans.ForkingServiceFactory;
import org.eclipse.stardust.engine.core.runtime.beans.InvocationManager;
import org.eclipse.stardust.engine.core.runtime.beans.daemons.DaemonCarrier;
import org.eclipse.stardust.engine.core.runtime.beans.daemons.DaemonHandler;
import org.eclipse.stardust.engine.core.runtime.beans.daemons.DaemonOperation;
import org.eclipse.stardust.engine.core.runtime.beans.daemons.DaemonOperationExecutor;
import org.eclipse.stardust.engine.core.runtime.beans.daemons.DaemonProperties;
import org.eclipse.stardust.engine.core.runtime.beans.interceptors.CallingInterceptor;
import org.eclipse.stardust.engine.core.runtime.beans.interceptors.ForkingDebugInterceptor;
import org.eclipse.stardust.engine.core.runtime.beans.interceptors.NonInteractiveSecurityContextInterceptor;
import org.eclipse.stardust.engine.core.runtime.beans.interceptors.PropertyLayerProviderInterceptor;
import org.eclipse.stardust.engine.core.runtime.beans.interceptors.RuntimeExtensionsInterceptor;
import org.eclipse.stardust.engine.core.runtime.removethis.EngineProperties;


/**
 * @author ubirkemeyer
 * @version $Revision$
 */
public class LocalForkingServiceImpl implements SessionBean, TimedObject, DaemonHandler
{
   private static final long serialVersionUID = 1L;

   private static final Logger trace = LogManager.getLogger(LocalForkingServiceImpl.class);
   
   private static Map<String, Long> lastRuns = CollectionUtils.newMap();

   private SessionContext sessionContext;
   private ExecuteActionInvocationManager manager;

   private ActionRunner service;

   public void ejbCreate() throws CreateException
   {
      ActionRunner serviceInstance = new ForkingServiceActionRunner();
      manager = new ExecuteActionInvocationManager(sessionContext, serviceInstance);

      this.service = (ActionRunner) Proxy.newProxyInstance(
            ActionRunner.class.getClassLoader(),
            new Class[]{ActionRunner.class}, manager);
   }

   public void setSessionContext(SessionContext ctx) throws EJBException, RemoteException
   {
      this.sessionContext = ctx;
   }

   public void ejbRemove() throws EJBException
   {
      service = null;
      manager = null;
   }

   public void ejbActivate() throws EJBException
   {
      this.service = (ActionRunner) Proxy.newProxyInstance(
            ActionRunner.class.getClassLoader(), new Class[]{}, manager);
   }

   public void ejbPassivate() throws EJBException
   {
      this.service = null;
   }

   public void ejbTimeout(Timer timer)
   {
      DaemonCarrier carrier = (DaemonCarrier) timer.getInfo();
      try
      {
         runDaemon(carrier);
      }
      catch (Exception ex)
      {
         trace.error("Unable to trigger execution of daemon '" + carrier.getType() + "'.");
      }
   }

   public Object run(Action action) throws WorkflowException
   {
      try
      {
         if (action instanceof DaemonOperation)
         {
            action = new DaemonOperationExecutor((DaemonOperation) action, this);
         }
         return service.execute(action);
      }
      catch (PublicException e)
      {
         throw new WorkflowException(e);
      }
   }

   private static class ExecuteActionInvocationManager extends InvocationManager
   {
      private static final long serialVersionUID = 1L;

      public ExecuteActionInvocationManager(EJBContext sessionContext,
            ActionRunner actionRunner)
      {
         super(actionRunner, setupInterceptors(sessionContext));
      }

      private static List setupInterceptors(EJBContext sessionContext)
      {
         List interceptors = new ArrayList(7);

         interceptors.add(new ForkingDebugInterceptor());
         interceptors.add(new PropertyLayerProviderInterceptor());
         interceptors.add(new ContainerConfigurationInterceptor("ForkingService",
               J2eeContainerType.EJB));
         interceptors.add(new CMTSessionInterceptor(
               SessionProperties.DS_NAME_AUDIT_TRAIL, sessionContext));
         interceptors.add(new NonInteractiveSecurityContextInterceptor());
         interceptors.add(new RuntimeExtensionsInterceptor());
         interceptors.add(new SessionBeanExceptionHandler(sessionContext));
         interceptors.add(new CallingInterceptor());

         return interceptors;
      }
   }

   private static class ForkingServiceActionRunner implements ActionRunner
   {
      public Object execute(Action action)
      {
         return action.execute();
      }
   }

   private Timer getTimer(DaemonCarrier carrier)
   {
      TimerService service = sessionContext.getTimerService();
      Collection<Timer> timers = service.getTimers();
      for (Timer timer : timers)
      {
         if (carrier.equals(timer.getInfo()))
         {
            return timer;
         }
      }
      return null;
   }

   public void startTimer(DaemonCarrier carrier)
   {
      DaemonCarrier innerCarrier = carrier.copy();
      if (getTimer(innerCarrier) == null)
      {
         TimerService service = sessionContext.getTimerService();
         long periodicity = Parameters.instance().getLong(
               innerCarrier.getType() + DaemonProperties.DAEMON_PERIODICITY_SUFFIX, 5) * 1000;
         service.createTimer(periodicity, periodicity, innerCarrier);
         trace.info("Timer '" + innerCarrier.getType() + "' started.");
         runDaemon(innerCarrier);
      }
   }

   public void stopTimer(DaemonCarrier carrier)
   {
      Timer timer = getTimer(carrier);
      if (timer != null)
      {
         timer.cancel();
         trace.info("Timer '" + carrier.getType() + "' was stopped.");
      }
   }

   public boolean checkTimer(DaemonCarrier carrier)
   {
      return getTimer(carrier) != null;
   }

   public void runDaemon(DaemonCarrier carrier)
   {
      // prevent firing of batch events if the system time is changed on a running engine.
      String type = carrier.getType();
      Long lastRun = lastRuns.get(type);
      long now = System.currentTimeMillis();
      if (lastRun == null || now - lastRun > 100 || now < lastRun)
      {
         ForkingServiceFactory factory = Parameters.instance().getObject(EngineProperties.FORKING_SERVICE_HOME);
         if (factory == null)
         {
            factory = new RemoteSessionForkingServiceFactory();
         }
         ForkingService forkingService = factory.get();
         forkingService.fork(carrier.copy(), false);
         
         lastRun = now;
         lastRuns.put(type, lastRun);
      }
   }
}
