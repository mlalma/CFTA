// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.tn.handlers;

import com.cfta.log.CFTALog;
import com.cfta.textnormalization.stanfordnlp.SentenceExtractor;
import com.cfta.tn.handlers.protocol.SentenceExtractionRequest;
import com.cfta.tn.handlers.protocol.SentenceExtractionResponse;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

// Handles sentence extraction
public class SentenceExtractionHandler implements Route {

    private Gson gson = new Gson();
    private SentenceExtractor sExtractor = new SentenceExtractor();

    // Constructor
    public SentenceExtractionHandler() {
    }

    @Override
    public Object handle(Request request, Response response) {
        long startTime = System.currentTimeMillis();

        String resultString;
        response.type("application/json");
        SentenceExtractionResponse responseData = new SentenceExtractionResponse();

        try {
            SentenceExtractionGatherer gatherer = new SentenceExtractionGatherer();
            SentenceExtractionRequest sRequest = gson.fromJson(request.body().trim(), SentenceExtractionRequest.class);
            sExtractor.extractSentences(sRequest.text, sRequest.newlineAsParagraphSeparation, gatherer);
            responseData.sentences = gatherer.getSentences();
            responseData.errorCode = SentenceExtractionResponse.RESPONSE_OK;
        } catch (Exception ex) {
            responseData.errorCode = SentenceExtractionResponse.RESPONSE_FAIL;
        }

        CFTALog.LL("Sentence extraction request done, took " + (System.currentTimeMillis() - startTime) + "ms");
        resultString = gson.toJson(responseData, SentenceExtractionResponse.class);
        return resultString;
    }
}
