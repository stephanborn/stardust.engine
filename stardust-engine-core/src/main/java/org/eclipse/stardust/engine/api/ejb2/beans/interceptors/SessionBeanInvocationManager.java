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
package org.eclipse.stardust.engine.api.ejb2.beans.interceptors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBContext;

import org.eclipse.stardust.common.utils.ejb.J2eeContainerType;
import org.eclipse.stardust.engine.api.ejb2.beans.EjbTxPolicy;
import org.eclipse.stardust.engine.core.persistence.jdbc.SessionProperties;
import org.eclipse.stardust.engine.core.runtime.beans.InvocationManager;
import org.eclipse.stardust.engine.core.runtime.beans.interceptors.CallingInterceptor;
import org.eclipse.stardust.engine.core.runtime.beans.interceptors.DebugInterceptor;
import org.eclipse.stardust.engine.core.runtime.beans.interceptors.GuardingInterceptor;
import org.eclipse.stardust.engine.core.runtime.beans.interceptors.PropertyLayerProviderInterceptor;
import org.eclipse.stardust.engine.core.runtime.beans.interceptors.RuntimeExtensionsInterceptor;


/**
 * @author ubirkemeyer
 * @version $Revision$
 */
public class SessionBeanInvocationManager extends InvocationManager implements Serializable
{
   private static final long serialVersionUID = 1L;

   public SessionBeanInvocationManager(EJBContext context, Object serviceInstance,
         String serviceName)
   {
      super(serviceInstance, setupInterceptors(context, serviceName));
   }

   private static List setupInterceptors(EJBContext context, String serviceName)
   {
      List interceptors = new ArrayList();

      EjbTxPolicy ejbTxPolicy = new EjbTxPolicy(serviceName);

      interceptors.add(new DebugInterceptor());
      interceptors.add(new PropertyLayerProviderInterceptor(false));
      interceptors.add(new ContainerConfigurationInterceptor(serviceName,
            J2eeContainerType.EJB));
      interceptors.add(new CMTSessionInterceptor(SessionProperties.DS_NAME_AUDIT_TRAIL,
            context, ejbTxPolicy));
      interceptors.add(new SessionBeanLoginInterceptor(context));
      interceptors.add(new GuardingInterceptor(serviceName));
      interceptors.add(new RuntimeExtensionsInterceptor());
      interceptors.add(new SessionBeanExceptionHandler(context, ejbTxPolicy));
      interceptors.add(new CallingInterceptor());

      return interceptors;
   }
}
