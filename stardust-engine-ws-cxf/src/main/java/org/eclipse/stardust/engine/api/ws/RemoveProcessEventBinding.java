/*******************************************************************************
 * Copyright (c) 2012 SunGard CSA LLC and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    SunGard CSA LLC - initial API and implementation and/or initial documentation
 *******************************************************************************/

package org.eclipse.stardust.engine.api.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="processOid" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="eventHandlerId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "processOid",
    "eventHandlerId"
})
@XmlRootElement(name = "removeProcessEventBinding")
public class RemoveProcessEventBinding {

    protected long processOid;
    @XmlElement(required = true, nillable = true)
    protected String eventHandlerId;

    /**
     * Gets the value of the processOid property.
     * 
     */
    public long getProcessOid() {
        return processOid;
    }

    /**
     * Sets the value of the processOid property.
     * 
     */
    public void setProcessOid(long value) {
        this.processOid = value;
    }

    /**
     * Gets the value of the eventHandlerId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventHandlerId() {
        return eventHandlerId;
    }

    /**
     * Sets the value of the eventHandlerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventHandlerId(String value) {
        this.eventHandlerId = value;
    }

}
