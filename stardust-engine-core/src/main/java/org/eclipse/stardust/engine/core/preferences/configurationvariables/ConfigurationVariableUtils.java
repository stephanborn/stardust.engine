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
package org.eclipse.stardust.engine.core.preferences.configurationvariables;

import static org.eclipse.stardust.common.StringUtils.isEmpty;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.emf.ecore.xml.type.internal.RegEx;
import org.eclipse.stardust.common.CollectionUtils;
import org.eclipse.stardust.common.StringUtils;
import org.eclipse.stardust.common.log.LogManager;
import org.eclipse.stardust.common.log.Logger;
import org.eclipse.stardust.engine.api.model.ConfigurationVariableDefinitionProvider;
import org.eclipse.stardust.engine.api.model.IModel;
import org.eclipse.stardust.engine.api.runtime.ModelReconfigurationInfo;
import org.eclipse.stardust.engine.api.runtime.ReconfigurationInfo;
import org.eclipse.stardust.engine.core.preferences.IPreferenceStorageManager;
import org.eclipse.stardust.engine.core.preferences.PreferenceScope;
import org.eclipse.stardust.engine.core.preferences.Preferences;
import org.eclipse.stardust.engine.core.runtime.beans.ModelManagerFactory;



public class ConfigurationVariableUtils
{
   private static final Logger trace = LogManager.getLogger(ConfigurationVariableUtils.class);

   // pattern: description
   //    (?<!\\\\): Lookbehind assertion: variable shall not be escaped, i.e start with \
   //    \\$\\{(\\w+)\\}: the variable matches ${name} and group results in "name"
   private static Pattern modelVars = Pattern.compile("(?<!\\\\)\\$\\{(\\w+)\\}");
   // pattern: description
   //    \\\\: variable is escaped, i.e starts with \
   //    (\\$\\{\\w+\\}): the escaped variable matches ${name} and group results in "${name}"
   private static Pattern escapedModelVars = Pattern.compile("\\\\(\\$\\{\\w+\\})");
   
   /**
    * The moduleId of preferences used to store configuration variables.
    * As preferencesId the modelId is used. 
    */
   public static final String CONFIGURATION_VARIABLES = "configuration-variables";

   public static ConfigurationVariables getConfigurationVariables(
         IPreferenceStorageManager preferenceStore, String modelId,
         boolean mergeDeployedModels)
   {
      List< ? extends ConfigurationVariableDefinitionProvider> providers;

      if (mergeDeployedModels)
      {
         Map<Long, IModel> models = CollectionUtils.newTreeMap();

         Iterator<IModel> modelIter = ModelManagerFactory.getCurrent().getAllModels();

         while (modelIter.hasNext())
         {
            IModel model = modelIter.next();

            if (model.getId().equals(modelId))
            {
               // TreeMap sorted ascending by modelOid
               models.put(model.getOID(), model);
            }
         }
         providers = CollectionUtils.newArrayList(models.values());
      }
      else
      {
         providers = Collections.emptyList();
      }

      return getConfigurationVariables(preferenceStore, modelId, providers);
   }

   public static ConfigurationVariables getConfigurationVariables(
         IPreferenceStorageManager preferenceStore, IModel externalModel)
   {
      return getConfigurationVariables(preferenceStore, externalModel.getId(),
            Collections.singletonList(externalModel));
   }

