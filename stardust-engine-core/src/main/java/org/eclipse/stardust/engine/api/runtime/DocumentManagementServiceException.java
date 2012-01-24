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

import org.eclipse.stardust.common.error.ErrorCase;
import org.eclipse.stardust.common.error.PublicException;

/**
 * Thrown when errors during performing DMS operations occur.
 */
public class DocumentManagementServiceException extends PublicException
{

   private static final long serialVersionUID = 1L;

   /**
    * Creates the exception with the provided error code.
    *
    * @param errorCase the error code.
    */
   public DocumentManagementServiceException(ErrorCase errorCase)
   {
      super(errorCase, null);
   }

   /**
    * Creates the exception with the provided error code and cause.
    *
    * @param errorCase the error code.
    * @param cause
    */
   public DocumentManagementServiceException(ErrorCase errorCase, Throwable cause)
   {
      super(errorCase, cause);
   }
   
   

}
