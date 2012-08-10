/**********************************************************************************
 * Copyright (c) 2012 SunGard CSA LLC and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    SunGard CSA LLC - initial API and implementation and/or initial documentation
 **********************************************************************************/
package org.eclipse.stardust.test.transientpi;

/**
 * <p>
 * This class contains constants related to the model used for tests
 * dealing with the <i>Transient Process Instance</i> functionality.
 * </p>
 *
 * @author Nicolas.Werlein
 * @version $Revision: $ 
 */
/* package-private */ class TransientProcessInstanceModelConstants
{
   /**
    * the ID of the model
    */
   /* package-private */ static final String MODEL_ID = "TransientProcessInstanceModel";
   
   
   /**
    * the ID of the non-forked process definition
    */
   /* package-private */ static final String PROCESS_DEF_ID_NON_FORKED = "Non_forkedProcess";
   
   /**
    * the ID of the forked process definition
    */
   /* package-private */ static final String PROCESS_DEF_ID_FORKED = "ForkedProcess";
   
   /**
    * the ID of the non-forked process definition that fails
    */
   /* package-private */ static final String PROCESS_DEF_ID_NON_FORKED_FAIL = "Non_forkedProcessFail";   

   /**
    * the ID of the forked process definition that fails
    */
   /* package-private */ static final String PROCESS_DEF_ID_FORKED_FAIL = "ForkedProcessFail";
   
   /**
    * the ID of the process definition that contains an AND split
    */
   /* package-private */ static final String PROCESS_DEF_ID_SPLIT = "SplitProcess";
   
   /**
    * the ID of the process definition that enforces a rollback
    */
   /* package-private */ static final String PROCESS_DEF_ID_ROLLBACK = "RollbackProcess";
   
   /**
    * the ID of the process definition that has a transient and a non-transient route
    */
   /* package-private */ static final String PROCESS_DEF_ID_TRANSIENT_NON_TRANSIENT_ROUTE = "TransientAndNon_transientRoute";
   
   
   /**
    * the ID of the data in model 'TransientAndNon_transientRoute' determining whether the transient or
    * the non-transient route should be taken
    */
   /* package-private */ static final String DATA_ID_TRANSIENT_ROUTE = "TransientRoute";
}