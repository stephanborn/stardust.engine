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
package org.eclipse.stardust.engine.core.compatibility.spi.runtime.gui;

import javax.swing.*;

import org.eclipse.stardust.engine.api.model.EventAware;
import org.eclipse.stardust.engine.api.runtime.EventHandlerBinding;


/**
 * @author ubirkemeyer
 * @version $Revision$
 */
public abstract class RuntimeConditionPanel extends JPanel
{
   public abstract void apply();

   public abstract void validateSettings();

   public abstract void setData(EventAware owner, EventHandlerBinding handler);
}
