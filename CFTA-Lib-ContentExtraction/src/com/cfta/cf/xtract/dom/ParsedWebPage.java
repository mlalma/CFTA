// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.cf.xtract.dom;

import org.w3c.dom.NodeList;

// Parsed web page data structure
public class ParsedWebPage {
    private final String BODY_ELEMENT = "body";
    private final String HEAD_ELEMENT = "head";
    //private final String META_NODE = "meta";
    //private final String TITLE_NODE = "title";

    private final String FFUL_WIDTH_ATTRIB = "fful-width";
    private final String FFUL_HEIGHT_ATTRIB = "fful-height";
    private final String FFUL_DISPLAY_ATTRIB = "fful-display";
    private final String FFUL_VISIBILITY_ATTRIB = "fful-visibility";
    private final String FFUL_X_POS_ATTRIB = "fful-rect-x";
    private final String FFUL_Y_POS_ATTRIB = "fful-rect-y";
    private final String FFUL_HEADER_SECTION = "fful-header-section";
    private final String FFUL_TEXT_SECTION = "fful-text-section";
    private final String FFUL_FONT_SIZE_ATTRIB = "fful-font-size";
    private final String FFUL_FONT_SIZE_PX = "px";
    private final String FFUL_FLOAT = "fful-float";
    private final String FFUL_FLOAT_LEFT = "left";
    private final String FFUL_FLOAT_RIGHT = "right";

    private final short ELEMENT_NODE = 1;
    private final short TEXT_NODE = 3;

    private int totalTextLength = 0;
    private int totalTextWithFonts = 0;
    private int webSiteHeight = -1;

    public HtmlNode headNode = null;
    public HtmlNode bodyNode = null;
    private org.w3c.dom.Node rootNode;

    public double avgTotalFontSize = 0.0;
    public String url = null;

    // Constructor
    public ParsedWebPage(org.w3c.dom.Node rootNode) {
        this.rootNode = rootNode;
    }

    // Creates proper node with needed text elements, parses attribute data to the node
    private HtmlNode parseNodeData(org.w3c.dom.Node n, int level) {
        HtmlNode node = new HtmlNode();
        node.nodeName = n.getNodeName().trim();
        node.text = "";

        if (n.getAttributes() != null && n.getAttributes().getNamedItem(FFUL_VISIBILITY_ATTRIB) != null) {
            node.visibile = n.getAttributes().getNamedItem(FFUL_VISIBILITY_ATTRIB).getNodeValue().equalsIgnoreCase("visible");
        }
        if (n.getAttributes() != null && n.getAttributes().getNamedItem(FFUL_X_POS_ATTRIB) != null) {
            node.xPos = (int) Double.valueOf(n.getAttributes().getNamedItem(FFUL_X_POS_ATTRIB).getNodeValue()).doubleValue();
        }
        if (n.getAttributes() != null && n.getAttributes().getNamedItem(FFUL_Y_POS_ATTRIB) != null) {
            node.yPos = (int) Double.valueOf(n.getAttributes().getNamedItem(FFUL_Y_POS_ATTRIB).getNodeValue()).doubleValue();
        }
        if (n.getAttributes() != null && n.getAttributes().getNamedItem(FFUL_WIDTH_ATTRIB) != null) {
            node.width = (int) Double.valueOf(n.getAttributes().getNamedItem(FFUL_WIDTH_ATTRIB).getNodeValue()).doubleValue();
        }
        if (n.getAttributes() != null && n.getAttributes().getNamedItem(FFUL_HEIGHT_ATTRIB) != null) {
            node.height = (int) Double.valueOf(n.getAttributes().getNamedItem(FFUL_HEIGHT_ATTRIB).getNodeValue()).doubleValue();
        }
        if (n.getAttributes() != null && n.getAttributes().getNamedItem(FFUL_FLOAT) != null) {
            node.floatElement = n.getAttributes().getNamedItem(FFUL_FLOAT).getNodeValue().trim().toLowerCase().equalsIgnoreCase(FFUL_FLOAT_LEFT);
            node.floatElement = node.floatElement || n.getAttributes().getNamedItem(FFUL_FLOAT).getNodeValue().trim().toLowerCase().equalsIgnoreCase(FFUL_FLOAT_RIGHT);
        }
        if (n.getAttributes() != null && n.getAttributes().getNamedItem(FFUL_DISPLAY_ATTRIB) != null) {
            node.display = n.getAttributes().getNamedItem(FFUL_DISPLAY_ATTRIB).getNodeValue().trim().toLowerCase();
        }
        if (n.getAttributes() != null && n.getAttributes().getNamedItem(FFUL_FONT_SIZE_ATTRIB) != null) {
            String fontSize = n.getAttributes().getNamedItem(FFUL_FONT_SIZE_ATTRIB).getNodeValue().trim();
            if (fontSize.endsWith(FFUL_FONT_SIZE_PX)) {
                fontSize = fontSize.substring(0, fontSize.length() - FFUL_FONT_SIZE_PX.length());
                try {
                    node.fontSize = (int) Double.valueOf(fontSize).doubleValue();
                } catch (Exception ignored) {
                }
            }
        }
        if (n.getAttributes() != null && n.getAttributes().getNamedItem(FFUL_HEADER_SECTION) != null) {
            node.isHeaderNode = n.getAttributes().getNamedItem(FFUL_HEADER_SECTION).getNodeValue().trim().equalsIgnoreCase("true");
        }
        if (n.getAttributes() != null && n.getAttributes().getNamedItem(FFUL_TEXT_SECTION) != null) {
            node.isTextNode = n.getAttributes().getNamedItem(FFUL_TEXT_SECTION).getNodeValue().trim().equalsIgnoreCase("true");
        }

        if (n.getAttributes() != null) {
            for (int i = 0; i < n.getAttributes().getLength(); i++) {
                String attribName = n.getAttributes().item(i).getNodeName().trim();
                String attribValue = n.getAttributes().item(i).getNodeValue().trim();
                node.attributes.put(attribName, attribValue);
            }
        }

        return node;
    }

