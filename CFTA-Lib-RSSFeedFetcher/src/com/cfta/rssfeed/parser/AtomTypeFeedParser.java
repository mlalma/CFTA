// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.rssfeed.parser;

import com.cfta.rssfeed.xmlparser.XMLNode;
import com.cfta.rssfeed.xmlparser.XMLParserUtil;
import com.cfta.cf.handlers.protocol.RSSFeedResponse;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringEscapeUtils;

public class AtomTypeFeedParser {

    private static final String ENTRY_NODE = "entry";
    private static final String SUBTITLE_NODE = "subtitle";
    private static final String TITLE_NODE = "title";
    private static final String CONTENT_NODE = "content";
    //private static final String ITEM_NODE = "item";        
    private static final String LINK_NODE = "link";
    private static final String UPDATED_NODE = "updated";
    private static final String PUB_DATE_NODE = "pubDate";
    private static final String HREF_ATTR = "href";

    public AtomTypeFeedParser() {

    }

    private RSSFeedResponse parseEntries(XMLNode rootNode) {
        RSSFeedResponse response = new RSSFeedResponse();

        XMLNode title = XMLParserUtil.findNode(TITLE_NODE, rootNode.childNodes);
        XMLNode description = XMLParserUtil.findNode(SUBTITLE_NODE, rootNode.childNodes);

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

        List<XMLNode> items = XMLParserUtil.findNodes(ENTRY_NODE, rootNode.childNodes);
        for (int i = 0; i < items.size(); i++) {
            XMLNode itemTitle = XMLParserUtil.findNode(TITLE_NODE, items.get(i).childNodes);
            List<XMLNode> itemLink = XMLParserUtil.findNodes(LINK_NODE, items.get(i).childNodes);
            XMLNode itemDescription = XMLParserUtil.findNode(CONTENT_NODE, items.get(i).childNodes);
            XMLNode itemDate = XMLParserUtil.findNode(UPDATED_NODE, items.get(i).childNodes);
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
                    String s = itemLink.get(j).getTextContent().trim();
                    if (s.length() == 0) {
                        s = itemLink.get(j).attributes.get(HREF_ATTR);
                        if (s.length() > 0) {
                            item.links.add(s);
                        }
                    } else {
                        item.links.add(s);
                    }
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
        return parseEntries(rootNode);
    }
}
