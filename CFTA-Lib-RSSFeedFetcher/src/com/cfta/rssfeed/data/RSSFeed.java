package com.cfta.rssfeed.data;

// Data structure for top-level RSS feed
public class RSSFeed {
  public final String title;
  public final String xmlUrl;
  public final String htmlUrl;

  // Constructor
  public RSSFeed(final String title, final String xmlUrl, final String htmlUrl) {
    this.title = title;
    this.xmlUrl = xmlUrl;
    this.htmlUrl = htmlUrl;
  }
}
