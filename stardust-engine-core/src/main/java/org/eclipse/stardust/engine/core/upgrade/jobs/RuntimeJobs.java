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
package org.eclipse.stardust.engine.core.upgrade.jobs;

import java.util.LinkedList;
import java.util.List;

/**
 * @author kberberich, ubirkemeyer
 * @version $Revision$
 */
public class RuntimeJobs
{
   private static List jobs;

   public static List getRuntimeJobs()
   {
      if (jobs == null)
      {
         jobs = new LinkedList();
         jobs.add(new R2_0_2fromPre2_0_0RuntimeJob());
         jobs.add(new R2_0_2from2_0_0RuntimeJob());
         jobs.add(new R2_0_3from2_0_2RuntimeJob()); 
         jobs.add(new R2_5_0from2_0_3RuntimeJob());
         jobs.add(new R2_5_1from2_5_0RuntimeJob());
         jobs.add(new R2_5_3from2_5_1RuntimeJob());
         jobs.add(new R2_6from2_5_1RuntimeJob());
         jobs.add(new R2_7_2from2_6RuntimeJob());
         jobs.add(new R2_7_3from2_7_2RuntimeJob());
         jobs.add(new R3_0_1from2_7_3RuntimeJob());
         jobs.add(new R3_0_1from3_0_0RuntimeJob());
         jobs.add(new R3_0_6from3_0_1RuntimeJob());
         jobs.add(new R3_2_0from3_0_6RuntimeJob());
         jobs.add(new R3_6_0from3_2_0RuntimeJob());
         jobs.add(new R4_0_0from3_6_0RuntimeJob());
         jobs.add(new R4_5_0from4_0_0RuntimeJob());
         jobs.add(new R4_6_0from4_5_0RuntimeJob());
         jobs.add(new R4_7_0from4_6_0RuntimeJob());
         jobs.add(new R4_9_0from4_7_0RuntimeJob());
         jobs.add(new R5_2_0from4_9_0RuntimeJob());
         jobs.add(new R6_0_0from5_2_0RuntimeJob());
         jobs.add(new R7_0_0from6_x_xRuntimeJob());
      }
      return jobs;
   }
}
