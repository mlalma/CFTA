package com.cfta.rssfeed.opml;

import com.cfta.rssfeed.data.RSSFeed;
import com.cfta.rssfeed.data.RSSFeedFolder;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.parser.Parser;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import static com.cfta.rssfeed.util.NodeTools.*;

// Parses OPML (Outline Processor Markup Language) Files
public class OPMLParser {

  private static final String OPML_HEADER = "opml";
  private static final String OPML_VERSION_ATTR = "version";
  private static final String HEAD_HEADER = "head";
  private static final String BODY_HEADER = "body";
  private static final String OPML_TITLE = "title";
  private static final String OUTLINE_ELEM = "outline";
  private static final String TYPE_ELEM = "type";
  private static final String XMLURL_ELEM = "xmlUrl";
  private static final String TITLE_ELEM = "title";
  private static final String TEXT_ELEM = "text";
  private static final String HTMLURL_ELEM = "htmlUrl";

  // Identified OPML version
  private enum OPMLVersion {
    eUnknown,
    eVersion1,
    eVersion11,
    eVersion2
  }

  // Parses OPML 1.0, 1.1 and 2.0 documents
  private void parseOPMLDoc(@NotNull final Node outlineNode, @NotNull final RSSFeedFolder parentFolder) {
    if (outlineNode.getNodeName().equalsIgnoreCase(OUTLINE_ELEM)) {
      String typeAttr = getAttributeValueOrEmpty(outlineNode, TYPE_ELEM);
      String xmlUrl = getAttributeValueOrEmpty(outlineNode, XMLURL_ELEM);

      String title = getAttributeValueOrEmpty(outlineNode, TITLE_ELEM);
      if (title.length() == 0) {
        title = getAttributeValueOrEmpty(outlineNode, TEXT_ELEM);
      }

      String htmlUrl = getAttributeValueOrEmpty(outlineNode, HTMLURL_ELEM);

      if (typeAttr.equalsIgnoreCase(TEXT_ELEM) || xmlUrl.length() == 0) {
        // Element is folder
        RSSFeedFolder folder = new RSSFeedFolder(title);
        parentFolder.folders.add(folder);
        for (int i = 0; i < outlineNode.getChildNodes().getLength(); i++) {
          parseOPMLDoc(outlineNode.getChildNodes().item(i), folder);
        }
      } else {
        // Element is RSS feed
        if (xmlUrl.toLowerCase().startsWith("http")) {
          RSSFeed feed = new RSSFeed(title, xmlUrl, htmlUrl);
          parentFolder.feeds.add(feed);
        } else {
          System.err.println(
              "ERROR! Could not add feed " + title + " to list, bad XML URL: " + xmlUrl);
        }
      }
    }
  }

  // Parses OPML document
  public RSSFeedFolder parseOPMLDoc(@NotNull final String docString) {
    org.jsoup.nodes.Document prettifiedDoc = Jsoup.parse(docString, "", Parser.xmlParser());
    W3CDom w3cDom = new W3CDom();
    Document doc = w3cDom.fromJsoup(prettifiedDoc);
    doc.getDocumentElement().normalize();

    Node rootNode = doc.getDocumentElement();
    if (rootNode == null || !rootNode.getNodeName().equalsIgnoreCase(OPML_HEADER)) {
      throw new RuntimeException("<opml> header not found, not a valid document!");
    }

    Node headNode = childNodeFromTree(rootNode, HEAD_HEADER);
    Node bodyNode = childNodeFromTree(rootNode, BODY_HEADER);

    if (bodyNode == null) {
      throw new RuntimeException("<body> header not found, not a valid document!");
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

    Node titleNode = childNode(headNode, OPML_TITLE);
    String title;
    if (titleNode != null) {
      title = titleNode.getTextContent().trim();
    } else {
      title = "<unknown OPML feed>";
    }
    RSSFeedFolder rootFolder = new RSSFeedFolder(title);

    NodeList nl = bodyNode.getChildNodes();

    for (int i = 0; i < nl.getLength(); i++) {
      if (nl.item(i).getNodeName().equalsIgnoreCase(OUTLINE_ELEM)) {
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
