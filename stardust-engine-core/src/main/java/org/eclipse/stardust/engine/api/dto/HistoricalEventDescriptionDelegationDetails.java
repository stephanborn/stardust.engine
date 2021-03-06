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

import org.eclipse.stardust.common.error.PublicException;
import org.eclipse.stardust.engine.api.model.Participant;
import org.eclipse.stardust.engine.api.runtime.HistoricalEventDescriptionDelegation;


public class HistoricalEventDescriptionDelegationDetails implements
      HistoricalEventDescriptionDelegation
{
   private Participant fromPerformer;
   private Participant toPerformer;

   public HistoricalEventDescriptionDelegationDetails(Participant fromPerformer,
         Participant toPerformer)
   {
      super();
      this.fromPerformer = fromPerformer;
      this.toPerformer = toPerformer;
   }

   private static final long serialVersionUID = 1L;

   public Serializable getItem(int idx)
   {
      switch (idx)
      {
         case FROM_PERFORMER_IDX:
            return fromPerformer;
         case TO_PERFORMER_IDX:
            return toPerformer;
         default:
            throw new PublicException("");
      }
   }

   /* (non-Javadoc)
    * @see org.eclipse.stardust.engine.api.dto.HistoricalEventDescriptionDelegation#getFromPerformer()
    */
   public Participant getFromPerformer()
   {
      return fromPerformer;
   }

   /* (non-Javadoc)
    * @see org.eclipse.stardust.engine.api.dto.HistoricalEventDescriptionDelegation#getToPerformer()
    */
   public Participant getToPerformer()
   {
      return toPerformer;
   }

}
