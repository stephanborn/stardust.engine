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
package org.eclipse.stardust.engine.api.runtime;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.stardust.common.error.AccessForbiddenException;
import org.eclipse.stardust.common.error.ConcurrencyException;
import org.eclipse.stardust.common.error.InvalidArgumentException;
import org.eclipse.stardust.common.error.ObjectNotFoundException;
import org.eclipse.stardust.common.security.InvalidPasswordException;
import org.eclipse.stardust.engine.core.runtime.utils.ExecutionPermission;


/**
 * The UserService provides functionality for operating on CARNOT users.
 * <p>
 * This includes:
 * <ul>
 * <li>creating, modifying and invalidating users, and</li>
 * <li>accessing user data.</li>
 * </ul>
 * 
 * @author ubirkemeyer
 * @version $Revision$
 */
public interface UserService extends Service
{
   /**
    * Constant used as sessionId on sessions opened on archive audit trails. 
    */
   public static final String ARCHIVE = "_archive_";

   /**
    * Constant used as sessionId on sessions opened with user disabled for tracking. 
    */
   public static final String DISABLED_FOR_USER = "_disabled_for_user_";

   /**
    * Tracks the starting of a new user session.
    * 
    * @param clientId the client starting the session.
    * @return the new session id.
    */
   // no restrictions
   String startSession(String clientId);
   
   /**
    * Tracks the ending of a user session.
    * 
    * @param sessionId the id of the ending session.
    */
   // no restrictions
   void closeSession(String sessionId);
   
   /**
    * Checks if internal authentication is used.
    * 
    * @return true if CARNOT services use internal authentication.
    * 
    * @deprecated Superseded by {@link #isInternalAuthentication()}.
    */
   // no restrictions
   boolean isInternalAuthentified();

   /**
    * Checks if internal authentication is used.
    * 
    * @return true if CARNOT services use internal authentication.
    */
   // no restrictions
   boolean isInternalAuthentication();

   /**
    * Checks if internal authorization is used.
    * 
    * @return true if Carnot services use internal authorization.
    */
   // no restrictions
   boolean isInternalAuthorization();
   
   /**
    * Retrieves information on the current user.
    * 
    * @return the current user.
    */
   // no restrictions
   User getUser();

   /**
    * Modifies the current user.
    * 
    * @param oldPassword
    *           the current password.
    * @param firstName
    *           first name of the user.
    * @param lastName
    *           last name of the user.
    * @param newPassword
    *           the new password.
    * @param eMail
    *           email address of the user.
    * 
    * @return the modified user.
    * 
    * @throws ConcurrencyException 
    *            if another user operates on the current user.
    * @throws IllegalOperationException 
    *            if the authentication is not internal.
    * @throws InvalidPasswordException 
    *            if the new password does not match the given rules.
    */
   // no restrictions
   User modifyLoginUser(String oldPassword, String firstName, String lastName,
         String newPassword, String eMail) throws ConcurrencyException,
         IllegalOperationException, InvalidPasswordException;

   /**
    * Modifies the specified user.
    * 
    * @param user
    *           the user to be modified.
    * 
    * @return the modified user.
    * 
    * @throws ConcurrencyException 
    *            if another user operates on the specified one.
    * @throws ObjectNotFoundException 
    *            if the user or a given grant is not found.
    * @throws IllegalOperationException 
    *            if the authentication is not internal.
    * @throws InvalidPasswordException 
    *            if the new password does not match the given rules.
    * @throws AccessForbiddenException 
    *            if the current user is not allowed for operation.
    */
   @ExecutionPermission(id=ExecutionPermission.Id.modifyUserData)
   User modifyUser(User user) throws ConcurrencyException, ObjectNotFoundException,
         IllegalOperationException, InvalidPasswordException, AccessForbiddenException;

