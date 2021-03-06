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

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 			The InputDocument is used to specify a non existing document including content and targetFolder.
 * 			
 * 
 * <p>Java class for InputDocument complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InputDocument">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="documentInfo" type="{http://eclipse.org/stardust/ws/v2012a/api}DocumentInfo"/>
 *         &lt;element name="content" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="versionInfo" type="{http://eclipse.org/stardust/ws/v2012a/api}DocumentVersionInfo" minOccurs="0"/>
 *         &lt;element name="targetFolder" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="globalVariableId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InputDocument", propOrder = {
    "documentInfo",
    "content",
    "versionInfo",
    "targetFolder",
    "globalVariableId"
})
public class InputDocumentXto {

    @XmlElement(required = true, nillable = true)
    protected DocumentInfoXto documentInfo;
    @XmlMimeType("*/*")
    protected DataHandler content;
    protected DocumentVersionInfoXto versionInfo;
    protected String targetFolder;
    protected String globalVariableId;

    /**
     * Gets the value of the documentInfo property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentInfoXto }
     *     
     */
    public DocumentInfoXto getDocumentInfo() {
        return documentInfo;
    }

    /**
     * Sets the value of the documentInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentInfoXto }
     *     
     */
    public void setDocumentInfo(DocumentInfoXto value) {
        this.documentInfo = value;
    }

    /**
     * Gets the value of the content property.
     * 
     * @return
     *     possible object is
     *     {@link DataHandler }
     *     
     */
    public DataHandler getContent() {
        return content;
    }

    /**
     * Sets the value of the content property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataHandler }
     *     
     */
    public void setContent(DataHandler value) {
        this.content = value;
    }

    /**
     * Gets the value of the versionInfo property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentVersionInfoXto }
     *     
     */
    public DocumentVersionInfoXto getVersionInfo() {
        return versionInfo;
    }

    /**
     * Sets the value of the versionInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentVersionInfoXto }
     *     
     */
    public void setVersionInfo(DocumentVersionInfoXto value) {
        this.versionInfo = value;
    }

    /**
     * Gets the value of the targetFolder property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTargetFolder() {
        return targetFolder;
    }

    /**
     * Sets the value of the targetFolder property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTargetFolder(String value) {
        this.targetFolder = value;
    }

    /**
     * Gets the value of the globalVariableId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGlobalVariableId() {
        return globalVariableId;
    }

    /**
     * Sets the value of the globalVariableId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGlobalVariableId(String value) {
        this.globalVariableId = value;
    }

}
