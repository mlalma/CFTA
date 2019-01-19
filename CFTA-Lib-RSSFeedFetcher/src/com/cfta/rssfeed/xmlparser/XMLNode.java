// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.rssfeed.xmlparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class XMLNode {
    static public final int TYPE_TEXT = 0;
    static public final int TYPE_NODE = 1;
    static public final String TEXT_NODE_NAME = "#text";

    public final int nodeType;
    public final String name;
    public final HashMap<String, String> attributes;
    public final XMLNode parent;
    public final List<XMLNode> childNodes;
    public final String textContent;

    private final StringBuffer sb = new StringBuffer();

    public XMLNode(int nodeType, String name, HashMap<String, String> attributes, XMLNode parent, String textContent) {
        this.nodeType = nodeType;
        this.name = name;
        this.attributes = attributes;
        this.parent = parent;
        this.textContent = textContent;
        childNodes = new ArrayList<>();
    }

    private void crawlTextContent(XMLNode n, StringBuffer sb) {
        for (int i = 0; i < n.childNodes.size(); i++) {
            if (n.childNodes.get(i).nodeType == TYPE_TEXT) {
                if (n.childNodes.get(i).textContent != null) {
                    sb.append(n.childNodes.get(i).textContent);
                }
            } else if (n.childNodes.get(i).nodeType == TYPE_NODE) {
                crawlTextContent(n.childNodes.get(i), sb);
            }
        }
    }

    public String getTextContent() {
        sb.setLength(0);
        crawlTextContent(this, sb);
        return sb.toString();
    }
}
