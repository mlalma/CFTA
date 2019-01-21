// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.ta.handlers;

import cfta.client.CFTARequest;
import com.cfta.log.CFTALog;
import com.cfta.ta.handlers.protocol.LanguageDetectionRequest;
import com.cfta.ta.handlers.protocol.LanguageDetectionResponse;
import com.cfta.ta.handlers.protocol.POSTagRequest;
import com.cfta.ta.handlers.protocol.POSTagResponse;
import com.cfta.ta.handlers.protocol.POSTagResponse.POSTag;
import com.cfta.ta.handlers.protocol.TextStatisticsRequest;
import com.cfta.ta.handlers.protocol.TextStatisticsResponse;
import com.cfta.textanalysis.statistics.TextStatisticsCalcEngine;
import com.google.gson.Gson;

import java.util.List;

import spark.Request;
import spark.Response;
import spark.Route;

// Calculates text statistics
public class TextStatisticsHandler implements Route {

    private int port;
    private Gson gson = new Gson();

    // Constructor
    public TextStatisticsHandler(int port) {
        this.port = port;
    }

    // Gets POS tags of the text
    private List<POSTag> getPOSTags(String text) {
        CFTARequest request = new CFTARequest();
        POSTagRequest req = new POSTagRequest();
        req.newlineAsParagraphSeparation = true;
        req.text = text;
        POSTagResponse response = (POSTagResponse) request.sendRequest(req, false, "127.0.0.1", port);
        return response.tags;
    }

    // Gets text language
    private String getLanguage(String text) {
        CFTARequest request = new CFTARequest();
        LanguageDetectionRequest langRequest = new LanguageDetectionRequest();
        langRequest.text = text;
        LanguageDetectionResponse langResponse = (LanguageDetectionResponse) request.sendRequest(langRequest, false, "127.0.0.1", port);
        return langResponse.language;
    }

    @Override
    // Calculates text statistics
    public Object handle(Request request, Response response) {
        long startTime = System.currentTimeMillis();
        String resultString;
        response.type("application/json");

        TextStatisticsResponse responseData;
        try {
            TextStatisticsRequest tRequest = gson.fromJson(request.body().trim(), TextStatisticsRequest.class);
            List<POSTag> tags = getPOSTags(tRequest.text);
            String language = getLanguage(tRequest.text);

            TextStatisticsCalcEngine statsEngine = new TextStatisticsCalcEngine();
            responseData = statsEngine.calcTextStatistics(tags, language);
        } catch (Exception ex) {
            responseData = new TextStatisticsResponse();
            responseData.errorCode = TextStatisticsResponse.RESPONSE_FAIL;
        }

        CFTALog.LL("Text statistics request done, took" + (System.currentTimeMillis() - startTime) + "ms");
        resultString = gson.toJson(responseData, TextStatisticsResponse.class);
        return resultString;
    }
}
