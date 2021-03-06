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
package org.eclipse.stardust.engine.api.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.eclipse.stardust.common.Attribute;
import org.eclipse.stardust.common.CollectionUtils;
import org.eclipse.stardust.common.CompareHelper;
import org.eclipse.stardust.common.config.Parameters;
import org.eclipse.stardust.common.error.InvalidArgumentException;
import org.eclipse.stardust.common.error.InvalidValueException;
import org.eclipse.stardust.common.log.LogManager;
import org.eclipse.stardust.common.log.Logger;
import org.eclipse.stardust.engine.api.model.IModel;
import org.eclipse.stardust.engine.api.model.IModelParticipant;
import org.eclipse.stardust.engine.api.model.IRole;
import org.eclipse.stardust.engine.api.model.ModelParticipant;
import org.eclipse.stardust.engine.api.model.ModelParticipantInfo;
import org.eclipse.stardust.engine.api.model.PredefinedConstants;
import org.eclipse.stardust.engine.api.model.QualifiedModelParticipantInfo;
import org.eclipse.stardust.engine.api.query.PreferenceQuery;
import org.eclipse.stardust.engine.api.runtime.BpmRuntimeError;
import org.eclipse.stardust.engine.api.runtime.Department;
import org.eclipse.stardust.engine.api.runtime.DepartmentInfo;
import org.eclipse.stardust.engine.api.runtime.Grant;
import org.eclipse.stardust.engine.api.runtime.PermissionState;
import org.eclipse.stardust.engine.api.runtime.User;
import org.eclipse.stardust.engine.api.runtime.UserGroup;
import org.eclipse.stardust.engine.api.runtime.UserRealm;
import org.eclipse.stardust.engine.api.runtime.UserService;
import org.eclipse.stardust.engine.core.persistence.Predicates;
import org.eclipse.stardust.engine.core.persistence.QueryExtension;
import org.eclipse.stardust.engine.core.persistence.Session;
import org.eclipse.stardust.engine.core.persistence.jdbc.SessionFactory;
import org.eclipse.stardust.engine.core.preferences.IPreferenceStorageManager;
import org.eclipse.stardust.engine.core.preferences.PreferenceScope;
import org.eclipse.stardust.engine.core.preferences.PreferenceStorageFactory;
import org.eclipse.stardust.engine.core.preferences.Preferences;
import org.eclipse.stardust.engine.core.runtime.beans.DetailsFactory;
import org.eclipse.stardust.engine.core.runtime.beans.IDepartment;
import org.eclipse.stardust.engine.core.runtime.beans.IUser;
import org.eclipse.stardust.engine.core.runtime.beans.IUserGroup;
import org.eclipse.stardust.engine.core.runtime.beans.ModelManagerFactory;
import org.eclipse.stardust.engine.core.runtime.beans.UserBean;
import org.eclipse.stardust.engine.core.runtime.beans.UserGroupDetails;
import org.eclipse.stardust.engine.core.runtime.beans.UserParticipantLink;
import org.eclipse.stardust.engine.core.runtime.beans.UserRealmDetails;
import org.eclipse.stardust.engine.core.runtime.beans.UserSessionBean;
import org.eclipse.stardust.engine.core.runtime.beans.UserUtils;
import org.eclipse.stardust.engine.core.runtime.utils.Authorization2;
import org.eclipse.stardust.engine.core.runtime.utils.AuthorizationContext;
import org.eclipse.stardust.engine.core.runtime.utils.ClientPermission;
import org.eclipse.stardust.engine.core.runtime.utils.DepartmentUtils;
import org.eclipse.stardust.engine.core.runtime.utils.Permissions;

/**
 * Client side view of a workflow user.
 * <p/>
 * Client side views of CARNOT model and runtime objects are exposed to a client as
 * readonly detail objects which contain a copy of the state of the corresponding server
 * object.
 * <p/>
 * <p/>
 * Contains information about roles and organizations the user belongs to as well
 * as corresponding worklist items.
 *
 * @author Marc Gille
 * @version $Revision$
 */
public class UserDetails implements User
{
   // TODO consider serializable contract stability
   private static final long serialVersionUID = 5031463670143301240L;

   private static final Logger trace = LogManager.getLogger(UserDetails.class);

   private UserDetailsLevel detailsLevel;

   private UserRealm realm;

