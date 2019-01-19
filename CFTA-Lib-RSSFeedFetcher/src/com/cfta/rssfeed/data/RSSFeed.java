// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.rssfeed.data;

// Data structure for top-level RSS feed
public class RSSFeed {
    public final String title;
    public final String xmlUrl;
    public final String htmlUrl;

    // Constructor
    public RSSFeed(String title, String xmlUrl, String htmlUrl) {
        this.title = title;
        this.xmlUrl = xmlUrl;
        this.htmlUrl = htmlUrl;
    }
}
