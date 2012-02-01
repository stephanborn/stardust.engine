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

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.namespace.QName;

import org.eclipse.stardust.common.*;
import org.eclipse.stardust.common.config.Parameters;
import org.eclipse.stardust.common.error.ObjectNotFoundException;
import org.eclipse.stardust.common.log.LogManager;
import org.eclipse.stardust.common.log.LogUtils;
import org.eclipse.stardust.common.log.Logger;
import org.eclipse.stardust.common.reflect.Reflect;
import org.eclipse.stardust.engine.api.dto.DeploymentInfoDetails;
import org.eclipse.stardust.engine.api.model.*;
import org.eclipse.stardust.engine.api.runtime.*;
import org.eclipse.stardust.engine.core.model.utils.IdentifiableElement;
import org.eclipse.stardust.engine.core.model.utils.ModelElement;
import org.eclipse.stardust.engine.core.model.utils.ModelElementList;
import org.eclipse.stardust.engine.core.model.utils.ModelUtils;
import org.eclipse.stardust.engine.core.persistence.Predicates;
import org.eclipse.stardust.engine.core.persistence.QueryExtension;
import org.eclipse.stardust.engine.core.persistence.jdbc.SessionFactory;
import org.eclipse.stardust.engine.core.runtime.beans.interceptors.PropertyLayerProviderInterceptor;
import org.eclipse.stardust.engine.core.runtime.beans.removethis.SecurityProperties;
import org.eclipse.stardust.engine.core.spi.extensions.runtime.EventActionInstance;
import org.eclipse.stardust.engine.core.spi.extensions.runtime.EventHandlerInstance;



/**
 * @author ubirkemeyer
 * @version $Revision$
 */
public class ModelManagerBean implements ModelManager
{
   static final Logger trace = LogManager.getLogger(ModelManagerBean.class);

   private static final String CACHED_RT_OID = ModelManagerPartition.class.getName() + ".CachedRtOid";
   private static final Date MINUS_INFINITY_DATE = new Date(0);
   private static final Date PLUS_INFINITY_DATE = new Date(Long.MAX_VALUE);

   private final AbstractModelLoaderFactory loaderFactory;

   private Map /* <Short, ModelManagerPartition> */managerPartitions = new HashMap();

   public ModelManagerBean(AbstractModelLoaderFactory loaderFactory)
   {
      LogUtils.traceObject(this, true);

      this.loaderFactory = loaderFactory;
   }

   /**
    * Returns all appearances of the participants with the ID <tt>id</tt> in all loaded
    * model versions.
    */
   public Iterator getParticipantsForID(final String id)
   {
      return getModelManagerPartition().getParticipantsForID(id);
   }

   /**
    * Returns Active Model: The version with most recent Valid from Date, and goes into
    * future.
    */
   public IModel findActiveModel()
   {
      return getModelManagerPartition().findActiveModel();
   }

   public IModel findLastDeployedModel()
   {
      return getModelManagerPartition().findLastDeployedModel();
   }

   public IModel findLastDeployedModel(String id)
   {
      return getModelManagerPartition().findLastDeployedModel(id);
   }

   public IModel findModel(Predicate predicate)
   {
      return getModelManagerPartition().findModel(predicate);
   }

   public IModel findModel(long modelOID)
   {
      return getModelManagerPartition().findModel(modelOID);
   }

   public IModel findModel(long modelOID, String modelId)
   {
      return getModelManagerPartition().findModel(modelOID, modelId);
   }

   public boolean isActive(IModel model)
   {
      return getModelManagerPartition().isActive(model);
   }

   public DependentObjectsCache getDependentCache()
   {
      return getModelManagerPartition().getDependentCache();
   }

   public void reanimate(IModel model)
   {
      getModelManagerPartition().reanimate(model);
   }

   public List validateDeployment(ParsedDeploymentUnit unit, DeploymentOptions options)
   {
      return getModelManagerPartition().validateDeployment(unit, options);
   }

   public List validateOverwrite(IModel oldModel, IModel newModel)
   {
      return getModelManagerPartition().validateOverwrite(oldModel, newModel);
   }

   public int getModelCount()
   {
      return getModelManagerPartition().getModelCount();
   }

   public DeploymentInfo overwriteModel(ParsedDeploymentUnit unit, DeploymentOptions options)
   {
      return getModelManagerPartition().overwriteModel(unit, options);
   }

   public List<DeploymentInfo> deployModel(List<ParsedDeploymentUnit> units, DeploymentOptions options)
   {
      return getModelManagerPartition().deployModel(units, options);
   }

   public List<IModel> getModelsForId(String id)
   {
      return getModelManagerPartition().getModelsForId(id);
   }

   public Iterator<IModel> getAllModelsForId(String id)
   {
      return getModelManagerPartition().getAllModelsForId(id);
   }

   public Iterator<IModel> getAllModels()
   {
      return getModelManagerPartition().getAllModels();
   }

   public List<IModel> getModels()
   {
      return getModelManagerPartition().getModels();
   }

   public List<IModel> findActiveModels()
   {
      return getModelManagerPartition().findActiveModels();
   }

   public IModel findActiveModel(String modelId)
   {
      return getModelManagerPartition().findActiveModel(modelId);
   }

   public org.eclipse.stardust.engine.core.model.utils.ModelElement lookupObjectByOID(long oid)
   {
      return getModelManagerPartition().lookupObjectByOID(oid);
   }

   public IModelParticipant findModelParticipant(long modelOid, long runtimeOid)
   {
      return getModelManagerPartition().findModelParticipant(modelOid, runtimeOid);
   }

   public IModelParticipant findModelParticipant(ModelParticipantInfo info)
   {
      return getModelManagerPartition().findModelParticipant(info);
   }

   public IData findData(long modelOid, long runtimeOid)
   {
      return getModelManagerPartition().findData(modelOid, runtimeOid);
   }

   public IProcessDefinition findProcessDefinition(long modelOid, long runtimeOid)
   {
      return getModelManagerPartition().findProcessDefinition(modelOid, runtimeOid);
   }

   public IActivity findActivity(long modelOid, long runtimeOid)
   {
      return getModelManagerPartition().findActivity(modelOid, runtimeOid);
   }

   public ITransition findTransition(long modelOid, long runtimeOid)
   {
      return getModelManagerPartition().findTransition(modelOid, runtimeOid);
   }

   public IEventHandler findEventHandler(long modelOid, long runtimeOid)
   {
      return getModelManagerPartition().findEventHandler(modelOid, runtimeOid);
   }

   public long getRuntimeOid(IdentifiableElement element)
   {
      return getModelManagerPartition().getRuntimeOid(element);
   }

   public long getRuntimeOid(IData data, String xPath)
   {
      return getModelManagerPartition().getRuntimeOid(data, xPath);
   }

   public DeploymentInfo deleteModel(IModel model)
   {
      return getModelManagerPartition().deleteModel(model);
   }

   public void deleteAllModels()
   {
      getModelManagerPartition().deleteAllModels();
   }

   public Iterator getAllAliveModels()
   {
      return getModelManagerPartition().getAllAliveModels();
   }

   public boolean isAlive(IModel model)
   {
      return getModelManagerPartition().isAlive(model);
   }

   public ModelManagerPartition getModelManagerPartition()
   {
      ModelManagerPartition modelManagerPartition;

      final BpmRuntimeEnvironment layer = PropertyLayerProviderInterceptor.getCurrent();
      if (null != layer)
      {
         modelManagerPartition = (ModelManagerPartition) layer.getModelManager();
         if (null == modelManagerPartition)
         {
            short partitionOid = SecurityProperties.getPartitionOid();
            modelManagerPartition = getModelManagerPartition(partitionOid);
            layer.setModelManager(modelManagerPartition);
         }
      }
      else
      {
         short partitionOid = SecurityProperties.getPartitionOid();
         modelManagerPartition = getModelManagerPartition(partitionOid);
      }

      return modelManagerPartition;
   }

