package com.cfta.rssfeed.parser;

import com.cfta.cf.handlers.protocol.RSSFeedResponse;

import java.util.List;
import java.util.Locale;

import com.cfta.rssfeed.util.NodeTools;
import org.apache.commons.lang.StringEscapeUtils;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Node;

// FeedBurner RSS Feed parser
public class FeedBurnerRSSTypeFeedParser {

  private static final String TITLE_NODE = "title";
  private static final String DESCRIPTION_NODE = "description";
  private static final String CONTENT_NODE = "content:encoded";
  private static final String ITEM_NODE = "item";
  private static final String LINK_NODE = "link";
  private static final String DC_DATE_NODE = "dc:date";
  private static final String PUB_DATE_NODE = "pubDate";

  // Constructor
  public FeedBurnerRSSTypeFeedParser() {}

  // Parses FeedBurner type feed
  private RSSFeedResponse parseChannel(@NotNull final Node rootNode) {
    RSSFeedResponse response = new RSSFeedResponse();

    Node title = NodeTools.childNode(rootNode, TITLE_NODE);
    Node description = NodeTools.childNode(rootNode, DESCRIPTION_NODE);

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

    List<Node> items = NodeTools.childNodes(rootNode, ITEM_NODE);
    for (int i = 0; i < items.size(); i++) {
      Node itemTitle = NodeTools.childNode(items.get(i), TITLE_NODE);
      List<Node> itemLink = NodeTools.childNodes(items.get(i), LINK_NODE);
      Node itemDescription = NodeTools.childNode(items.get(i), CONTENT_NODE);
      Node itemDate = NodeTools.childNode(items.get(i), DC_DATE_NODE);
      Node pubDate = NodeTools.childNode(items.get(i), PUB_DATE_NODE);

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

  // Entry point to the parser
  public RSSFeedResponse parseFeed(@NotNull final Node rootNode) {
    return parseChannel(rootNode);
  }
}
