// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
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
    org.w3c.dom.Node docNode;
    HtmlNode parent = null;

    // Node features for machine learning purposes
    int textLength = 0;
    private int textLengthInsideLinks = 0;
    private int tagNum = 0;
    boolean floatElement = false;
    boolean visibile = true;

    public int xPos = 0;
    public int yPos = 0;
    public int width = -1;
    public int height = -1;
    public double xPosRatio = 0.0;
    public double yPosRatio = 0.0;
    public double widthRatio = 0.0;
    public double heightRatio = 0.0;

    public int fontSize = 0;
    String display = "block";
    public String fontStyle = "";
    public String fontWeight = "";

    public boolean doOutput = false;

    boolean isTextNode = false;
    boolean isHeaderNode = false;

    private static final int MIN_VALID_IMAGE_WIDTH = 300;
    private static final int MIN_VALID_IMAGE_HEIGHT = 220;

    public static final int MIN_VALID_IMAGE_AREA = 200 * 200;

    // Constructor
    HtmlNode() {
    }

    // Checks if tag is a header
    public boolean isHeader(String tag) {
        return (tag.equalsIgnoreCase("h1") || tag.equalsIgnoreCase("h2") || tag.equalsIgnoreCase("h3") || tag.equalsIgnoreCase("h4") || tag.equalsIgnoreCase("h5") || tag.equalsIgnoreCase("h6"));
    }

    // Checks if link or inside the link
    private boolean isLink() {
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
                docNode.getAttributes() != null && docNode.getAttributes().getNamedItem(SRC_ATTRIB) != null &&
                !docNode.getAttributes().getNamedItem(SRC_ATTRIB).getNodeValue().startsWith("data")) {

            return width >= MIN_VALID_IMAGE_WIDTH && height >= MIN_VALID_IMAGE_HEIGHT;
        }

        return false;
    }

    // Checks if node is preformatted and we should respect that
    public boolean isPreformattedNode() {
        return (nodeName.equalsIgnoreCase("pre") || nodeName.equalsIgnoreCase("code"));
    }

    // Checks if tag is video
    private boolean isVideo() {
        return HtmlDocumentParser.isVideo(docNode);
        //return false;
    }

    boolean isHidden() {
        if (display.equalsIgnoreCase("hidden") || display.equalsIgnoreCase("none") || !visibile) {
            return !nodeName.equalsIgnoreCase("head") && !nodeName.equalsIgnoreCase("title") &&
                    !nodeName.equalsIgnoreCase("meta") && !nodeName.equalsIgnoreCase("link");
        } else {
            return false;
        }
    }

    // Checks if node is a proper one
    private boolean isProperNode() {
        if (nodeName.equalsIgnoreCase("style") || nodeName.equalsIgnoreCase("script") || nodeName.equalsIgnoreCase("noscript") ||
                (nodeName.equalsIgnoreCase("iframe") && !isVideo())) {
            return false;
        } else if (isHidden()) {
            return false;
        }

        return !nodeName.equalsIgnoreCase("br");
    }

    // Calculates the feature values of the particular node
    void createFeatureValues() {
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

    private void calculateFeatureValues() {
        for (HtmlNode child : childNodes) {
            child.calculateFeatureValues();
        }
    }
}