   public synchronized void releaseModelManagerPartition(ModelManager modelManager)
   {
      if ((modelManager instanceof ModelManagerPartition)
            && managerPartitions.containsValue(modelManager))
      {
         managerPartitions.remove(new Short(((ModelManagerPartition) modelManager)
               .getPartitionOid()));
         //also clear runtime environment from model manager
         BpmRuntimeEnvironment rtEnv = PropertyLayerProviderInterceptor.getCurrent();
         if(rtEnv != null)
         {
            rtEnv.setModelManager(null);
         }
      }
   }

   protected synchronized ModelManagerPartition getModelManagerPartition(
         short partitionOid)
   {
      ModelManagerPartition managerPartition;
      Short partitionOidValue = new Short(partitionOid);
      if (managerPartitions.containsKey(partitionOidValue))
      {
         managerPartition = (ModelManagerPartition) managerPartitions
               .get(partitionOidValue);
      }
      else
      {
         managerPartition = new ModelManagerPartition(partitionOid, loaderFactory
               .instance(partitionOid));
         managerPartitions.put(partitionOidValue, managerPartition);

         // must be done after the complete initialization and inclusion into the list of partitions,
         // otherwise we get an endless loop / stack overflow
         managerPartition.resolveModelReferences();
      }

      return managerPartition;
   }

   private static void appendFqIds(Map allFqIds, ModelManagerPartition modelManager,
         ModelElementList elements)
   {
      for (int i = 0; i < elements.size(); ++i)
      {
         final IdentifiableElement element = (IdentifiableElement) elements.get(i);

         long rtOid = modelManager.getRuntimeOid(element);
         String[] fqId = modelManager.rtOidRegistry.getFqId(getElementType(element),
               rtOid);

         allFqIds.put(RuntimeOidUtils.internalizeFqId(fqId), element);
      }
   }

   private static Map getAllFqIds(ModelManagerPartition modelManager, IModel model)
   {
      Assert.isNotNull(modelManager.findModel(model.getModelOID()));

      Map allFqIds = new HashMap();

      appendFqIds(allFqIds, modelManager, model.getProcessDefinitions());
      appendFqIds(allFqIds, modelManager, model.getData());
      appendFqIds(allFqIds, modelManager, model.getParticipants());

      for (int i = 0; i < model.getProcessDefinitions().size(); ++i)
      {
         IProcessDefinition processDefinition = (IProcessDefinition) model
               .getProcessDefinitions().get(i);

         appendFqIds(allFqIds, modelManager, processDefinition.getActivities());
         appendFqIds(allFqIds, modelManager, processDefinition.getTransitions());
         appendFqIds(allFqIds, modelManager, processDefinition.getTriggers());
         appendFqIds(allFqIds, modelManager, processDefinition.getEventHandlers());
      }

      return allFqIds;
   }

   /**
    * @param element
    * @return
    */
   private static IRuntimeOidRegistry.ElementType getElementType(
         IdentifiableElement element)
   {
      IRuntimeOidRegistry.ElementType type;
      if (element instanceof IData)
      {
         type = IRuntimeOidRegistry.DATA;
      }
      else if (element instanceof IModelParticipant)
      {
         type = IRuntimeOidRegistry.PARTICIPANT;
      }
      else if (element instanceof IProcessDefinition)
      {
         type = IRuntimeOidRegistry.PROCESS;
      }
      else if (element instanceof ITrigger)
      {
         type = IRuntimeOidRegistry.TRIGGER;
      }
      else if (element instanceof IActivity)
      {
         type = IRuntimeOidRegistry.ACTIVITY;
      }
      else if (element instanceof ITransition)
      {
         type = IRuntimeOidRegistry.TRANSITION;
      }
      else if (element instanceof IEventHandler)
      {
         type = IRuntimeOidRegistry.EVENT_HANDLER;
      }
      else
      {
         type = null;
      }

      return type;
   }

   private static class MyDependentObjectsCache<T> implements DependentObjectsCache<T>
   {
      private final Map<Long, ModelCache<T>> modelCaches = new ConcurrentHashMap<Long, ModelCache<T>>();

      private static final ModelCache EMPTY_CACHE = new ModelCache();

      public Iterator<T> getEmitters()
      {
         if (modelCaches.isEmpty())
         {
            return Collections.<T> emptyList().iterator();
         }

         final Iterator<ModelCache<T>> i = modelCaches.values().iterator();
         return new Iterator<T>()
         {
            private Iterator<T> currentCache = (i.next()).getEmitters();

            public boolean hasNext()
            {
               if (currentCache == null)
               {
                  return false;
               }
               if (currentCache.hasNext())
               {
                  return true;
               }
               nextCache();
               return hasNext();
            }

            public T next()
            {
               if (currentCache == null)
               {
                  throw new NoSuchElementException();
               }
               if (currentCache.hasNext())
               {
                  return currentCache.next();
               }
               nextCache();
               return next();
            }

            public void remove()
            {
               throw new UnsupportedOperationException();
            }

            private void nextCache()
            {
               if (i.hasNext())
               {
                  currentCache = (i.next()).getEmitters();
               }
               else
               {
                  currentCache = null;
               }
            }
         };
      }

      public EventHandlerInstance getHandlerInstance(IEventHandler handler)
      {
         return getModelCache(handler).getCondition(handler);
      }

      public EventActionInstance getActionInstance(IAction action)
      {
         return getModelCache(action).getActionInstance(action);
      }

      public T get(Object key)
      {
         ModelCache<T> cache = getModelCache(key);
         return cache.getDetail(key);
      }

      public void put(Object key, T detail)
      {
         ModelCache<T> cache = getModelCache(key);
         if (EMPTY_CACHE != cache)
         {
            cache.setDetail(key, detail);
         }
      }

      public void reload(IModel model)
      {
         Long modelOID = Long.valueOf(model.getModelOID());
         modelCaches.put(modelOID, new ModelCache<T>(model));
      }

      private ModelCache<T> getModelCache(Object element)
      {
         ModelCache<T> cache;

         IModel model = null;
         if (element instanceof Pair)
         {
            element = ((Pair) element).getFirst();
         }
         if (element instanceof ModelElement)
         {
            model = (IModel) ((ModelElement) element).getModel();
         }
         if (null != model)
         {
            final Long modelOID = Long.valueOf(model.getModelOID());

            cache = modelCaches.get(modelOID);

            if (null == cache)
            {
               cache = new ModelCache<T>(model);
               modelCaches.put(modelOID, cache);
            }
         }
         else
         {
            cache = EMPTY_CACHE;
         }

         return cache;
      }

      private void clear()
      {
         modelCaches.clear();
      }

      private void remove(IModel model)
      {
         modelCaches.remove(Long.valueOf(model.getModelOID()));
      }
   }

   private static class ModelCache<T>
   {
      private final List emitters;

      private final Map<IEventHandler, EventHandlerInstance> conditionInstances;

      private final Map actionInstances;

      private final Map<Object, T> details;

      ModelCache(IModel model)
      {
         this.emitters = new ArrayList();
         this.conditionInstances = new ConcurrentHashMap<IEventHandler, EventHandlerInstance>();
         this.actionInstances = new ConcurrentHashMap();
         this.details = new ConcurrentHashMap<Object, T>();

         for (int i = 0; i < model.getProcessDefinitions().size(); ++i)
         {
            IProcessDefinition process = (IProcessDefinition) model
                  .getProcessDefinitions().get(i);

            inspectHandlerOwner(process);
            for (int j = 0; j < process.getActivities().size(); ++j)
            {
               inspectHandlerOwner((IActivity) process.getActivities().get(j));
            }
         }
      }

      /**
       * Initializes an empty, unmodifiable model cache.
       */
      ModelCache()
      {
         this.emitters = Collections.EMPTY_LIST;
         this.conditionInstances = Collections.emptyMap();
         this.actionInstances = Collections.EMPTY_MAP;
         this.details = Collections.emptyMap();
      }

      public Iterator getEmitters()
      {
         return emitters.iterator();
      }

      public EventHandlerInstance getCondition(IEventHandler handler)
      {
         return conditionInstances.get(handler);
      }

      /*public Iterator getActions(IEventHandler handler)
      {
         return ((Collection) actionInstances.get(handler)).iterator();
      }*/

