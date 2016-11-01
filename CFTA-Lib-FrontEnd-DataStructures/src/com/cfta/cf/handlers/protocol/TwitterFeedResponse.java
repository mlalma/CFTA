// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2013
package com.cfta.cf.handlers.protocol;

import java.util.ArrayList;
import java.util.List;

// Twitter feed fetch response data structure
public class TwitterFeedResponse {    
    public final static int RESPONSE_OK = 0;
    public final static int RESPONSE_FAIL = -1;
    
    public int errorCode = RESPONSE_OK;
    
    // Single tweet data structure
    public class Tweet {
        public String text = "";
        public int retweetCount = 0;
        public long id = -1;
        public long replyToId = -1;
        public String replyToUser = "";
        public long retweetId = -1;
        public String retweetUserScreenName = "";
        public long creationTime = 0;
        public int favouriteCount = 0;
        public boolean isRetweet = false;    
    }
    
    public String userName = "";
    public String screenName = "";
    public String profileImageUrl = "";
    public String location = "";
    public int friendsCount = 0;
    public int followersCount = 0;
    public int favouritesCount = 0;
    public List<Tweet> tweets = new ArrayList<>();
    public long userId = -1;
    
    public int limit = -1;
    public int remaining = -1;
    
    public Tweet newTweet() { return new Tweet(); }
}
