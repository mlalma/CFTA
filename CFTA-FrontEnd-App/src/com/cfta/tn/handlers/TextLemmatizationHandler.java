// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.tn.handlers;

import cfta.client.CFTARequest;
import com.cfta.log.CFTALog;
import com.cfta.textnormalization.stanfordnlp.TextLemmatizer;
import com.cfta.tn.handlers.protocol.LemmatizationRequest;
import com.cfta.tn.handlers.protocol.LemmatizationResponse;
import com.cfta.tn.handlers.protocol.SentenceExtractionRequest;
import com.cfta.tn.handlers.protocol.SentenceExtractionResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import spark.Request;
import spark.Response;
import spark.Route;

// Lemmatizes text
public class TextLemmatizationHandler implements Route {

    private Gson gson = new Gson();
    private TextLemmatizer lemmatizer = new TextLemmatizer();
    private int port;

    public TextLemmatizationHandler(int port) {
        this.port = port;
    }

    @Override
    public Object handle(Request request, Response response) {
        long startTime = System.currentTimeMillis();

        String resultString;
        response.type("application/json");
        LemmatizationResponse responseData = new LemmatizationResponse();
        responseData.tokens = new ArrayList<>();

        try {
            TextLemmatizationGatherer gatherer = new TextLemmatizationGatherer();
            LemmatizationRequest tRequest = gson.fromJson(request.body().trim(), LemmatizationRequest.class);

            List<String> texts = new ArrayList<>();
            if (tRequest.addSentenceSeparationTokens) {
                // If separate newlines are required, first need to divide the text to sentences
                SentenceExtractionRequest sRequest = new SentenceExtractionRequest();
                sRequest.newlineAsParagraphSeparation = tRequest.newlineAsParagraphSeparation;
                sRequest.text = tRequest.text;
                CFTARequest cftaReq = new CFTARequest();
                SentenceExtractionResponse sResponse = (SentenceExtractionResponse) cftaReq.sendRequest(sRequest, false, "127.0.0.1", port);
                if (sResponse.errorCode == SentenceExtractionResponse.RESPONSE_OK) {
                    texts = sResponse.sentences;
                } else {
                    throw new RuntimeException("Failed to extract sentences from text");
                }
            } else {
                texts.add(tRequest.text);
            }

            for (String s : texts) {
                lemmatizer.lemmatizeText(s, gatherer);
                responseData.tokens.addAll(gatherer.getTokens());
                if (tRequest.addSentenceSeparationTokens) {
                    responseData.tokens.add("\n");
                }
                gatherer.clearTokens();
            }
            responseData.errorCode = LemmatizationResponse.RESPONSE_OK;
        } catch (Exception ex) {
            responseData.errorCode = LemmatizationResponse.RESPONSE_FAIL;
        }

        CFTALog.LL("Text lemmatization request done, took " + (System.currentTimeMillis() - startTime) + "ms");
        resultString = gson.toJson(responseData, LemmatizationResponse.class);
        return resultString;
    }
}