      public EventActionInstance getActionInstance(IAction action)
      {
         return (EventActionInstance) actionInstances.get(action);
      }

      public T getDetail(Object key)
      {
         return details.get(getDetailsKey(key));
      }

      public void setDetail(Object key, T detail)
      {
         details.put(getDetailsKey(key), detail);
      }

      private Object getDetailsKey(Object key)
      {
         Object kv = null;
         if (key instanceof ModelElement)
         {
            ModelElement me = (ModelElement) key;
            kv = Long.valueOf(me.getElementOID());
         }
         return kv;
      }

      private void inspectHandlerOwner(EventHandlerOwner process)
      {
         for (int k = 0; k < process.getEventHandlers().size(); ++k)
         {
            IEventHandler handler = (IEventHandler) process.getEventHandlers().get(k);
            if (((IEventConditionType) handler.getType()).getImplementation() == EventType.Pull)
            {
               String emitter = handler.getType().getStringAttribute(
                     PredefinedConstants.PULL_EVENT_EMITTER_ATT);

               if (StringUtils.isEmpty(emitter))
               {
                  emitters.add(Reflect
                        .createInstance(PredefinedConstants.DEFAULT_EVENT_EMITTER_CLASS));
               }
               else
               {
                  try
                  {
                     emitters.add(Reflect.createInstance(emitter));
                  }
                  catch (Exception e)
                  {
                     trace.warn("No emitter '" + emitter + "' found. Reason: ", e);
                  }
               }
            }
            else
            {
               EventHandlerInstance condition = EventUtils.createHandlerInstance(handler);

               condition.bootstrap(handler.getAllAttributes());
               conditionInstances.put(handler, condition);
               for (Iterator l = handler.getAllEventActions(); l.hasNext();)
               {
                  IEventAction action = (IEventAction) l.next();
                  EventActionInstance instance = EventUtils.createActionInstance(action);
                  instance.bootstrap(action.getAllAttributes(), handler
                        .getAllAccessPoints());
                  actionInstances.put(action, instance);
               }
            }
         }
      }
   }

   private class HeatingEntry
   {
      //private Date from;

      private Date to;

      private long oid;

      public HeatingEntry(long oid, /*Date from,*/ Date to)
      {
         this.oid = oid;
         //this.from = from;
         this.to = to;
      }

      /*public Date getFrom()
      {
         return from;
      }*/

      public Date getTo()
      {
         return to;
      }

      public long getModelOID()
      {
         return oid;
      }
   }

   // TODO (kafka): assure that models belong to the current partition.
   public class ModelManagerPartition implements ModelManager
   {
      private static final String NO_MODEL_WITH_OID = "No model with oid {0} found.";
      private static final String NO_ACTIVE_MODEL = "No model active.";
      private static final String NO_MODELS = "No model deployed.";
      private final short partitionOid;
      private final List<IModel> models;
      private final List<IModel> unorderedModels;

      private final ModelLoader loader;

      private HashSet deadModels = new HashSet();

      private List heatingEntries = new LinkedList();

      private MyDependentObjectsCache dependentObjectCache = new MyDependentObjectsCache();

      private final IRuntimeOidRegistry rtOidRegistry;

      private class ElementByRtOidCache
      {
         public IData[] data;

         public IProcessDefinition[] processes;

         public IActivity[] activities;

         public ITransition[] transitions;
      }

      private ElementByRtOidCache[] elementsByRtOid;

      public ModelManagerPartition(short partitionOid, ModelLoader loader)
      {
         this.partitionOid = partitionOid;
         this.models = CollectionUtils.newList();
         this.unorderedModels = CollectionUtils.newList();
         this.loader = loader;

         this.rtOidRegistry = new RuntimeOidRegistry(partitionOid);

         loader.loadRuntimeOidRegistry(rtOidRegistry);

         // sort with ascending model OID
         Map<Long, IModelPersistor> loadedModels = new TreeMap();
         for (Iterator i = loader.loadModels(); i.hasNext();)
         {
            IModelPersistor persistor = (IModelPersistor) i.next();
            loadedModels.put(persistor.getModelOID(), persistor);
            unorderedModels.add(persistor.fetchModel());
         }
         Collections.reverse(unorderedModels);

         // building list of models, paying attention to predecessor relationship
         while (!loadedModels.isEmpty())
         {
            boolean progressing = false;

            for (Iterator i = loadedModels.values().iterator(); i.hasNext();)
            {
               IModelPersistor mPersistor = (IModelPersistor) i.next();
               IModel predecessor = findModel(mPersistor.getPredecessorOID());
               if (null != predecessor)
               {
                  models.add(models.indexOf(predecessor) + 1, mPersistor.fetchModel());
                  i.remove();
                  progressing = true;
               }
               else if (!loadedModels.containsKey(new Long(mPersistor.getPredecessorOID())))
               {
                  // there is no predecessor available
                  models.add(0, mPersistor.fetchModel());
                  i.remove();
                  progressing = true;
               }
            }

            if (!progressing && !loadedModels.isEmpty())
            {
               // remaining models most probably are linked with circular predecessors
               trace.warn(MessageFormat.format(
                     "Unable to properly resolve model predecessor relationship for models "
                           + "with OIDs {0}. Please check for circularity.",
                     new Object[] {loadedModels.keySet()}));

               for (Iterator i = loadedModels.values().iterator(); i.hasNext();)
               {
                  IModelPersistor mPersistor = (IModelPersistor) i.next();
                  addModel(mPersistor.getPredecessorOID(), mPersistor.fetchModel(), false);
                  i.remove();
               }
            }
         }

         if (loader instanceof RuntimeModelLoader)
         {
            // setting the archive flag. This should override any previous settings, i.e.
            // from carnot.properties.
            boolean archive = PropertyPersistor
            .findByName(Constants.CARNOT_ARCHIVE_AUDITTRAIL) != null;
            Parameters.instance()
            .setBoolean(Constants.CARNOT_ARCHIVE_AUDITTRAIL, archive);

            // Load predefined model only if a model is already deployed and it does not exist.
            // This only happens on runtime upgrade. Usually the predefined model is deployed with the first model deployment.
            if ( !archive && getModelCount() > 0
                  && null == findActiveModel(PredefinedConstants.PREDEFINED_MODEL_ID))
            {
               List<ParsedDeploymentUnit> predefinedModelElement = ModelUtils.getPredefinedModelElement();
               if (predefinedModelElement != null)
               {
                  loader.deployModel(predefinedModelElement, DeploymentOptions.DEFAULT,
                        rtOidRegistry);

                  Iterator<IModelPersistor> loadedModelsIncludingPredefinedModel = loader.loadModels();
                  while (loadedModelsIncludingPredefinedModel.hasNext())
                  {
                     IModelPersistor persistedModel = loadedModelsIncludingPredefinedModel.next();
                     IModel model = persistedModel.fetchModel();
                     if (PredefinedConstants.PREDEFINED_MODEL_ID.equals(model.getId()))
                     {
                        models.add(0, model);
                     }
                  }
               }
               else
               {
                  trace.warn("Could not load PredefinedModel.xpdl");
               }
            }

         }

         recomputeAlivenessCache();

         for (Iterator i = getModels().iterator(); i.hasNext();)
         {
            ((MyDependentObjectsCache) getDependentCache()).reload((IModel) i.next());
         }

         // initialize RT-OID reverse lookup
         long maxModelOid = 0;
         long maxDataRtOid = 0;
         long maxProcessRtOid = 0;
         long maxActivityRtOid = 0;
         long maxTransitionRtOid = 0;
         for (IModel model : models)
         {
            long modelOID = model.getModelOID();
            maxModelOid = Math.max(maxModelOid, modelOID);

            for (int j = 0; j < model.getData().size(); ++j)
            {
               IData data = (IData) model.getData().get(j);
               maxDataRtOid = Math.max(maxDataRtOid, getRuntimeOid(data));
            }

            for (int j = 0; j < model.getProcessDefinitions().size(); ++j)
            {
               IProcessDefinition process = (IProcessDefinition) model
                     .getProcessDefinitions().get(j);
               maxProcessRtOid = Math.max(maxProcessRtOid, getRuntimeOid(process));

               for (int k = 0; k < process.getActivities().size(); ++k)
               {
                  IActivity activity = (IActivity) process.getActivities().get(k);
                  maxActivityRtOid = Math.max(maxActivityRtOid, getRuntimeOid(activity));
               }

               for (int k = 0; k < process.getTransitions().size(); ++k)
               {
                  ITransition transition = (ITransition) process.getTransitions().get(k);
                  maxTransitionRtOid = Math.max(maxTransitionRtOid,
                        getRuntimeOid(transition));
               }
            }
         }

         this.elementsByRtOid = new ElementByRtOidCache[(int) (maxModelOid + 1)];

         for (IModel model : models)
         {
            elementsByRtOid[model.getModelOID()] = new ElementByRtOidCache();
            ElementByRtOidCache byRtOid = this.elementsByRtOid[model.getModelOID()];

            byRtOid.data = (maxDataRtOid <= 1000000)
                  ? new IData[(int) maxDataRtOid + 1]
                  : null;
            byRtOid.processes = (maxProcessRtOid <= 1000000)
                  ? new IProcessDefinition[(int) maxProcessRtOid + 1]
                  : null;
            byRtOid.activities = (maxActivityRtOid <= 1000000)
                  ? new IActivity[(int) maxActivityRtOid + 1]
                  : null;
            byRtOid.transitions = (maxTransitionRtOid <= 1000000)
                  ? new ITransition[(int) maxTransitionRtOid + 1]
                  : null;

            if (null != byRtOid.data)
            {
               for (int j = 0; j < model.getData().size(); ++j)
               {
                  IData data = (IData) model.getData().get(j);
                  byRtOid.data[(int) getRuntimeOid(data)] = data;
               }
            }

            for (int j = 0; j < model.getProcessDefinitions().size(); ++j)
            {
               IProcessDefinition process = (IProcessDefinition) model
                     .getProcessDefinitions().get(j);
               if (null != byRtOid.processes)
               {
                  byRtOid.processes[(int) getRuntimeOid(process)] = process;
               }

               if (null != byRtOid.activities)
               {
                  for (int k = 0; k < process.getActivities().size(); ++k)
                  {
                     IActivity activity = (IActivity) process.getActivities().get(k);
                     byRtOid.activities[(int) getRuntimeOid(activity)] = activity;
                  }
               }

               if (null != byRtOid.transitions)
               {
                  for (int k = 0; k < process.getTransitions().size(); ++k)
                  {
                     ITransition transition = (ITransition) process.getTransitions().get(
                           k);
                     byRtOid.transitions[(int) getRuntimeOid(transition)] = transition;
                  }
               }
            }
         }
      }

