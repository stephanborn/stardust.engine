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
package org.eclipse.stardust.engine.api.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.stardust.common.utils.ejb.J2eeContainerType;
import org.eclipse.stardust.engine.api.ejb2.beans.interceptors.ContainerConfigurationInterceptor;
import org.eclipse.stardust.engine.core.persistence.jdbc.SessionProperties;
import org.eclipse.stardust.engine.core.runtime.beans.InvocationManager;
import org.eclipse.stardust.engine.core.runtime.beans.interceptors.*;


/**
 * @author ubirkemeyer
 * @version $Revision$
 */
public class PlainWebInvocationManager extends InvocationManager
{
   private static final long serialVersionUID = 1L;

   public PlainWebInvocationManager(Object service, String serviceName,
         Principal principal)
   {
      super(service, setupInterceptors(service, serviceName, principal));
   }

   private static List setupInterceptors(Object service, String serviceName,
         Principal principal)
   {
      List interceptors = new ArrayList();

      interceptors.add(new DebugInterceptor());
      interceptors.add(new PropertyLayerProviderInterceptor());
      interceptors.add(new ContainerConfigurationInterceptor(service.getClass().getName(), J2eeContainerType.WEB));
      interceptors.add(new POJOForkingInterceptor());
      interceptors.add(new POJOSessionInterceptor(SessionProperties.DS_NAME_AUDIT_TRAIL));
      interceptors.add(new J2eeSecurityLoginInterceptor(principal));
      interceptors.add(new GuardingInterceptor(serviceName));
      interceptors.add(new RuntimeExtensionsInterceptor());
      interceptors.add(new POJOExceptionHandler());
      interceptors.add(new CallingInterceptor());

      return interceptors;
   }
}
