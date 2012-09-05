//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-558 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.06.05 at 11:30:22 AM CEST 
//


package org.eclipse.stardust.engine.extensions.jaxws.addressing.wsdl;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.infinity.bpm.rt.integration.webservices.addressing.wsdl package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    public static final String NAMESPACE = "http://www.w3.org/2006/02/addressing/wsdl";

    public final static String PREFIX = "wsaw";

    private final static QName _ServiceName_QNAME = new QName(NAMESPACE, "ServiceName", PREFIX);
    private final static QName _InterfaceName_QNAME = new QName(NAMESPACE, "InterfaceName", PREFIX);

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.infinity.addressing.wsdl
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link UsingAddressing }
     * 
     */
    public UsingAddressing createUsingAddressing() {
        return new UsingAddressing();
    }

    /**
     * Create an instance of {@link ServiceNameType }
     * 
     */
    public ServiceNameType createServiceNameType() {
        return new ServiceNameType();
    }

    /**
     * Create an instance of {@link Anonymous }
     * 
     */
    public Anonymous createAnonymous() {
        return new Anonymous();
    }

    /**
     * Create an instance of {@link AttributedQNameType }
     * 
     */
    public AttributedQNameType createAttributedQNameType() {
        return new AttributedQNameType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceNameType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = NAMESPACE, name = "ServiceName")
    public JAXBElement<ServiceNameType> createServiceName(ServiceNameType value) {
        return new JAXBElement<ServiceNameType>(_ServiceName_QNAME, ServiceNameType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AttributedQNameType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = NAMESPACE, name = "InterfaceName")
    public JAXBElement<AttributedQNameType> createInterfaceName(AttributedQNameType value) {
        return new JAXBElement<AttributedQNameType>(_InterfaceName_QNAME, AttributedQNameType.class, null, value);
    }

}