      private void resolveModelReferences()
      {
         for (IModel model : models)
         {
            List<IExternalPackage> references = model.getExternalPackages();
            for (IExternalPackage pkg : references)
            {
               try
               {
                  pkg.getReferencedModel();
               }
               catch (UnresolvedExternalReference ex)
               {
                  // ignore.
               }
            }
         }
      }

      public short getPartitionOid()
      {
         return partitionOid;
      }

      public List<IModel> getModels()
      {
         return models;
      }

      public void recomputeAlivenessCache()
      {
         TreeSet ts = new TreeSet();

         for (IModel model : models)
         {
            Date from = (Date) model.getAttribute(PredefinedConstants.VALID_FROM_ATT);
            Date to = (Date) model.getAttribute(PredefinedConstants.VALID_TO_ATT);
            if (from == null)
            {
               from = MINUS_INFINITY_DATE;
            }
            if (to == null)
            {
               to = PLUS_INFINITY_DATE;
            }
            ts.add(from);
            ts.add(to);
         }

         heatingEntries.clear();
         Object[] ta = ts.toArray();
         for (int i = 0; i < ta.length - 1; i++)
         {
            Date from = (Date) ta[i];
            Date to = (Date) ta[i + 1];
            IModel valid = findActiveModel(from);
            heatingEntries.add(new HeatingEntry(valid != null ? valid.getModelOID() : 0,
                  /*from,*/ to));
         }

         for (Iterator i = deadModels.iterator(); i.hasNext();)
         {
            IModel model = (IModel) i.next();
            if (isHeated(model))
            {
               i.remove();
            }
         }
      }

      public void release()
      {
         ModelManagerBean.this.releaseModelManagerPartition(this);
      }

      public IModel findLastDeployedModel()
      {
         if (!unorderedModels.isEmpty())
         {
            return unorderedModels.get(0);
         }
         return null;
      }

      public IModel findLastDeployedModel(String id)
      {
         for (IModel candidate : unorderedModels)
         {
            if (CompareHelper.areEqual(candidate.getId(), id))
            {
               return candidate;
            }
         }
         return null;
      }

      private List<IModel> findLastDeployedModels()
      {
         Date now = new Date();
         List<IModel> result = CollectionUtils.newList();
         Set<String> ids = CollectionUtils.newSet();
         for (IModel candidate : unorderedModels)
         {
            Date from = (Date) candidate.getAttribute(PredefinedConstants.VALID_FROM_ATT);
            String id = candidate.getId();
            if (!ids.contains(id) && (from == null || from.before(now)))
            {
               result.add(candidate);
               ids.add(id);
            }
         }
         return result;
      }

      /**
       * Returns Active Model: The version with most recent Valid from Date, and goes into
       * future.
       */
      public IModel findActiveModel()
      {
         return findActiveModel(new Date());
      }

      public List<IModel> findActiveModels()
      {
         return findActiveModels(new Date());
      }

      public void reanimate(IModel model)
      {
         if (deadModels.contains(model))
         {
            deadModels.remove(model);
         }
      }

      public void deleteAllModels()
      {
         dependentObjectCache.clear();
         models.clear();
         deadModels.clear();
         heatingEntries.clear();
      }

      public DeploymentInfo deleteModel(IModel model)
      {
         DeploymentInfoDetails result = new DeploymentInfoDetails(model);

         dependentObjectCache.remove(model);
         detachModel(model);
         deadModels.remove(model);

         ModelPersistorBean persistor = ModelPersistorBean.findByModelOID(model
               .getModelOID());
         persistor.delete();

         DeploymentUtils.attachDeploymentAttributes(result, model);

         recomputeAlivenessCache();

         return result;
      }

      public List<DeploymentInfo> deployModel(List<ParsedDeploymentUnit> units, DeploymentOptions options)
      {
         BpmRuntimeEnvironment runtimeEnvironment = PropertyLayerProviderInterceptor.getCurrent();
         List<DeploymentInfo> infos = CollectionUtils.newList(units.size());
         boolean valid = true;
         boolean errors = false;
         try
         {
            runtimeEnvironment.setModelOverrides(prepareOverrides(units));
            for (ParsedDeploymentUnit unit : units)
            {
               IModel model = unit.getModel();
               Date validFrom = options.getValidFrom();
               DeploymentInfoDetails info = new DeploymentInfoDetails(
                     validFrom == null ? (Date) model.getAttribute(PredefinedConstants.VALID_FROM_ATT) : validFrom,
                     model.getId(), options.getComment());
               info.addInconsistencies(validateDeployment(unit, options));
               infos.add(info);
               valid = valid && info.isValid();
               errors = errors || info.hasErrors();
            }
         }
         finally
         {
            runtimeEnvironment.setModelOverrides(null);
         }

         if (errors || !options.isIgnoreWarnings() && !valid)
         {
            return infos;
         }

         boolean replicateRoles = SecurityProperties.isInternalAuthentication()
               || SecurityProperties.isInternalAuthorization();

         Set<String> oldModelIds = Collections.emptySet();
         if (replicateRoles && !models.isEmpty())
         {
            oldModelIds = CollectionUtils.newSet();
            for (ParsedDeploymentUnit unit : units)
            {
               String id = unit.getModel().getId();
               IModel oldModel = getCurrentModel(id);
               if (oldModel != null)
               {
                  oldModelIds.add(id);
               }
            }
         }

         loader.deployModel(units, options, rtOidRegistry);

         for (ParsedDeploymentUnit unit : units)
         {
            IModel model = unit.getModel();
            addModel(unit.getReferencedModelOid(), model, true);
            if (replicateRoles)
            {
               if (!oldModelIds.contains(model.getId()))
               {
                  IRole role = (IRole) model.findParticipant(PredefinedConstants.ADMINISTRATOR_ROLE);
                  SecurityProperties.getUser().addToParticipants(role, null);
               }
            }
         }

         recomputeAlivenessCache();

         ModelManagerFactory.setDirty();
         SynchronizationService.flush();

         for (int i = 0; i < infos.size(); i++)
         {
            DeploymentUtils.attachDeploymentAttributes(
                  (DeploymentInfoDetails) infos.get(i), units.get(i).getModel());
         }

         return infos;
      }

