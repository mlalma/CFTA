//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.5-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.06.10 at 11:58:14 PM EDT 
//


package yarfraw.generated.mrss.elements;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element ref="{http://tools.search.yahoo.com/mrss/}group" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://tools.search.yahoo.com/mrss/}content" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://tools.search.yahoo.com/mrss/}rating" minOccurs="0"/>
 *         &lt;element ref="{http://tools.search.yahoo.com/mrss/}title" minOccurs="0"/>
 *         &lt;element ref="{http://tools.search.yahoo.com/mrss/}description" minOccurs="0"/>
 *         &lt;element ref="{http://tools.search.yahoo.com/mrss/}keywords" minOccurs="0"/>
 *         &lt;element ref="{http://tools.search.yahoo.com/mrss/}thumbnail" minOccurs="0"/>
 *         &lt;element ref="{http://tools.search.yahoo.com/mrss/}category" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://tools.search.yahoo.com/mrss/}hash" minOccurs="0"/>
 *         &lt;element ref="{http://tools.search.yahoo.com/mrss/}player" minOccurs="0"/>
 *         &lt;element ref="{http://tools.search.yahoo.com/mrss/}credit" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://tools.search.yahoo.com/mrss/}restriction" minOccurs="0"/>
 *         &lt;element ref="{http://tools.search.yahoo.com/mrss/}copyright" minOccurs="0"/>
 *         &lt;element ref="{http://tools.search.yahoo.com/mrss/}text" minOccurs="0"/>
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
    "group",
    "content",
    "rating",
    "title",
    "description",
    "keywords",
    "thumbnail",
    "category",
    "hash",
    "player",
    "credit",
    "restriction",
    "copyright",
    "text"
})
@XmlRootElement(name = "mrssExtension")
public class MrssExtension {

    protected List<MrssGroupType> group;
    protected List<MrssContentType> content;
    protected MrssRatingType rating;
    protected MrssTitleType title;
    protected MrssDescriptionType description;
    protected String keywords;
    protected MrssThumbnailType thumbnail;
    protected List<MrssCategoryType> category;
    protected MrssHashType hash;
    protected MrssPlayerType player;
    protected List<MrssCreditType> credit;
    protected MrssRestrictionType restriction;
    protected MrssCopyrightType copyright;
    protected MrssTextType text;

    /**
     *  
     * <strong>&lt;media:group&gt;</strong></p>
     * <p>&lt;media:group&gt; is a sub-element of &lt;item&gt;. It allows grouping of &lt;media:content&gt; elements that are effectively the same content, yet different representations. &nbsp; For instance: the same song recorded in both the WAV and MP3 format.
     * It's an optional element that must only be used for this purpose.</p>
     * 						 
     * 						Gets the value of the group property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the group property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGroup().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MrssGroupType }
     * 
     * 
     */
    public List<MrssGroupType> getGroup() {
        if (group == null) {
            group = new ArrayList<MrssGroupType>();
        }
        return this.group;
    }

