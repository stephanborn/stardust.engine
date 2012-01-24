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
package org.eclipse.stardust.engine.core.spi.persistence;

import java.util.List;

import org.eclipse.stardust.engine.core.persistence.Persistent;


/**
 * 
 * @author thomas.wolfram
 * 
 */
public interface IPersistentListener
{
   void updated(Persistent persistent);
   
   void created(Persistent persistent);

   public interface Factory
   {
      List<IPersistentListener> createListener(Class<? extends Persistent> clazz);
   }
}
