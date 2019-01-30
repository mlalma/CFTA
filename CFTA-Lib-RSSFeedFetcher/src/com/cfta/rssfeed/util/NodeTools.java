package com.cfta.rssfeed.util;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// Common utilities used to find / parse node trees
public class NodeTools {

    public static final String EMPTY_STRING = "";

    // Returns attribute value or empty string if none
    static public String getAttributeValueOrEmpty(final String attrName, final Node node) {
        if (node.getAttributes() != null) {
            for (int i = 0; i < node.getAttributes().getLength(); i++) {
                if (node.getAttributes().item(i).getNodeName().trim().equalsIgnoreCase(attrName.trim())) {
                    return node.getAttributes().item(i).getNodeValue();
                }
            }
        }
        return EMPTY_STRING;
    }

    // Returns child node with given name
    static public Node childNode(final Node n, final String nodeName) {
        for (int i = 0; i < n.getChildNodes().getLength(); i++) {
            Node child = n.getChildNodes().item(i);
            if (child.getNodeName() != null && child.getNodeName().trim().equalsIgnoreCase(nodeName)) {
                return child;
            }
        }
        return null;
    }

    // Private constructor, this class can never be instantiated
    private NodeTools() {
    }
}
