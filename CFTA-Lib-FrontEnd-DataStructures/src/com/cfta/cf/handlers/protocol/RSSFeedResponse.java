// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.cf.handlers.protocol;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Returns RSS feed and feed items
public class RSSFeedResponse {
    public static final int RESPONSE_OK = 0;
    public static final int RESPONSE_FAIL = -1;

    public int errorCode = RESPONSE_OK;

    // Single feed item
    public class RSSItem {
        public String title = "";
        public Date date = null;
        public String description = "";
        public List<String> links = new ArrayList<>();
        public List<String> categories = new ArrayList<>();
    }

    public String feedTitle = "";
    public String description = "";
    public List<String> links = new ArrayList<>();
    public List<RSSItem> rssItems = new ArrayList<>();

    public RSSItem newItem() {
        return new RSSItem();
    }
}
