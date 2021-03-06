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

package org.eclipse.stardust.engine.api.ws.query;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 		OrderCriteria are used to sort the result of a query.
 *         
 * 
 * <p>Java class for OrderCriteria complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OrderCriteria">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="attributeOrder" type="{http://eclipse.org/stardust/ws/v2012a/api/query}AttributeOrder"/>
 *           &lt;element name="dataOrder" type="{http://eclipse.org/stardust/ws/v2012a/api/query}DataOrder"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderCriteria", propOrder = {
    "attributeOrderOrDataOrder"
})
public class OrderCriteriaXto {

    @XmlElements({
        @XmlElement(name = "dataOrder", type = DataOrderXto.class),
        @XmlElement(name = "attributeOrder", type = AttributeOrderXto.class)
    })
    protected List<OrderCriterionXto> attributeOrderOrDataOrder;

    /**
     * Gets the value of the attributeOrderOrDataOrder property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attributeOrderOrDataOrder property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttributeOrderOrDataOrder().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DataOrderXto }
     * {@link AttributeOrderXto }
     * 
     * 
     */
    public List<OrderCriterionXto> getAttributeOrderOrDataOrder() {
        if (attributeOrderOrDataOrder == null) {
            attributeOrderOrDataOrder = new ArrayList<OrderCriterionXto>();
        }
        return this.attributeOrderOrDataOrder;
    }

}
