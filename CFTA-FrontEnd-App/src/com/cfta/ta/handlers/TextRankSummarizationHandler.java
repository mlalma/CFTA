// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.ta.handlers;

import cfta.client.CFTARequest;
import com.cfta.cf.handlers.protocol.ContentExtractionRequest;
import com.cfta.cf.handlers.protocol.ContentExtractionResponse;
import com.cfta.log.CFTALog;
import com.cfta.ta.handlers.protocol.LanguageDetectionRequest;
import com.cfta.ta.handlers.protocol.LanguageDetectionResponse;
import com.cfta.ta.handlers.protocol.TextSummarizationRequest;
import com.cfta.ta.handlers.protocol.TextSummarizationResponse;
import com.cfta.textanalysis.stopwords.StopwordFilter;
import com.cfta.textanalysis.stopwords.StopwordFilterFactory;
import com.cfta.textanalysis.textrank.TextRankGraph;
import com.cfta.textanalysis.textrank.TextRankSentenceNode;
import com.cfta.textanalysis.textrank.TextRankSummarizationMode;
import com.cfta.textanalysis.textrank.TextRankSummarizer;
import com.cfta.tn.handlers.protocol.LemmatizationRequest;
import com.cfta.tn.handlers.protocol.LemmatizationResponse;
import com.cfta.tn.handlers.protocol.SentenceExtractionRequest;
import com.cfta.tn.handlers.protocol.SentenceExtractionResponse;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

// Summarizes text using TextRank algorithm
public class TextRankSummarizationHandler implements Route {

    private int port;
    private Gson gson = new Gson();

    // Constructor
    public TextRankSummarizationHandler(int port) {
        this.port = port;
    }

    // Splits text to sentences, lemmatizes words and removes stopwords
    private TextRankGraph doSentenceSplit(String text, String language) {
        TextRankGraph sentences = new TextRankGraph();
        CFTARequest request = new CFTARequest();

        StopwordFilter sFilter = StopwordFilterFactory.getStopwordFilter(language);

        SentenceExtractionRequest sReq = new SentenceExtractionRequest();
        sReq.text = text;
        sReq.newlineAsParagraphSeparation = true;
        SentenceExtractionResponse sResp = (SentenceExtractionResponse) request.sendRequest(sReq, false, "127.0.0.1", port);
        int sentencePosition = 0;

        if (sResp.errorCode == SentenceExtractionResponse.RESPONSE_OK) {
            for (String s : sResp.sentences) {
                if (s.length() > 0) {
                    TextRankSentenceNode n = new TextRankSentenceNode();
                    n.originalString = s;
                    n.position = sentencePosition;
                    sentencePosition++;
                    LemmatizationRequest req = new LemmatizationRequest();
                    req.newlineAsParagraphSeparation = false;
                    req.addSentenceSeparationTokens = false;
                    req.text = s;
                    LemmatizationResponse resp = (LemmatizationResponse) request.sendRequest(req, false, "127.0.0.1", port);
                    n.originalStringLength = resp.tokens.size();
                    for (String s2 : resp.tokens) {
                        if (!sFilter.isStopword(s2)) {
                            n.normalizedString.add(s2.trim().toLowerCase());
                        }
                    }
                    sentences.graph.add(n);
                }
            }
        }

        return sentences;
    }

    @Override
    // TextRank text summarizer request handler
    public Object handle(Request request, Response response) {
        long startTime = System.currentTimeMillis();
        String resultString;
        response.type("application/json");

        TextSummarizationResponse responseData = new TextSummarizationResponse();
        try {
            CFTARequest cftaReq = new CFTARequest();
            TextSummarizationRequest sRequest = gson.fromJson(request.body().trim(), TextSummarizationRequest.class);

            String text;
            String language = "";

            // Text is given as a part of the request or then it is fetched behind the url
            if (sRequest.url != null && sRequest.url.length() > 0) {
                ContentExtractionRequest extractRequest = new ContentExtractionRequest();
                extractRequest.extractHeader = true;
                extractRequest.url = sRequest.url;
                ContentExtractionResponse extractResponse = (ContentExtractionResponse) cftaReq.sendRequest(extractRequest, false, "127.0.0.1", port);

                if (extractResponse.errorCode == ContentExtractionResponse.RESPONSE_OK) {
                    if (extractResponse.title != null && extractResponse.title.length() > 0) {
                        text = extractResponse.title + "\n" + extractResponse.text;
                    } else {
                        text = extractResponse.text;
                        sRequest.dropTitleFromSummaryCreation = false;
                    }
                    language = extractResponse.language;
                } else {
                    throw new RuntimeException("Failed to fetch webpage");
                }
            } else {
                if (sRequest.title != null && sRequest.title.length() > 0) {
                    text = sRequest.title + "\n" + sRequest.text;
                } else {
                    text = sRequest.text;
                    sRequest.dropTitleFromSummaryCreation = false;
                }
            }

            // Detect language of the text for simple stopword removal
            if (language.length() == 0) {
                LanguageDetectionRequest langRequest = new LanguageDetectionRequest();
                langRequest.text = text;
                LanguageDetectionResponse langResponse = (LanguageDetectionResponse) cftaReq.sendRequest(langRequest, false, "127.0.0.1", port);
                language = langResponse.language;
            }

            TextRankGraph g = doSentenceSplit(text, language);

            TextRankSummarizationMode summaryMode = new TextRankSummarizationMode();
            summaryMode.allSentences = sRequest.allSentences;
            summaryMode.approxNumOfWords = sRequest.approxNumOfWords;
            summaryMode.approxPercentageOfText = sRequest.approxPercentageOfText;
            summaryMode.numOfSentences = sRequest.summarySentenceNum;

            TextRankSummarizer summarizer = new TextRankSummarizer();
            responseData = summarizer.summarize(g, sRequest.dropTitleFromSummaryCreation, summaryMode);

            responseData.errorCode = TextSummarizationResponse.RESPONSE_OK;
        } catch (Exception ex) {
            responseData.errorCode = TextSummarizationResponse.RESPONSE_FAIL;
        }

        CFTALog.LL("TextRank summarization request done, took" + (System.currentTimeMillis() - startTime) + "ms");
        resultString = gson.toJson(responseData, TextSummarizationResponse.class);
        return resultString;
    }
}
