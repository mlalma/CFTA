// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.rssfeed.parser;

import com.cfta.rssfeed.xmlparser.XMLNode;
import com.cfta.rssfeed.xmlparser.XMLParserUtil;
import com.cfta.cf.handlers.protocol.RSSFeedResponse;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringEscapeUtils;

public class RSSTypeFeedParser {

    //private static final String RSS_FEED_ROOT_TYPE = "rss";
    private static final String CHANNEL_NODE = "channel";

    private static final String TITLE_NODE = "title";
    private static final String DESCRIPTION_NODE = "description";
    private static final String ITEM_NODE = "item";
    private static final String LINK_NODE = "link";
    private static final String DC_DATE_NODE = "dc:date";
    private static final String PUB_DATE_NODE = "pubDate";

    public RSSTypeFeedParser() {
    }

    private RSSFeedResponse parseChannel(XMLNode rootNode) {
        XMLNode n = XMLParserUtil.findNode(CHANNEL_NODE, rootNode.childNodes);

        RSSFeedResponse response = new RSSFeedResponse();
        XMLNode title = XMLParserUtil.findNode(TITLE_NODE, n.childNodes);
        XMLNode description = XMLParserUtil.findNode(DESCRIPTION_NODE, n.childNodes);

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

        List<XMLNode> items = XMLParserUtil.findNodes(ITEM_NODE, n.childNodes);
        for (int i = 0; i < items.size(); i++) {
            XMLNode itemTitle = XMLParserUtil.findNode(TITLE_NODE, items.get(i).childNodes);
            List<XMLNode> itemLink = XMLParserUtil.findNodes(LINK_NODE, items.get(i).childNodes);
            XMLNode itemDescription = XMLParserUtil.findNode(DESCRIPTION_NODE, items.get(i).childNodes);
            XMLNode itemDate = XMLParserUtil.findNode(DC_DATE_NODE, items.get(i).childNodes);
            XMLNode pubDate = XMLParserUtil.findNode(PUB_DATE_NODE, items.get(i).childNodes);

            if (itemTitle != null && itemLink.size() > 0) {
                RSSFeedResponse.RSSItem item = response.newItem();
                item.title = StringEscapeUtils.unescapeHtml(itemTitle.getTextContent());
                if (itemDescription != null) {
                    item.description = itemDescription.getTextContent();
                } else {
                    item.description = "";
                }

                for (int j = 0; j < itemLink.size(); j++) {
                    item.links.add(itemLink.get(j).getTextContent());
                }

                if (item.date == null && pubDate != null) {
                    item.date = XMLParserUtil.parseDate(pubDate.getTextContent().trim(), Locale.ENGLISH);
                }

                if (item.date == null && itemDate != null) {
                    item.date = XMLParserUtil.parseDate(itemDate.getTextContent().trim(), Locale.ENGLISH);
                }

                response.rssItems.add(item);
            }
        }

        return response;
    }

    public RSSFeedResponse parseFeed(XMLNode rootNode) {
        return parseChannel(rootNode);
    }
}
