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
package org.eclipse.stardust.engine.core.preferences;

import java.io.Serializable;
import java.util.Date;

public class PreferenceCacheHint implements Serializable
{   
   private static final long serialVersionUID = 1L;

   private Date realmCacheLastModified;
  
   private Date partitionCacheLastModified;
   
   public PreferenceCacheHint(Date realmCacheLastModified, Date partitionCacheLastModified)
   {
      this.realmCacheLastModified = realmCacheLastModified;
      this.partitionCacheLastModified = partitionCacheLastModified; 
   }


   public Date getRealmCacheLastModified()
   {
      return realmCacheLastModified;
   }
   
   public Date getPartitionCacheLastModified()
   {
      return partitionCacheLastModified;
   }
}