   private long oid;
   private String account;
   private String firstName;
   private String lastName;
   private String eMail;
   private Date validFrom;
   private Date validTo;
   private String description;
   private List<Grant> grants = CollectionUtils.newArrayList();
   private Map<String, Object> properties = CollectionUtils.newHashMap();
   private String password;
   private Set<AddedGrant> newGrants = CollectionUtils.newHashSet();
   private List<UserGroup> groups = CollectionUtils.newArrayList();
   private Set<String> newGroupIds = CollectionUtils.newHashSet();
   private Date previousLoginTime;
   private boolean passwordExpired;
   private boolean isAdministrator = false;
   private Integer qualityAssurancePropability = null;

   private Map permissions;

   UserDetails(IUser user)
   {
      initDetailsLevel();
      init(user);
      initPublicPreferences(user);

      if (UserDetailsLevel.Full == detailsLevel)
      {
         // TODO: old grants does not know about departments !
         if (user instanceof UserBean)
         {
            UserBean ub = (UserBean) user;
            Iterator<UserParticipantLink> links = ub.getAllParticipantLinks();
            while (links.hasNext())
            {
               UserParticipantLink link = links.next();
               IModelParticipant participant = link.getParticipant();
               if (participant != null)
               {
                  if(participant instanceof IRole)
                  {
                     if (participant.getId().equals(PredefinedConstants.ADMINISTRATOR_ROLE))
                     {
                        if (isAdministrator)
                        {
                           continue;
                        }
                        isAdministrator = true;
                     }
                  }

                  Department department = DetailsFactory.create(link.getDepartment(),
                        IDepartment.class, DepartmentDetails.class);
                  grants.add(new GrantDetails(participant, department));
               }
               else
               {
                  if (trace.isDebugEnabled())
                  {
                     trace.debug("Will ignore grant with oid " + link.getOID()
                           + " for user " + user.getRealmQualifiedAccount()
                           + ": could not resolve participant.");
                  }
               }
            }
         }
         else
         {
            for (Iterator i = user.getAllParticipants(); i.hasNext();)
            {
               IModelParticipant participant = (IModelParticipant) i.next();
               if(participant instanceof IRole)
               {
                  if (participant.getId().equals(PredefinedConstants.ADMINISTRATOR_ROLE))
                  {
                     if (isAdministrator)
                     {
                        continue;
                     }
                     isAdministrator = true;
                  }
               }
               grants.add(new GrantDetails(participant, null));
            }
         }
         for (Iterator i = grants.iterator(); i.hasNext();)
         {
            Grant grant = ((Grant) i.next());
            newGrants.add(new AddedGrant(grant.getQualifiedId(), grant.getDepartment()));
         }

         for (Iterator i = user.getAllUserGroups(false); i.hasNext();)
         {
            IUserGroup group = (IUserGroup) i.next();
            groups.add(new UserGroupDetails(group));
            newGroupIds.add(group.getId());
         }
      }

      permissions = CollectionUtils.newHashMap();

      List<IModel> activeModels = ModelManagerFactory.getCurrent().findActiveModels();
      if (activeModels.isEmpty())
      {
         if(account.equals(PredefinedConstants.MOTU))
         {
            isAdministrator = true;
         }
      }

      if (!activeModels.isEmpty())
      {
         AuthorizationContext ctx = AuthorizationContext.create(new ClientPermission(
               Permissions.MODEL_MANAGE_AUTHORIZATION));
         ctx.setModels(activeModels);
         PermissionState ps = Authorization2.hasPermission(ctx) ? PermissionState.Granted : PermissionState.Denied;
         permissions.put(ctx.getPermissionId(), ps);

         ctx = AuthorizationContext.create(UserService.class, "modifyUser", User.class);
         ctx.setModels(activeModels);
         ps = Authorization2.hasPermission(ctx) ? PermissionState.Granted : PermissionState.Denied;
         permissions.put(ctx.getPermissionId(), ps);

         ctx = AuthorizationContext.create(UserService.class, "getUser", long.class);
         ctx.setModels(activeModels);
         ps = Authorization2.hasPermission(ctx) ? PermissionState.Granted : PermissionState.Denied;
         permissions.put(ctx.getPermissionId(), ps);
      }
   }

