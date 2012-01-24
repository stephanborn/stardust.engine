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
package org.eclipse.stardust.common.utils.xml;

import junit.framework.TestSuite;

import org.eclipse.stardust.common.utils.xml.stream.StaxUtilsTest;
import org.eclipse.stardust.engine.extensions.xml.data.XMLAccessPathTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses( {StaxUtilsTest.class, XMLAccessPathTest.class} )
public class XmlUtilsTestSuite extends TestSuite
{
   /* test suite */
}
