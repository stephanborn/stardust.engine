package org.eclipse.stardust.engine.extensions.jaxws.wssecurity;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * JAXB class implementing wsse:UsernameToken element.
 * 
 * @author Florin.Herinean
 * @version $Revision: $
 */
class UsernameToken
{
   /**
    * Hardcoded value since we add only one wsse:UsernameToken in the security header.
    */
   @XmlAttribute(name = "Id", namespace = WSSecurity.WS_SECURITY_UTILITY_NAMESPACE)
   final String id = "UsernameToken-1";

   /**
    * The wsse:Username element.
    */
   @XmlElement(name = "Username", namespace = WSSecurity.WS_SECURITY_NAMESPACE, required = true)
   String username;

   /**
    * The wsse:Password element. Can be one of PasswordText or PasswordDigest.
    */
   @XmlElements({
      @XmlElement(name = "Password", namespace = WSSecurity.WS_SECURITY_NAMESPACE, type = Password.Text.class),
      @XmlElement(name = "Password", namespace = WSSecurity.WS_SECURITY_NAMESPACE, type = Password.Digest.class)})
   Password password;

   /**
    * The wsse:Nonce element.
    */
   @XmlElement(name = "Nonce", namespace = WSSecurity.WS_SECURITY_NAMESPACE)
   Nonce nonce;

   /**
    * The wsu:Created element.
    */
   @XmlElement(name = "Created", namespace = WSSecurity.WS_SECURITY_UTILITY_NAMESPACE)
   XMLGregorianCalendar created;
}
