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

import org.eclipse.stardust.engine.core.runtime.beans.daemons.DaemonLog;
import org.eclipse.stardust.engine.core.struct.beans.StructuredDataBean;
import org.eclipse.stardust.engine.core.struct.beans.StructuredDataValueBean;


/**
 * All constants needed by this package.
 */
public interface Constants
{
   String CARNOT_VERSION = "carnot.version";
   String CARNOT_ARCHIVE_AUDITTRAIL = "carnot.audittrail.archive";
   String UPGRADE_LOCK = "upgrade.lock";
   String SYSOP_PASSWORD = "sysop.password";
   String DEFAULT_PASSWORD = "sysop";
   String FORCE_IMMEDIATE_INSERT_ON_SESSION = "Infinity.ForceImmediateInsert";

   /**
    * All tables for runtime classes.
    */
   public static final Class[] PERSISTENT_RUNTIME_CLASSES =
         new Class[]{ActivityInstanceBean.class,
                     ActivityInstanceLogBean.class,
                     ActivityInstanceHistoryBean.class,
                     ActivityInstanceProperty.class,
                     TransitionInstanceBean.class,
                     TransitionTokenBean.class,
                     DaemonLog.class,
                     DataValueBean.class,
                     EventBindingBean.class,
                     LogEntryBean.class,
                     PropertyPersistor.class,
                     TimerLog.class,
                     UserGroupBean.class,
                     UserGroupProperty.class,
                     UserBean.class,
                     UserProperty.class,
                     UserSessionBean.class,
                     UserParticipantLink.class,
                     UserUserGroupLink.class,
                     ProcessInstanceBean.class,
                     ProcessInstanceProperty.class,
                     ProcessInstanceScopeBean.class,
                     ProcessInstanceHierarchyBean.class,
                     StructuredDataValueBean.class,
                     UserDomainBean.class,
                     UserDomainHierarchyBean.class,
                     UserDomainUserBean.class,
                     UserRealmBean.class,
                     WorkItemBean.class,
                     StructuredDataBean.class,
                     ClobDataBean.class,
                     DepartmentBean.class,
                     DepartmentHierarchyBean.class,
                     ModelRefBean.class,
                     ModelDeploymentBean.class,
                     PreferencesBean.class,
                     ProcessInstanceLinkBean.class,
                     ProcessInstanceLinkTypeBean.class
         };

   /**
    * All tables for modeling classes.
    */
   public static final Class[] PERSISTENT_MODELING_CLASSES =
         new Class[]{
            ModelPersistorBean.class,
            LargeStringHolder.class,
            AuditTrailActivityBean.class,
            AuditTrailDataBean.class,
            AuditTrailEventHandlerBean.class,
            AuditTrailParticipantBean.class,
            AuditTrailPartitionBean.class,
            AuditTrailProcessDefinitionBean.class,
            AuditTrailTransitionBean.class,
            AuditTrailTriggerBean.class,
         };
}
