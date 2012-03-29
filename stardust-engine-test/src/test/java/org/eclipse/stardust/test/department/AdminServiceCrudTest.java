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
package org.eclipse.stardust.test.department;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.eclipse.stardust.test.department.DepartmentModelConstants.*;
import static org.eclipse.stardust.test.util.TestConstants.MOTU;

import org.eclipse.stardust.common.error.InvalidArgumentException;
import org.eclipse.stardust.common.error.ObjectNotFoundException;
import org.eclipse.stardust.engine.api.dto.DepartmentInfoDetails;
import org.eclipse.stardust.engine.api.dto.OrganizationInfoDetails;
import org.eclipse.stardust.engine.api.model.Organization;
import org.eclipse.stardust.engine.api.model.OrganizationInfo;
import org.eclipse.stardust.engine.api.runtime.AdministrationService;
import org.eclipse.stardust.engine.api.runtime.Department;
import org.eclipse.stardust.engine.api.runtime.DepartmentInfo;
import org.eclipse.stardust.engine.api.runtime.QueryService;
import org.eclipse.stardust.test.api.ClientServiceFactory;
import org.eclipse.stardust.test.api.DepartmentHome;
import org.eclipse.stardust.test.api.LocalJcrH2Test;
import org.eclipse.stardust.test.api.RuntimeConfigurer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

/**
 * <p>
 * This class tests CRUD functionality regarding <i>Departments</i>.
 * </p>
 * 
 * @author Nicolas.Werlein
 * @version $Revision: $
 */
public class AdminServiceCrudTest extends LocalJcrH2Test
{
   private static final String DEPT_ID = "Dept";
   
   private final ClientServiceFactory sf = new ClientServiceFactory(MOTU, MOTU);
   private final RuntimeConfigurer rtConfigurer = new RuntimeConfigurer(sf, MODEL_NAME);
   
   @Rule
   public TestRule chain = RuleChain.outerRule(sf)
                                    .around(rtConfigurer);
   
   private AdministrationService adminService;
   private QueryService queryService;
   
   private Organization org;
   
   @Before
   public void setUp()
   {
      adminService = sf.getAdministrationService();
      queryService = sf.getQueryService();
      
      org = (Organization) queryService.getParticipant(ORG_ID_1);
   }
   
   /**
    * <p>
    * The id must not be null.
    * </p>
    */
   @Test(expected = InvalidArgumentException.class)
   public void testCreateDepartmentIdNull()
   {
      adminService.createDepartment(null, DEPT_ID, DEPT_ID, null, org);
      fail("The ID must not be null.");
   }

   /**
    * <p>
    * The id must not be empty.
    * </p>
    */
   @Test(expected = InvalidArgumentException.class)
   public void testCreateDepartmentIdEmpty()
   {
      adminService.createDepartment("", DEPT_ID, DEPT_ID, null, org);
      fail("The ID must not be empty.");
   }
   
   /**
    * <p>
    * The parent may be null for a top level department.
    * </p>
    */
   @Test
   public void testCreateDepartmentParentNullTld()
   {
      DepartmentHome.create(DEP_ID_DE, ORG_ID_1, null, sf);
   }
   
   /**
    * <p>
    * The parent must not be null for a department that is not
    * a top level one.
    * </p>
    */
   @Test(expected = InvalidArgumentException.class)
   public void testCreateDepartmentParentNullNotTld()
   {
      DepartmentHome.create(SUB_DEP_ID_NORTH, SUB_ORG_ID_2, null, sf);
      fail("The parent must not be null for a department that is not a top level one.");
   }
   
   /**
    * <p>
    * The parent must resolve to an existing department.
    * </p>
    */
   @Test(expected = ObjectNotFoundException.class)
   public void testCreateDepartmentParentDoesNotExist()
   {
      final DepartmentInfo dep = new DepartmentInfoDetails(-1, "N/A", "N/A", -1);
      DepartmentHome.create(SUB_DEP_ID_NORTH, SUB_ORG_ID_2, dep, sf);
      fail("The parent must resolve to an exisiting department.");
   }
   
   /**
    * <p>
    * It is possible that the department's parent is a direct parent organization
    * of the organization the department is created for.
    * </p>
    */
   @Test
   public void testCreateDepartmentParentIsDirectParent()
   {
      final Department parent = DepartmentHome.create(DEP_ID_DE, ORG_ID_2, null, sf);
      DepartmentHome.create(SUB_DEP_ID_NORTH, SUB_ORG_ID_2, parent, sf);
   }

