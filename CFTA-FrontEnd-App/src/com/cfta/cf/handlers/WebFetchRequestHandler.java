// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2013
package com.cfta.cf.handlers;

import com.cfta.cf.handlers.protocol.WebFetchRequest;
import com.cfta.cf.handlers.protocol.WebFetchResponse;
import com.cfta.cf.httpfetch.ApacheHttpClientFetcher;
import com.cfta.cf.httpfetch.HttpFetcherBase;
import com.cfta.log.CFTALog;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

// Handles web fetch request
public class WebFetchRequestHandler extends Route {

    private Gson gson = new Gson();            

    // Constructor
    public WebFetchRequestHandler(String path) {
        super(path);
    }
    
    @Override
    // Retrieves a web page and returns it
    public Object handle(Request request, Response response) {
        long startTime = System.currentTimeMillis();
        String resultString = "";
        response.type("application/json");
        WebFetchResponse responseData = new WebFetchResponse();
        
        try {
            WebFetchRequest fetchRequest = gson.fromJson(request.body().trim(), WebFetchRequest.class);
            HttpFetcherBase fetcher = null;
            if (fetchRequest.usedFetcher.trim().equalsIgnoreCase(WebFetchRequest.FETCHER_APACHE)) {
                fetcher = new ApacheHttpClientFetcher();
            } else {
                throw new RuntimeException("Unknown fetcher in fetch request!");
            }

            String page = fetcher.getWebPage(fetchRequest.url);
            if (page.length() == 0) {
                responseData.errorCode = WebFetchResponse.RESPONSE_FAIL;
                responseData.html = "";                
            } else {
                responseData.errorCode = WebFetchResponse.RESPONSE_OK;
                responseData.html = page.trim();
            }
        } catch (Exception ex) {
            responseData.errorCode = WebFetchResponse.RESPONSE_FAIL;
            responseData.html = "";
        }
        
        CFTALog.LL("Web fetch request done, took " + (System.currentTimeMillis() - startTime) + "ms");
        resultString = gson.toJson(responseData, WebFetchResponse.class);
        return resultString;
    }
}