   private void initPublicPreferences(IUser user)
   {
      IPreferenceStorageManager preferenceStorageManager = PreferenceStorageFactory
            .getCurrent();

      final String IPP_VIEWS_COMMON = "ipp-views-common";
      final String PREFERENCES_ID = "preference";
      final String PICTURE_TYPE = "ipp-views-common.user-profile.prefs.myPicture.type";
      final String PICTURE_URL = "ipp-views-common.user-profile.prefs.myPicture.http.url";

      Preferences viewsCommonPreferences = preferenceStorageManager.getPreferences(
            user, PreferenceScope.USER, IPP_VIEWS_COMMON, PREFERENCES_ID);
      if (viewsCommonPreferences != null)
      {
         Map<String, Serializable> preferences = viewsCommonPreferences.getPreferences();
         if (preferences != null)
         {
            if (preferences.get(PICTURE_TYPE) != null)
            {
               this.setProperty(PICTURE_TYPE, preferences.get(PICTURE_TYPE));
            }
            if (preferences.get(PICTURE_URL) != null)
            {
               this.setProperty(PICTURE_URL, preferences.get(PICTURE_URL));
            }
         }
      }

      final String IPP_ADMIN_PORTAL = "ipp-admin-portal";
      final String USER_NAME_DISPLAY_FORMAT = "ipp-admin-portal.userNameDisplayFormat.prefs.displayFormat";

      Preferences adminPreferences = preferenceStorageManager.getPreferences(user,
            PreferenceScope.USER, IPP_ADMIN_PORTAL, PREFERENCES_ID);
      if (adminPreferences != null)
      {
         Map<String, Serializable> preferences = adminPreferences.getPreferences();
         if (preferences != null)
         {
            if (preferences.get(USER_NAME_DISPLAY_FORMAT) != null)
            {
               this.setProperty(USER_NAME_DISPLAY_FORMAT,
                     preferences.get(USER_NAME_DISPLAY_FORMAT));
            }
         }
      }
   }

   private void init(IUser user)
   {
      this.realm = new UserRealmDetails(user.getRealm());
      this.oid = user.getOID();
      this.account = user.getAccount();
      this.firstName = user.getFirstName();
      this.lastName = user.getLastName();
      this.eMail = user.getEMail();
      this.validFrom = user.getValidFrom();
      this.validTo = user.getValidTo();
      this.description = user.getDescription();
      this.previousLoginTime = user.getLastLoginTime();
      this.passwordExpired = user.isPasswordExpired();
      this.qualityAssurancePropability = user.getQualityAssuranceProbability();

      if (previousLoginTime != null)
      {
         Session session = SessionFactory.getSession(SessionFactory.AUDIT_TRAIL);
         UserSessionBean result = (UserSessionBean) session.findFirst(UserSessionBean.class,
               QueryExtension.where(Predicates.andTerm(
                     Predicates.isEqual(UserSessionBean.FR__USER, oid),
                     Predicates.lessThan(UserSessionBean.FR__START_TIME, previousLoginTime.getTime()))).
                     addOrderBy(UserSessionBean.FR__START_TIME, false));
         if (result != null)
         {
            previousLoginTime = result.getStartTime();
         }
      }

      if (ModelManagerFactory.getCurrent().findActiveModel() == null)
      {
         if (account.equals(PredefinedConstants.MOTU))
         {
            isAdministrator = true;
         }
      }

      if (UserDetailsLevel.WithProperties == detailsLevel
            || UserDetailsLevel.Full == detailsLevel)
      {
         UserUtils.collectProperties(user, properties);
      }
   }

   private void initDetailsLevel()
   {
      detailsLevel = (UserDetailsLevel) Parameters.instance().get(
            UserDetailsLevel.PRP_USER_DETAILS_LEVEL);
      if (null == detailsLevel)
      {
         detailsLevel = UserDetailsLevel.Full;
      }
   }

   /**
    * @deprecated Use getRealm().getPartitionOid();
    */
   public short getPartitionOID()
   {
      return realm.getPartitionOid();
   }

   /**
    * @deprecated Use getRealm().getPartitionId();
    */
   public String getPartitionId()
   {
      return realm.getPartitionId();
   }

   public long getOID()
   {
      return oid;
   }

   /**
    * @deprecated Use getRealm().getOID();
    */
   public long getRealmOID()
   {
      return realm.getOID();
   }

   /**
    * @deprecated Use getRealm().getId();
    */
   public String getRealmId()
   {
      return realm.getId();
   }

   public String getId()
   {
      return account;
   }

   /**
    * @deprecated
    */
   public String getNamespace()
   {
      return null;
   }

   public String getAccount()
   {
      return getId();
   }

   public String getName()
   {
      return firstName;
   }

   public String getFirstName()
   {
      return getName();
   }

   public String getLastName()
   {
      return lastName;
   }

   public String getEMail()
   {
      return eMail;
   }