    /**
     *  
     * <strong>&lt;media:content&gt;</strong></p>
     * <p>&lt;media:content&gt; is a sub-element of either &lt;item&gt; or &lt;media:group&gt;.&nbsp;Media objects that are not the same content should not be included in the same &lt;media:group&gt; element. &nbsp;The sequence of these items implies the order of presentation.
     * While many of the attributes appear to be audio/video specific, this element can be used to publish any type of media. 
     * It contains 14 attributes, most of which are optional.</p>
     * 
     * <pre> 
     *         &lt;media:content 
     *                url="http://www.foo.com/movie.mov" 
     *                fileSize="12216320" 
     *                type="video/quicktime"
     *                medium="video"
     *                isDefault="true" 
     *                expression="full" 
     *                bitrate="128" 
     *                framerate="25"
     *                samplingrate="44.1"
     *                channels="2"
     *                duration="185" 
     *                height="200"
     *                width="300" 
     *                lang="en" /&gt;</pre>
     * 
     * <p><em>url</em> should specify the direct url to the media object. If not included, a &lt;media:player&gt; element must be specified.</p><p><em>fileSize</em> is the number of bytes of the media object. It is an optional attribute.</p>
     * <p><em>type</em> is the standard MIME type of the object. It is an optional attribute.</p>
     * 
     * <p><em>medium</em> is the type of object (image | audio | video | document | executable). While this attribute can at times seem redundant if <em>type</em> is supplied, it is included because it simplifies decision making on the reader side, as well as flushes out any ambiguities between MIME type and object type. It is an optional attribute.</p> 
     * 
     * 
     * <p><em>isDefault</em> determines if this is the default object that should be used for the &lt;media:group&gt;.  There should only be one default object per &lt;media:group&gt;.  It is an optional attribute.</p>
     * 
     * <p><em>expression</em> determines if the object is a sample or the full version of the object, or even if it is a continuous stream (sample | full | nonstop).  
     * Default value is 'full'.  
     * It is an optional attribute. </p>
     * 
     * <p><em>bitrate</em> is the kilobits per second rate of media.  It is an optional attribute.</p>
     * <p><em>framerate</em> is the number of frames per second for the media object.  It is an optional attribute.</p>
     * <p><em>samplingrate</em> is the number of samples per second taken to create the media object. It is expressed in thousands of samples per second (kHz). It is an optional attribute.</p>
     * <p><em>channels</em> is number of audio channels in the media object. It is an optional attribute.
     * </p><p><em>duration</em> is the number of seconds the media object plays.   It is an optional attribute.</p>
     * 
     * 
     * 
     * 
     * 
     * <p><em>height</em> is the height of the media object.  It is an optional attribute.</p>
     * <p><em>width</em> is the width of the media object.  It is an optional attribute.</p>
     * 
     * <p><em>lang</em> is the primary language encapsulated in the media object. Language codes possible are detailed in RFC 3066. This attribute is used similar to the <em>xml:lang</em> attribute detailed in the XML 1.0 Specification (Third Edition).  It is an optional attribute.</p>
     * 
     *         
     *         <p> These optional attributes, along with the optional elements below, contain the primary metadata entries needed to index and organize media content.
     *             Additional supported attributes for describing images, audio, and video may be added in future revisions of this document.</p>
     * <p><strong>Note:</strong> 
     * While both &lt;media:content&gt; and &lt;media:group&gt; have no limitations on the number of times they can appear, the general nature of RSS should be preserved: an &lt;item&gt; represents a "story". Simply stated, this is similar to the blog style of syndication.
     *  However, if one is using this module to strictly publish media, there should be one &lt;item&gt; element for each media object/group. This is to allow for proper attribution for the origination of the media content through the &lt;link&gt; element.  It also allows the full benefit of the other RSS elements to be realized.</p>
     * 						 
     * 						Gets the value of the content property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MrssContentType }
     * 
     * 
     */
    public List<MrssContentType> getContent() {
        if (content == null) {
            content = new ArrayList<MrssContentType>();
        }
        return this.content;
    }

    /**
     *  
     * <p>This allows the permissible audience to be declared. If this element is not included, it assumes that no restrictions are necessary. It has one optional attribute. </p>
     * 
     * <pre>               &lt;media:rating scheme="urn:simple"&gt;adult&lt;/media:rating&gt;
     *                &lt;media:rating scheme="urn:icra"&gt;r (cz 1 lz 1 nz 1 oz 1 vz 1)&lt;/media:rating&gt;
     *                &lt;media:rating scheme="urn:mpaa"&gt;pg&lt;/media:rating&gt;
     *                &lt;media:rating scheme="urn:v-chip"&gt;tv-y7-fv&lt;/media:rating&gt;</pre>
     * 
     * 
     * <p><em>scheme</em> is the URI that identifies the rating scheme. It is an optional attribute. If this attribute is not included, the default scheme is urn:simple (adult | nonadult).</p>
     * 						 
     * 						
     * 
     * @return
     *     possible object is
     *     {@link MrssRatingType }
     *     
     */
    public MrssRatingType getRating() {
        return rating;
    }

