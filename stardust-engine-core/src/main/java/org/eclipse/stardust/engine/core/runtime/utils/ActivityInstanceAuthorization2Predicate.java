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
package org.eclipse.stardust.engine.core.runtime.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.stardust.common.log.LogManager;
import org.eclipse.stardust.common.log.Logger;
import org.eclipse.stardust.engine.core.persistence.FieldRef;
import org.eclipse.stardust.engine.core.runtime.beans.ActivityInstanceBean;
import org.eclipse.stardust.engine.core.runtime.beans.IActivityInstance;


/**
 * 
 * @author Florin.Herinean
 * @version $Revision: $
 */
public class ActivityInstanceAuthorization2Predicate extends AbstractAuthorization2Predicate
{
   private static final Logger trace = LogManager.getLogger(ActivityInstanceAuthorization2Predicate.class);
   
   private static final FieldRef[] LOCAL_STRINGS = {
      ActivityInstanceBean.FR__ACTIVITY,
      ActivityInstanceBean.FR__MODEL,
      ActivityInstanceBean.FR__CURRENT_PERFORMER,
      ActivityInstanceBean.FR__CURRENT_USER_PERFORMER,
      ActivityInstanceBean.FR__PROCESS_INSTANCE,
      ActivityInstanceBean.FR__CURRENT_DEPARTMENT
   };

   public ActivityInstanceAuthorization2Predicate(AuthorizationContext context)
   {
      super(context);
   }

   public FieldRef[] getLocalFields()
   {
      return LOCAL_STRINGS;
   }

   public boolean accept(Object o)
   {
      boolean result = true;
      if (delegate != null)
      {
         result = delegate.accept(o);
      }
      if (result && super.accept(o))
      {
         if (o instanceof ResultSet)
         {
            ResultSet rs = (ResultSet) o;
            try
            {
               long activityRtOid = rs.getLong(ActivityInstanceBean.FIELD__ACTIVITY);
               long modelOid = rs.getLong(ActivityInstanceBean.FIELD__MODEL);
               long currentPerformer = rs.getLong(ActivityInstanceBean.FIELD__CURRENT_PERFORMER);
               long currentUserPerformer = rs.getLong(ActivityInstanceBean.FIELD__CURRENT_USER_PERFORMER);
               long processInstanceOid = rs.getLong(ActivityInstanceBean.FIELD__PROCESS_INSTANCE);
               long departmentOid = rs.getLong(ActivityInstanceBean.FIELD__CURRENT_DEPARTMENT);
               context.setActivityData(processInstanceOid, activityRtOid, modelOid, currentPerformer,
                     currentUserPerformer, departmentOid);
               return Authorization2.hasPermission(context);
            }
            catch (SQLException e)
            {
               trace.warn("", e);

               return false;
            }
         }
         else if (o instanceof IActivityInstance)
         {
            IActivityInstance ai = (IActivityInstance) o;
            context.setActivityInstance(ai);
            return Authorization2.hasPermission(context);
         }
      }
      return result;
   }
}