   /**
    * Resets the password of specified user by generated password according to configured
    * password rules.
    * 
    * @param account the user account to be modified.
    * @param properties Map providing further login properties.
    * 
    * @throws ConcurrencyException 
    *            if another user operates on the specified one.
    * @throws ObjectNotFoundException 
    *            if the user or a given grant is not found.
    * @throws IllegalOperationException 
    *            if the authentication is not internal.
    */
   @ExecutionPermission(
         id = ExecutionPermission.Id.resetUserPassword, 
         defaults = { ExecutionPermission.Default.ALL })
   void resetPassword(String account, Map properties) throws ConcurrencyException,
         ObjectNotFoundException, IllegalOperationException;

   /**
    * Modifies the specified user.
    * 
    * @param user
    *           the user to be modified.
    * @param generatePassword
    *           if set to true a password will be generated and send by mail to the user.
    * 
    * @return the modified user.
    * 
    * @throws ConcurrencyException 
    *            if another user operates on the specified one.
    * @throws ObjectNotFoundException 
    *            if the user or a given grant is not found.
    * @throws IllegalOperationException 
    *            if the authentication is not internal.
    * @throws InvalidPasswordException 
    *            if the new password does not match the given rules.
    * @throws AccessForbiddenException 
    *            if the current user is not allowed for operation.
    */
   @ExecutionPermission(id=ExecutionPermission.Id.modifyUserData)
   User modifyUser(User user, boolean generatePassword) throws ConcurrencyException, ObjectNotFoundException,
         IllegalOperationException, InvalidPasswordException, AccessForbiddenException;   
   
   /**
    * Creates a new user with default realm ID.
    * 
    * @param account
    *           the account name.
    * @param firstName
    *           first name of the user.
    * @param lastName
    *           last name of the user.
    * @param description
    *           short description.
    * @param password
    *           the user password.
    * @param eMail
    *           email address of the user.
    * @param validFrom
    *           validity start time or null if unlimited.
    * @param validTo
    *           validity end time or null if unlimited.
    * 
    * @return the newly created user.
    * 
    * @throws UserExistsException 
    *            if another user with the specified account already exists.
    * @throws IllegalOperationException 
    *            if the authentication is not internal.
    */
   @ExecutionPermission(id=ExecutionPermission.Id.modifyUserData)
   User createUser(String account, String firstName, String lastName, String description,
         String password, String eMail, Date validFrom, Date validTo)
         throws UserExistsException, IllegalOperationException, InvalidPasswordException;

   /**
    * Creates a new user.
    * 
    * @param realm
    *           the user's realm ID.
    * @param account
    *           the account name.
    * @param firstName
    *           first name of the user.
    * @param lastName
    *           last name of the user.
    * @param description
    *           short description.
    * @param password
    *           the user password.
    * @param eMail
    *           email address of the user.
    * @param validFrom
    *           validity start time or null if unlimited.
    * @param validTo
    *           validity end time or null if unlimited.
    * 
    * @return the newly created user.
    * 
    * @throws UserExistsException 
    *            if another user with the specified account already exists.
    * @throws IllegalOperationException 
    *            if the authentication is not internal.
    */
   @ExecutionPermission(id=ExecutionPermission.Id.modifyUserData)
   User createUser(String realm, String account, String firstName, String lastName,
         String description, String password, String eMail, Date validFrom, Date validTo)
         throws UserExistsException, IllegalOperationException, InvalidPasswordException;

   /**
    * Retrieves the user associated with the given account.
    * 
    * @param account
    *           the account name of the user to retrieve.
    * 
    * @return the user.
    * 
    * @throws ObjectNotFoundException 
    *            if there is no user with the specified account.
    */
   @ExecutionPermission(
         id=ExecutionPermission.Id.readUserData,
         defaults={ExecutionPermission.Default.ALL})
   User getUser(String account) throws ObjectNotFoundException, IllegalOperationException;

