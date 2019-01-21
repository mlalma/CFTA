// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.tn.handlers;

import cfta.client.CFTARequest;
import com.cfta.log.CFTALog;
import com.cfta.textnormalization.stanfordnlp.TextTokenizer;
import com.cfta.tn.handlers.protocol.SentenceExtractionRequest;
import com.cfta.tn.handlers.protocol.SentenceExtractionResponse;
import com.cfta.tn.handlers.protocol.TokenExtractionRequest;
import com.cfta.tn.handlers.protocol.TokenExtractionResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import spark.Request;
import spark.Response;
import spark.Route;

// Token extraction handler
public class TokenExtractionHandler implements Route {

    private Gson gson = new Gson();
    private TextTokenizer tTokenizer = new TextTokenizer();
    private int port;

    // Constructor
    public TokenExtractionHandler(int port) {
        this.port = port;
    }

    @Override
    // Tokenizes given text
    public Object handle(Request request, Response response) {
        long startTime = System.currentTimeMillis();

        String resultString;
        response.type("application/json");
        TokenExtractionResponse responseData = new TokenExtractionResponse();
        responseData.tokens = new ArrayList<>();

        try {
            TokenExtractionGatherer gatherer = new TokenExtractionGatherer();
            TokenExtractionRequest tRequest = gson.fromJson(request.body().trim(), TokenExtractionRequest.class);

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
                tTokenizer.tokenizeText(s, gatherer);
                responseData.tokens.addAll(gatherer.getTokens());
                if (tRequest.addSentenceSeparationTokens) {
                    responseData.tokens.add("\n");
                }
                gatherer.clearTokens();
            }
            responseData.errorCode = TokenExtractionResponse.RESPONSE_OK;
        } catch (Exception ex) {
            responseData.errorCode = TokenExtractionResponse.RESPONSE_FAIL;
        }

        CFTALog.LL("Token extraction done, took " + (System.currentTimeMillis() - startTime) + "ms");
        resultString = gson.toJson(responseData, TokenExtractionResponse.class);
        return resultString;
    }
}
