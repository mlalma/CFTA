// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.rssfeed.parser;

import com.cfta.rssfeed.xmlparser.XMLNode;
import com.cfta.rssfeed.xmlparser.XMLParserException;
import com.cfta.rssfeed.xmlparser.XMLParserUtil;
import com.cfta.rssfeed.xmlparser.XMLTreeParser;

import java.io.IOException;

public class RSSFeedRecognizer {

    private static final String RSS_FEED_ROOT_TYPE = "rss";
    private static final String CHANNEL_NODE = "channel";
    private static final String ATOM_FEED_ROOT_TYPE = "feed";
    private static final String FEEDBURNER_RSS_FEED_TYPE = "rdf:RDF";
    private static final String ITEM_NODE = "item";

    private final XMLTreeParser treeParser = new XMLTreeParser();
    //private final RSSFeedCleaner feedCleaner = new RSSFeedCleaner();

    public enum RSSFeedType {
        eRSSFeed,
        eAtom,
        eFeedBurnerRSSFeed,
        eUnknown
    }

    public RSSFeedRecognizer() {
    }

    private XMLNode doRecognize(XMLNode root) {
        for (int i = 0; i < root.childNodes.size(); i++) {
            XMLNode n = root.childNodes.get(i);
            if (n.name.toLowerCase().equalsIgnoreCase(RSS_FEED_ROOT_TYPE)) {
                XMLNode channel = XMLParserUtil.findNode(CHANNEL_NODE, n.childNodes);
                if (channel != null) {
                    return n;
                }
            } else if (n.name.equalsIgnoreCase(ATOM_FEED_ROOT_TYPE)) {
                return n;
            } else if (n.name.equalsIgnoreCase(FEEDBURNER_RSS_FEED_TYPE)) {
                XMLNode channel = XMLParserUtil.findNode(CHANNEL_NODE, n.childNodes);
                XMLNode item = XMLParserUtil.findNode(ITEM_NODE, n.childNodes);
                if (channel != null && item != null) {
                    return n;
                }
            } else {
                XMLNode childFeed = doRecognize(root.childNodes.get(i));
                if (childFeed != null) {
                    return childFeed;
                }
            }
        }

        return null;
    }

    // Finds feed root node
    public XMLNode findFeedRootNode(String page) throws XMLParserException, IOException {
        XMLNode root = treeParser.createXMLTreeFromString(page);
        return doRecognize(root);
    }

    // Recognizes feed type from node
    public RSSFeedType recognizeFeedType(XMLNode n) {
        if (n == null) {
            return RSSFeedType.eUnknown;
        } else if (n.name.equalsIgnoreCase(RSS_FEED_ROOT_TYPE)) {
            return RSSFeedType.eRSSFeed;
        } else if (n.name.equalsIgnoreCase(ATOM_FEED_ROOT_TYPE)) {
            return RSSFeedType.eAtom;
        } else if (n.name.equalsIgnoreCase(FEEDBURNER_RSS_FEED_TYPE)) {
            return RSSFeedType.eFeedBurnerRSSFeed;
        } else {
            return RSSFeedType.eUnknown;
        }
    }

}