   /**
    * Retrieves the user associated with the given account.
    * 
    * @param realm
    *           the realm ID of the user to retrieve.
    * @param account
    *           the account name of the user to retrieve.
    * 
    * @return the user.
    * 
    * @throws ObjectNotFoundException 
    *            if there is no user with the specified account.
    */
   @ExecutionPermission(
         id=ExecutionPermission.Id.readUserData,
         defaults={ExecutionPermission.Default.ALL})
   User getUser(String realm, String account) throws ObjectNotFoundException;

   /**
    * Retrieves the specified user.
    * 
    * @param userOID
    *           the OID of the user to retrieve.
    * 
    * @return the user.
    * 
    * @throws ObjectNotFoundException 
    *            if there is no user with the specified oid.
    */
   @ExecutionPermission(
         id=ExecutionPermission.Id.readUserData,
         defaults={ExecutionPermission.Default.ALL})
   User getUser(long userOID) throws ObjectNotFoundException;

   /**
    * @deprecated Please use {@link #invalidateUser(String)} instead.
    */
   @ExecutionPermission(id=ExecutionPermission.Id.modifyUserData)
   User invalidate(String account) throws ObjectNotFoundException,
         IllegalOperationException;

   /**
    * Invalidates the user with the specified account.
    * 
    * @param account
    *           the account name of the user to invalidate.
    * 
    * @return the invalidated user.
    * 
    * @throws ObjectNotFoundException 
    *            if there is no user with the specified account.
    * @throws IllegalOperationException 
    *            if the authentication is not internal.
    */
   @ExecutionPermission(id=ExecutionPermission.Id.modifyUserData)
   User invalidateUser(String account) throws ObjectNotFoundException,
         IllegalOperationException;
   
   /**
    * Invalidates the user with the specified account.
    * 
    * @param realm
    *           the realm ID of the user to invalidate.
    * @param account
    *           the account name of the user to invalidate.
    * 
    * @return the invalidated user.
    * 
    * @throws ObjectNotFoundException 
    *            if there is no user with the specified account.
    * @throws IllegalOperationException 
    *            if the authentication is not internal.
    */
   @ExecutionPermission(id=ExecutionPermission.Id.modifyUserData)
   User invalidateUser(String realm, String account) throws ObjectNotFoundException,
         IllegalOperationException;
   
   /**
    * Creates a new user group.
    * 
    * @param id
    *           the user group ID. Must not be null or empty and must be unique.
    * @param name
    *           the user group name. Must not be null or empty.
    * @param description
    *           short description. Must not be null.
    * @param validFrom
    *           validity start time or null if unlimited.
    * @param validTo
    *           validity end time or null if unlimited.
    * 
    * @return the newly created user group.
    * 
    * @throws UserGroupExistsException 
    *           if another user group with the specified ID already exists.
    * @throws InvalidArgumentException
    *           if ID is empty
    *           if name is empty
    *           if description is empty
    * @throws IllegalOperationException 
    *           if operation is not allowed in this context.
    */
   @ExecutionPermission(id=ExecutionPermission.Id.modifyUserData)
   UserGroup createUserGroup(String id, String name, String description, Date validFrom,
         Date validTo) throws UserGroupExistsException, IllegalOperationException,
         InvalidPasswordException, InvalidArgumentException;

   /**
    * Retrieves the user group associated with the given ID.
    * 
    * @param id
    *           the id of the user group to retrieve.
    * 
    * @return the user group.
    * 
    * @throws ObjectNotFoundException 
    *           if there is no user group with the specified ID.
    */
   @ExecutionPermission(
         id=ExecutionPermission.Id.readUserData,
         defaults={ExecutionPermission.Default.ALL})
   UserGroup getUserGroup(String id) throws ObjectNotFoundException;
   
