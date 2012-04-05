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
package org.eclipse.stardust.test.workflow;

import static org.eclipse.stardust.test.util.TestConstants.MOTU;
import static org.eclipse.stardust.test.workflow.BasicWorkflowModelConstants.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isOneOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.eclipse.stardust.common.CollectionUtils;
import org.eclipse.stardust.common.error.AccessForbiddenException;
import org.eclipse.stardust.common.error.ObjectNotFoundException;
import org.eclipse.stardust.engine.api.runtime.ProcessInstance;
import org.eclipse.stardust.engine.api.runtime.ProcessInstanceState;
import org.eclipse.stardust.engine.core.runtime.beans.AbortScope;
import org.eclipse.stardust.test.api.ClientServiceFactory;
import org.eclipse.stardust.test.api.LocalJcrH2Test;
import org.eclipse.stardust.test.api.RuntimeConfigurer;
import org.eclipse.stardust.test.api.UserHome;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

/**
 * <p>
 * Tests basic functionality regarding the workflow of process instances.
 * </p>
 * 
 * @author Nicolas.Werlein
 * @version $Revision$
 */
public class ProcessInstanceWorkflowTest extends LocalJcrH2Test
{
   private static final String DEFAULT_ROLE_USER_ID = "u1";
   
   private final ClientServiceFactory adminSf = new ClientServiceFactory(MOTU, MOTU);
   private final RuntimeConfigurer rtConfigurer = new RuntimeConfigurer(adminSf, MODEL_NAME);
   private final ClientServiceFactory userSf = new ClientServiceFactory(DEFAULT_ROLE_USER_ID, DEFAULT_ROLE_USER_ID);
   
   @Rule
   public TestRule chain = RuleChain.outerRule(adminSf)
                                    .around(rtConfigurer)
                                    .around(userSf);
   
   @Before
   public void setUp()
   {
      UserHome.create(adminSf, DEFAULT_ROLE_USER_ID, DEFAULT_ROLE_ID);
   }
   
   /**
    * <p>
    * Tests whether starting the process instance synchronously
    * works correctly.
    * </p>
    */
   @Test
   public void testStartProcessSynchronously()
   {
      final ProcessInstance pi = userSf.getWorkflowService().startProcess(PD_1_ID, null, true);
      assertNotNull(pi);
      assertEquals(ProcessInstanceState.Active, pi.getState());
   }

   /**
    * <p>
    * Tests whether starting the process instance asynchronously
    * works correctly.
    * </p>
    */
   @Test
   public void testStartProcessAsynchronously()
   {
      final ProcessInstance pi = userSf.getWorkflowService().startProcess(PD_1_ID, null, false);
      assertNotNull(pi);
      assertEquals(ProcessInstanceState.Active, pi.getState());
   }

   /**
    * <p>
    * Tests whether starting the process instance by passing a
    * qualified ID works correctly.
    * </p>
    */
   @Test
   public void testStartProcessQualifiedId()
   {
      final String modelId = MODEL_NAME;
      final String fqProcessId = "{" + modelId + "}" + PD_1_ID;
      final ProcessInstance pi = userSf.getWorkflowService().startProcess(fqProcessId, null, true);
      assertNotNull(pi);
      assertEquals(ProcessInstanceState.Active, pi.getState());
   }
   
   /**
    * <p>
    * Tests whether the process data will be initialized correctly with the
    * passed data values (tests only String and Integer data).
    * </p>
    */
   @Test
   public void testStartProcessPassData()
   {
      final String originalString = "a string";
      final int originalInt = 81;
      final Map<String, Object> data = CollectionUtils.newHashMap();
      data.put(MY_STRING_DATA_ID, originalString);
      data.put(MY_INT_DATA_ID, originalInt);
      
      final ProcessInstance pi = userSf.getWorkflowService().startProcess(PD_1_ID, data, true);
      
      final String actualString = (String) userSf.getWorkflowService().getInDataPath(pi.getOID(), MY_STRING_IN_DATA_PATH_ID);
      final int actualInt = (Integer) userSf.getWorkflowService().getInDataPath(pi.getOID(), MY_INT_IN_DATA_PATH_ID);
      
      assertThat(actualString, equalTo(originalString));
      assertThat(actualInt, equalTo(originalInt));
   }   
   
   /**
    * <p>
    * Tests whether the appropriate exception is thrown when the process definition
    * cannot be found.
    * </p>
    */
   @Test(expected = ObjectNotFoundException.class)
   public void testStartProcessFailProcessDefinitionNotFound()
   {
      userSf.getWorkflowService().startProcess("N/A", null, true);
   }
   
   /**
    * <p>
    * Tests whether aborting a process instance works correctly.
    * </p>
    */
   @Test
   public void testAbortProcessInstance()
   {
      final ProcessInstance pi = userSf.getWorkflowService().startProcess(PD_1_ID, null, true);
      final ProcessInstance abortedPi = adminSf.getWorkflowService().abortProcessInstance(pi.getOID(), AbortScope.SubHierarchy);
      assertThat(abortedPi.getState(), isOneOf(ProcessInstanceState.Aborting, ProcessInstanceState.Aborted));
   }
   
   /**
    * <p>
    * Tests whether the process instance abortion throws the correct exception
    * when the process instance cannot be found.
    * </p>
    */
   @Test(expected = ObjectNotFoundException.class)
   public void testAbortProcessInstanceFailProcessInstanceNotFound()
   {
      userSf.getWorkflowService().startProcess(PD_1_ID, null, true);
      userSf.getWorkflowService().abortProcessInstance(-1, AbortScope.SubHierarchy);
   }

   /**
    * <p>
    * Tests whether the process instance abortion throws the correct exception
    * when the user has insufficient grants.
    * </p>
    */
   @Test(expected = AccessForbiddenException.class)
   public void testAbortProcessInstanceFailInsufficientGrants()
   {
      final ProcessInstance pi = userSf.getWorkflowService().startProcess(PD_1_ID, null, true);
      userSf.getWorkflowService().abortProcessInstance(pi.getOID(), AbortScope.SubHierarchy);
   }
   
   // TODO write test cases for getProcessInstance()
   
   // TODO write test cases for getProcessResults()
   
   // TODO write test cases for getStartableProcessDefinitions()
   
   // TODO write test cases for setProcessInstanceAttributes()
}
