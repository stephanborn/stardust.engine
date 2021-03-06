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
package org.eclipse.stardust.engine.extensions.jms.app;

import org.eclipse.stardust.common.StringKey;

/**
 *
 * @author ubirkemeyer
 * @version $Revision$
 */
public class JMSLocation extends StringKey
{
   public static final JMSLocation HEADER = new JMSLocation("HEADER", "Header");
   public static final JMSLocation BODY = new JMSLocation("BODY", "Body");

   public JMSLocation(String id, String defaultName)
   {
      super(id, defaultName);
   }
}