      private Map<String, IModel> prepareOverrides(List<ParsedDeploymentUnit> units)
      {
         Map<String, IModel> overrides = CollectionUtils.newMap();
         List<IModel> lastDeployedModels = findLastDeployedModels();
         for (IModel model : lastDeployedModels)
         {
            overrides.put(model.getId(), model);
         }
         for (ParsedDeploymentUnit unit : units)
         {
            // TODO: which model to put here? Raw or varEvaluated version?
            IModel model = unit.getModel();
            overrides.put(model.getId(), model);
         }
         return overrides;
      }

      private IModel getCurrentModel(String id)
      {
         IModel lastDeployedModel = null;
         Date now = new Date();
         for (IModel model : models)
         {
            if (!id.equals(model.getId()))
            {
               continue;
            }
            if (lastDeployedModel == null)
            {
               lastDeployedModel = model;
            }
            if (model.getBooleanAttribute(PredefinedConstants.IS_DISABLED_ATT))
            {
               continue;
            }
            Date from = (Date) model.getAttribute(PredefinedConstants.VALID_FROM_ATT);
            Date to = (Date) model.getAttribute(PredefinedConstants.VALID_TO_ATT);
            if (from == null)
            {
               from = MINUS_INFINITY_DATE;
            }
            if (to == null)
            {
               to = PLUS_INFINITY_DATE;
            }
            if (!now.before(from) && now.before(to))
            {
               return model;
            }
         }
         return lastDeployedModel;
      }

      public IModel findActiveModel(String id)
      {
         Date now = new Date();
         for (IModel model : models)
         {
            if (!id.equals(model.getId()))
            {
               continue;
            }

            if (model.getId().equals(PredefinedConstants.PREDEFINED_MODEL_ID)
                  && !id.equals(PredefinedConstants.PREDEFINED_MODEL_ID))
            {
               continue;
            }            
            
            if (model.getBooleanAttribute(PredefinedConstants.IS_DISABLED_ATT))
            {
               continue;
            }
            Date from = (Date) model.getAttribute(PredefinedConstants.VALID_FROM_ATT);
            Date to = (Date) model.getAttribute(PredefinedConstants.VALID_TO_ATT);
            if (from == null)
            {
               from = MINUS_INFINITY_DATE;
            }
            if (to == null)
            {
               to = PLUS_INFINITY_DATE;
            }
            if (!now.before(from) && now.before(to))
            {
               return model;
            }
         }
         return null;
      }

      public IActivity findActivity(long modelOid, long runtimeOid)
      {
         IActivity activity = null;

         ElementByRtOidCache byRtOid = (modelOid < elementsByRtOid.length)
               ? elementsByRtOid[(int) modelOid]
               : null;
         if ((null != byRtOid) && (null != byRtOid.activities)
               && (runtimeOid < byRtOid.activities.length))
         {
            activity = byRtOid.activities[(int) runtimeOid];
         }

         if (null == activity)
         {
            IModel model = findModel(modelOid);
            if (null != model)
            {
               String[] fqId = rtOidRegistry.getFqId(IRuntimeOidRegistry.ACTIVITY,
                     runtimeOid);
               IProcessDefinition process = model
                     .findProcessDefinition(fqId[fqId.length - 2]);
               if (null != process)
               {
                  activity = process.findActivity(fqId[fqId.length - 1]);
               }
            }
         }

         return activity;
      }

      public IData findData(long modelOid, long runtimeOid)
      {
         IData data = null;

         ElementByRtOidCache byRtOid = (modelOid < elementsByRtOid.length)
               ? elementsByRtOid[(int) modelOid]
               : null;
         if ((null != byRtOid) && (null != byRtOid.data)
               && (runtimeOid < byRtOid.data.length))
         {
            data = byRtOid.data[(int) runtimeOid];
         }

         if (null == data)
         {
            IModel model = findModel(modelOid);
            if (null != model)
            {
               String[] fqId = rtOidRegistry
                     .getFqId(IRuntimeOidRegistry.DATA, runtimeOid);
               data = model.findData(fqId[fqId.length - 1]);
            }
         }

         return data;
      }

      public IEventHandler findEventHandler(long modelOid, long runtimeOid)
      {
         IEventHandler handler = null;

         IModel model = findModel(modelOid);
         if (null != model)
         {
            // TODO
            String[] fqId = rtOidRegistry.getFqId(IRuntimeOidRegistry.EVENT_HANDLER, runtimeOid);
            boolean processHandler = fqId.length == 3;
            IProcessDefinition process = model.findProcessDefinition(fqId[fqId.length - (processHandler ? 2 : 3)]);
            if (null != process)
            {
               if (processHandler)
               {
                  handler = process.findHandlerById(fqId[fqId.length - 1]);
               }
               else
               {
                  IActivity activity = process.findActivity(fqId[fqId.length - 2]);
                  if (null != activity)
                  {
                     handler = activity.findHandlerById(fqId[fqId.length - 1]);
                  }
               }
            }
         }
         return handler;
      }

      public IModel findModel(Predicate predicate)
      {
         IModel activeModel = findActiveModel();
         if (activeModel != null && predicate.accept(activeModel))
         {
            return activeModel;
         }
         for (Iterator i = getAllModels(); i.hasNext();)
         {
            IModel model = (IModel) i.next();
            if (model != activeModel && predicate.accept(model))
            {
               return model;
            }
         }
         return null;
      }

      public IModel findModel(long modelOID)
      {
         IModel model = null;
         switch ((int) modelOID)
         {
         case PredefinedConstants.ACTIVE_MODEL:
         case PredefinedConstants.ANY_MODEL:
            model = findActiveModel();
            if (model != null || modelOID == PredefinedConstants.ACTIVE_MODEL)
            {
               break;
            }
         case PredefinedConstants.LAST_DEPLOYED_MODEL:
            model = findLastDeployedModel();
            break;
         default:
            for (Iterator i = getAllModels(); i.hasNext();)
            {
               IModel aModel = (IModel) i.next();
               if (aModel.getModelOID() == modelOID)
               {
                  model = aModel;
                  break;
               }
            }
         }
         return model;
      }

      public IModel findModel(long modelOID, String modelId)
      {
         IModel model = null;
         switch ((int) modelOID)
         {
         case PredefinedConstants.ACTIVE_MODEL:
         case PredefinedConstants.ANY_MODEL:
            model = findActiveModel(modelId);
            if (model != null || modelOID == PredefinedConstants.ACTIVE_MODEL)
            {
               break;
            }
         case PredefinedConstants.LAST_DEPLOYED_MODEL:
            model = findLastDeployedModel(modelId);
            break;
         default:
            for (Iterator i = getAllModels(); i.hasNext();)
            {
               IModel aModel = (IModel) i.next();
               if (aModel.getModelOID() == modelOID && aModel.getId().equals(modelId))
               {
                  model = aModel;
                  break;
               }
            }
         }
         return model;
      }

