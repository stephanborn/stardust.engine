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

import java.util.Map;

import org.eclipse.stardust.engine.api.ejb2.tunneling.TunneledContext;
import org.eclipse.stardust.engine.api.runtime.ServiceNotAvailableException;


/**
 * @author sauer
 * @version $Revision: $
 */
public interface TunnelingAwareSecureSessionFactory extends SecureSessionFactory
{

   SecureSession getSecureSession(String jndiName, Class homeClass,
         Class remoteClass, Class[] creationArgTypes, Object[] creationArgs,
         Map credentials, Map properties)
         throws ServiceNotAvailableException;

   class SecureSession
   {
      public final Object endpoint;
      
      public final TunneledContext tunneledContext;

      public SecureSession(Object endpoint, TunneledContext tunneledContext)
      {
         this.endpoint = endpoint;
         this.tunneledContext = tunneledContext;
      }
   }

}
