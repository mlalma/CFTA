package com.cfta.rssfeed.opml;

import com.cfta.rssfeed.data.RSSFeed;
import com.cfta.rssfeed.data.RSSFeedFolder;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class OPMLParser {
    private static final String OPML_HEADER = "opml";
    private static final String OPML_VERSION_ATTR = "version";
    private static final String BODY_HEADER = "body";
    private static final String OUTLINE_ELEM = "outline";

    private static final String ROOT_FOLDER = "root";

    private enum OPMLVersion {
        eUnknown,
        eVersion1,
        eVersion2
    }
    
    /*private boolean isFolder(Node n) {                
        return false;
    }*/

    private String getAttributeValueOrNull(String attrName, Node node) {
        if (node.getAttributes() != null && node.getAttributes().getNamedItem(attrName) != null) {
            return node.getAttributes().getNamedItem(attrName).getNodeValue();
        }
        return null;
    }

    private String getAttributeValueOrEmpty(String attrName, Node node) {
        if (node.getAttributes() != null && node.getAttributes().getNamedItem(attrName) != null) {
            return node.getAttributes().getNamedItem(attrName).getNodeValue();
        }
        return "";
    }

    // Parses OPML 1.0 doc
    private void parseOPML10Doc(NodeList outlineNodes, RSSFeedFolder parentFolder) {
        for (int i = 0; i < outlineNodes.getLength(); i++) {
            if (outlineNodes.item(i).getNodeName().equalsIgnoreCase(OUTLINE_ELEM)) {
                String typeAttr = getAttributeValueOrNull("type", outlineNodes.item(i));
                if (typeAttr == null || typeAttr.equalsIgnoreCase("text")) {
                    // is folder
                    String title = getAttributeValueOrEmpty("title", outlineNodes.item(i));
                    RSSFeedFolder folder = new RSSFeedFolder(title);
                    parentFolder.folders.add(folder);
                    parseOPML10Doc(outlineNodes.item(i).getChildNodes(), folder);
                } else if (typeAttr.equalsIgnoreCase("rss") || typeAttr.equalsIgnoreCase("link")) {
                    // is RSS feed
                    String title = getAttributeValueOrEmpty("title", outlineNodes.item(i));
                    String xmlUrl = getAttributeValueOrEmpty("xmlUrl", outlineNodes.item(i));
                    String htmlUrl = getAttributeValueOrEmpty("htmlUrl", outlineNodes.item(i));

                    if (xmlUrl.length() > 0 && xmlUrl.toLowerCase().startsWith("http")) {
                        RSSFeed feed = new RSSFeed(title, xmlUrl, htmlUrl);
                        parentFolder.feeds.add(feed);
                    } else {
                        System.err.println("ERROR! Could not add feed " + title + " to list, bad XML URL: " + xmlUrl);
                    }
                }
            }
        }
    }

    // Parses OPML document
    public RSSFeedFolder parseOPMLDoc(String docString) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        Document doc = dBuilder.parse(IOUtils.toInputStream(docString));
        doc.getDocumentElement().normalize();

        Node rootNode = doc.getDocumentElement();
        if (!rootNode.getNodeName().equalsIgnoreCase(OPML_HEADER)) {
            throw new RuntimeException("<opml> header not found, not a valid document!");
        }

        String version = rootNode.getAttributes().getNamedItem(OPML_VERSION_ATTR).getNodeValue();
        OPMLVersion opmlVer = OPMLVersion.eUnknown;
        if (version.trim().equalsIgnoreCase("1.0") || version.trim().equalsIgnoreCase("1")) {
            opmlVer = OPMLVersion.eVersion1;
        } else if (version.trim().equalsIgnoreCase("2.0") || version.trim().equalsIgnoreCase("2")) {
            opmlVer = OPMLVersion.eVersion2;
        }

        if (opmlVer == OPMLVersion.eUnknown) {
            throw new RuntimeException("unknown OPML version, not a valid document!");
        }

        RSSFeedFolder folder = null;
        boolean bodyNodeFound = false;
        NodeList nl = doc.getDocumentElement().getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeName().equalsIgnoreCase(BODY_HEADER)) {
                if (opmlVer == OPMLVersion.eVersion1) {
                    folder = new RSSFeedFolder(ROOT_FOLDER);
                    parseOPML10Doc(nl.item(i).getChildNodes(), folder);
                } else if (opmlVer == OPMLVersion.eVersion2) {
                    throw new RuntimeException("OPML v2.0 parser not yet implemented!");
                }
                bodyNodeFound = true;
                break;
            }
        }

        if (!bodyNodeFound) {
            throw new RuntimeException("<body> header not found, not a valid OPML document!");
        }

        return folder;
    }
}