      public IModelParticipant findModelParticipant(long modelOid, long runtimeOid)
      {
         String[] fqId = rtOidRegistry.getFqId(IRuntimeOidRegistry.PARTICIPANT, runtimeOid);
         if (fqId.length == 0)
         {
            return null;
         }
         final String modelId = fqId.length == 1 ? null : fqId[0];
         final String id = fqId.length == 1 ? fqId[0] : fqId[1];
         final IModelParticipant[] participant = new IModelParticipant[1];
         if (modelOid == PredefinedConstants.ANY_MODEL)
         {
            findModel(new Predicate()
            {
               public boolean accept(Object o)
               {
                  IModel model = (IModel) o;
                  if (modelId != null && !modelId.equals(model.getId()))
                  {
                     return false;
                  }
                  participant[0] = model.findParticipant(id);
                  return participant[0] != null;
               }
            });
         }
         else
         {
            IModel model = findModel(modelOid);
            if (null != model)
            {
               participant[0] = model.findParticipant(id);
            }
         }
         return participant[0];
      }

      public IModelParticipant findModelParticipant(ModelParticipantInfo info)
      {
         long rtOid = info.getRuntimeElementOID();
         IModelParticipant participant = rtOid == 0 ? null : findModelParticipant(
               PredefinedConstants.ANY_MODEL, rtOid);
         if (participant == null)
         {
            String orgId = info.getId();
            if (orgId != null && rtOid == 0)
            {
               Iterator<IModelParticipant> participants = getParticipantsForID(orgId);
               if (participants.hasNext())
               {
                  participant = participants.next();
               }
               else
               {
                  throw new ObjectNotFoundException(
                        BpmRuntimeError.MDL_UNKNOWN_PARTICIPANT_ID.raise(orgId), info
                              .toString());
               }
            }
            else
            {
               throw new ObjectNotFoundException(
                     BpmRuntimeError.MDL_UNKNOWN_PARTICIPANT_RUNTIME_OID.raise(rtOid),
                     info.toString());
            }
         }
         return participant;
      }

      public IProcessDefinition findProcessDefinition(long modelOid, long runtimeOid)
      {
         IProcessDefinition process = null;

         ElementByRtOidCache byRtOid = (modelOid < elementsByRtOid.length)
               ? elementsByRtOid[(int) modelOid]
               : null;
         if ((null != byRtOid) && (null != byRtOid.processes)
               && (runtimeOid < byRtOid.processes.length))
         {
            process = byRtOid.processes[(int) runtimeOid];
         }

         if (null == process)
         {
            IModel model = findModel(modelOid);
            if (null != model)
            {
               String[] fqId = rtOidRegistry.getFqId(IRuntimeOidRegistry.PROCESS,
                     runtimeOid);
               process = model.findProcessDefinition(fqId[fqId.length - 1]);
            }
         }

         return process;
      }

      public ITransition findTransition(long modelOid, long runtimeOid)
      {
         ITransition transition = null;

         ElementByRtOidCache byRtOid = (modelOid < elementsByRtOid.length)
               ? elementsByRtOid[(int) modelOid]
               : null;
         if ((null != byRtOid) && (null != byRtOid.transitions)
               && (runtimeOid < byRtOid.transitions.length))
         {
            transition = byRtOid.transitions[(int) runtimeOid];
         }

         if (null == transition)
         {
            IModel model = findModel(modelOid);
            if (null != model)
            {
               String[] fqId = rtOidRegistry.getFqId(IRuntimeOidRegistry.TRANSITION,
                     runtimeOid);
               IProcessDefinition process = model.findProcessDefinition(fqId[fqId.length - 2]);
               if (null != process)
               {
                  transition = process.findTransition(fqId[fqId.length - 1]);
               }
            }
         }

         return transition;
      }

      public Iterator getAllAliveModels()
      {
         return new FilteringIterator(getAllModels(), new Predicate()
         {
            public boolean accept(Object o)
            {
               return isAlive((IModel) o);
            }
         });
      }

      public List<IModel> getModelsForId(String id)
      {
         List<IModel> modelsForId = CollectionUtils.newList();
         for (Iterator modelItr = models.iterator(); modelItr.hasNext();)
         {
            IModel model = (IModel) modelItr.next();
            if(model.getId().equals(id))
            {
               modelsForId.add(model);
            }
         }
         return modelsForId;
      }

      public Iterator<IModel> getAllModelsForId(String id)
      {
         return getModelsForId(id).iterator();
      }

      public Iterator<IModel> getAllModels()
      {
         return models.iterator();
      }

      public DependentObjectsCache getDependentCache()
      {
         return dependentObjectCache;
      }

      public int getModelCount()
      {
         return models.size();
      }

      public Iterator getParticipantsForID(String id)
      {
         QName qname = QName.valueOf(id);
         final String modelId = qname.getNamespaceURI();
         final String participantId = qname.getLocalPart();
         final Iterator<IModel> modelsIterator;
         if (StringUtils.isEmpty(modelId))
         {
            modelsIterator = getAllModels();
         }
         else
         {
            modelsIterator = getAllModelsForId(modelId);
         }

         Functor<IModel, IModelParticipant> transformer = new Functor<IModel, IModelParticipant>()
         {
            public IModelParticipant execute(IModel model)
            {
               return model.findParticipant(participantId);
            }
         };
         Iterator<IModelParticipant> participants = new TransformingIterator(modelsIterator, transformer);
         Predicate<IModelParticipant> participantFilter = new Predicate<IModelParticipant>()
         {
            public boolean accept(IModelParticipant participant)
            {
               return participant instanceof IRole || participant instanceof IOrganization;
            }
         };
         return new FilteringIterator(participants, participantFilter);
      }

      public long getRuntimeOid(IdentifiableElement element)
      {
         if (!(loader instanceof TransientModelLoader))
         {
            // cacheing computed RT OID to prevent frequent full lookup

            Long cachedRtOid = (Long) element
                  .getRuntimeAttribute(ModelManagerBean.CACHED_RT_OID);
            if (null != cachedRtOid)
            {
               return cachedRtOid.longValue();
            }
         }

         IRuntimeOidRegistry.ElementType type = getElementType(element);
         long oid;
         if (null != type)
         {
            oid = rtOidRegistry.getRuntimeOid(type, RuntimeOidUtils.getFqId(element));
         }
         else
         {
            oid = 0;
         }

         if (!(loader instanceof TransientModelLoader) && (oid != 0 || type == null))
         {
            element.setRuntimeAttribute(ModelManagerBean.CACHED_RT_OID, new Long(oid));
         }

         return oid;
      }

      public long getRuntimeOid(IData data, String xPath)
      {
         return rtOidRegistry.getRuntimeOid(IRuntimeOidRegistry.STRUCTURED_DATA_XPATH,
               RuntimeOidUtils.getFqId(data, xPath));
      }

      public boolean isActive(IModel model)
      {
         return model == findActiveModel(model.getId());
      }

      public boolean isAlive(IModel model)
      {
         return isHeated(model) || isRunning(model);
      }

      public ModelElement lookupObjectByOID(long oid)
      {
         IModel model = findModel((int) (oid >>> 32));
         if (model != null)
         {
            return model.lookupElement((int) oid);
         }
         return null;
      }

      public DeploymentInfo overwriteModel(ParsedDeploymentUnit unit, DeploymentOptions options)
      {
         IModel model = unit.getModel();
         DeploymentInfoDetails info = new DeploymentInfoDetails(options.getValidFrom(), model.getId(), options.getComment());

         if (getModelCount() == 0)
         {
            info.addInconsistency(new Inconsistency(Inconsistency.ERROR, null, NO_MODELS));
            return info;
         }

         int modelOID = unit.getReferencedModelOid();
         IModel oldModel = findModel(modelOID);
         if (oldModel == null)
         {
            if (modelOID == PredefinedConstants.ACTIVE_MODEL)
            {
               info.addInconsistency(new Inconsistency(Inconsistency.ERROR, null, NO_ACTIVE_MODEL));
            }
            else
            {
               info.addInconsistency(new Inconsistency(Inconsistency.ERROR, null, NO_MODEL_WITH_OID, modelOID));
            }
            return info;
         }

         BpmRuntimeEnvironment runtimeEnvironment = PropertyLayerProviderInterceptor.getCurrent();
         List<ParsedDeploymentUnit> units = CollectionUtils.newList(1);
         runtimeEnvironment.setModelOverrides(prepareOverrides(units));

         // @todo (france, ub): questionable, set on info
         info.addInconsistencies(validateOverwrite(oldModel, model));
         if (info.hasErrors() || info.hasWarnings() && !options.isIgnoreWarnings())
         {
            return info;
         }

         int index = models.indexOf(oldModel);
         unit.setReferencedModelOid(oldModel.getModelOID());
         loader.modifyModel(unit, options, rtOidRegistry);
         DeploymentUtils.attachDeploymentAttributes(info, model);

         models.set(index, model);
         dependentObjectCache.reload(model);
         recomputeAlivenessCache();

         return info;
      }

