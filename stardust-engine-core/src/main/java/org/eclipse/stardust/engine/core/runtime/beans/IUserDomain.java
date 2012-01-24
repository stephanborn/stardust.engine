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
package org.eclipse.stardust.engine.core.runtime.beans;

import org.eclipse.stardust.engine.core.persistence.IdentifiablePersistent;

/**
 *
 */
public interface IUserDomain extends IdentifiablePersistent
{
   /**
    *
    */
   String getId();

   /**
    *
    */
   void setId(String id);

   IAuditTrailPartition getPartition();

   /**
    *
    */
   String getDescription();

   /**
    *
    */
   void setDescription(String description);
   
   /**
   *
   */
   public IUserDomain getSuperDomain();
}
