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
package org.eclipse.stardust.engine.api.ejb2;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJBObject;

import org.eclipse.stardust.common.config.Parameters;
import org.eclipse.stardust.common.reflect.Reflect;
import org.eclipse.stardust.engine.api.ejb2.tunneling.TunneledContext;
import org.eclipse.stardust.engine.api.runtime.Service;
import org.eclipse.stardust.engine.core.runtime.beans.AbstractSessionAwareServiceFactory;
import org.eclipse.stardust.engine.core.runtime.beans.ManagedService;
import org.eclipse.stardust.engine.core.runtime.beans.removethis.SecurityProperties;


/**
 * @author ubirkemeyer
 * @version $Revision$
 */
public class RemoteServiceFactory extends AbstractSessionAwareServiceFactory
{
   private Map credentials = new HashMap();

   private SecureSessionFactory secureSessionFactory;

   public RemoteServiceFactory()
   {
      this.secureSessionFactory = (SecureSessionFactory) Reflect.createInstance(
            Parameters.instance()
            .getString(SecurityProperties.SECURE_SESSION_FACTORY,
                  "org.eclipse.stardust.engine.api.ejb2.InternallyAuthentifiedSecureSessionFactory"));
   }

   protected Service getNewServiceInstance(Class service)
   {
      String serviceName = service.getName();
      int dot = serviceName.lastIndexOf(".");
      String className = serviceName.substring(dot + 1);
      Class home = Reflect.getClassFromClassName("org.eclipse.stardust.engine.api.ejb2.Remote"
            + className + "Home");
      Class remote = Reflect.getClassFromClassName("org.eclipse.stardust.engine.api.ejb2.Remote"
            + className);

      EJBObject inner;
      TunneledContext tunneledContext;
      if (secureSessionFactory instanceof TunnelingAwareSecureSessionFactory)
      {
         TunnelingAwareSecureSessionFactory.SecureSession session = ((TunnelingAwareSecureSessionFactory) secureSessionFactory).getSecureSession(
               Parameters.instance().getString(
                     Reflect.getHumanReadableClassName(service) + ".JndiName",
                     service.getName()), //
               home, remote, new Class[] {}, new Object[] {}, //
               credentials, getProperties());
         
         inner = (EJBObject) session.endpoint;
         tunneledContext = session.tunneledContext;
      }
      else
      {
         inner = (EJBObject) secureSessionFactory.get(Parameters.instance()
               .getString(Reflect.getHumanReadableClassName(service) + ".JndiName",
                     service.getName()),
                     home, remote, new Class[]{}, new Object[]{}, credentials, getProperties());

         // no tunneling
         tunneledContext = null;
      }

      Service result = (Service) Proxy.newProxyInstance(service.getClassLoader(),
            new Class[]{service, ManagedService.class},
            new ClientInvocationHandler(inner, tunneledContext));
      
      return result;
   }

   public void setCredentials(Map credentials)
   {
      this.credentials = credentials;
   }
}