      public List validateDeployment(ParsedDeploymentUnit unit, DeploymentOptions options)
      {
         final int predecessorOID = unit.getReferencedModelOid();
         final IModel model = unit.getModel();

         List inconsistencies = new ArrayList();

         // TODO: add inconsistencies at top for detected invalid variable usages on model load

         inconsistencies.addAll(DeploymentUtils.checkModelVersion(model));
         inconsistencies.addAll(checkPredecessor(predecessorOID));
         Date validFrom = (Date) model.getAttribute(PredefinedConstants.VALID_FROM_ATT);
         try
         {
            Date optionsValidFrom = options.getValidFrom();
            if (optionsValidFrom != null)
            {
               model.setAttribute(PredefinedConstants.VALID_FROM_ATT, optionsValidFrom);
            }
            inconsistencies.addAll(model.checkConsistency());
         }
         finally
         {
            model.setAttribute(PredefinedConstants.VALID_FROM_ATT, validFrom);
         }

         // trying to create a model cache to report possible creation exceptions:
         try
         {
            new ModelCache(model);
         }
         catch (Exception e)
         {
            inconsistencies.add(new Inconsistency(e.getMessage(), null, Inconsistency.ERROR));
         }

         List<IExternalPackage> packages = model.getExternalPackages();
         if (packages != null)
         {
            for (IExternalPackage pkg : packages)
            {
               if (pkg.getReferencedModel() == null)
               {
                  inconsistencies.add(new Inconsistency(
                        "Referenced package with namespace '" + pkg.getHref()
                              + "' not found.", null, Inconsistency.WARNING));
               }
            }
         }

         return inconsistencies;
      }

      public List validateOverwrite(IModel oldModel, IModel newModel)
      {
         List inconsistencies = new ArrayList();

         if (!oldModel.getId().equals(newModel.getId()))
         {
            inconsistencies.add(new Inconsistency("The audit trail database contains "
                  + "a model with ID '" + oldModel.getId() + "' which differs from "
                  + "the ID '" + newModel.getId() + "' of this model.",
                  null, Inconsistency.ERROR));
            return inconsistencies;
         }

         inconsistencies.addAll(DeploymentUtils.checkModelVersion(newModel));

         inconsistencies.addAll(newModel.checkConsistency());

         // try to create a model cache to report possible creation exceptions
         try
         {
            new ModelCache(newModel);
         }
         catch (Exception e)
         {
            inconsistencies.add(new Inconsistency(Inconsistency.ERROR, null, e.getMessage()));
         }

         ComparisonModelLoaderFactory comparisonModelLoaderFactory =
            new ComparisonModelLoaderFactory(newModel, SecurityProperties.getPartitionOid());
         ModelManagerBean comparisonModelManager = new ModelManagerBean(comparisonModelLoaderFactory)
         {
            public ModelManagerPartition getModelManagerPartition()
            {
               // explicitly do not use partition local model manager stored in PropertyLayer
               short partitionOid = SecurityProperties.getPartitionOid();
               return getModelManagerPartition(partitionOid);
            }
         };

         final Map allOldElements = getAllFqIds(this, oldModel);
         final Map allNewElements = getAllFqIds(comparisonModelManager.getModelManagerPartition(), newModel);

         Set deletedElements = new HashSet(allOldElements.keySet());
         deletedElements.removeAll(allNewElements.keySet());

         // check for referenced runtime items
         for (Iterator i = deletedElements.iterator(); i.hasNext();)
         {
            String fqId = (String) i.next();
            ModelElement deletedElement = (ModelElement) allOldElements.get(fqId);
            DeploymentUtils.validateDanglingRuntimeItems(inconsistencies, deletedElement);
         }

         final Set sharedElements = new HashSet(allOldElements.keySet());
         sharedElements.retainAll(allNewElements.keySet());

         // check for item compatibility
         for (Iterator i = sharedElements.iterator(); i.hasNext();)
         {
            String fqId = (String) i.next();

            ModelElement oldElement = (ModelElement) allOldElements.get(fqId);
            ModelElement newElement = (ModelElement) allNewElements.get(fqId);

            // here inside the ModelManager, both old and new model elements should have the same class
            // since the same loading mechanism was used.
            if (DeploymentUtils.checkSameClass(inconsistencies, oldElement, newElement) && (newElement instanceof IProcessDefinition))
            {
               DeploymentUtils.checkCompatibleInterface(inconsistencies, (IProcessDefinition) oldElement, (IProcessDefinition) newElement, false);
            }
         }

         return inconsistencies;
      }

      /**
       * Used to add models created externally.
       */
      private void addModel(long predecessor, IModel model, boolean persistLink)
      {
         int predecessorIndex = models.indexOf(findModel(predecessor));
         models.add(predecessorIndex + 1, model);
         dependentObjectCache.reload(model);

         if (!persistLink)
         {
            return;
         }

         if (predecessorIndex + 2 < models.size())
         {
            IModel successor = models.get(predecessorIndex + 2);
            ModelPersistorBean successorPersistor = ModelPersistorBean
                  .findByModelOID(successor.getModelOID());
            successorPersistor.setPredecessor(model.getModelOID());
            successor.setAttribute(PredefinedConstants.PREDECESSOR_ATT, new Integer(model
                  .getModelOID()));
         }

         return;
      }

      private void detachModel(IModel model)
      {
         int index = models.indexOf(model);
         models.remove(index);
         if (index < models.size())
         {
            IModel successor = models.get(index);
            int predecessorOID = 0;
            if (index != 0)
            {
               IModel predecessor = models.get(index - 1);
               predecessorOID = predecessor.getModelOID();
            }
            ModelPersistorBean successorPersistor = ModelPersistorBean
                  .findByModelOID(successor.getModelOID());
            successorPersistor.setPredecessor(predecessorOID);
            successor.setAttribute(PredefinedConstants.PREDECESSOR_ATT, new Integer(
                  predecessorOID));
         }
      }

      private IModel findActiveModel(Date then)
      {
         for (Iterator i = getAllModels(); i.hasNext();)
         {
            IModel model = (IModel) i.next();

            if (model.getId().equals(PredefinedConstants.PREDEFINED_MODEL_ID))
            {
               continue;
            }
            
            if (model.getBooleanAttribute(PredefinedConstants.IS_DISABLED_ATT))
            {
               continue;
            }

            Date from = (Date) model.getAttribute(PredefinedConstants.VALID_FROM_ATT);
            Date to = (Date) model.getAttribute(PredefinedConstants.VALID_TO_ATT);

            if (from == null)
            {
               from = MINUS_INFINITY_DATE;
            }
            if (to == null)
            {
               to = PLUS_INFINITY_DATE;
            }

            if (!then.before(from) && then.before(to))
            {
               return model;
            }
         }
         return null;
      }

      private List<IModel> findActiveModels(Date then)
      {
         List<IModel> result = CollectionUtils.newList();
         Set<String> namespaces = CollectionUtils.newSet();
         for (Iterator i = getAllModels(); i.hasNext();)
         {
            IModel model = (IModel) i.next();

            if (model.getId().equals(PredefinedConstants.PREDEFINED_MODEL_ID))
            {
               continue;
            }            
            
            if (model.getBooleanAttribute(PredefinedConstants.IS_DISABLED_ATT))
            {
               continue;
            }

            Date from = (Date) model.getAttribute(PredefinedConstants.VALID_FROM_ATT);
            Date to = (Date) model.getAttribute(PredefinedConstants.VALID_TO_ATT);

            if (from == null)
            {
               from = MINUS_INFINITY_DATE;
            }
            if (to == null)
            {
               to = PLUS_INFINITY_DATE;
            }

            if (!then.before(from) && then.before(to))
            {
               String ns = model.getId();
               if (!namespaces.contains(ns))
               {
                  result.add(model);
                  namespaces.add(ns);
               }
            }
         }
         return result;
      }