    // Recursively parses the complete DOM tree
    private void parseRecursivelyNode(HtmlNode body, NodeList n, int level) {
        if (body.docNode.getNodeType() == ELEMENT_NODE) {
            for (int l = 0; l < n.getLength(); l++) {
                if (n.item(l).getNodeType() == ELEMENT_NODE) {
                    HtmlNode childNode = parseNodeData(n.item(l), level);
                    childNode.parent = body;
                    childNode.docNode = n.item(l);

                    if (childNode.height > webSiteHeight) {
                        webSiteHeight = childNode.height;
                    }

                    body.childNodes.add(childNode);

                    if (n.item(l).hasChildNodes()) {
                        parseRecursivelyNode(childNode, n.item(l).getChildNodes(), level + 1);
                    }
                } else if (n.item(l).getNodeType() == TEXT_NODE) {
                    body.text = body.text + n.item(l).getTextContent().replaceAll(" ", " ").replaceAll("( )+", " ").trim();
                }
            }
        }
    }

    // Parses web page using Firefox parser
    public void parse() {
        for (int i = 0; i < rootNode.getChildNodes().item(0).getChildNodes().getLength(); i++) {
            org.w3c.dom.Node n = rootNode.getChildNodes().item(0).getChildNodes().item(i);

            if (n.getNodeName().equalsIgnoreCase(HEAD_ELEMENT)) {
                headNode = parseNodeData(n, 0);
                headNode.docNode = n;
                parseRecursivelyNode(headNode, n.getChildNodes(), 1);
            } else if (n.getNodeName().equalsIgnoreCase(BODY_ELEMENT)) {
                bodyNode = parseNodeData(n, 0);
                bodyNode.docNode = n;
                webSiteHeight = -1;
                parseRecursivelyNode(bodyNode, n.getChildNodes(), 1);
                bodyNode.height = webSiteHeight;
            }
        }

        if (bodyNode.attributes.get("url") != null && bodyNode.attributes.get("url").startsWith("http")) {
            url = bodyNode.attributes.get("url");
        }
    }

    // Calculates average font size
    private void calcAvgFontSize(HtmlNode n) {
        totalTextLength += n.textLength;
        totalTextWithFonts += n.textLength * n.fontSize;

        for (HtmlNode c : n.childNodes) {
            calcAvgFontSize(c);
        }
    }

    // Creates feature values for all of the nodes
    public void createFeatureValues() {
        if (bodyNode != null) {
            bodyNode.createFeatureValues();
        }

        assert bodyNode != null;
        calcAvgFontSize(bodyNode);
        avgTotalFontSize = (double) totalTextWithFonts / (double) totalTextLength;
    }
}