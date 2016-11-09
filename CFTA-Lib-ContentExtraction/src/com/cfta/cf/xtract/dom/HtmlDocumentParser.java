package com.cfta.cf.xtract.dom;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;

import nu.validator.htmlparser.common.DoctypeExpectation;
import nu.validator.htmlparser.common.Heuristics;
import nu.validator.htmlparser.common.XmlViolationPolicy;
import nu.validator.htmlparser.dom.HtmlDocumentBuilder;

// Constructs and manipulates DOM tree for the content extraction algorithms
public class HtmlDocumentParser {
    
    private Document doc = null;
    
    private final String FFUL_WIDTH_ATTRIB = "fful-width";
    private final String FFUL_HEIGHT_ATTRIB = "fful-height";
        
    private final static String SRC_ATTRIB = "src";
    private final String IMG_ATTRIB = "img";
    //private final String SCRIPT_NODE = "script";
    //private final String NOSCRIPT_NODE = "noscript";
    //private final String COMMENT_NODE = "#comment";
    //private final String STYLE_NODE = "style";
    //private final String META_NODE = "meta";
    private final static String IFRAME_NODE = "iframe";
    //private final String TITLE_NODE = "title";
    private final static String VIDEO_NODE= "video";

    private final String BODY_ELEMENT = "body";
    //private final String HEAD_ELEMENT = "head";
    //private int webSiteHeight = -1;
    
    private final static String[] VIDEO_URLS = {
        "//player.vimeo.com/",
        "//www.youtube.com/embed",
        "//www.dailymotion.com/embed"
        // Add somehow support to BrightCove player
    };
    
        
    // Small utility class
    public class ImageDimension {
        public int width, height;

        public ImageDimension(int w, int h) {
            width = w;
            height = h;
        }

        public void set(int w, int h) {
            width = w;
            height = h;
        }

        public int getWidth() { return width; }
        public int getHeight() { return height; }
    }
            
    // Constructor
    public HtmlDocumentParser() {        
    }
        
    // Inits the DOM tree
    public void initDOM(String page) throws Exception {
        HtmlDocumentBuilder builder = new HtmlDocumentBuilder();
        builder.setCommentPolicy(XmlViolationPolicy.ALTER_INFOSET);
        builder.setContentNonXmlCharPolicy(XmlViolationPolicy.ALTER_INFOSET);
        builder.setContentSpacePolicy(XmlViolationPolicy.ALTER_INFOSET);        
        builder.setNamePolicy(XmlViolationPolicy.ALTER_INFOSET);
        builder.setStreamabilityViolationPolicy(XmlViolationPolicy.ALTER_INFOSET);
        builder.setXmlnsPolicy(XmlViolationPolicy.ALTER_INFOSET);
        builder.setMappingLangToXmlLang(true);
        builder.setHtml4ModeCompatibleWithXhtml1Schemata(true);
        builder.setHeuristics(Heuristics.ALL);
        builder.setCheckingNormalization(false);
        builder.setDoctypeExpectation(DoctypeExpectation.NO_DOCTYPE_ERRORS);
        builder.setIgnoringComments(true);
        builder.setScriptingEnabled(true);
        builder.setXmlPolicy(XmlViolationPolicy.ALTER_INFOSET);
        
        doc = builder.parse(IOUtils.toInputStream(page));
        
        if (doc.getElementsByTagName(BODY_ELEMENT).getLength() == 0) {
            throw new RuntimeException("Problem parsing document - invalid HTML, no body element found");
        }        
    }
    
    // Returns document
    public Document getDocument() {
        return doc;
    }
    
    // Is node image or not?
    public boolean isImage(org.w3c.dom.Node n) {
        final int MIN_VALID_IMAGE_WIDTH = 128;
        final int MIN_VALID_IMAGE_HEIGHT = 128;

        boolean validImage = false;

        if (n.getNodeName() != null && n.getNodeName().equalsIgnoreCase(IMG_ATTRIB) &&
            n.getAttributes()!= null && n.getAttributes().getNamedItem(SRC_ATTRIB) != null &&
            !n.getAttributes().getNamedItem(SRC_ATTRIB).getNodeValue().startsWith("data")) {            
                try {
                    //ImageDimension d = getImageDimensions(n.getAttributes().getNamedItem("src").getNodeValue().trim());
                    ImageDimension d = new ImageDimension((int)Double.valueOf(n.getAttributes().getNamedItem(FFUL_WIDTH_ATTRIB).getNodeValue()).doubleValue(),
                                                          (int)Double.valueOf(n.getAttributes().getNamedItem(FFUL_HEIGHT_ATTRIB).getNodeValue()).doubleValue());

                    if (d.getHeight() < MIN_VALID_IMAGE_HEIGHT || d.getWidth() < MIN_VALID_IMAGE_WIDTH) {
                        validImage = false;
                    } else {
                        validImage = true;
                    }
                } catch (Exception ex) {                    
                }
        }
        
        return validImage;
    }
              
    // Is node video node or not?
    static public boolean isVideo(org.w3c.dom.Node n) {
        if (n.getNodeName() != null && n.getNodeName().trim().equalsIgnoreCase(IFRAME_NODE) &&
            n.getAttributes()!= null && n.getAttributes().getNamedItem(SRC_ATTRIB) != null) {
            for (String videoUrl : VIDEO_URLS) {
                if (n.getAttributes().getNamedItem(SRC_ATTRIB).getNodeValue().contains(videoUrl)) {
                    return true;
                }
            }
        } else if (n.getNodeName() != null && n.getNodeName().trim().equalsIgnoreCase(VIDEO_NODE)) {
            return true;
        }

        return false;
    }               
}