   /**
    * Retrieves the specified user group.
    * 
    * @param oid
    *           the OID of the user group to retrieve.
    * 
    * @return the user group.
    * 
    * @throws ObjectNotFoundException 
    *           if there is no user group with the specified OID.
    */
   @ExecutionPermission(
         id=ExecutionPermission.Id.readUserData,
         defaults={ExecutionPermission.Default.ALL})
   UserGroup getUserGroup(long oid) throws ObjectNotFoundException;

   /**
    * Modifies the specified user group.
    * 
    * @param userGroup
    *           the user group to be modified.
    * 
    * @return the modified user group.
    * 
    * @throws ConcurrencyException 
    *           if another user operates on the specified user group.
    * @throws ObjectNotFoundException 
    *           if the user group is not found.
    * @throws IllegalOperationException 
    *           if operation is not allowed in this context.
    */
   @ExecutionPermission(id=ExecutionPermission.Id.modifyUserData)
   UserGroup modifyUserGroup(UserGroup userGroup) throws ConcurrencyException,
         ObjectNotFoundException, IllegalOperationException;
   
   /**
    * Invalidates the user group associated with the given ID.
    * 
    * @param id
    *           the ID of the user group to be invalidated.
    * 
    * @return the invalidated user group.
    * 
    * @throws ConcurrencyException 
    *           if another user operates on the specified user group.
    * @throws ObjectNotFoundException 
    *           if the user group is not found.
    * @throws IllegalOperationException 
    *           if operation is not allowed in this context.
    */
   @ExecutionPermission(id=ExecutionPermission.Id.modifyUserData)
   UserGroup invalidateUserGroup(String id) throws ConcurrencyException,
         ObjectNotFoundException, IllegalOperationException;
   
   /**
    * Invalidates the specified user group.
    * 
    * @param oid
    *           the OID of the user group to invalidate.
    * 
    * @return the invalidated user group.
    * 
    * @throws ConcurrencyException 
    *           if another user operates on the specified user group.
    * @throws ObjectNotFoundException 
    *           if the user group is not found.
    * @throws IllegalOperationException 
    *           if operation is not allowed in this context.
    */
   @ExecutionPermission(id=ExecutionPermission.Id.modifyUserData)
   UserGroup invalidateUserGroup(long oid) throws ConcurrencyException,
         ObjectNotFoundException, IllegalOperationException;
   
   /**
    * Creates a new user realm.
    * 
    * @param id
    *           the user realm ID.
    * @param name
    *           the user realm name.
    * @param description
    *           short description.
    * 
    * @return the newly created user realm.
    * 
    * @throws UserRealmExistsException 
    *            if another user realm with the specified ID already exists.
    * @throws IllegalOperationException 
    *           if operation is not allowed in this context.
    */
   @ExecutionPermission(id=ExecutionPermission.Id.modifyUserData)
   UserRealm createUserRealm(String id, String name, String description)
         throws UserRealmExistsException, IllegalOperationException;
   
   /**
    * Drops the user realm associated with the given ID.
    * 
    * @param id
    *           the ID of the user realm to be dropped.
    * 
    * @throws ConcurrencyException 
    *           if another user operates on the specified user realm.
    * @throws ObjectNotFoundException 
    *           if the user realm is not found.
    * @throws IllegalOperationException 
    *           if at least one user is assigned to the user realm.
    */
   @ExecutionPermission(id=ExecutionPermission.Id.modifyUserData)
   void dropUserRealm(String id) throws ConcurrencyException, ObjectNotFoundException,
         IllegalOperationException;
   
   /**
    * Retrives all existing user realms.
    * 
    * @return list of all existing user realms.
    * 
    * @throws ConcurrencyException 
    *           if another user operates on the user realms.
    * @throws IllegalOperationException 
    *           if operation is not allowed in this context.
    */
   @ExecutionPermission(
         id=ExecutionPermission.Id.readUserData,
         defaults={ExecutionPermission.Default.ALL})
   List getUserRealms() throws ConcurrencyException, IllegalOperationException;
}
