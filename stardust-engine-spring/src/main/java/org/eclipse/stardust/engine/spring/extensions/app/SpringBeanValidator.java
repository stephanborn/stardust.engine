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
package org.eclipse.stardust.engine.spring.extensions.app;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.stardust.common.error.InternalException;
import org.eclipse.stardust.common.reflect.Reflect;
import org.eclipse.stardust.engine.api.model.Inconsistency;
import org.eclipse.stardust.engine.api.model.PredefinedConstants;
import org.eclipse.stardust.engine.api.spring.SpringConstants;
import org.eclipse.stardust.engine.core.pojo.app.PlainJavaValidator;


/**
 * @author ubirkemeyer
 * @version $Revision$
 */
public class SpringBeanValidator extends PlainJavaValidator
{
   public List validate(Map attributes, Map typeAttributes, Iterator accessPoints)
   {
      ArrayList inconsistencies = new ArrayList();
      String className = (String) attributes.get(PredefinedConstants.CLASS_NAME_ATT);

      if (className == null)
      {
         inconsistencies.add(new Inconsistency("Bean type is not specified.",
               Inconsistency.WARNING));
      }
      else
      {
         try
         {
            Class clazz = Class.forName(className);
            String methodName = (String) attributes.get(PredefinedConstants.METHOD_NAME_ATT);

            if (methodName == null)
            {
               inconsistencies.add(new Inconsistency("Completion method not specified.",
                     Inconsistency.WARNING));
            }
            else
            {
               try
               {
                  Reflect.decodeMethod(clazz, methodName);
               }
               catch (InternalException e)
               {
                  inconsistencies.add(new Inconsistency("Couldn't find method '"
                        + methodName + "' in class '" + clazz.getName() + "'.",
                        Inconsistency.WARNING));
               }
            }

            String beanId = (String) attributes.get(SpringConstants.ATTR_BEAN_ID);

            if (beanId == null)
            {
               inconsistencies.add(new Inconsistency("Bean ID was not specified.",
                     Inconsistency.ERROR));
            }
         }
         catch (ClassNotFoundException e)
         {
            inconsistencies.add(new Inconsistency("Class '" + className + "' not found.",
                  Inconsistency.WARNING));
         }
         catch (NoClassDefFoundError e)
         {
            inconsistencies.add(new Inconsistency("Class '" + className
                  + "' could not be loaded.", Inconsistency.WARNING));
         }
      }
      return inconsistencies;
   }
}
