// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.ta.handlers;

import com.cfta.log.CFTALog;
import com.cfta.ta.handlers.protocol.LanguageDetectionRequest;
import com.cfta.ta.handlers.protocol.LanguageDetectionResponse;
import com.cfta.textanalysis.language.LangDetectionBase;
import com.cfta.textanalysis.language.LanguageDetection;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

// Detects used language from text
public class LanguageDetectionHandler implements Route {

    private Gson gson = new Gson();
    private LangDetectionBase lang = null;

    // Constructor
    public LanguageDetectionHandler() {
        try {
            lang = new LanguageDetection();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    // Handles language detection request
    public Object handle(Request request, Response response) {
        long startTime = System.currentTimeMillis();

        String resultString;
        response.type("application/json");
        LanguageDetectionResponse responseData = new LanguageDetectionResponse();

        try {
            LanguageDetectionRequest langRequest = gson.fromJson(request.body().trim(), LanguageDetectionRequest.class);
            responseData.language = lang.getLanguage(langRequest.text);
            responseData.errorCode = LanguageDetectionResponse.RESPONSE_OK;
        } catch (Exception ex) {
            responseData.errorCode = LanguageDetectionResponse.RESPONSE_FAIL;
        }

        CFTALog.LL("Language detection request done, took " + (System.currentTimeMillis() - startTime) + "ms");
        resultString = gson.toJson(responseData, LanguageDetectionResponse.class);
        return resultString;
    }

}