    /**
     * Sets the value of the rating property.
     * 
     * @param value
     *     allowed object is
     *     {@link MrssRatingType }
     *     
     */
    public void setRating(MrssRatingType value) {
        this.rating = value;
    }

    /**
     *  
     * <strong>&lt;media:title&gt;</strong></p>
     * <p>The title of the particular media object.  It has 1 optional attribute.</p>
     * <pre>        &lt;media:title type="plain"&gt;The Judy's - The Moo Song&lt;/media:title&gt;</pre>
     * 
     * <p><em>type</em> specifies the type of text embedded. Possible values are either 'plain' or 'html'. Default value is 'plain'. All html must be entity-encoded. It is an optional attribute.
     * 						 
     * 						
     * 
     * @return
     *     possible object is
     *     {@link MrssTitleType }
     *     
     */
    public MrssTitleType getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link MrssTitleType }
     *     
     */
    public void setTitle(MrssTitleType value) {
        this.title = value;
    }

    /**
     *  
     * <strong>&lt;media:description&gt;</strong></p>
     * <p>Short description describing the media object typically a sentence in length. It has 1 optional attribute.</p>
     * <pre>        &lt;media:description type="plain"&gt;This was some really bizarre band I listened to as a young lad.&lt;/media:description&gt;</pre>
     * <p><em>type</em> specifies the type of text embedded. Possible values are either 'plain' or 'html'. Default value is 'plain'. All html must be entity-encoded. It is an optional attribute.</p>
     * 						 
     * 						
     * 
     * @return
     *     possible object is
     *     {@link MrssDescriptionType }
     *     
     */
    public MrssDescriptionType getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link MrssDescriptionType }
     *     
     */
    public void setDescription(MrssDescriptionType value) {
        this.description = value;
    }

    /**
     *  
     * <strong>&lt;media:keywords&gt;</strong></p>
     * <p>Highly relevant keywords describing the media object with typically a maximum of ten words. The keywords and phrases should be comma delimited.</p>
     * 
     * <pre>        &lt;media:keywords&gt;kitty, cat, big dog, yarn, fluffy&lt;/media:keywords&gt;</pre>
     * 						 
     * 						
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeywords() {
        return keywords;
    }

    /**
     * Sets the value of the keywords property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeywords(String value) {
        this.keywords = value;
    }

    /**
     *  
     * <strong>&lt;media:thumbnail&gt;</strong></p>
     * 
     * 
     * <p>Allows particular images to be used as representative images for the media object. If multiple thumbnails are included, and time coding is not at play, it is assumed that the images are in order of importance. It has 1 required attribute and 3 optional attributes.</p>
     * 
     * 
     * 
     * 
     * 
     *          <pre>        &lt;media:thumbnail url="http://www.foo.com/keyframe.jpg" width="75" height="50" time="12:05:01.123" /&gt;</pre>
     * <p><em>url</em> specifies the url of the thumbnail. It is a required attribute.</p>        <p> <em>height</em> specifies the height of the thumbnail. It is an optional attribute.</p>
     *         <p> <em>width</em> specifies the width of the thumbnail. It is an optional attribute.</p>
     * 
     * 
     * <p><em>time</em>
     * specifies the time offset in relation to the media object. Typically this is used when creating multiple keyframes within a single video. The format for this attribute should be in the DSM-CC's Normal Play Time (NTP) as used in RTSP [<a href="http://www.ietf.org/rfc/rfc2326.txt">RFC 2326 3.6 Normal Play Time</a>]. It is an optional attribute. </p>
     * 
     * <p><strong>Notes:</strong></p>
     * <p>NTP has a second or subsecond resolution. It is specified as H:M:S.h (npt-hhmmss) or S.h (npt-sec), where H=hours, M=minutes, S=second and h=fractions of a second.</p>
     * <p>A possible alternative to NTP would be SMPTE. It is believed that NTP is simpler and easier to use.</p>
     * 						 
     * 						
     * 
     * @return
     *     possible object is
     *     {@link MrssThumbnailType }
     *     
     */
    public MrssThumbnailType getThumbnail() {
        return thumbnail;
    }

    /**
     * Sets the value of the thumbnail property.
     * 
     * @param value
     *     allowed object is
     *     {@link MrssThumbnailType }
     *     
     */
    public void setThumbnail(MrssThumbnailType value) {
        this.thumbnail = value;
    }

    /**
     *  
     * <strong>&lt;media:category&gt;</strong></p>
     *         <p> Allows a taxonomy to be set that gives an indication of the type of media content, and its particular contents.  
     * It has 2 optional attributes.  </p>
     * <pre>        &lt;media:category scheme="http://search.yahoo.com/mrss/category_
     *         schema"&gt;music/artist/album/song&lt;/media:category&gt;
     * 
     *         &lt;media:category scheme="http://dmoz.org" label="Ace Ventura - Pet 
     *         Detective"&gt;Arts/Movies/Titles/A/Ace_Ventura_Series/Ace_Ventura_
     *         -_Pet_Detective&lt;/media:category&gt;
     * 
     *         &lt;media:category scheme="urn:flickr:tags"&gt;ycantpark 
     *         mobile&lt;/media:category&gt;</pre>
     * 
     * <p><em>scheme</em> is the URI that identifies the categorization scheme. It is an optional attribute. If this attribute is not included, the default scheme is 'http://search.yahoo.com/mrss/category_schema'.</p> 
     * 
     * <p><em>label</em> is the human readable label that can be displayed in end user applications.  It is an optional attribute.</p>
     * 						 
     * 						Gets the value of the category property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the category property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCategory().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MrssCategoryType }
     * 
     * 
     */
    public List<MrssCategoryType> getCategory() {
        if (category == null) {
            category = new ArrayList<MrssCategoryType>();
        }
        return this.category;
    }

    /**
     *  
     * <strong>&lt;media:hash&gt;</strong></p>
     * 
     * <p> This is the hash of the binary media file. It can appear multiple times as long as each instance is a different <em>algo</em>. It has 1 optional attribute.</p><p></p>
     * 
     * <pre>        &lt;media:hash algo="md5"&gt;dfdec888b72151965a34b4b59031290a&lt;/media:hash&gt;</pre>
     * 
     * <p><em>algo</em> indicates the algorithm used to create the hash. Possible values are 'md5' and 'sha-1'. Default value is 'md5'. It is an optional attribute.
     * 						 
     * 						
     * 
     * @return
     *     possible object is
     *     {@link MrssHashType }
     *     
     */
    public MrssHashType getHash() {
        return hash;
    }

    /**
     * Sets the value of the hash property.
     * 
     * @param value
     *     allowed object is
     *     {@link MrssHashType }
     *     
     */
    public void setHash(MrssHashType value) {
        this.hash = value;
    }

    /**
     *  
     * <strong>&lt;media:player&gt;</strong></p>
     * <p>Allows the media object to be accessed through a web browser media player console. 
     *  This element is required only if a direct media <em>url</em> attribute is not specified in the &lt;media:content&gt; element.  It has 1 required attribute, and 2 optional attributes.</p>
     * 
     * <pre>        &lt;media:player url="http://www.foo.com/player?id=1111" height="200" width="400" /&gt;</pre>
     * <p><em>url</em> is the url of the player console that plays the media.  It is a required attribute.</p>
     * <p><em>height</em> is the height of the browser window that the <em>url</em> should be opened in.   It is an optional attribute.</p>
     * <p><em>width</em> is the width of the browser window that the <em>url</em> should be opened in.  It is an optional attribute.</p>
     * 						 
     * 						
     * 
     * @return
     *     possible object is
     *     {@link MrssPlayerType }
     *     
     */
    public MrssPlayerType getPlayer() {
        return player;
    }

    /**
     * Sets the value of the player property.
     * 
     * @param value
     *     allowed object is
     *     {@link MrssPlayerType }
     *     
     */
    public void setPlayer(MrssPlayerType value) {
        this.player = value;
    }

    /**
     *  
     * <strong>&lt;media:credit&gt;</strong></p>
     * <p>Notable entity and the contribution to the creation of the media object. Current entities can include people, companies, locations, etc. Specific entities can have multiple roles, and several entities can have the same role. 
     *  These should appear as distinct &lt;media:credit&gt; elements. 
     *  It has 2 optional attributes.</p>
     * <pre>        &lt;media:credit role="producer" scheme="urn:ebu"&gt;entity name&lt;/media:credit&gt;
     * </pre>
     * <p>role specifies the role the entity played. Must be lowercase.  It is an optional attribute.</p>
     * 
     * <p><em>scheme</em> is the URI that identifies the role scheme. It is an optional attribute. If this attribute is not included, the default scheme is 'urn:ebu'. See: European Broadcasting Union Role Codes.</p>
     * 
     * 
     * <p>Example roles:</p>
     * <pre>        actor
     *         anchor person
     *         author
     *         choreographer
     *         composer
     *         conductor
     *         director
     *         editor
     *         graphic designer     
     *         grip
     *         illustrator
     *         lyricist
     *         music arranger
     *         music group
     *         musician
     *         orchestra
     *         performer
     *         photographer
     *         producer
     *         reporter
     *         vocalist
     * </pre>
     * <p>Additional roles: <a href="http://www.ebu.ch/en/technical/metadata/specifications/role_codes.php">European Broadcasting Union Role Codes</a>
     * 						 
     * 						Gets the value of the credit property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the credit property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCredit().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MrssCreditType }
     * 
     * 
     */
    public List<MrssCreditType> getCredit() {
        if (credit == null) {
            credit = new ArrayList<MrssCreditType>();
        }
        return this.credit;
    }

    /**
     *  
     * <strong>&lt;media:restriction&gt; </strong></p>
     * 
     * <p>Allows restrictions to be placed on the aggregator rendering the media in the feed. Currently, restrictions are based on distributor (uri) and country codes.  This element is purely informational and no obligation can be assumed or implied. 
     * Only one &lt;media:restriction&gt; element of the same <em>type</em> can be applied to a media object - all others will be ignored.&nbsp;Entities in this element should be space separated. To allow the producer to explicitly declare his/her intentions, two literals are reserved: 'all', 'none'. These literals can only be used once. This element has 1 required attribute, and 1 optional attribute (with strict requirements for its exclusion).</p>
     * 
     * <pre>        &lt;media:restriction relationship="allow" type="country"&gt;au us&lt;/media:restriction&gt;</pre>
     * 
     * <p><em>relationship</em> indicates the type of relationship that the restriction represents (allow | deny). In the example above, the media object should only be syndicated in Australia and the United States. It is a required attribute.</p>
     * 
     * <p><strong>Note:</strong> If the "allow" element is empty and the type is relationship is "allow", it is assumed that the empty list means "allow nobody" and the media should not be syndicated.</p>
     * <p>A more explicit method would be:</p>
     * 
     * <pre>        &lt;media:restriction relationship="allow" type="country"&gt;au us&lt;/media:restriction&gt;</pre>
     * 
     * <p><em>type</em> specifies the type of restriction (country | uri) that the media can be syndicated. It is an optional attribute; however can only be excluded when using one of the literal values "all" or "none". </p>
     * 
     * <p>"country" allows restrictions to be placed based on country code. [<a href="http://www.iso.org/iso/en/prods-services/iso3166ma/index.html">ISO 3166</a>]</p>
     * <p>"uri" allows restrictions based on URI. Examples: urn:apple, http://images.google.com, urn:yahoo, etc.
     * 						 
     * 						
     * 
     * @return
     *     possible object is
     *     {@link MrssRestrictionType }
     *     
     */
    public MrssRestrictionType getRestriction() {
        return restriction;
    }

    /**
     * Sets the value of the restriction property.
     * 
     * @param value
     *     allowed object is
     *     {@link MrssRestrictionType }
     *     
     */
    public void setRestriction(MrssRestrictionType value) {
        this.restriction = value;
    }

    /**
     *  
     * <strong>&lt;media:copyright&gt;</strong></p>
     * <p>Copyright information for media object.  It has 1 optional attribute.</p>
     * <pre>        &lt;media:copyright url="http://blah.com/additional-info.html"&gt;2005 FooBar Media&lt;/media:copyright&gt;</pre>
     * <p><em>url</em> is the url for a terms of use page or additional copyright information. If the media is operating under a Creative Commons license, the Creative Commons module should be used instead. It is an optional attribute.</p>
     * 						 
     * 						
     * 
     * @return
     *     possible object is
     *     {@link MrssCopyrightType }
     *     
     */
    public MrssCopyrightType getCopyright() {
        return copyright;
    }

    /**
     * Sets the value of the copyright property.
     * 
     * @param value
     *     allowed object is
     *     {@link MrssCopyrightType }
     *     
     */
    public void setCopyright(MrssCopyrightType value) {
        this.copyright = value;
    }

    /**
     *  
     * <strong>&lt;media:text&gt;</strong></p>
     * <p>Allows the inclusion of a text transcript, closed captioning, or lyrics of the media content. Many of these elements are permitted to provide a time series of text. In such cases, it is encouraged, but not required, that the elements be grouped by language and appear in time sequence order based on the <em>start</em> time. Elements can have overlapping <em>start</em> and <em>end</em> times. It has 4 optional attributes.</p><pre>        &lt;media:text type="plain" lang="en" start="00:00:03.000" 
     *         end="00:00:10.000"&gt; Oh, say, can you see&lt;/media:text&gt;
     * 
     *         &lt;media:text type="plain" lang="en" start="00:00:10.000" 
     *         end="00:00:17.000"&gt;By the dawn's early light&lt;/media:text&gt;
     * </pre>
     * <p><em>type</em> specifies the type of text embedded. Possible values are either 'plain' or 'html'. Default value is 'plain'. All html must be entity-encoded. It is an optional attribute. </p>
     * 
     * 
     * 
     * 
     * 
     * 
     * <p><em>lang</em> is the primary language encapsulated in the media object. Language codes possible are detailed in RFC 3066. This attribute is used similar to the xml:lang attribute detailed in the XML 1.0 Specification (Third Edition). It is an optional attribute.</p>
     * 
     * <p><em>start</em> specifies the start time offset that the text starts being relevant to the media object. An example of this would be for closed captioning. 
     *  It uses the NTP time code format (see: the time attribute used in &lt;media:thumbnail&gt;).  &nbsp; It is an optional attribute.</p>
     * 
     * <p><em>end</em> specifies the end time that the text is relevant. 
     * If this attribute is not provided, and a <em>start</em> time is used, it is expected that the end time is either the end of the clip or the start of the next &lt;media:text&gt; element.
     * 
     * 						 
     * 						
     * 
     * @return
     *     possible object is
     *     {@link MrssTextType }
     *     
     */
    public MrssTextType getText() {
        return text;
    }

    /**
     * Sets the value of the text property.
     * 
     * @param value
     *     allowed object is
     *     {@link MrssTextType }
     *     
     */
    public void setText(MrssTextType value) {
        this.text = value;
    }

}
