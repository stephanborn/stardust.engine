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
package org.eclipse.stardust.engine.core.model.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.eclipse.stardust.common.CollectionUtils;
import org.eclipse.stardust.common.SplicingIterator;
import org.eclipse.stardust.common.error.ObjectNotFoundException;
import org.eclipse.stardust.engine.api.model.*;
import org.eclipse.stardust.engine.api.runtime.BpmRuntimeError;
import org.eclipse.stardust.engine.api.runtime.DeploymentElement;
import org.eclipse.stardust.engine.api.runtime.ParsedDeploymentUnit;
import org.eclipse.stardust.engine.core.runtime.beans.AdministrationServiceImpl;
import org.eclipse.stardust.engine.core.runtime.beans.BpmRuntimeEnvironment;
import org.eclipse.stardust.engine.core.runtime.beans.ModelManagerFactory;
import org.eclipse.stardust.engine.core.runtime.beans.interceptors.PropertyLayerProviderInterceptor;
import org.eclipse.stardust.engine.core.runtime.utils.XmlUtils;

/**
 * @author rsauer
 * @version $Revision$
 */
public class ModelUtils
{
   private static final ModelElementList NULL_MODEL_ELEMENT_LIST =
      new ModelElementListAdapter(Collections.emptyList());

   private static final String PREDEFINED_MODEL_PATH = "/META-INF/resources/models/PredefinedModel.xpdl";

   public static <T extends ModelElement> ModelElementList<T> getModelElementList(List<T> modelElements)
   {
      if (modelElements == null)
      {
         return NULL_MODEL_ELEMENT_LIST;
      }
      return new ModelElementListAdapter(modelElements);
   }

   public static int nullSafeGetModelOID(ModelElement element)
   {
      return ((null != element) && (null != element.getModel()))
            ? element.getModel().getModelOID() : 0;
   }

   public static String nullSafeGetModelNamespace(ModelElement element)
   {
      return element == null ? null : element.getModel().getId();
   }

   public static String nullSafeGetID(Identifiable identifiable)
   {
      return (null != identifiable) ? identifiable.getId() : null;
   }

   public static String nullSafeGetName(Nameable nameable)
   {
      return (null != nameable) ? nameable.getName() : null;
   }

   public static IData getMappedData(IProcessDefinition process, String formalParameterId)
      throws ObjectNotFoundException
   {
      IData result = process.getMappedData(formalParameterId);
      if (null == result)
      {
         throw new ObjectNotFoundException(BpmRuntimeError.MDL_UNKNOWN_DATA_FOR_FORMAL_PARAMETER.raise(
               formalParameterId, getQualifiedId(process)), formalParameterId);
      }
      return result;
   }

   public static IData getData(IProcessDefinition process, String dataId)
      throws ObjectNotFoundException
   {
      IModel model = (IModel) process.getModel();
      IData result = model.findData(dataId);
      if (null == result)
      {
         throw new ObjectNotFoundException(
               BpmRuntimeError.MDL_UNKNOWN_DATA_ID.raise(dataId), dataId);
      }
      return result;
   }

   public static <T extends Identifiable> T findById(List<T> items, String id)
   {
      for (T item : items)
      {
         if (id.equals(item.getId()))
         {
            return item;
         }
      }
      return null;
   }

   public static Iterator iterator(Collection coll)
   {
      if (coll == null)
      {
         return Collections.emptyList().iterator();
      }
      return coll.iterator();
   }

   public static <V> List<V> trim(List<V> list)
   {
      if (list == null || list.isEmpty())
      {
         return Collections.emptyList();
      }
      if (list.size() == 1)
      {
         return Collections.singletonList(list.get(0));
      }
      if (list instanceof ArrayList)
      {
         ((ArrayList<V>) list).trimToSize();
         return list;
      }
      return new ArrayList<V>(list);
   }

   public static <K, V> Map<K, V> trim(Map<K, V> map)
   {
      if (map == null || map.isEmpty())
      {
         return Collections.emptyMap();
      }
      if (map.size() == 1)
      {
         K key = map.keySet().iterator().next();
         return Collections.singletonMap(key, map.get(key));
      }
      return new HashMap<K, V>(map);
   }

   public static String getQualifiedId(IdentifiableElement element)
   {
      if (element == null)
      {
         return null;
      }
      String id = element.getId();
      if (element instanceof IRole && id.equals(PredefinedConstants.ADMINISTRATOR_ROLE))
      {
         return PredefinedConstants.ADMINISTRATOR_ROLE;
      }
      RootElement model = element.getModel();
      if (model == null)
      {
         return id;
      }
      String modelId = model.getId();
      return '{' + (modelId == null ? "" : modelId) + '}' + id;
   }

   public static long size(Collection coll)
   {
      return coll == null ? 0 : coll.size();
   }

   public static List<IModel> findUsing(IModel used)
   {
      List<IModel> using = null;
      if (ModelManagerFactory.isAvailable())
      {
         BpmRuntimeEnvironment rtEnv = PropertyLayerProviderInterceptor.getCurrent();
         Iterator<IModel> overrideModelsIterator = rtEnv.getModelOverrides() != null
               ? rtEnv.getModelOverrides().values().iterator()
               : null;
         Iterator<IModel> allModelsIterator = ModelManagerFactory.getCurrent()
               .getAllModels();

         Iterator<IModel> models = new SplicingIterator<IModel>(
               overrideModelsIterator,
               allModelsIterator);
         while (models.hasNext())
         {
            IModel candidate = models.next();
            for (IExternalPackage pkg : candidate.getExternalPackages())
            {
               if (pkg.getReferencedModel().equals(used))
               {
                  (using == null ? using = CollectionUtils.newList() : using).add(candidate);
               }
            }
         }
      }
      return using == null ? Collections.<IModel>emptyList() : using;
   }

   public static List<IdentifiableElement> findUsing(IProcessDefinition used)
   {
      List<IdentifiableElement> using = null;
      for (IModel model : findUsing((IModel) used.getModel()))
      {
         for (IProcessDefinition pd : model.getProcessDefinitions())
         {
            if (isUsing(pd.getExternalReference(), used))
            {
               (using == null ? using = CollectionUtils.newList() : using).add(pd);
            }
            for (IActivity activity : pd.getActivities())
            {
               if (isUsing(activity.getExternalReference(), used))
               {
                  (using == null ? using = CollectionUtils.newList() : using).add(activity);
               }
            }
         }
      }
      return using == null ? Collections.<IdentifiableElement>emptyList() : using;
   }

   public static List<ParsedDeploymentUnit> getPredefinedModelElement()
   {
      byte[] content = null;
      try
      {
         InputStream in = AdministrationServiceImpl.class.getResourceAsStream(PREDEFINED_MODEL_PATH);
         content = XmlUtils.getContent(in);
      }
      catch (IOException e)
      {
         content = null;
      }
      if (content != null)
      {
         return Collections.singletonList(new ParsedDeploymentUnit(new DeploymentElement(content), 0));
      }
      return null;
   }

   private static boolean isUsing(IReference ref, IProcessDefinition process)
   {
      return ref != null && ref.getExternalPackage().getReferencedModel() == process.getModel()
          && ref.getId().equals(process.getId());
   }

   private ModelUtils()
   {
      // utility class
   }
}