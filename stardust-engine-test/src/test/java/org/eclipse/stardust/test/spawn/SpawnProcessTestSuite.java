/**********************************************************************************
 * Copyright (c) 2012 SunGard CSA LLC and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    SunGard CSA LLC - initial API and implementation and/or initial documentation
 **********************************************************************************/
package org.eclipse.stardust.test.spawn;

import org.eclipse.stardust.test.api.junit.LocalJcrH2TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * <p>
 * This test suite bundles tests for the <i>Spawn Process</i> functionality,
 * which allows for ad hoc spawning of process instances (refer to the Stardust documentation 
 * for details about <i>Spawn Process</i>).
 * </p>
 * 
 * @author Nicolas.Werlein
 * @version $Revision$
 */
@RunWith(Suite.class)
@SuiteClasses({ SpawnProcessTest.class })
public class SpawnProcessTestSuite extends LocalJcrH2TestSuite
{
   /* test suite */
}