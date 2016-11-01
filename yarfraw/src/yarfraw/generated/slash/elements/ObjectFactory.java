//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.5-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.06.10 at 11:58:15 PM EDT 
//


package yarfraw.generated.slash.elements;

import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the yarfraw.generated.slash.elements package. 
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

    private final static QName _Department_QNAME = new QName("http://purl.org/rss/1.0/modules/slash/", "department");
    private final static QName _HitParade_QNAME = new QName("http://purl.org/rss/1.0/modules/slash/", "hit_parade");
    private final static QName _Section_QNAME = new QName("http://purl.org/rss/1.0/modules/slash/", "section");
    private final static QName _Comments_QNAME = new QName("http://purl.org/rss/1.0/modules/slash/", "comments");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: yarfraw.generated.slash.elements
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SlashExtension }
     * 
     */
    public SlashExtension createSlashExtension() {
        return new SlashExtension();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://purl.org/rss/1.0/modules/slash/", name = "department")
    public JAXBElement<String> createDepartment(String value) {
        return new JAXBElement<String>(_Department_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://purl.org/rss/1.0/modules/slash/", name = "hit_parade")
    public JAXBElement<String> createHitParade(String value) {
        return new JAXBElement<String>(_HitParade_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://purl.org/rss/1.0/modules/slash/", name = "section")
    public JAXBElement<String> createSection(String value) {
        return new JAXBElement<String>(_Section_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://purl.org/rss/1.0/modules/slash/", name = "comments")
    public JAXBElement<BigInteger> createComments(BigInteger value) {
        return new JAXBElement<BigInteger>(_Comments_QNAME, BigInteger.class, null, value);
    }

}
