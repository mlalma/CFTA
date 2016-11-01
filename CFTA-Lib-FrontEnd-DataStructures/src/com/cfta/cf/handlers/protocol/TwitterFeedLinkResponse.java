// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2013
package com.cfta.cf.handlers.protocol;

import java.util.ArrayList;
import java.util.List;

// Twitter feed link response data structure
public class TwitterFeedLinkResponse {
    public final static int RESPONSE_OK = 0;
    public final static int RESPONSE_FAIL = -1;
    
    public int errorCode = RESPONSE_OK;
    
    // Tweet link data structure with favourite + retweet amounts
    public class TweetLink {
        public String linkUrl = "";
        public int favouriteCount = -1;
        public int retweetCount = -1;
        public long creationTime = 0;
    }
    
    public List<TweetLink> links = new ArrayList<>();
    
    public TweetLink newLink() { return new TweetLink(); }
}
