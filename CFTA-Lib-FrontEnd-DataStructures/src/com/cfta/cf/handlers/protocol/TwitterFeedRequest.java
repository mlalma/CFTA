// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.cf.handlers.protocol;

// Twitter feed read request
public class TwitterFeedRequest {
    public String screenName = "";
    public int numberOfTweets = 20;
    public long sinceMessageId = -1;
}