      private List checkPredecessor(long predecessorOID)
      {
         List inconsistencies = new ArrayList();
         if (predecessorOID == 0)
         {
            return inconsistencies;
         }
         IModel predecessor = findModel(predecessorOID);
         if (predecessor == null)
         {
            inconsistencies.add(new Inconsistency("Predecessor model with oid "
                  + predecessorOID + " not found.", null, Inconsistency.ERROR));
         }
         return inconsistencies;
      }

      private boolean isRunning(IModel model)
      {
         if (deadModels.contains(model))
         {
            return false;
         }

         if (model.getProcessDefinitions().isEmpty())
         {
            // model with no processes can not be running
            return false;
         }

         long result = SessionFactory.getSession(SessionFactory.AUDIT_TRAIL).getCount(
               ProcessInstanceBean.class,
               QueryExtension.where(Predicates
                     .andTerm(Predicates.isEqual(ProcessInstanceBean.FR__MODEL, model
                           .getModelOID()), Predicates.notInList(
                           ProcessInstanceBean.FR__STATE, new int[] {
                                 ProcessInstanceState.COMPLETED,
                                 ProcessInstanceState.ABORTED}))));

         if (result == 0 && !isHeated(model))
         {
            deadModels.add(model);
         }

         return result > 0;
      }

      private boolean isHeated(IModel model)
      {
         Date now = new Date();

         for (Iterator i = heatingEntries.iterator(); i.hasNext();)
         {
            HeatingEntry entry = (HeatingEntry) i.next();

            if (!now.before(entry.getTo()))
            {
               continue;
            }
            if (entry.getModelOID() == model.getModelOID())
            {
               return true;
            }
         }
         return false;
      }

      public IModel getFirstByPriority(List<IModel> candidates)
      {
         for (IModel model : models)
         {
            if (candidates.contains(model))
            {
               return model;
            }
         }
         return null;
      }
   }

   private final class ComparisonModelLoaderFactory extends AbstractModelLoaderFactory
   {
      private final IModel model;

      private final short partitionOid;

      protected ComparisonModelLoaderFactory(final IModel model, final short partitionOid)
      {
         super();
         this.model = model;
         this.partitionOid = partitionOid;
      }

      public synchronized ModelLoader instance(short partitionOid)
      {
         Assert.condition(this.partitionOid == partitionOid);
         return new TransientModelLoader(model, partitionOid);
      }
   }

   /**
    * This model loader is used to compare the new model with an old model from audit
    * trail when a model will be overwritten.
    *
    * @author sborn
    * @version $Revision$
    */
   private static final class TransientModelLoader implements ModelLoader
   {
      private final IModel model;

      private final short partitionOid;

      private TransientModelLoader(IModel model, short partitionOid)
      {
         this.model = model;
         this.partitionOid = partitionOid;
      }

      public void loadRuntimeOidRegistry(IRuntimeOidRegistry rtOidRegistry)
      {
         // runtime OID is just the model element's element OIDs modified by partitionOid.

         // load data runtime OIDs
         for (int i = 0; i < model.getData().size(); ++i)
         {
            IData data = (IData) model.getData().get(i);

            rtOidRegistry.registerRuntimeOid(IRuntimeOidRegistry.DATA, RuntimeOidUtils.getFqId(data),
                  getPartitionAwareRtOid(partitionOid, data.getElementOID()));
         }

         // load model participant runtime OIDs
         for (int i = 0; i < model.getParticipants().size(); ++i)
         {
            IParticipant participant = (IParticipant) model.getParticipants().get(i);
            if ((participant instanceof IModelParticipant)
                  && !(participant instanceof IModeler))
            {
               rtOidRegistry.registerRuntimeOid(IRuntimeOidRegistry.PARTICIPANT, RuntimeOidUtils.getFqId(participant),
                     getPartitionAwareRtOid(partitionOid, ((IModelParticipant) participant).getElementOID()));
            }
         }

         // load process definition runtime OIDs
         for (int i = 0; i < model.getProcessDefinitions().size(); ++i)
         {
            IProcessDefinition process = (IProcessDefinition) model
                  .getProcessDefinitions().get(i);
            rtOidRegistry.registerRuntimeOid(IRuntimeOidRegistry.PROCESS, RuntimeOidUtils.getFqId(process),
                  getPartitionAwareRtOid(partitionOid, process.getElementOID()));

            // load trigger runtime OIDs
            for (int j = 0; j < process.getTriggers().size(); ++j)
            {
               ITrigger trigger = (ITrigger) process.getTriggers().get(j);
               rtOidRegistry.registerRuntimeOid(IRuntimeOidRegistry.TRIGGER, RuntimeOidUtils.getFqId(trigger),
                     getPartitionAwareRtOid(partitionOid, trigger.getElementOID()));
            }

            // load activity runtime OIDs
            for (int j = 0; j < process.getActivities().size(); ++j)
            {
               IActivity activity = (IActivity) process.getActivities().get(j);
               rtOidRegistry.registerRuntimeOid(IRuntimeOidRegistry.ACTIVITY, RuntimeOidUtils.getFqId(activity),
                     getPartitionAwareRtOid(partitionOid, activity.getElementOID()));

               // load event handler runtime OIDs
               for (int k = 0; k < activity.getEventHandlers().size(); ++k)
               {
                  IEventHandler handler = (IEventHandler) activity.getEventHandlers()
                        .get(k);
                  rtOidRegistry
                        .registerRuntimeOid(IRuntimeOidRegistry.EVENT_HANDLER, RuntimeOidUtils.getFqId(handler),
                              getPartitionAwareRtOid(partitionOid, handler.getElementOID()));
               }
            }

            // load transition runtime OIDs
            for (int j = 0; j < process.getTransitions().size(); ++j)
            {
               ITransition transition = (ITransition) process.getTransitions().get(j);
               rtOidRegistry.registerRuntimeOid(IRuntimeOidRegistry.TRANSITION, RuntimeOidUtils.getFqId(transition),
                     getPartitionAwareRtOid(partitionOid, transition.getElementOID()));
            }

            // load event handler runtime OIDs
            for (int j = 0; j < process.getEventHandlers().size(); ++j)
            {
               IEventHandler handler = (IEventHandler) process.getEventHandlers().get(j);
               rtOidRegistry.registerRuntimeOid(IRuntimeOidRegistry.EVENT_HANDLER, RuntimeOidUtils.getFqId(handler),
                     getPartitionAwareRtOid(partitionOid, handler.getElementOID()));
            }
         }
      }

      /**
       *
       */
      private long getPartitionAwareRtOid(short partitionOid, int elementOid)
      {
         return elementOid
               + ((partitionOid - 1l) << RuntimeOidRegistry.PARTITION_PART_SHIFT);
      }

      public Iterator loadModels()
      {
         return Collections.singleton(new ModelPersistorBean()
         {
            private static final long serialVersionUID = 1L;

            public IModel fetchModel()
            {
               return model;
            }

            public IAuditTrailPartition getPartition()
            {
               return SecurityProperties.getPartition();
            }

            public long getPredecessorOID()
            {
               return -1;
            }
         }).iterator();
      }

      public void deployModel(List<ParsedDeploymentUnit> units, DeploymentOptions options,
            IRuntimeOidRegistry rtOidRegistry)
      {
         throw new UnsupportedOperationException();
      }

      public void modifyModel(ParsedDeploymentUnit unit, DeploymentOptions options, IRuntimeOidRegistry rtOidRegistry)
      {
         throw new UnsupportedOperationException();
      }
   }

   public IModel getFirstByPriority(List<IModel> candidates)
   {
      return getModelManagerPartition().getFirstByPriority(candidates);
   }
}