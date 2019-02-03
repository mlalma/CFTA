// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.rssfeed.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.parser.Parser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import static com.cfta.rssfeed.util.NodeTools.childNode;

public class RSSFeedRecognizer {

    private static final String RSS_FEED_ROOT_TYPE = "rss";
    private static final String CHANNEL_NODE = "channel";
    private static final String ATOM_FEED_ROOT_TYPE = "feed";
    private static final String FEEDBURNER_RSS_FEED_TYPE = "rdf:RDF";
    private static final String ITEM_NODE = "item";

    public enum RSSFeedType {
        eRSSFeed,
        eAtom,
        eFeedBurnerRSSFeed,
        eUnknown
    }

    // Constructor
    public RSSFeedRecognizer() {
    }

    // Tries to find the node that defines the RSS Feed type
    private Node doRecognize(final Node root) {
        for (int i = 0; i < root.getChildNodes().getLength(); i++) {
            Node n = root.getChildNodes().item(i);
            if (n.getNodeName().trim().equalsIgnoreCase(RSS_FEED_ROOT_TYPE)) {
                Node channel = childNode(n, CHANNEL_NODE);
                if (channel != null) {
                    return n;
                }
            } else if (n.getNodeName().trim().equalsIgnoreCase(ATOM_FEED_ROOT_TYPE)) {
                return n;
            } else if (n.getNodeName().trim().equalsIgnoreCase(FEEDBURNER_RSS_FEED_TYPE)) {
                Node channel = childNode(n, CHANNEL_NODE);
                Node item = childNode(n, ITEM_NODE);
                if (channel != null && item != null) {
                    return n;
                }
            } else {
                Node childFeed = doRecognize(root.getChildNodes().item(i));
                if (childFeed != null) {
                    return childFeed;
                }
            }
        }

        return null;
    }

    // Finds feed root node
    public Node findFeedRootNode(final String page) {
        org.jsoup.nodes.Document prettifiedDoc = Jsoup.parse(page, "", Parser.xmlParser());
        W3CDom w3cDom = new W3CDom();
        Document doc = w3cDom.fromJsoup(prettifiedDoc);
        doc.getDocumentElement().normalize();
        return doRecognize(doc);
    }

    // Recognizes feed type from node
    public RSSFeedType recognizeFeedType(final Node n) {
        if (n == null) {
            return RSSFeedType.eUnknown;
        } else if (n.getNodeName().trim().equalsIgnoreCase(RSS_FEED_ROOT_TYPE)) {
            return RSSFeedType.eRSSFeed;
        } else if (n.getNodeName().trim().equalsIgnoreCase(ATOM_FEED_ROOT_TYPE)) {
            return RSSFeedType.eAtom;
        } else if (n.getNodeName().trim().equalsIgnoreCase(FEEDBURNER_RSS_FEED_TYPE)) {
            return RSSFeedType.eFeedBurnerRSSFeed;
        } else {
            return RSSFeedType.eUnknown;
        }
    }

}
