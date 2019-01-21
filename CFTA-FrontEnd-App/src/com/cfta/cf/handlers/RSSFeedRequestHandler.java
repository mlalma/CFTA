// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.cf.handlers;

import com.cfta.cf.feeds.RSSFeedParser;
import com.cfta.cf.handlers.protocol.RSSFeedRequest;
import com.cfta.cf.handlers.protocol.RSSFeedResponse;
import com.cfta.log.CFTALog;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

// Handles RSS feed fetch & parse requests
public class RSSFeedRequestHandler implements Route {

    private Gson gson = new Gson();

    // Constructor
    public RSSFeedRequestHandler() {
    }

    @Override
    // Handles RSS feed request
    public Object handle(Request request, Response response) {
        long startTime = System.currentTimeMillis();
        String resultString;
        response.type("application/json");
        RSSFeedResponse responseData = new RSSFeedResponse();

        try {
            RSSFeedRequest rssRequest = gson.fromJson(request.body().trim(), RSSFeedRequest.class);
            RSSFeedParser parser = new RSSFeedParser();
            RSSFeedResponse rssResponse = parser.parseFeedFromUrl(rssRequest.url);
            if (rssResponse != null) {
                responseData = rssResponse;
            } else {
                responseData.errorCode = RSSFeedResponse.RESPONSE_FAIL;
            }
        } catch (Exception ex) {
            responseData.errorCode = RSSFeedResponse.RESPONSE_FAIL;
        }

        CFTALog.LL("RSS feed request done, took" + (System.currentTimeMillis() - startTime) + "ms");
        resultString = gson.toJson(responseData, RSSFeedResponse.class);
        return resultString;
    }
}
