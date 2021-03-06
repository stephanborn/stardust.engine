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
package org.eclipse.stardust.engine.core.query.statistics.evaluation;

import java.util.Calendar;
import java.util.Date;

final class DateRange
{
   final Date now;
   final Date beginOfDay;
   final Date beginOfThisWeek;
   final Date beginOfLastWeek;
   final Date beginOfThisMonth;
   final Date beginOfLastMonth;
   
   public DateRange()
   {
      this.now = new Date();
      final Calendar cal = Calendar.getInstance();

      cal.setTime(now);
      cal.set(Calendar.HOUR_OF_DAY, 0);
      cal.set(Calendar.MINUTE, 0);
      cal.set(Calendar.SECOND, 0);
      cal.set(Calendar.MILLISECOND, 0);
      this.beginOfDay = cal.getTime();
      
      cal.setTime(beginOfDay);
      cal.setFirstDayOfWeek(Calendar.MONDAY);
      cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
      this.beginOfThisWeek = cal.getTime();
      
      cal.add(Calendar.WEEK_OF_YEAR, -1);
      this.beginOfLastWeek = cal.getTime();
      
      cal.setTime(beginOfDay);
      cal.set(Calendar.DAY_OF_MONTH, 1);
      this.beginOfThisMonth = cal.getTime();
      
      cal.add(Calendar.MONTH, -1);
      this.beginOfLastMonth = cal.getTime();
   }
   
   public Date getNow()
   {
      return now;
   }

   public Date getBeginOfDay()
   {
      return beginOfDay;
   }

   public Date getBeginOfThisWeek()
   {
      return beginOfThisWeek;
   }

   public Date getBeginOfLastWeek()
   {
      return beginOfLastWeek;
   }

   public Date getBeginOfThisMonth()
   {
      return beginOfThisMonth;
   }

   public Date getBeginOfLastMonth()
   {
      return beginOfLastMonth;
   }
}
