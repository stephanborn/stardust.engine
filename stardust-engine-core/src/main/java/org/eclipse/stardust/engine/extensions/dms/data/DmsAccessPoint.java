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
package org.eclipse.stardust.engine.extensions.dms.data;

import org.eclipse.stardust.common.AttributeHolderImpl;
import org.eclipse.stardust.common.Direction;
import org.eclipse.stardust.engine.api.model.PluggableType;
import org.eclipse.stardust.engine.core.spi.extensions.model.AccessPoint;
import org.eclipse.stardust.engine.core.spi.extensions.model.DataType;


/**
 * @author rsauer
 * @version $Revision$
 */
public class DmsAccessPoint extends AttributeHolderImpl implements AccessPoint
{
   private final String id;
   
   private final String name;
   
   private final Direction direction;
   
   private final DataType type;
   
   public DmsAccessPoint(String id, String name, Direction direction, DataType type)
   {
      this.id = id;
      this.name = name;

      this.direction = direction;
      
      this.type = type;
   }

   public Direction getDirection()
   {
      return direction;
   }

   public String getId()
   {
      return id;
   }

   public String getName()
   {
      return name;
   }

   public void markModified()
   {
      // ignore
   }

   public PluggableType getType()
   {
      return type;
   }

}
