package com.cfta.rssfeed.opml;

import com.cfta.rssfeed.data.RSSFeed;
import com.cfta.rssfeed.data.RSSFeedFolder;

import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

// Parses OPML (Outline Processor Markup Language) Files
public class OPMLParser {

    private static final String OPML_HEADER = "opml";
    private static final String OPML_VERSION_ATTR = "version";
    private static final String BODY_HEADER = "body";
    private static final String OPML_TITLE = "title";
    private static final String OUTLINE_ELEM = "outline";
    private static final String TYPE_ELEM = "type";
    private static final String XMLURL_ELEM = "xmlUrl";
    private static final String TITLE_ELEM = "title";
    private static final String TEXT_ELEM = "text";
    private static final String HTMLURL_ELEM = "htmlUrl";

    private static final String EMPTY_STRING = "";

    // Identified OPML version
    private enum OPMLVersion {
        eUnknown,
        eVersion1,
        eVersion11,
        eVersion2
    }

    // Returns attribute value or empty string if none
    private String getAttributeValueOrEmpty(String attrName, Node node) {
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
    private Node childNode(Node n, String nodeName) {
        for (int i = 0; i < n.getChildNodes().getLength(); i++) {
            Node child = n.getChildNodes().item(i);
            if (child.getNodeName() != null && child.getNodeName().trim().equalsIgnoreCase(nodeName)) {
                return child;
            }
        }
        return null;
    }

    // Parses OPML 1.0, 1.1 and 2.0 documents
    private void parseOPMLDoc(Node outlineNode, RSSFeedFolder parentFolder) {
        if (outlineNode.getNodeName().equalsIgnoreCase(OUTLINE_ELEM)) {
            String typeAttr = getAttributeValueOrEmpty(TYPE_ELEM, outlineNode);

            String xmlUrl = getAttributeValueOrEmpty(XMLURL_ELEM, outlineNode);

            String title = getAttributeValueOrEmpty(TITLE_ELEM, outlineNode);
            if (title.length() == 0) {
                title = getAttributeValueOrEmpty(TEXT_ELEM, outlineNode);
            }

            String htmlUrl = getAttributeValueOrEmpty(HTMLURL_ELEM, outlineNode);

            if (typeAttr.equalsIgnoreCase(TEXT_ELEM) || xmlUrl.length() == 0) {
                // is folder
                RSSFeedFolder folder = new RSSFeedFolder(title);
                parentFolder.folders.add(folder);
                for (int i = 0; i < outlineNode.getChildNodes().getLength(); i++) {
                    parseOPMLDoc(outlineNode.getChildNodes().item(i), folder);
                }
            } else {
                // is RSS feed
                if (xmlUrl.toLowerCase().startsWith("http")) {
                    RSSFeed feed = new RSSFeed(title, xmlUrl, htmlUrl);
                    parentFolder.feeds.add(feed);
                } else {
                    System.err.println("ERROR! Could not add feed " + title + " to list, bad XML URL: " + xmlUrl);
                }
            }
        }
    }

    // Parses OPML document
    public RSSFeedFolder parseOPMLDoc(String docString) {
        // Using JSoup reorganizes the document structure a bit, but it can handle mal-formatted documents better
        org.jsoup.nodes.Document prettifiedDoc = Jsoup.parse(docString);
        W3CDom w3cDom = new W3CDom();
        Document doc = w3cDom.fromJsoup(prettifiedDoc);
        doc.getDocumentElement().normalize();

        Node bodyNode = childNode(doc.getDocumentElement(), BODY_HEADER);
        Node rootNode = null;
        if (bodyNode != null) {
            rootNode = childNode(bodyNode, OPML_HEADER);
        }

        if (rootNode == null || !rootNode.getNodeName().equalsIgnoreCase(OPML_HEADER)) {
            throw new RuntimeException("<opml> header not found, not a valid document!");
        }

        String version = rootNode.getAttributes().getNamedItem(OPML_VERSION_ATTR).getNodeValue();
        OPMLVersion opmlVer = OPMLVersion.eUnknown;
        if (version.trim().equalsIgnoreCase("1.0") || version.trim().equalsIgnoreCase("1")) {
            opmlVer = OPMLVersion.eVersion1;
        } else if (version.trim().equalsIgnoreCase("1.1")) {
            opmlVer = OPMLVersion.eVersion11;
        } else if (version.trim().equalsIgnoreCase("2.0") || version.trim().equalsIgnoreCase("2")) {
            opmlVer = OPMLVersion.eVersion2;
        }

        if (opmlVer == OPMLVersion.eUnknown) {
            throw new RuntimeException("unknown OPML version, not a valid document!");
        }

        RSSFeedFolder rootFolder = new RSSFeedFolder("<unknown OPML feed>");
        NodeList nl = rootNode.getChildNodes();

        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeName().equalsIgnoreCase(OPML_TITLE)) {
                rootFolder.title = nl.item(i).getTextContent().trim();
            } else if (nl.item(i).getNodeName().equalsIgnoreCase(OUTLINE_ELEM)) {
                if (opmlVer == OPMLVersion.eVersion1) {
                    parseOPMLDoc(nl.item(i), rootFolder);
                } else if (opmlVer == OPMLVersion.eVersion11) {
                    parseOPMLDoc(nl.item(i), rootFolder);
                } else if (opmlVer == OPMLVersion.eVersion2) {
                    parseOPMLDoc(nl.item(i), rootFolder);
                }
            }
        }

        return rootFolder;
    }
}
