// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2013
package com.cfta.cf.handlers;

import com.cfta.cf.handlers.protocol.TwitterFeedRequest;
import com.cfta.cf.handlers.protocol.TwitterFeedResponse;
import com.cfta.cf.twitter.TwitterFeedParser;
import com.cfta.log.CFTALog;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

// Twitter request handler
public class TwitterRequestHandler extends Route {
    
    private Gson gson = new Gson();
    
    // Constructor
    public TwitterRequestHandler(String route) {
        super(route);
    }
    
    @Override
    // Handles Twitter feed request
    public Object handle(Request request, Response response) {
        long startTime = System.currentTimeMillis();
        String resultString;
        response.type("application/json");
        TwitterFeedResponse responseData = new TwitterFeedResponse();
        
        try {
            TwitterFeedRequest twitterRequest = gson.fromJson(request.body().trim(), TwitterFeedRequest.class);            
            TwitterFeedParser parser = new TwitterFeedParser();
            TwitterFeedResponse tResponse = parser.getTwitterFeed(twitterRequest.screenName, twitterRequest.numberOfTweets, twitterRequest.sinceMessageId);
            if (tResponse != null) {
                responseData = tResponse;
            } else {
                responseData.errorCode = TwitterFeedResponse.RESPONSE_FAIL;
            }
        } catch (Exception ex) {
            responseData.errorCode = TwitterFeedResponse.RESPONSE_FAIL;
        }
        
        CFTALog.LL("Twitter feed request done, took" + (System.currentTimeMillis() - startTime) + "ms");
        resultString = gson.toJson(responseData, TwitterFeedResponse.class);
        return resultString;
    }    
}