   private static ConfigurationVariables getConfigurationVariables(
         IPreferenceStorageManager preferenceStore, String modelId,
         List< ? extends ConfigurationVariableDefinitionProvider> providers)
   {
      Preferences preferences = preferenceStore.getPreferences(PreferenceScope.PARTITION,
            CONFIGURATION_VARIABLES, modelId);

      final Map<String, Serializable> preferencesMap = preferences.getPreferences();

      Map<String, ConfigurationVariable> mergedConfigurationVariables = CollectionUtils.newHashMap();
      
      if (providers.isEmpty())
      {
         for (Entry<String, Serializable> prefEntry : preferencesMap.entrySet())
         {
            if (prefEntry.getValue() != null)
            {
               ConfigurationVariableDefinition definition = new ConfigurationVariableDefinition(
                     prefEntry.getKey(), "", "", -1);
               ConfigurationVariable variable = new ConfigurationVariable(definition,
                     (String) prefEntry.getValue());
               mergedConfigurationVariables.put(definition.getName(), variable);
            }
         }
      }

      // precondition: List is sorted ascending, e.g. by modelOid if providers are models.
      // Variable will be overwritten if it occurs again, so order is important.
      for (ConfigurationVariableDefinitionProvider provider : providers)
      {
         for (IConfigurationVariableDefinition confVarDef : provider
               .getConfigurationVariableDefinitions())
         {
            String value = (String) preferencesMap.get(confVarDef.getName());
            if (isEmpty(value))
            {
               // dont substitute default value here leave that to GUI.
               value = "";
            }
            ConfigurationVariable confVar = new ConfigurationVariable(confVarDef, value);

            mergedConfigurationVariables.put(confVarDef.getName(), confVar);
         }
      }

      ConfigurationVariables configurationVariables = new ConfigurationVariables(modelId);
      configurationVariables.setConfigurationVariables(new ArrayList(
            mergedConfigurationVariables.values()));

      return configurationVariables;
   }

   public static List<ModelReconfigurationInfo> saveConfigurationVariables(
         IPreferenceStorageManager preferenceStore,
         ConfigurationVariables configurationVariables, boolean force)
   {
      List<ConfigurationVariable> variableList = configurationVariables.getConfigurationVariables();

      Map<String, Serializable> preferencesMap = CollectionUtils.newHashMap();

      for (ConfigurationVariable configurationVariable : variableList)
      {
         final String name = configurationVariable.getName();
         final String defaultValue = configurationVariable.getDefaultValue();
         String value = configurationVariable.getValue();

         // compare defaultValue and value to prevent saving defaultValue to store.
         if (!isEmpty(value) && (defaultValue == null || !defaultValue.equals(value)))
         {
            preferencesMap.put(name, value);
         }
      }

      Preferences preferences = new Preferences(PreferenceScope.PARTITION,
            CONFIGURATION_VARIABLES, configurationVariables.getModelId(), preferencesMap);
      
      final List<ReconfigurationInfo> reconInfos = preferenceStore.savePreferences(
            preferences, force);
      final List<ModelReconfigurationInfo> modelReconInfos = CollectionUtils
            .newArrayList(reconInfos.size());

      for (ReconfigurationInfo reconfigurationInfo : reconInfos)
      {
         if (reconfigurationInfo instanceof ModelReconfigurationInfo)
         {
            ModelReconfigurationInfo mri = (ModelReconfigurationInfo) reconfigurationInfo;
            modelReconInfos.add(mri);
         }
         else
         {
            trace.info("Unrecognized reconfiguration info type: "
                  + reconfigurationInfo.getClass());
         }
      }

      return modelReconInfos;
   }
   
   public static Matcher getConfigurationVariablesMatcher(String text)
   {
      return modelVars.matcher(text);
   }

   public static Matcher getEscapedConfigurationVariablesMatcher(String text)
   {
      return escapedModelVars.matcher(text);
   }
   
   /**
    * Returns a representation of input string "literal" with all RegEx meta characters
    * quoted.
    * 
    * @param literal the literal text used within regular expressions
    * @return quoted representation of literal in terms of regular expressions
    */
   public static String quoteMeta(String literal)
   {
      return RegEx.REUtil.quoteMeta(literal);
   }
   
   public static String replace(ConfigurationVariable modelVariable,
         String value)
   {
      String result;
      
      String name = modelVariable.getName();
      // pattern: description
      //    (?<!\\\\): Lookbehind assertion: variable shall not be escaped, i.e start with \
      //    (\\$\\{" + name + "\\}): the variable matches ${name}
      String tobeReplacedPattern = "(?<!\\\\)(\\$\\{" + name + "\\})";
      String newValue = StringUtils.isEmpty(modelVariable.getValue())
            ? modelVariable.getDefaultValue()
            : modelVariable.getValue();
      if (newValue == null)
      {
         newValue = "";
      }
      newValue = quoteMeta(newValue);
      result = value.replaceAll(tobeReplacedPattern, newValue);
      
      return result;
   }


}
