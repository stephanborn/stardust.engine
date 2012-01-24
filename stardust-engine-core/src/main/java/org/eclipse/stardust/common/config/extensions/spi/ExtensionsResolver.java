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
package org.eclipse.stardust.common.config.extensions.spi;

import java.util.List;
import java.util.Map;

import org.eclipse.stardust.common.annotations.SPI;
import org.eclipse.stardust.common.annotations.Status;
import org.eclipse.stardust.common.annotations.UseRestriction;


@SPI(status = Status.Experimental, useRestriction = UseRestriction.Internal)
public interface ExtensionsResolver
{
   <T> List<T> resolveExtensionProviders(Class<T> providerIntfc, Map<String, ?> resolutionConfig);
}
