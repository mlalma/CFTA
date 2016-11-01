// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2013
package com.cfta.cf.handlers;

import com.cfta.cf.handlers.protocol.TwitterFeedLinkRequest;
import com.cfta.cf.handlers.protocol.TwitterFeedLinkResponse;
import com.cfta.cf.handlers.protocol.TwitterFeedResponse;
import com.cfta.cf.twitter.TwitterFeedParser;
import com.cfta.log.CFTALog;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

// Twitter link request handler
public class TwitterLinkRequestHandler extends Route {

    private Gson gson = new Gson();

    // Constructor
    public TwitterLinkRequestHandler(String route) {
        super(route);
    }

    @Override
    // Handles twitter link parsing request
    public Object handle(Request request, Response response) {
        long startTime = System.currentTimeMillis();
        String resultString;
        response.type("application/json");
        TwitterFeedLinkResponse responseData = new TwitterFeedLinkResponse();
        
        try {
            TwitterFeedLinkRequest twitterRequest = gson.fromJson(request.body().trim(), TwitterFeedLinkRequest.class);            
            TwitterFeedParser parser = new TwitterFeedParser();
            TwitterFeedLinkResponse tResponse = parser.getTwitterFeedLinks(twitterRequest.screenName, twitterRequest.numberOfTweets, twitterRequest.sinceMessageId);            
            if (tResponse != null) {
                responseData = tResponse;
            } else {
                responseData.errorCode = TwitterFeedResponse.RESPONSE_FAIL;
            }
        } catch (Exception ex) {
            responseData.errorCode = TwitterFeedLinkResponse.RESPONSE_FAIL;
        }
        
        CFTALog.LL("Twitter link feed request done, took" + (System.currentTimeMillis() - startTime) + "ms");
        resultString = gson.toJson(responseData, TwitterFeedLinkResponse.class);
        return resultString;
    }
}
