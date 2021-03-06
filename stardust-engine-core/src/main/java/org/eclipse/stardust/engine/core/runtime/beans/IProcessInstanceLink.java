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

import java.util.Date;

import org.eclipse.stardust.engine.core.persistence.Persistent;


public interface IProcessInstanceLink extends Persistent
{
   long getProcessInstanceOID();

   IProcessInstance getProcessInstance();

   long getLinkedProcessInstanceOID();
   
   IProcessInstance getLinkedProcessInstance();

   IProcessInstanceLinkType getLinkType();

   Date getCreateTime();

   long getCreatingUserOID();

   IUser getCreatingUser();

   String getComment();
}