   public Date getValidFrom()
   {
      return validFrom;
   }

   public Date getValidTo()
   {
      return validTo;
   }

   public String getDescription()
   {
      return description;
   }

   public void setAccount(String account)
   {
      this.account = account;
   }

   public void setDescription(String description)
   {
      this.description = description;
   }

   public void setEMail(String eMail)
   {
      this.eMail = eMail;
   }

   public void setFirstName(String firstName)
   {
      this.firstName = firstName;
   }

   public void setLastName(String lastName)
   {
      this.lastName = lastName;
   }

   public void setValidFrom(Date validFrom)
   {
      this.validFrom = validFrom;
   }

   public void setValidTo(Date validTo)
   {
      this.validTo = validTo;
   }

   public void removeAllGrants()
   {
      newGrants.clear();
   }

   public void setAllProperties(Map<String, Object> properties)
   {
      this.properties = CollectionUtils.newHashMap();

      for (Iterator i = properties.entrySet().iterator(); i.hasNext();)
      {
         Map.Entry entry = (Map.Entry) i.next();
         String key = (String) entry.getKey();
         Object property = entry.getValue();
         if (property instanceof Attribute)
         {
            if (!UserUtils.PROTECTED_ATTRIBUTES.contains(((Attribute) property).getName()))
            {
               this.properties.put(key, property);
            }
            else
            {
               if (trace.isDebugEnabled())
               {
                  trace.debug(((Attribute) property).getName() + " is an protected attribute!");
               }
            }
         }
         else
         {
            this.properties.put(key, property);
         }
      }
   }

   public Object getAttribute(String name)
   {
      return properties.get(name);
   }

   public Serializable getProperty(String name)
   {
      if (!UserUtils.PROTECTED_ATTRIBUTES.contains(name))
      {
         return (Serializable) getAttribute(name);
      }
      else
      {
         if (trace.isDebugEnabled())
         {
            trace.debug(name + " is an protected attribute!");
         }
      }
      return null;
   }

   public void setProperty(String name, Serializable value)
   {
      if (!UserUtils.PROTECTED_ATTRIBUTES.contains(name))
      {
         properties.put(name, value);
      }
      else
      {
         if (trace.isDebugEnabled())
         {
            trace.debug(name + " is an protected attribute!");
         }
      }
   }

   public Map<String, Object> getAllAttributes()
   {
      return Collections.unmodifiableMap(properties);
   }

   public Map<String, Object> getAllProperties()
   {
      return getAllAttributes();
   }

   public List<Grant> getAllGrants()
   {
      return Collections.unmodifiableList(grants);
   }

   public void setPassword(String password)
   {
      this.password = password;
   }

   public String getPassword()
   {
      return password;
   }

   private void addGrant(String qualifiedParticipantId, DepartmentInfo department)
   {
      newGrants.add(new AddedGrant(qualifiedParticipantId, department));
   }

   private void removeGrant(String qualifiedParticipantId, DepartmentInfo department)
   {
      Set<AddedGrant> toRemove = CollectionUtils.newSet();
      for (AddedGrant grant : newGrants)
      {
         if ((grant.getQualifiedId().equals(qualifiedParticipantId)
               || grant.getId().equals(qualifiedParticipantId))
               && DepartmentUtils.areEqual(grant.getDepartment(), department))
         {
            toRemove.add(grant);
         }
      }
      newGrants.removeAll(toRemove);
   }

   public void removeGrant(String qualifiedParticipantId)
   {
      removeGrant(qualifiedParticipantId, null);
   }

   public void removeGrant(ModelParticipantInfo participant)
      throws InvalidArgumentException
   {
      if (participant == null)
      {
         throw new InvalidArgumentException(BpmRuntimeError.BPMRT_NULL_ARGUMENT.raise("participant"));
      }
      String qualifiedId = participant instanceof QualifiedModelParticipantInfo
         ? ((QualifiedModelParticipantInfo) participant).getQualifiedId() : participant.getId();
      removeGrant(qualifiedId, participant.getDepartment());
   }

   public void addGrant(String qualifiedParticipantId)
   {
      addGrant(qualifiedParticipantId, null);
   }

   public void addGrant(ModelParticipantInfo participant)
      throws InvalidArgumentException
   {
      if (participant == null)
      {
         throw new InvalidArgumentException(BpmRuntimeError.BPMRT_NULL_ARGUMENT.raise("participant"));
      }
      String qualifiedId = participant instanceof QualifiedModelParticipantInfo
         ? ((QualifiedModelParticipantInfo) participant).getQualifiedId() : participant.getId();
      addGrant(qualifiedId, participant.getDepartment());
   }

