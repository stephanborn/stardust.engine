//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-558 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.06.05 at 12:11:22 PM CEST 
//


package org.eclipse.stardust.engine.extensions.jaxws.addressing.infinity;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for valueEndpointType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="valueEndpointType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="data"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "valueEndpointType")
@XmlEnum
public enum ValueEndpointType {

    @XmlEnumValue("data")
    DATA("data");
    private final String value;

    ValueEndpointType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ValueEndpointType fromValue(String v) {
        for (ValueEndpointType c: ValueEndpointType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
