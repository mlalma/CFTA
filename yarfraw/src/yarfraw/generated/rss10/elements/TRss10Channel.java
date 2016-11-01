//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.5-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.06.10 at 11:58:13 PM EDT 
//


package yarfraw.generated.rss10.elements;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;


/**
 * The channel element contains metadata describing the channel itself, 
 * 		including a title, brief description, and URL link to the described resource 
 * 		(the channel provider's home page, for instance). The {resource} URL of the channel 
 * 		element's rdf:about attribute must be unique with respect to any other rdf:about 
 * 		attributes in the RSS document and is a URI which identifies the channel. Most commonly, 
 * 		this is either the URL of the homepage being described or a URL where the RSS file can be found.
 * 
 * <p>Java class for tRss10Channel complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tRss10Channel">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element name="title" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="link" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *           &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="image" type="{http://purl.org/rss/1.0/}tRss10Image" minOccurs="0"/>
 *           &lt;element ref="{http://purl.org/rss/1.0/}textinput" minOccurs="0"/>
 *           &lt;element ref="{http://purl.org/rss/1.0/}image" minOccurs="0"/>
 *           &lt;element ref="{http://purl.org/rss/1.0/}items"/>
 *           &lt;element ref="{http://purl.org/rss/1.0/modules/syndication/}updatePeriod" minOccurs="0"/>
 *           &lt;element ref="{http://purl.org/rss/1.0/modules/syndication/}updateFrequency" minOccurs="0"/>
 *           &lt;element ref="{http://purl.org/dc/elements/1.1/}subject" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://purl.org/dc/elements/1.1/}publisher" minOccurs="0"/>
 *           &lt;element ref="{http://purl.org/dc/elements/1.1/}creator" minOccurs="0"/>
 *           &lt;element ref="{http://purl.org/dc/elements/1.1/}rights" minOccurs="0"/>
 *           &lt;element ref="{http://purl.org/dc/elements/1.1/}date" minOccurs="0"/>
 *           &lt;element ref="{http://purl.org/dc/elements/1.1/}language" minOccurs="0"/>
 *           &lt;element ref="{http://purl.org/dc/elements/1.1/}contributor" minOccurs="0"/>
 *           &lt;any/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute ref="{http://www.w3.org/1999/02/22-rdf-syntax-ns#}resource"/>
 *       &lt;attribute ref="{http://www.w3.org/1999/02/22-rdf-syntax-ns#}about use="required""/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tRss10Channel", propOrder = {
    "titleOrLinkOrDescription"
})
public class TRss10Channel {

    @XmlElementRefs({
        @XmlElementRef(name = "date", namespace = "http://purl.org/dc/elements/1.1/", type = JAXBElement.class),
        @XmlElementRef(name = "description", namespace = "http://purl.org/rss/1.0/", type = JAXBElement.class),
        @XmlElementRef(name = "publisher", namespace = "http://purl.org/dc/elements/1.1/", type = JAXBElement.class),
        @XmlElementRef(name = "link", namespace = "http://purl.org/rss/1.0/", type = JAXBElement.class),
        @XmlElementRef(name = "contributor", namespace = "http://purl.org/dc/elements/1.1/", type = JAXBElement.class),
        @XmlElementRef(name = "image", namespace = "http://purl.org/rss/1.0/", type = JAXBElement.class),
        @XmlElementRef(name = "rights", namespace = "http://purl.org/dc/elements/1.1/", type = JAXBElement.class),
        @XmlElementRef(name = "updatePeriod", namespace = "http://purl.org/rss/1.0/modules/syndication/", type = JAXBElement.class),
        @XmlElementRef(name = "creator", namespace = "http://purl.org/dc/elements/1.1/", type = JAXBElement.class),
        @XmlElementRef(name = "subject", namespace = "http://purl.org/dc/elements/1.1/", type = JAXBElement.class),
        @XmlElementRef(name = "title", namespace = "http://purl.org/rss/1.0/", type = JAXBElement.class),
        @XmlElementRef(name = "updateFrequency", namespace = "http://purl.org/rss/1.0/modules/syndication/", type = JAXBElement.class),
        @XmlElementRef(name = "items", namespace = "http://purl.org/rss/1.0/", type = JAXBElement.class),
        @XmlElementRef(name = "language", namespace = "http://purl.org/dc/elements/1.1/", type = JAXBElement.class),
        @XmlElementRef(name = "textinput", namespace = "http://purl.org/rss/1.0/", type = JAXBElement.class)
    })
    @XmlAnyElement
    protected List<Object> titleOrLinkOrDescription;
    @XmlAttribute(namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#")
    @XmlSchemaType(name = "anyURI")
    protected String resource;
    @XmlAttribute(namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String about;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the titleOrLinkOrDescription property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the titleOrLinkOrDescription property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTitleOrLinkOrDescription().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link DcType }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link DcType }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link DcType }{@code >}
     * {@link JAXBElement }{@code <}{@link TRss10Image }{@code >}
     * {@link JAXBElement }{@code <}{@link DcType }{@code >}
     * {@link JAXBElement }{@code <}{@link UpdatePeriodEnum }{@code >}
     * {@link JAXBElement }{@code <}{@link DcType }{@code >}
     * {@link JAXBElement }{@code <}{@link DcType }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link BigInteger }{@code >}
     * {@link JAXBElement }{@code <}{@link Items }{@code >}
     * {@link JAXBElement }{@code <}{@link DcType }{@code >}
     * {@link Element }
     * {@link JAXBElement }{@code <}{@link TRss10TextInput }{@code >}
     * 
     * 
     */
    public List<Object> getTitleOrLinkOrDescription() {
        if (titleOrLinkOrDescription == null) {
            titleOrLinkOrDescription = new ArrayList<Object>();
        }
        return this.titleOrLinkOrDescription;
    }

    /**
     * Gets the value of the resource property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResource() {
        return resource;
    }

    /**
     * Sets the value of the resource property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResource(String value) {
        this.resource = value;
    }

    /**
     * Gets the value of the about property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAbout() {
        return about;
    }

    /**
     * Sets the value of the about property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAbout(String value) {
        this.about = value;
    }

    /**
     * Gets a map that contains attributes that aren't bound to any typed property on this class.
     * 
     * <p>
     * the map is keyed by the name of the attribute and 
     * the value is the string value of the attribute.
     * 
     * the map returned by this method is live, and you can add new attribute
     * by updating the map directly. Because of this design, there's no setter.
     * 
     * 
     * @return
     *     always non-null
     */
    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

}