   public Collection<AddedGrant> getNewGrants()
   {
      return Collections.unmodifiableSet(newGrants);
   }

   /**
    * @deprecated This method has moved to {@link ModelParticipant#getModelOID()}
    */
   public int getModelOID()
   {
      return 0;
   }

   /**
    * @deprecated This method has moved to {@link ModelParticipant#getElementOID()}
    */
   public int getElementOID()
   {
      return 0;
   }

   /**
    * @deprecated This method has moved to
    *             {@link ModelParticipant#getAllSuperOrganizations()}
    */
   public List getAllSuperOrganizations()
   {
      return Collections.EMPTY_LIST;
   }

   @Override
   public String toString()
   {
      return "User: " + lastName + ", " + firstName + " [oid: " + oid + ", account: " + account + ']';
   }

   public class AddedGrant implements Serializable
   {
      private static final long serialVersionUID = 1L;

      private final String id;
      private final String qualifiedId;
      private DepartmentInfo department;

      public AddedGrant(String qualifiedId, DepartmentInfo department)
      {
         this.id = QName.valueOf(qualifiedId).getLocalPart();
         this.qualifiedId = qualifiedId;
         this.department = department;
      }

      public boolean equals(Object other)
      {
         return (other instanceof AddedGrant)
               && CompareHelper.areEqual(qualifiedId, ((AddedGrant) other).qualifiedId)
               && compareDepartments(this.department, ((AddedGrant)other).department);
      }

      @Override
      public int hashCode()
      {
         final int prime = 31;
         int result = 1;
         result = prime * result + ((department == null || department == Department.DEFAULT) ? 0 : department.hashCode());
         result = prime * result + ((qualifiedId == null) ? 0 : qualifiedId.hashCode());
         return result;
      }

      public String getId()
      {
         return id;
      }

      public String getQualifiedId()
      {
         return qualifiedId;
      }

      public DepartmentInfo getDepartment()
      {
         return department;
      }
   }

   public List<UserGroup> getAllGroups()
   {
      return Collections.unmodifiableList(groups);
   }

   public boolean compareDepartments(DepartmentInfo dep1, DepartmentInfo dep2)
   {
      if (dep1 == null || dep1 == Department.DEFAULT)
      {
         return dep2 == null || dep2 == Department.DEFAULT;
      }
      if (dep2 == null || dep2 == Department.DEFAULT)
      {
         return false;
      }
      return dep1.getOID() == dep2.getOID();
   }

   /**
    * Returns a collection of user group id strings
    *
    * @return collection of user group id strings
    */
   public Collection<String> getNewGroupIds()
   {
      return Collections.unmodifiableSet(newGroupIds);
   }

   public void joinGroup(String id)
   {
      newGroupIds.add(id);
   }

   public void leaveGroup(String id)
   {
      newGroupIds.remove(id);
   }

   public UserRealm getRealm()
   {
      return realm;
   }

   public boolean equals(Object rawRhs)
   {
      boolean isEqual = false;

      if (this == rawRhs)
      {
         isEqual = true;
      }
      else if (rawRhs instanceof UserDetails)
      {
         final UserDetails rhs = (UserDetails) rawRhs;
         isEqual = (rhs.getOID() == oid);
      }

      return isEqual;
   }

   public UserDetailsLevel getDetailsLevel()
   {
      return detailsLevel;
   }

   public PermissionState getPermission(String permissionId)
   {
      PermissionState ps = (PermissionState) permissions.get(permissionId);
      return ps == null ? PermissionState.Unknown : ps;
   }

   public Date getPreviousLoginTime()
   {
      return previousLoginTime;
   }

   public boolean isPasswordExpired()
   {
      return passwordExpired;
   }

   public boolean isAdministrator()
   {
      return isAdministrator;
   }

   public String getQualifiedId()
   {
      // users have no namespace
      return getId();
   }

   public Integer getQualityAssuranceProbability()
   {
      return qualityAssurancePropability;
   }

   public void setQualityAssuranceProbability(Integer probability) throws InvalidValueException
   {
      if(probability != null && ((probability < 0) || (probability > 100)))
      {
         throw new InvalidValueException(
               BpmRuntimeError.BPMRT_INVALID_VALUE.raise(probability));
      }
      
      qualityAssurancePropability = probability;
   }
}