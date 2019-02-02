// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.rssfeed.parser;

import com.cfta.rssfeed.util.NodeTools;
import com.cfta.cf.handlers.protocol.RSSFeedResponse;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.Node;

// Parses <rss> type feeds
public class RSSTypeFeedParser {

    private static final String CHANNEL_NODE = "channel";

    private static final String TITLE_NODE = "title";
    private static final String DESCRIPTION_NODE = "description";
    private static final String ITEM_NODE = "item";
    private static final String LINK_NODE = "link";
    private static final String DC_DATE_NODE = "dc:date";
    private static final String PUB_DATE_NODE = "pubDate";
    private static final String CATEGORY_NODE = "category";

    // Constructor
    public RSSTypeFeedParser() {
    }

    // Parses everything under <channel> tag
    private RSSFeedResponse parseChannel(Node rootNode) {
        Node n = NodeTools.childNode(rootNode, CHANNEL_NODE);

        RSSFeedResponse response = new RSSFeedResponse();
        Node title = NodeTools.childNode(n, TITLE_NODE);
        Node description = NodeTools.childNode(n, DESCRIPTION_NODE);

        if (title != null) {
            response.feedTitle = StringEscapeUtils.unescapeHtml(title.getTextContent());
        } else {
            response.feedTitle = "";
        }

        if (description != null) {
            response.description = StringEscapeUtils.unescapeHtml(description.getTextContent());
        } else {
            response.description = "";
        }

        List<Node> items = NodeTools.childNodes(n, ITEM_NODE);
        for (int i = 0; i < items.size(); i++) {
            Node itemTitle = NodeTools.childNode(items.get(i), TITLE_NODE);
            List<Node> itemLink = NodeTools.childNodes(items.get(i), LINK_NODE);
            Node itemDescription = NodeTools.childNode(items.get(i), DESCRIPTION_NODE);
            Node itemDate = NodeTools.childNode(items.get(i), DC_DATE_NODE);
            Node pubDate = NodeTools.childNode(items.get(i), PUB_DATE_NODE);
            List<Node> categoryNodes = NodeTools.childNodes(items.get(i), CATEGORY_NODE);

            if (itemTitle != null && itemLink.size() > 0) {
                RSSFeedResponse.RSSItem item = response.newItem();
                item.title = StringEscapeUtils.unescapeHtml(itemTitle.getTextContent());
                if (itemDescription != null) {
                    item.description =  StringEscapeUtils.unescapeHtml(itemDescription.getTextContent().trim());
                } else {
                    item.description = "";
                }

                for (int j = 0; j < itemLink.size(); j++) {
                    item.links.add(itemLink.get(j).getTextContent().trim());
                }

                for (int j = 0; j < categoryNodes.size(); j++) {
                    item.categories.add(categoryNodes.get(j).getTextContent().trim());
                }

                if (item.date == null && pubDate != null) {
                    item.date = NodeTools.parseDate(pubDate.getTextContent().trim(), Locale.ENGLISH);
                }

                if (item.date == null && itemDate != null) {
                    item.date = NodeTools.parseDate(itemDate.getTextContent().trim(), Locale.ENGLISH);
                }

                response.rssItems.add(item);
            }
        }

        return response;
    }

    // Returns parsed data structure containing all the RSS items
    public RSSFeedResponse parseFeed(Node rootNode) {
        return parseChannel(rootNode);
    }
}
