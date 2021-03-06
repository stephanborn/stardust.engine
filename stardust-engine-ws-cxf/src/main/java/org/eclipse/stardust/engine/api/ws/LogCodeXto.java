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

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LogCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="LogCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Unknown"/>
 *     &lt;enumeration value="ProcessWarehouse"/>
 *     &lt;enumeration value="Security"/>
 *     &lt;enumeration value="Engine"/>
 *     &lt;enumeration value="Recovery"/>
 *     &lt;enumeration value="Daemon"/>
 *     &lt;enumeration value="Event"/>
 *     &lt;enumeration value="WfXML"/>
 *     &lt;enumeration value="External"/>
 *     &lt;enumeration value="Administration"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "LogCode")
@XmlEnum
public enum LogCodeXto {

    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown"),
    @XmlEnumValue("ProcessWarehouse")
    PROCESS_WAREHOUSE("ProcessWarehouse"),
    @XmlEnumValue("Security")
    SECURITY("Security"),
    @XmlEnumValue("Engine")
    ENGINE("Engine"),
    @XmlEnumValue("Recovery")
    RECOVERY("Recovery"),
    @XmlEnumValue("Daemon")
    DAEMON("Daemon"),
    @XmlEnumValue("Event")
    EVENT("Event"),
    @XmlEnumValue("WfXML")
    WF_XML("WfXML"),
    @XmlEnumValue("External")
    EXTERNAL("External"),
    @XmlEnumValue("Administration")
    ADMINISTRATION("Administration");
    private final String value;

    LogCodeXto(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LogCodeXto fromValue(String v) {
        for (LogCodeXto c: LogCodeXto.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
