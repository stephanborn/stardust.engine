/*******************************************************************************
 * Copyright (c) 2012 SunGard CSA LLC and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    SunGard CSA LLC - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.stardust.engine.api.runtime;

import java.io.Serializable;
import java.util.Map;

/**
 * TODO
 * @author Florin.Herinean
 * @version $Revision: $
 */
public class DataCopyOptions
{
   public static final DataCopyOptions DEFAULT = new DataCopyOptions();

   /**
    * TODO
    */
   private boolean copyAllData;
   
   /**
    * TODO
    */
   private Map<String, String> dataTranslationTable;
   
   /**
    * TODO
    */
   private Map<String, ? extends Serializable> replacementTable;
   
   /**
    * TODO
    */
   private boolean useHeuristics;

   private DataCopyOptions()
   {
      this(true, null, null, true);
   }

   /**
    * TODO
    * @param copyAllData
    * @param dataTranslationTable
    * @param replacementTable
    * @param useHeuristics
    */
   public DataCopyOptions(boolean copyAllData, Map<String, String> dataTranslationTable,
         Map<String, ? extends Serializable> replacementTable, boolean useHeuristics)
   {
      this.copyAllData = copyAllData;
      this.dataTranslationTable = dataTranslationTable;
      this.replacementTable = replacementTable;
      this.useHeuristics = useHeuristics;
   }

   /**
    * TODO
    * @return
    */
   public boolean copyAllData()
   {
      return copyAllData;
   }

   /**
    * TODO
    * @return
    */
   public Map<String, String> getDataTranslationTable()
   {
      return dataTranslationTable;
   }

   /**
    * TODO
    * @return
    */
   public Map<String, ? extends Serializable> getReplacementTable()
   {
      return replacementTable;
   }

   /**
    * TODO
    * @return
    */
   public boolean useHeuristics()
   {
      return useHeuristics;
   }
}