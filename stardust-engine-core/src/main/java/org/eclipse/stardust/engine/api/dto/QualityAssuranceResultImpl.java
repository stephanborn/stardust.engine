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

import java.util.Set;

import org.eclipse.stardust.engine.api.model.QualityAssuranceCode;




/**
 * Default implementation for {@link QualityAssuranceResult}
 * 
 * @author holger.prause
 * @version $Revision: $
 */
public class QualityAssuranceResultImpl implements QualityAssuranceResult
{
   /**
    * 
    */
   private static final long serialVersionUID = 2278284910575975122L;

   private ResultState state = ResultState.PASS_NO_CORRECTION;

   private boolean assignToLastPerformer = true;

   private Set<QualityAssuranceCode> codes;
   
   public void setQualityAssuranceCodes(Set<QualityAssuranceCode> codes)
   {
      this.codes  = codes;
   }

   public Set<QualityAssuranceCode> getQualityAssuranceCodes()
   {
      return codes;
   }

   public void setQualityAssuranceState(ResultState state)
   {
      this.state = state;
   }

   public ResultState getQualityAssuranceState()
   {
      return state;
   }

   public void setAssignToLastActivityPerformer(boolean assignToLastPerformer)
   {
      this.assignToLastPerformer = assignToLastPerformer;
   }

   public boolean getAssignToLastActivityPerformer()
   {
      return assignToLastPerformer;
   }
}
