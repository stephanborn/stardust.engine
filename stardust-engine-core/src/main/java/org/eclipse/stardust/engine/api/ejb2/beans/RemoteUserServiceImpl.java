/*
 * Generated from  Revision: 56264 
 */
package org.eclipse.stardust.engine.api.ejb2.beans;

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
 * @version 56264
 */
public class RemoteUserServiceImpl extends org.eclipse.stardust.engine.api.ejb2.beans.RemoteServiceImpl
{

    /**
     * Tracks the starting of a new user session.
     *
     * @param clientId the client starting the session.
     *
     * @return the new session id.
     *
     * @throws org.eclipse.stardust.engine.api.ejb2.WorkflowException as a wrapper for
     *         org.eclipse.stardust.engine.api.ejb2.PublicExceptions and org.eclipse.stardust.engine.api.ejb2.ResourceExceptions
     *
     * @see org.eclipse.stardust.engine.api.runtime.UserService#startSession(
     *     java.lang.String clientId)
     */
    public java.lang.String startSession(java.lang.String clientId)
         throws org.eclipse.stardust.engine.api.ejb2.WorkflowException
    {
      try
      {
         return ((org.eclipse.stardust.engine.api.runtime.UserService)
            service).startSession(clientId);
      }
      catch(org.eclipse.stardust.common.error.PublicException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
      catch(org.eclipse.stardust.common.error.ResourceException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
    }

    /**
     * Tracks the ending of a user session.
     *
     * @param sessionId the id of the ending session.
     *
     * @throws org.eclipse.stardust.engine.api.ejb2.WorkflowException as a wrapper for
     *         org.eclipse.stardust.engine.api.ejb2.PublicExceptions and org.eclipse.stardust.engine.api.ejb2.ResourceExceptions
     *
     * @see org.eclipse.stardust.engine.api.runtime.UserService#closeSession(
     *     java.lang.String sessionId)
     */
    public void closeSession(java.lang.String sessionId)
         throws org.eclipse.stardust.engine.api.ejb2.WorkflowException
    {
      try
      {
         ((org.eclipse.stardust.engine.api.runtime.UserService)
            service).closeSession(sessionId);
      }
      catch(org.eclipse.stardust.common.error.PublicException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
      catch(org.eclipse.stardust.common.error.ResourceException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
    }

    /**
     * Checks if internal authentication is used.
     *
     * @return true if CARNOT services use internal authentication.
     *
     * @deprecated Superseded by {@link #isInternalAuthentication()}.
     *
     * @throws org.eclipse.stardust.engine.api.ejb2.WorkflowException as a wrapper for
     *         org.eclipse.stardust.engine.api.ejb2.PublicExceptions and org.eclipse.stardust.engine.api.ejb2.ResourceExceptions
     *
     * @see org.eclipse.stardust.engine.api.runtime.UserService#isInternalAuthentified()
     */
    public boolean isInternalAuthentified()
         throws org.eclipse.stardust.engine.api.ejb2.WorkflowException
    {
      try
      {
         return ((org.eclipse.stardust.engine.api.runtime.UserService)
            service).isInternalAuthentified();
      }
      catch(org.eclipse.stardust.common.error.PublicException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
      catch(org.eclipse.stardust.common.error.ResourceException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
    }

    /**
     * Checks if internal authentication is used.
     *
     * @return true if CARNOT services use internal authentication.
     *
     * @throws org.eclipse.stardust.engine.api.ejb2.WorkflowException as a wrapper for
     *         org.eclipse.stardust.engine.api.ejb2.PublicExceptions and org.eclipse.stardust.engine.api.ejb2.ResourceExceptions
     *
     * @see org.eclipse.stardust.engine.api.runtime.UserService#isInternalAuthentication()
     */
    public boolean isInternalAuthentication()
         throws org.eclipse.stardust.engine.api.ejb2.WorkflowException
    {
      try
      {
         return ((org.eclipse.stardust.engine.api.runtime.UserService)
            service).isInternalAuthentication();
      }
      catch(org.eclipse.stardust.common.error.PublicException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
      catch(org.eclipse.stardust.common.error.ResourceException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
    }

    /**
     * Checks if internal authorization is used.
     *
     * @return true if Carnot services use internal authorization.
     *
     * @throws org.eclipse.stardust.engine.api.ejb2.WorkflowException as a wrapper for
     *         org.eclipse.stardust.engine.api.ejb2.PublicExceptions and org.eclipse.stardust.engine.api.ejb2.ResourceExceptions
     *
     * @see org.eclipse.stardust.engine.api.runtime.UserService#isInternalAuthorization()
     */
    public boolean isInternalAuthorization()
         throws org.eclipse.stardust.engine.api.ejb2.WorkflowException
    {
      try
      {
         return ((org.eclipse.stardust.engine.api.runtime.UserService)
            service).isInternalAuthorization();
      }
      catch(org.eclipse.stardust.common.error.PublicException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
      catch(org.eclipse.stardust.common.error.ResourceException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
    }

    /**
     * Retrieves information on the current user.
     *
     * @return the current user.
     *
     * @throws org.eclipse.stardust.engine.api.ejb2.WorkflowException as a wrapper for
     *         org.eclipse.stardust.engine.api.ejb2.PublicExceptions and org.eclipse.stardust.engine.api.ejb2.ResourceExceptions
     *
     * @see org.eclipse.stardust.engine.api.runtime.UserService#getUser()
     */
    public org.eclipse.stardust.engine.api.runtime.User getUser()
         throws org.eclipse.stardust.engine.api.ejb2.WorkflowException
    {
      try
      {
         return ((org.eclipse.stardust.engine.api.runtime.UserService)
            service).getUser();
      }
      catch(org.eclipse.stardust.common.error.PublicException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
      catch(org.eclipse.stardust.common.error.ResourceException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
    }

    /**
     * Modifies the current user.
     *
     * @param oldPassword
     *               the current password.
     * @param firstName
     *               first name of the user.
     * @param lastName
     *               last name of the user.
     * @param newPassword
     *               the new password.
     * @param eMail
     *               email address of the user.
     *
     * @return the modified user.
     *
     * @throws org.eclipse.stardust.common.error.ConcurrencyException 
     *                if another user operates on the current user.
     *     <em>Instances of {@link org.eclipse.stardust.common.error.ConcurrencyException} will
     *     be wrapped inside {@link org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.runtime.IllegalOperationException 
     *                if the authentication is not internal.
     *     <em>Instances of {@link
     *     org.eclipse.stardust.engine.api.runtime.IllegalOperationException} will be wrapped
     *     inside {@link org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.common.security.InvalidPasswordException 
     *                if the new password does not match the given rules.
     *     <em>Instances of {@link org.eclipse.stardust.common.security.InvalidPasswordException}
     *     will be wrapped inside {@link
     *     org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.ejb2.WorkflowException as a wrapper for
     *         org.eclipse.stardust.engine.api.ejb2.PublicExceptions and org.eclipse.stardust.engine.api.ejb2.ResourceExceptions
     *
     * @see org.eclipse.stardust.engine.api.runtime.UserService#modifyLoginUser(
     *     java.lang.String oldPassword, java.lang.String firstName, java.lang.String lastName,
     *     java.lang.String newPassword, java.lang.String eMail)
     */
    public org.eclipse.stardust.engine.api.runtime.User
         modifyLoginUser(
         java.lang.String oldPassword, java.lang.String firstName, java.lang.String
         lastName, java.lang.String newPassword, java.lang.String eMail)
         throws org.eclipse.stardust.engine.api.ejb2.WorkflowException
    {
      try
      {
         return ((org.eclipse.stardust.engine.api.runtime.UserService)
            service).modifyLoginUser(oldPassword, firstName, lastName, newPassword, eMail);
      }
      catch(org.eclipse.stardust.common.error.PublicException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
      catch(org.eclipse.stardust.common.error.ResourceException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
    }

    /**
     * Modifies the specified user.
     *
     * @param user
     *               the user to be modified.
     *
     * @return the modified user.
     *
     * @throws org.eclipse.stardust.common.error.ConcurrencyException 
     *                if another user operates on the specified one.
     *     <em>Instances of {@link org.eclipse.stardust.common.error.ConcurrencyException} will
     *     be wrapped inside {@link org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.common.error.ObjectNotFoundException 
     *                if the user or a given grant is not found.
     *     <em>Instances of {@link org.eclipse.stardust.common.error.ObjectNotFoundException}
     *     will be wrapped inside {@link
     *     org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.runtime.IllegalOperationException 
     *                if the authentication is not internal.
     *     <em>Instances of {@link
     *     org.eclipse.stardust.engine.api.runtime.IllegalOperationException} will be wrapped
     *     inside {@link org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.common.security.InvalidPasswordException 
     *                if the new password does not match the given rules.
     *     <em>Instances of {@link org.eclipse.stardust.common.security.InvalidPasswordException}
     *     will be wrapped inside {@link
     *     org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.common.error.AccessForbiddenException 
     *                if the current user is not allowed for operation.
     *     <em>Instances of {@link org.eclipse.stardust.common.error.AccessForbiddenException}
     *     will be wrapped inside {@link
     *     org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.ejb2.WorkflowException as a wrapper for
     *         org.eclipse.stardust.engine.api.ejb2.PublicExceptions and org.eclipse.stardust.engine.api.ejb2.ResourceExceptions
     *
     * @see org.eclipse.stardust.engine.api.runtime.UserService#modifyUser(
     *     org.eclipse.stardust.engine.api.runtime.User user)
     */
    public org.eclipse.stardust.engine.api.runtime.User
         modifyUser(org.eclipse.stardust.engine.api.runtime.User user)
         throws org.eclipse.stardust.engine.api.ejb2.WorkflowException
    {
      try
      {
         return ((org.eclipse.stardust.engine.api.runtime.UserService)
            service).modifyUser(user);
      }
      catch(org.eclipse.stardust.common.error.PublicException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
      catch(org.eclipse.stardust.common.error.ResourceException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
    }

    /**
     * Resets the password of specified user by generated password according to configured
     * password rules.
     *
     * @param account the user account to be modified.
     * @param properties Map providing further login properties.
     *
     * @throws org.eclipse.stardust.common.error.ConcurrencyException 
     *                if another user operates on the specified one.
     *     <em>Instances of {@link org.eclipse.stardust.common.error.ConcurrencyException} will
     *     be wrapped inside {@link org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.common.error.ObjectNotFoundException 
     *                if the user or a given grant is not found.
     *     <em>Instances of {@link org.eclipse.stardust.common.error.ObjectNotFoundException}
     *     will be wrapped inside {@link
     *     org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.runtime.IllegalOperationException 
     *                if the authentication is not internal.
     *     <em>Instances of {@link
     *     org.eclipse.stardust.engine.api.runtime.IllegalOperationException} will be wrapped
     *     inside {@link org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.ejb2.WorkflowException as a wrapper for
     *         org.eclipse.stardust.engine.api.ejb2.PublicExceptions and org.eclipse.stardust.engine.api.ejb2.ResourceExceptions
     *
     * @see org.eclipse.stardust.engine.api.runtime.UserService#resetPassword(
     *     java.lang.String account, java.util.Map properties)
     */
    public void resetPassword(java.lang.String account, java.util.Map properties)
         throws org.eclipse.stardust.engine.api.ejb2.WorkflowException
    {
      try
      {
         ((org.eclipse.stardust.engine.api.runtime.UserService)
            service).resetPassword(account, properties);
      }
      catch(org.eclipse.stardust.common.error.PublicException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
      catch(org.eclipse.stardust.common.error.ResourceException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
    }

    /**
     * Modifies the specified user.
     *
     * @param user
     *               the user to be modified.
     * @param generatePassword
     *               if set to true a password will be generated and send by mail to the user.
     *
     * @return the modified user.
     *
     * @throws org.eclipse.stardust.common.error.ConcurrencyException 
     *                if another user operates on the specified one.
     *     <em>Instances of {@link org.eclipse.stardust.common.error.ConcurrencyException} will
     *     be wrapped inside {@link org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.common.error.ObjectNotFoundException 
     *                if the user or a given grant is not found.
     *     <em>Instances of {@link org.eclipse.stardust.common.error.ObjectNotFoundException}
     *     will be wrapped inside {@link
     *     org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.runtime.IllegalOperationException 
     *                if the authentication is not internal.
     *     <em>Instances of {@link
     *     org.eclipse.stardust.engine.api.runtime.IllegalOperationException} will be wrapped
     *     inside {@link org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.common.security.InvalidPasswordException 
     *                if the new password does not match the given rules.
     *     <em>Instances of {@link org.eclipse.stardust.common.security.InvalidPasswordException}
     *     will be wrapped inside {@link
     *     org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.common.error.AccessForbiddenException 
     *                if the current user is not allowed for operation.
     *     <em>Instances of {@link org.eclipse.stardust.common.error.AccessForbiddenException}
     *     will be wrapped inside {@link
     *     org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.ejb2.WorkflowException as a wrapper for
     *         org.eclipse.stardust.engine.api.ejb2.PublicExceptions and org.eclipse.stardust.engine.api.ejb2.ResourceExceptions
     *
     * @see org.eclipse.stardust.engine.api.runtime.UserService#modifyUser(
     *     org.eclipse.stardust.engine.api.runtime.User user, boolean generatePassword)
     */
    public org.eclipse.stardust.engine.api.runtime.User
         modifyUser(
         org.eclipse.stardust.engine.api.runtime.User user, boolean generatePassword)
         throws org.eclipse.stardust.engine.api.ejb2.WorkflowException
    {
      try
      {
         return ((org.eclipse.stardust.engine.api.runtime.UserService)
            service).modifyUser(user, generatePassword);
      }
      catch(org.eclipse.stardust.common.error.PublicException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
      catch(org.eclipse.stardust.common.error.ResourceException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
    }

    /**
     * Creates a new user with default realm ID.
     *
     * @param account
     *               the account name.
     * @param firstName
     *               first name of the user.
     * @param lastName
     *               last name of the user.
     * @param description
     *               short description.
     * @param password
     *               the user password.
     * @param eMail
     *               email address of the user.
     * @param validFrom
     *               validity start time or null if unlimited.
     * @param validTo
     *               validity end time or null if unlimited.
     *
     * @return the newly created user.
     *
     * @throws org.eclipse.stardust.engine.api.runtime.UserExistsException 
     *                if another user with the specified account already exists.
     *     <em>Instances of {@link org.eclipse.stardust.engine.api.runtime.UserExistsException}
     *     will be wrapped inside {@link
     *     org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.runtime.IllegalOperationException 
     *                if the authentication is not internal.
     *     <em>Instances of {@link
     *     org.eclipse.stardust.engine.api.runtime.IllegalOperationException} will be wrapped
     *     inside {@link org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.ejb2.WorkflowException as a wrapper for
     *         org.eclipse.stardust.engine.api.ejb2.PublicExceptions and org.eclipse.stardust.engine.api.ejb2.ResourceExceptions
     *
     * @see org.eclipse.stardust.engine.api.runtime.UserService#createUser(
     *     java.lang.String account, java.lang.String firstName, java.lang.String lastName,
     *     java.lang.String description, java.lang.String password, java.lang.String eMail,
     *     java.util.Date validFrom, java.util.Date validTo)
     */
    public org.eclipse.stardust.engine.api.runtime.User
         createUser(
         java.lang.String account, java.lang.String firstName, java.lang.String lastName,
         java.lang.String description, java.lang.String password, java.lang.String eMail,
         java.util.Date validFrom, java.util.Date validTo)
         throws org.eclipse.stardust.engine.api.ejb2.WorkflowException
    {
      try
      {
         return ((org.eclipse.stardust.engine.api.runtime.UserService)
            service).createUser(
            account, firstName, lastName, description, password, eMail, validFrom, validTo);
      }
      catch(org.eclipse.stardust.common.error.PublicException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
      catch(org.eclipse.stardust.common.error.ResourceException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
    }

    /**
     * Creates a new user.
     *
     * @param realm
     *               the user's realm ID.
     * @param account
     *               the account name.
     * @param firstName
     *               first name of the user.
     * @param lastName
     *               last name of the user.
     * @param description
     *               short description.
     * @param password
     *               the user password.
     * @param eMail
     *               email address of the user.
     * @param validFrom
     *               validity start time or null if unlimited.
     * @param validTo
     *               validity end time or null if unlimited.
     *
     * @return the newly created user.
     *
     * @throws org.eclipse.stardust.engine.api.runtime.UserExistsException 
     *                if another user with the specified account already exists.
     *     <em>Instances of {@link org.eclipse.stardust.engine.api.runtime.UserExistsException}
     *     will be wrapped inside {@link
     *     org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.runtime.IllegalOperationException 
     *                if the authentication is not internal.
     *     <em>Instances of {@link
     *     org.eclipse.stardust.engine.api.runtime.IllegalOperationException} will be wrapped
     *     inside {@link org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.ejb2.WorkflowException as a wrapper for
     *         org.eclipse.stardust.engine.api.ejb2.PublicExceptions and org.eclipse.stardust.engine.api.ejb2.ResourceExceptions
     *
     * @see org.eclipse.stardust.engine.api.runtime.UserService#createUser(
     *     java.lang.String realm, java.lang.String account, java.lang.String firstName,
     *     java.lang.String lastName, java.lang.String description, java.lang.String password,
     *     java.lang.String eMail, java.util.Date validFrom, java.util.Date validTo)
     */
    public org.eclipse.stardust.engine.api.runtime.User
         createUser(
         java.lang.String realm, java.lang.String account, java.lang.String firstName,
         java.lang.String lastName, java.lang.String description, java.lang.String
         password, java.lang.String eMail, java.util.Date validFrom, java.util.Date
         validTo)
         throws org.eclipse.stardust.engine.api.ejb2.WorkflowException
    {
      try
      {
         return ((org.eclipse.stardust.engine.api.runtime.UserService)
            service).createUser(
            realm, account, firstName, lastName, description, password, eMail, validFrom,
            validTo);
      }
      catch(org.eclipse.stardust.common.error.PublicException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
      catch(org.eclipse.stardust.common.error.ResourceException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
    }

    /**
     * Retrieves the user associated with the given account.
     *
     * @param account
     *               the account name of the user to retrieve.
     *
     * @return the user.
     *
     * @throws org.eclipse.stardust.common.error.ObjectNotFoundException 
     *                if there is no user with the specified account.
     *     <em>Instances of {@link org.eclipse.stardust.common.error.ObjectNotFoundException}
     *     will be wrapped inside {@link
     *     org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.ejb2.WorkflowException as a wrapper for
     *         org.eclipse.stardust.engine.api.ejb2.PublicExceptions and org.eclipse.stardust.engine.api.ejb2.ResourceExceptions
     *
     * @see org.eclipse.stardust.engine.api.runtime.UserService#getUser(java.lang.String account)
     */
    public org.eclipse.stardust.engine.api.runtime.User getUser(
         java.lang.String account)
         throws org.eclipse.stardust.engine.api.ejb2.WorkflowException
    {
      try
      {
         return ((org.eclipse.stardust.engine.api.runtime.UserService)
            service).getUser(account);
      }
      catch(org.eclipse.stardust.common.error.PublicException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
      catch(org.eclipse.stardust.common.error.ResourceException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
    }

    /**
     * Retrieves the user associated with the given account.
     *
     * @param realm
     *               the realm ID of the user to retrieve.
     * @param account
     *               the account name of the user to retrieve.
     *
     * @return the user.
     *
     * @throws org.eclipse.stardust.common.error.ObjectNotFoundException 
     *                if there is no user with the specified account.
     *     <em>Instances of {@link org.eclipse.stardust.common.error.ObjectNotFoundException}
     *     will be wrapped inside {@link
     *     org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.ejb2.WorkflowException as a wrapper for
     *         org.eclipse.stardust.engine.api.ejb2.PublicExceptions and org.eclipse.stardust.engine.api.ejb2.ResourceExceptions
     *
     * @see org.eclipse.stardust.engine.api.runtime.UserService#getUser(
     *     java.lang.String realm, java.lang.String account)
     */
    public org.eclipse.stardust.engine.api.runtime.User getUser(
         java.lang.String realm, java.lang.String account)
         throws org.eclipse.stardust.engine.api.ejb2.WorkflowException
    {
      try
      {
         return ((org.eclipse.stardust.engine.api.runtime.UserService)
            service).getUser(realm, account);
      }
      catch(org.eclipse.stardust.common.error.PublicException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
      catch(org.eclipse.stardust.common.error.ResourceException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
    }

    /**
     * Retrieves the specified user.
     *
     * @param userOID
     *               the OID of the user to retrieve.
     *
     * @return the user.
     *
     * @throws org.eclipse.stardust.common.error.ObjectNotFoundException 
     *                if there is no user with the specified oid.
     *     <em>Instances of {@link org.eclipse.stardust.common.error.ObjectNotFoundException}
     *     will be wrapped inside {@link
     *     org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.ejb2.WorkflowException as a wrapper for
     *         org.eclipse.stardust.engine.api.ejb2.PublicExceptions and org.eclipse.stardust.engine.api.ejb2.ResourceExceptions
     *
     * @see org.eclipse.stardust.engine.api.runtime.UserService#getUser(long userOID)
     */
    public org.eclipse.stardust.engine.api.runtime.User getUser(long userOID)
         throws org.eclipse.stardust.engine.api.ejb2.WorkflowException
    {
      try
      {
         return ((org.eclipse.stardust.engine.api.runtime.UserService)
            service).getUser(userOID);
      }
      catch(org.eclipse.stardust.common.error.PublicException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
      catch(org.eclipse.stardust.common.error.ResourceException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
    }

    /**
     * 
     *
     * @deprecated Please use {@link #invalidateUser(String)} instead.
     *
     * @throws org.eclipse.stardust.engine.api.ejb2.WorkflowException as a wrapper for
     *         org.eclipse.stardust.engine.api.ejb2.PublicExceptions and org.eclipse.stardust.engine.api.ejb2.ResourceExceptions
     *
     * @see org.eclipse.stardust.engine.api.runtime.UserService#invalidate(
     *     java.lang.String account)
     */
    public org.eclipse.stardust.engine.api.runtime.User
         invalidate(java.lang.String account)
         throws org.eclipse.stardust.engine.api.ejb2.WorkflowException
    {
      try
      {
         return ((org.eclipse.stardust.engine.api.runtime.UserService)
            service).invalidate(account);
      }
      catch(org.eclipse.stardust.common.error.PublicException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
      catch(org.eclipse.stardust.common.error.ResourceException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
    }

    /**
     * Invalidates the user with the specified account.
     *
     * @param account
     *               the account name of the user to invalidate.
     *
     * @return the invalidated user.
     *
     * @throws org.eclipse.stardust.common.error.ObjectNotFoundException 
     *                if there is no user with the specified account.
     *     <em>Instances of {@link org.eclipse.stardust.common.error.ObjectNotFoundException}
     *     will be wrapped inside {@link
     *     org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.runtime.IllegalOperationException 
     *                if the authentication is not internal.
     *     <em>Instances of {@link
     *     org.eclipse.stardust.engine.api.runtime.IllegalOperationException} will be wrapped
     *     inside {@link org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.ejb2.WorkflowException as a wrapper for
     *         org.eclipse.stardust.engine.api.ejb2.PublicExceptions and org.eclipse.stardust.engine.api.ejb2.ResourceExceptions
     *
     * @see org.eclipse.stardust.engine.api.runtime.UserService#invalidateUser(
     *     java.lang.String account)
     */
    public org.eclipse.stardust.engine.api.runtime.User
         invalidateUser(java.lang.String account)
         throws org.eclipse.stardust.engine.api.ejb2.WorkflowException
    {
      try
      {
         return ((org.eclipse.stardust.engine.api.runtime.UserService)
            service).invalidateUser(account);
      }
      catch(org.eclipse.stardust.common.error.PublicException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
      catch(org.eclipse.stardust.common.error.ResourceException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
    }

    /**
     * Invalidates the user with the specified account.
     *
     * @param realm
     *               the realm ID of the user to invalidate.
     * @param account
     *               the account name of the user to invalidate.
     *
     * @return the invalidated user.
     *
     * @throws org.eclipse.stardust.common.error.ObjectNotFoundException 
     *                if there is no user with the specified account.
     *     <em>Instances of {@link org.eclipse.stardust.common.error.ObjectNotFoundException}
     *     will be wrapped inside {@link
     *     org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.runtime.IllegalOperationException 
     *                if the authentication is not internal.
     *     <em>Instances of {@link
     *     org.eclipse.stardust.engine.api.runtime.IllegalOperationException} will be wrapped
     *     inside {@link org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.ejb2.WorkflowException as a wrapper for
     *         org.eclipse.stardust.engine.api.ejb2.PublicExceptions and org.eclipse.stardust.engine.api.ejb2.ResourceExceptions
     *
     * @see org.eclipse.stardust.engine.api.runtime.UserService#invalidateUser(
     *     java.lang.String realm, java.lang.String account)
     */
    public org.eclipse.stardust.engine.api.runtime.User
         invalidateUser(java.lang.String realm, java.lang.String account)
         throws org.eclipse.stardust.engine.api.ejb2.WorkflowException
    {
      try
      {
         return ((org.eclipse.stardust.engine.api.runtime.UserService)
            service).invalidateUser(realm, account);
      }
      catch(org.eclipse.stardust.common.error.PublicException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
      catch(org.eclipse.stardust.common.error.ResourceException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
    }

    /**
     * Creates a new user group.
     *
     * @param id
     *               the user group ID. Must not be null or empty and must be unique.
     * @param name
     *               the user group name. Must not be null or empty.
     * @param description
     *               short description. Must not be null.
     * @param validFrom
     *               validity start time or null if unlimited.
     * @param validTo
     *               validity end time or null if unlimited.
     *
     * @return the newly created user group.
     *
     * @throws org.eclipse.stardust.engine.api.runtime.UserGroupExistsException 
     *               if another user group with the specified ID already exists.
     *     <em>Instances of {@link
     *     org.eclipse.stardust.engine.api.runtime.UserGroupExistsException} will be wrapped
     *     inside {@link org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws InvalidArgumentException
     *               if ID is empty
     *               if name is empty
     *               if description is empty
     *     <em>Instances of {@link InvalidArgumentException
     *     } will be wrapped inside {@link
     *     org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.runtime.IllegalOperationException 
     *               if operation is not allowed in this context.
     *     <em>Instances of {@link
     *     org.eclipse.stardust.engine.api.runtime.IllegalOperationException} will be wrapped
     *     inside {@link org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.ejb2.WorkflowException as a wrapper for
     *         org.eclipse.stardust.engine.api.ejb2.PublicExceptions and org.eclipse.stardust.engine.api.ejb2.ResourceExceptions
     *
     * @see org.eclipse.stardust.engine.api.runtime.UserService#createUserGroup(
     *     java.lang.String id, java.lang.String name, java.lang.String description,
     *     java.util.Date validFrom, java.util.Date validTo)
     */
    public org.eclipse.stardust.engine.api.runtime.UserGroup
         createUserGroup(
         java.lang.String id, java.lang.String name, java.lang.String description,
         java.util.Date validFrom, java.util.Date validTo)
         throws org.eclipse.stardust.engine.api.ejb2.WorkflowException
    {
      try
      {
         return ((org.eclipse.stardust.engine.api.runtime.UserService)
            service).createUserGroup(id, name, description, validFrom, validTo);
      }
      catch(org.eclipse.stardust.common.error.PublicException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
      catch(org.eclipse.stardust.common.error.ResourceException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
    }

    /**
     * Retrieves the user group associated with the given ID.
     *
     * @param id
     *               the id of the user group to retrieve.
     *
     * @return the user group.
     *
     * @throws org.eclipse.stardust.common.error.ObjectNotFoundException 
     *               if there is no user group with the specified ID.
     *     <em>Instances of {@link org.eclipse.stardust.common.error.ObjectNotFoundException}
     *     will be wrapped inside {@link
     *     org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.ejb2.WorkflowException as a wrapper for
     *         org.eclipse.stardust.engine.api.ejb2.PublicExceptions and org.eclipse.stardust.engine.api.ejb2.ResourceExceptions
     *
     * @see org.eclipse.stardust.engine.api.runtime.UserService#getUserGroup(java.lang.String id)
     */
    public org.eclipse.stardust.engine.api.runtime.UserGroup
         getUserGroup(java.lang.String id)
         throws org.eclipse.stardust.engine.api.ejb2.WorkflowException
    {
      try
      {
         return ((org.eclipse.stardust.engine.api.runtime.UserService)
            service).getUserGroup(id);
      }
      catch(org.eclipse.stardust.common.error.PublicException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
      catch(org.eclipse.stardust.common.error.ResourceException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
    }

    /**
     * Retrieves the specified user group.
     *
     * @param oid
     *               the OID of the user group to retrieve.
     *
     * @return the user group.
     *
     * @throws org.eclipse.stardust.common.error.ObjectNotFoundException 
     *               if there is no user group with the specified OID.
     *     <em>Instances of {@link org.eclipse.stardust.common.error.ObjectNotFoundException}
     *     will be wrapped inside {@link
     *     org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.ejb2.WorkflowException as a wrapper for
     *         org.eclipse.stardust.engine.api.ejb2.PublicExceptions and org.eclipse.stardust.engine.api.ejb2.ResourceExceptions
     *
     * @see org.eclipse.stardust.engine.api.runtime.UserService#getUserGroup(long oid)
     */
    public org.eclipse.stardust.engine.api.runtime.UserGroup getUserGroup(
         long oid)
         throws org.eclipse.stardust.engine.api.ejb2.WorkflowException
    {
      try
      {
         return ((org.eclipse.stardust.engine.api.runtime.UserService)
            service).getUserGroup(oid);
      }
      catch(org.eclipse.stardust.common.error.PublicException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
      catch(org.eclipse.stardust.common.error.ResourceException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
    }

    /**
     * Modifies the specified user group.
     *
     * @param userGroup
     *               the user group to be modified.
     *
     * @return the modified user group.
     *
     * @throws org.eclipse.stardust.common.error.ConcurrencyException 
     *               if another user operates on the specified user group.
     *     <em>Instances of {@link org.eclipse.stardust.common.error.ConcurrencyException} will
     *     be wrapped inside {@link org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.common.error.ObjectNotFoundException 
     *               if the user group is not found.
     *     <em>Instances of {@link org.eclipse.stardust.common.error.ObjectNotFoundException}
     *     will be wrapped inside {@link
     *     org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.runtime.IllegalOperationException 
     *               if operation is not allowed in this context.
     *     <em>Instances of {@link
     *     org.eclipse.stardust.engine.api.runtime.IllegalOperationException} will be wrapped
     *     inside {@link org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.ejb2.WorkflowException as a wrapper for
     *         org.eclipse.stardust.engine.api.ejb2.PublicExceptions and org.eclipse.stardust.engine.api.ejb2.ResourceExceptions
     *
     * @see org.eclipse.stardust.engine.api.runtime.UserService#modifyUserGroup(
     *     org.eclipse.stardust.engine.api.runtime.UserGroup userGroup)
     */
    public org.eclipse.stardust.engine.api.runtime.UserGroup
         modifyUserGroup(org.eclipse.stardust.engine.api.runtime.UserGroup userGroup)
         throws org.eclipse.stardust.engine.api.ejb2.WorkflowException
    {
      try
      {
         return ((org.eclipse.stardust.engine.api.runtime.UserService)
            service).modifyUserGroup(userGroup);
      }
      catch(org.eclipse.stardust.common.error.PublicException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
      catch(org.eclipse.stardust.common.error.ResourceException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
    }

    /**
     * Invalidates the user group associated with the given ID.
     *
     * @param id
     *               the ID of the user group to be invalidated.
     *
     * @return the invalidated user group.
     *
     * @throws org.eclipse.stardust.common.error.ConcurrencyException 
     *               if another user operates on the specified user group.
     *     <em>Instances of {@link org.eclipse.stardust.common.error.ConcurrencyException} will
     *     be wrapped inside {@link org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.common.error.ObjectNotFoundException 
     *               if the user group is not found.
     *     <em>Instances of {@link org.eclipse.stardust.common.error.ObjectNotFoundException}
     *     will be wrapped inside {@link
     *     org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.runtime.IllegalOperationException 
     *               if operation is not allowed in this context.
     *     <em>Instances of {@link
     *     org.eclipse.stardust.engine.api.runtime.IllegalOperationException} will be wrapped
     *     inside {@link org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.ejb2.WorkflowException as a wrapper for
     *         org.eclipse.stardust.engine.api.ejb2.PublicExceptions and org.eclipse.stardust.engine.api.ejb2.ResourceExceptions
     *
     * @see org.eclipse.stardust.engine.api.runtime.UserService#invalidateUserGroup(
     *     java.lang.String id)
     */
    public org.eclipse.stardust.engine.api.runtime.UserGroup
         invalidateUserGroup(java.lang.String id)
         throws org.eclipse.stardust.engine.api.ejb2.WorkflowException
    {
      try
      {
         return ((org.eclipse.stardust.engine.api.runtime.UserService)
            service).invalidateUserGroup(id);
      }
      catch(org.eclipse.stardust.common.error.PublicException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
      catch(org.eclipse.stardust.common.error.ResourceException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
    }

    /**
     * Invalidates the specified user group.
     *
     * @param oid
     *               the OID of the user group to invalidate.
     *
     * @return the invalidated user group.
     *
     * @throws org.eclipse.stardust.common.error.ConcurrencyException 
     *               if another user operates on the specified user group.
     *     <em>Instances of {@link org.eclipse.stardust.common.error.ConcurrencyException} will
     *     be wrapped inside {@link org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.common.error.ObjectNotFoundException 
     *               if the user group is not found.
     *     <em>Instances of {@link org.eclipse.stardust.common.error.ObjectNotFoundException}
     *     will be wrapped inside {@link
     *     org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.runtime.IllegalOperationException 
     *               if operation is not allowed in this context.
     *     <em>Instances of {@link
     *     org.eclipse.stardust.engine.api.runtime.IllegalOperationException} will be wrapped
     *     inside {@link org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.ejb2.WorkflowException as a wrapper for
     *         org.eclipse.stardust.engine.api.ejb2.PublicExceptions and org.eclipse.stardust.engine.api.ejb2.ResourceExceptions
     *
     * @see org.eclipse.stardust.engine.api.runtime.UserService#invalidateUserGroup(long oid)
     */
    public org.eclipse.stardust.engine.api.runtime.UserGroup
         invalidateUserGroup(long oid)
         throws org.eclipse.stardust.engine.api.ejb2.WorkflowException
    {
      try
      {
         return ((org.eclipse.stardust.engine.api.runtime.UserService)
            service).invalidateUserGroup(oid);
      }
      catch(org.eclipse.stardust.common.error.PublicException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
      catch(org.eclipse.stardust.common.error.ResourceException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
    }

    /**
     * Creates a new user realm.
     *
     * @param id
     *               the user realm ID.
     * @param name
     *               the user realm name.
     * @param description
     *               short description.
     *
     * @return the newly created user realm.
     *
     * @throws org.eclipse.stardust.engine.api.runtime.UserRealmExistsException 
     *                if another user realm with the specified ID already exists.
     *     <em>Instances of {@link
     *     org.eclipse.stardust.engine.api.runtime.UserRealmExistsException} will be wrapped
     *     inside {@link org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.runtime.IllegalOperationException 
     *               if operation is not allowed in this context.
     *     <em>Instances of {@link
     *     org.eclipse.stardust.engine.api.runtime.IllegalOperationException} will be wrapped
     *     inside {@link org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.ejb2.WorkflowException as a wrapper for
     *         org.eclipse.stardust.engine.api.ejb2.PublicExceptions and org.eclipse.stardust.engine.api.ejb2.ResourceExceptions
     *
     * @see org.eclipse.stardust.engine.api.runtime.UserService#createUserRealm(
     *     java.lang.String id, java.lang.String name, java.lang.String description)
     */
    public org.eclipse.stardust.engine.api.runtime.UserRealm
         createUserRealm(
         java.lang.String id, java.lang.String name, java.lang.String description)
         throws org.eclipse.stardust.engine.api.ejb2.WorkflowException
    {
      try
      {
         return ((org.eclipse.stardust.engine.api.runtime.UserService)
            service).createUserRealm(id, name, description);
      }
      catch(org.eclipse.stardust.common.error.PublicException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
      catch(org.eclipse.stardust.common.error.ResourceException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
    }

    /**
     * Drops the user realm associated with the given ID.
     *
     * @param id
     *               the ID of the user realm to be dropped.
     *
     * @throws org.eclipse.stardust.common.error.ConcurrencyException 
     *               if another user operates on the specified user realm.
     *     <em>Instances of {@link org.eclipse.stardust.common.error.ConcurrencyException} will
     *     be wrapped inside {@link org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.common.error.ObjectNotFoundException 
     *               if the user realm is not found.
     *     <em>Instances of {@link org.eclipse.stardust.common.error.ObjectNotFoundException}
     *     will be wrapped inside {@link
     *     org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.runtime.IllegalOperationException 
     *               if at least one user is assigned to the user realm.
     *     <em>Instances of {@link
     *     org.eclipse.stardust.engine.api.runtime.IllegalOperationException} will be wrapped
     *     inside {@link org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.ejb2.WorkflowException as a wrapper for
     *         org.eclipse.stardust.engine.api.ejb2.PublicExceptions and org.eclipse.stardust.engine.api.ejb2.ResourceExceptions
     *
     * @see org.eclipse.stardust.engine.api.runtime.UserService#dropUserRealm(java.lang.String id)
     */
    public void dropUserRealm(java.lang.String id)
         throws org.eclipse.stardust.engine.api.ejb2.WorkflowException
    {
      try
      {
         ((org.eclipse.stardust.engine.api.runtime.UserService)
            service).dropUserRealm(id);
      }
      catch(org.eclipse.stardust.common.error.PublicException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
      catch(org.eclipse.stardust.common.error.ResourceException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
    }

    /**
     * Retrives all existing user realms.
     *
     * @return list of all existing user realms.
     *
     * @throws org.eclipse.stardust.common.error.ConcurrencyException 
     *               if another user operates on the user realms.
     *     <em>Instances of {@link org.eclipse.stardust.common.error.ConcurrencyException} will
     *     be wrapped inside {@link org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.runtime.IllegalOperationException 
     *               if operation is not allowed in this context.
     *     <em>Instances of {@link
     *     org.eclipse.stardust.engine.api.runtime.IllegalOperationException} will be wrapped
     *     inside {@link org.eclipse.stardust.engine.api.ejb2.WorkflowException}.</em>
     * @throws org.eclipse.stardust.engine.api.ejb2.WorkflowException as a wrapper for
     *         org.eclipse.stardust.engine.api.ejb2.PublicExceptions and org.eclipse.stardust.engine.api.ejb2.ResourceExceptions
     *
     * @see org.eclipse.stardust.engine.api.runtime.UserService#getUserRealms()
     */
    public java.util.List getUserRealms()
         throws org.eclipse.stardust.engine.api.ejb2.WorkflowException
    {
      try
      {
         return ((org.eclipse.stardust.engine.api.runtime.UserService)
            service).getUserRealms();
      }
      catch(org.eclipse.stardust.common.error.PublicException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
      catch(org.eclipse.stardust.common.error.ResourceException e)
      {
         throw new org.eclipse.stardust.engine.api.ejb2.WorkflowException(e);
      }
    }

    public void ejbCreate() throws javax.ejb.CreateException
    {
      super.init(org.eclipse.stardust.engine.api.runtime.UserService.class,
            org.eclipse.stardust.engine.core.runtime.beans.UserServiceImpl.class);
    }
}