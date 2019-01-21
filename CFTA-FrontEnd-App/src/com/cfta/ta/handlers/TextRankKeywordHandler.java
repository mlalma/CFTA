// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.ta.handlers;

import cfta.client.CFTARequest;
import com.cfta.cf.handlers.protocol.ContentExtractionRequest;
import com.cfta.cf.handlers.protocol.ContentExtractionResponse;
import com.cfta.log.CFTALog;
import com.cfta.ta.handlers.protocol.KeywordRequest;
import com.cfta.ta.handlers.protocol.KeywordResponse;
import com.cfta.ta.handlers.protocol.POSTagRequest;
import com.cfta.ta.handlers.protocol.POSTagResponse;
import com.cfta.textanalysis.textrank.TextRankKeywordFinder;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

// Finds keywords from text using TextRank algorithm
public class TextRankKeywordHandler implements Route {

    private int port;
    private Gson gson = new Gson();

    // Constructor
    public TextRankKeywordHandler(int port) {
        this.port = port;
    }

    @Override
    // Handles the keyword parsing request
    public Object handle(Request request, Response response) {
        long startTime = System.currentTimeMillis();
        String resultString;
        response.type("application/json");
        KeywordResponse responseData = new KeywordResponse();

        try {
            CFTARequest cftaReq = new CFTARequest();
            KeywordRequest kRequest = gson.fromJson(request.body().trim(), KeywordRequest.class);

            String text;
            if (kRequest.url != null && kRequest.url.length() > 0) {
                ContentExtractionRequest extractRequest = new ContentExtractionRequest();
                extractRequest.extractHeader = true;
                extractRequest.url = kRequest.url;
                ContentExtractionResponse extractResponse = (ContentExtractionResponse) cftaReq.sendRequest(extractRequest, false, "127.0.0.1", port);

                if (extractResponse.errorCode == ContentExtractionResponse.RESPONSE_OK) {
                    if (extractResponse.title != null && extractResponse.title.length() > 0) {
                        text = extractResponse.title + "\n" + extractResponse.text;
                    } else {
                        text = extractResponse.text;
                    }
                } else {
                    throw new RuntimeException("Failed to fetch webpage");
                }
            } else {
                if (kRequest.title != null && kRequest.title.length() > 0) {
                    text = kRequest.title + "\n" + kRequest.text;
                } else {
                    text = kRequest.text;
                }
            }

            POSTagRequest req = new POSTagRequest();
            req.newlineAsParagraphSeparation = true;
            req.text = text;
            POSTagResponse posResponse = (POSTagResponse) cftaReq.sendRequest(req, false, "127.0.0.1", port);

            TextRankKeywordFinder keywordFinder = new TextRankKeywordFinder();
            int numOfKeywords = kRequest.maxAmountOfKeywords;
            if (kRequest.returnAllKeywords) {
                numOfKeywords = -1;
            }
            responseData = keywordFinder.getKeywords(posResponse.tags, numOfKeywords);
            responseData.errorCode = KeywordResponse.RESPONSE_OK;
        } catch (Exception ex) {
            responseData.errorCode = KeywordResponse.RESPONSE_FAIL;
        }

        CFTALog.LL("TextRank keyword request done, took " + (System.currentTimeMillis() - startTime) + "ms");
        resultString = gson.toJson(responseData, KeywordResponse.class);
        return resultString;
    }
}
