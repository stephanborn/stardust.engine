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
package org.eclipse.stardust.engine.core.extensions.conditions.timer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.eclipse.stardust.common.Direction;
import org.eclipse.stardust.common.Period;
import org.eclipse.stardust.common.StringUtils;
import org.eclipse.stardust.engine.api.model.*;
import org.eclipse.stardust.engine.core.model.utils.ModelUtils;
import org.eclipse.stardust.engine.core.spi.extensions.model.BridgeObject;
import org.eclipse.stardust.engine.core.spi.extensions.model.EventConditionValidator;


public class TimerValidator implements EventConditionValidator
{
   private static final BridgeObject DATE_TARGET = new BridgeObject(Date.class,
         Direction.IN);
   private static final BridgeObject LONG_TARGET = new BridgeObject(Long.class,
         Direction.IN);
   private static final BridgeObject PERIOD_TARGET = new BridgeObject(Period.class,
         Direction.IN);

   public Collection validate(EventHandlerOwner context, Map attributes)
   {
      ArrayList list = new ArrayList();

      Object type = attributes.get(PredefinedConstants.TIMER_CONDITION_USE_DATA_ATT);
      if (!(type instanceof Boolean))
      {
         type = Boolean.FALSE;
      }

      if (Boolean.TRUE.equals(type))
      {
         Object dataId = attributes.get(PredefinedConstants.TIMER_CONDITION_DATA_ATT);
         if (!(dataId instanceof String) || StringUtils.isEmpty((String) dataId))
         {
            list.add(new Inconsistency("No Data specified.", Inconsistency.WARNING));
         }
         IProcessDefinition process = null;
         if (context instanceof IProcessDefinition)
         {
            process = (IProcessDefinition) context;
         }
         else if (context instanceof IActivity)
         {
            process = ((IActivity) context).getProcessDefinition();
         }
         if (null != process)
         {
            IData data = ModelUtils.getData(process, (String) dataId);
            if (null != data)
            {
               Object dataPath = attributes
                     .get(PredefinedConstants.TIMER_CONDITION_DATA_PATH_ATT);
               if ((null == dataPath) || (dataPath instanceof String))
               {
                  BridgeObject bo = BridgeObject.getBridge(data, (String) dataPath,
                        Direction.OUT, null);
                  if (!DATE_TARGET.acceptAssignmentFrom(bo)
                        && !LONG_TARGET.acceptAssignmentFrom(bo)
                        && !PERIOD_TARGET.acceptAssignmentFrom(bo))
                  {
                     list.add(new Inconsistency("Invalid data mapping",
                           Inconsistency.WARNING));
                  }
               }
               else
               {
                  list.add(new Inconsistency("Invalid data path",
                        Inconsistency.WARNING));
               }
            }
            else
            {
               list.add(new Inconsistency("Invalid data specified",
                           Inconsistency.WARNING));
            }
         }
         else
         {
            list.add(new Inconsistency("Timer condition using data has to have either "
                  + "process or activity context", Inconsistency.WARNING));
         }
         // todo: check bridge object
      }
      else
      {
         Object period = attributes.get(PredefinedConstants.TIMER_PERIOD_ATT);
         if (!(period instanceof Period))
         {
            list.add(new Inconsistency("No period specified.", Inconsistency.WARNING));
         }
      }
      return list;
   }
}
