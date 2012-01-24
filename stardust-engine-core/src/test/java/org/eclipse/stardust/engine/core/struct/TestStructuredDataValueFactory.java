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
package org.eclipse.stardust.engine.core.struct;

import org.eclipse.stardust.engine.core.runtime.beans.BigData;
import org.eclipse.stardust.engine.core.struct.IStructuredDataValueFactory;
import org.eclipse.stardust.engine.core.struct.beans.IStructuredDataValue;


public class TestStructuredDataValueFactory implements IStructuredDataValueFactory
{
   private OidGeneratorForTest oidGenerator = new OidGeneratorForTest();

   public IStructuredDataValue createKeyedElementEntry(long rootOid, long parentOid,
         long xPathOid, String index, String value, int typeKey)
   {
      return new TestStructuredDataValue(oidGenerator.getNextOid(), rootOid, parentOid,
            xPathOid, value, index, typeKey);
   }

   public IStructuredDataValue createRootElementEntry(long rootOid, long xPathOid,
         String key, String value)
   {
      return new TestStructuredDataValue(rootOid, rootOid,
            IStructuredDataValue.NO_PARENT, xPathOid, value, key, BigData.NULL);
   }
}
