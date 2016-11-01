package com.cfta.cf.xtract.dom;

import java.util.ArrayList;
import java.util.HashMap;

// Single DOM-tree node
public class HtmlNode {   
    public HashMap<String, String> attributes = new HashMap<>();
    public String nodeName = "";
    public String text = "";
    public boolean properNode = false;
    public ArrayList<HtmlNode> childNodes = new ArrayList<>();
    public org.w3c.dom.Node docNode;
    public HtmlNode parent = null;

    // Node features for machine learning purposes
    public int textLength = 0;
    public int textLengthInsideLinks = 0;
    public int tagNum = 0;
    public boolean floatElement = false;
    public boolean visibile = true;

    public int xPos = 0;
    public int yPos = 0;
    public int width = -1;
    public int height = -1;
    public double xPosRatio = 0.0;
    public double yPosRatio = 0.0;
    public double widthRatio = 0.0;
    public double heightRatio = 0.0;
    
    public int fontSize = 0;
    public String display = "block";
    public String fontStyle = "";
    public String fontWeight = "";
    
    public boolean doOutput = false;
   
    public boolean isTextNode = false;
    public boolean isHeaderNode = false;
        
    public static final int MIN_VALID_IMAGE_WIDTH = 300;
    public static final int MIN_VALID_IMAGE_HEIGHT = 220;
    
    public static final int MIN_VALID_IMAGE_AREA = 200*200;
   
    // Constructor
    public HtmlNode() {        
    }

    // Checks if tag is a header
    public boolean isHeader(String tag) {
        return (tag.equalsIgnoreCase("h1") || tag.equalsIgnoreCase("h2") || tag.equalsIgnoreCase("h3") || tag.equalsIgnoreCase("h4") || tag.equalsIgnoreCase("h5") || tag.equalsIgnoreCase("h6"));
    }

    // Checks if link or inside the link
    public boolean isLink() {
        return ((nodeName.equalsIgnoreCase("a") || nodeName.equalsIgnoreCase("nav")) || (parent != null && parent.isLink()));
    }
        
    // Checks if tag is image
    public boolean isImage() {
        return (nodeName.toLowerCase().trim().equalsIgnoreCase("img"));
    }
    
    // Checks if node is valid 
    public boolean isArticleImage() {    
        final String IMG_ATTRIB = "img";
        final String SRC_ATTRIB = "src";
        
        if (docNode.getNodeName() != null && docNode.getNodeName().equalsIgnoreCase(IMG_ATTRIB) &&
            docNode.getAttributes()!= null && docNode.getAttributes().getNamedItem(SRC_ATTRIB) != null &&
            !docNode.getAttributes().getNamedItem(SRC_ATTRIB).getNodeValue().startsWith("data")) {            
        
            if (width >= MIN_VALID_IMAGE_WIDTH && height >= MIN_VALID_IMAGE_HEIGHT) {
                return true;
            }
        }

        return false;
    }
    
    // Checks if node is preformatted and we should respect that
    public boolean isPreformattedNode() {
        return (nodeName.equalsIgnoreCase("pre") || nodeName.equalsIgnoreCase("code"));
    }
    
    // Checks if tag is video
    public boolean isVideo() {
        return HtmlDocumentParser.isVideo(docNode);
        //return false;
    }
    
    public boolean isHidden() {
        if (display.equalsIgnoreCase("hidden") || display.equalsIgnoreCase("none") || !visibile) {
            if (nodeName.equalsIgnoreCase("head") || nodeName.equalsIgnoreCase("title") || nodeName.equalsIgnoreCase("meta") || nodeName.equalsIgnoreCase("link")) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    // Checks if node is a proper one
    public boolean isProperNode() {
        if (nodeName.equalsIgnoreCase("style") || nodeName.equalsIgnoreCase("script") || nodeName.equalsIgnoreCase("noscript") ||
            (nodeName.equalsIgnoreCase("iframe") && !isVideo())) {
            return false;
        } else if (isHidden()) {
            return false;
        }

        if (nodeName.equalsIgnoreCase("br")) {
            return false;
        }
        
        return true;
    }
                
    // Calculates the feature values of the particular node
    public void createFeatureValues() throws Exception {
        properNode = isProperNode();
                
        if (nodeName.equalsIgnoreCase("#text")) {
            return;
        }

        for (HtmlNode child : childNodes) {
            child.createFeatureValues();
        }

        tagNum = 1;
        for (HtmlNode child : childNodes) {
            tagNum += child.tagNum;
        }
        
        // Calc textLength
        if (properNode) {
            int length = text.length();
            for (HtmlNode child : childNodes) {
                if (child.properNode) {
                    length += child.textLength;
                }
            }
            textLength = length;
        } else {
            textLength = 0;
        }

        // Calc textLengthInsideLinks
        if (properNode) {
            int length = 0;
            if (nodeName.equalsIgnoreCase("a") || nodeName.equalsIgnoreCase("nav")) {
                length += text.length();
            } else {
                for (HtmlNode child : childNodes) {
                    if (child.properNode) {
                        length += child.textLengthInsideLinks;
                    }
                }
            }
            textLengthInsideLinks = length;
        } else {
            textLengthInsideLinks = 0;
        }
    }

    public void calculateFeatureValues() throws Exception {
        for (HtmlNode child : childNodes) {
            child.calculateFeatureValues();
        }
    }
}