   /**
    * <p>
    * It is not possible that the department's parent is an indirect parent organization
    * of the organization the department is created for.
    * </p>
    */
   @Test(expected = InvalidArgumentException.class)
   public void testCreateDepartmentParentIsIndirectParent()
   {
      final Department parent = DepartmentHome.create(DEP_ID_DE, ORG_ID_2, null, sf);
      DepartmentHome.create(SUB_SUB_DEP_ID_HH, SUB_SUB_ORG_ID_2, parent, sf);
   }
   
   /**
    * <p>
    * It shouldn't be possible that the department's parent isn't a direct parent organization
    * of the organization the department is created for.
    * </p>
    */
   @Test(expected = InvalidArgumentException.class)
   public void testCreateDepartmentParentIsNotParent()
   {
      final Department parent = DepartmentHome.create(DEP_ID_DE, ORG_ID_1, null, sf);
      DepartmentHome.create(SUB_DEP_ID_NORTH, SUB_ORG_ID_2, parent, sf);
      fail("The department's parent must be a direct parent organization " +
      		"of the organization the department is created for.");
   }
   
   /**
    * <p>
    * The organization must not be null.
    * </p>
    */
   @Test(expected = InvalidArgumentException.class)
   public void testCreateDepartmentOrganizationNull()
   {
      adminService.createDepartment(DEP_ID_DE, DEP_ID_DE, null, null, null);
      fail("The organization must not be null.");
   }
   
   /**
    * <p>
    * The organization must resolve to an existing model participant
    * organization.
    * </p>
    */
   @Test(expected = ObjectNotFoundException.class)
   public void testCreateDepartmentOrganizationDoesNotExist()
   {
      final OrganizationInfo org = new OrganizationInfoDetails("N/A");
      adminService.createDepartment(DEP_ID_DE, DEP_ID_DE, null, null, org);
      fail("The organization must resolve to an existing model participant organization");
   }

   /**
    * <p>
    * It should be possible to get a formerly created department.
    * </p>
    */
   @Test
   public void testGetDepartment()
   {
      final Department createdDep = DepartmentHome.create(DEP_ID_DE, ORG_ID_1, null, sf);
      final Department retrievedDep = adminService.getDepartment(createdDep.getOID());
      assertEquals(createdDep, retrievedDep);
   }
   
   /**
    * <p>
    * An exception should be thrown when a department cannot be found.
    * </p>
    */
   @Test(expected = ObjectNotFoundException.class)
   public void testGetDepartmentDoesNotExist()
   {
      adminService.getDepartment(-1);
      fail("Found a non-existing department.");
   }
   
   /**
    * <p>
    * It should be possible to modify the description of a formerly created department.
    * </p>
    */
   @Test
   public void testModifyDepartmentDescription()
   {
      final Department createdDep = DepartmentHome.create(DEP_ID_DE, ORG_ID_1, null, sf);
      final String newDesc = "the new description";
      adminService.modifyDepartment(createdDep.getOID(), createdDep.getName(), newDesc);
      
      final Department retrievedDep = adminService.getDepartment(createdDep.getOID());
      assertEquals(newDesc, retrievedDep.getDescription());
   }
   
   /**
    * <p>
    * An exception should be thrown when a department cannot be found.
    * </p>
    */
   @Test(expected = ObjectNotFoundException.class)
   public void testModifyDepartmentDoesNotExist()
   {
      adminService.modifyDepartment(-1, "Hello", null);
      fail("Found a department which does not exist.");
   }
   
   /**
    * <p>
    * It should be possible to remove a formerly created department.
    * </p> 
    */
   @Test(expected = ObjectNotFoundException.class)
   public void testRemoveDepartment()
   {
      final Department dep = DepartmentHome.create(DEP_ID_DE, ORG_ID_1, null, sf);
      adminService.removeDepartment(dep.getOID());
      
      adminService.getDepartment(dep.getOID());
      fail("Found a department which has been removed.");
   }
   
   /**
    * <p>
    * An exception should be thrown when a department can not be found.
    * </p>
    */
   @Test(expected = ObjectNotFoundException.class)
   public void testRemoveDepartmentDoesNotExist()
   {
      adminService.removeDepartment(-1);
      fail("Tried to remove a non-existing department.");
   }
}
