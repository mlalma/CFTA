// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.cf.handlers;

import cfta.client.CFTARequest;
import com.cfta.cf.contentextraction.BoilerpipeContentExtraction;
import com.cfta.cf.contentextraction.ContentExtractionBase;
import com.cfta.cf.contentextraction.XtractContent;
import com.cfta.cf.handlers.protocol.ContentExtractionRequest;
import com.cfta.cf.handlers.protocol.ContentExtractionResponse;
import com.cfta.cf.handlers.protocol.WebFetchRequest;
import com.cfta.cf.handlers.protocol.WebFetchResponse;
import com.cfta.log.CFTALog;
import com.cfta.ta.handlers.protocol.LanguageDetectionRequest;
import com.cfta.ta.handlers.protocol.LanguageDetectionResponse;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

// Handles text extraction requests
public class ContentExtractionHandler implements Route {

    private Gson gson = new Gson();
    private int port;

    // Constructor
    public ContentExtractionHandler(int port) {
        this.port = port;
    }

    @Override
    // Handles content extraction request
    public Object handle(Request request, Response response) {
        long startTime = System.currentTimeMillis();

        String resultString;
        response.type("application/json");
        ContentExtractionResponse responseData = new ContentExtractionResponse();
        CFTARequest cftaReq = new CFTARequest();

        try {
            ContentExtractionBase contentExtractor;
            ContentExtractionRequest extractRequest = gson.fromJson(request.body().trim(), ContentExtractionRequest.class);

            if (extractRequest.extractionAlgorithm.equalsIgnoreCase(ContentExtractionRequest.BOILERPIPE_ALGORITHM)) {
                contentExtractor = new BoilerpipeContentExtraction();
            } else if (extractRequest.extractionAlgorithm.equalsIgnoreCase(ContentExtractionRequest.XTRACT_ALGORITHM)) {
                contentExtractor = new XtractContent();
            } else {
                throw new RuntimeException("Unknown content extraction algorithm");
            }

            String html;
            if (extractRequest.url.length() > 0) {
                // If page html has not been sent as a part of request, do a web fetch request and get the page
                WebFetchRequest htmlRequest = new WebFetchRequest();
                htmlRequest.url = extractRequest.url;

                if (contentExtractor instanceof XtractContent) {
                    htmlRequest.usedFetcher = WebFetchRequest.FETCHER_XTRACT;
                }

                WebFetchResponse fetchResponse = (WebFetchResponse) cftaReq.sendRequest(htmlRequest, false, "127.0.0.1", port);
                if (fetchResponse.errorCode == WebFetchResponse.RESPONSE_OK) {
                    html = fetchResponse.html;
                } else {
                    html = "";
                }
            } else {
                html = extractRequest.html;
            }

            if (html.trim().length() > 0) {
                contentExtractor.extractContent(html, extractRequest.extractHeader, extractRequest.extractMainImage, extractRequest.extractText);

                responseData.text = contentExtractor.getArticleText();
                responseData.title = contentExtractor.getTitle();
                responseData.mainImageUrl = contentExtractor.getMainImageUrl();

                if (responseData.text.length() > 0) {
                    String text = responseData.text;
                    if (text.length() > 5000) {
                        text = text.substring(0, 5000);
                    }
                    LanguageDetectionRequest langRequest = new LanguageDetectionRequest();
                    langRequest.text = text;
                    LanguageDetectionResponse langResponse = (LanguageDetectionResponse) cftaReq.sendRequest(langRequest, false, "127.0.0.1", port);
                    responseData.language = langResponse.language;
                }

                responseData.errorCode = ContentExtractionResponse.RESPONSE_OK;
            } else {
                responseData.errorCode = ContentExtractionResponse.RESPONSE_FAIL;
            }
        } catch (Exception ex) {
            responseData.errorCode = ContentExtractionResponse.RESPONSE_FAIL;
        }

        CFTALog.LL("Text extraction request done, took " + (System.currentTimeMillis() - startTime) + "ms");
        resultString = gson.toJson(responseData, ContentExtractionResponse.class);
        return resultString;
    }
}
