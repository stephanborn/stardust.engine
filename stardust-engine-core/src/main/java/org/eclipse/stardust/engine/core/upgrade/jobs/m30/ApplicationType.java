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
package org.eclipse.stardust.engine.core.upgrade.jobs.m30;

/**
 * @author ubirkemeyer
 * @version $Revision$
 */
public class ApplicationType extends IdentifiableElement
{
   private boolean synchronous;

   public ApplicationType(String id, String name, boolean predefined, boolean synchronous)
   {
      super(id, name, null);
      this.predefined = predefined;
      this.synchronous = synchronous;
   }

   public boolean isSynchronous()
   {
      return synchronous;
   }
}
