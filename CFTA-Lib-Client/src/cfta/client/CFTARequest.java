// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package cfta.client;

import com.cfta.cf.handlers.protocol.RSSFeedRequest;
import com.cfta.cf.handlers.protocol.RSSFeedResponse;
import com.cfta.cf.handlers.protocol.ContentExtractionRequest;
import com.cfta.cf.handlers.protocol.ContentExtractionResponse;
import com.cfta.cf.handlers.protocol.TwitterFeedLinkRequest;
import com.cfta.cf.handlers.protocol.TwitterFeedLinkResponse;
import com.cfta.cf.handlers.protocol.TwitterFeedRequest;
import com.cfta.cf.handlers.protocol.TwitterFeedResponse;
import com.cfta.cf.handlers.protocol.WebFetchRequest;
import com.cfta.cf.handlers.protocol.WebFetchResponse;
import com.cfta.handlers.data.HandlerPaths;
import com.cfta.ping.protocol.PingRequest;
import com.cfta.ping.protocol.PingResponse;
import com.cfta.ta.handlers.protocol.KeywordRequest;
import com.cfta.ta.handlers.protocol.KeywordResponse;
import com.cfta.ta.handlers.protocol.LanguageDetectionRequest;
import com.cfta.ta.handlers.protocol.LanguageDetectionResponse;
import com.cfta.ta.handlers.protocol.NamedEntityRequest;
import com.cfta.ta.handlers.protocol.NamedEntityResponse;
import com.cfta.ta.handlers.protocol.POSTagRequest;
import com.cfta.ta.handlers.protocol.POSTagResponse;
import com.cfta.ta.handlers.protocol.TextStatisticsRequest;
import com.cfta.ta.handlers.protocol.TextStatisticsResponse;
import com.cfta.ta.handlers.protocol.TextSummarizationRequest;
import com.cfta.ta.handlers.protocol.TextSummarizationResponse;
import com.cfta.tn.handlers.protocol.LemmatizationRequest;
import com.cfta.tn.handlers.protocol.LemmatizationResponse;
import com.cfta.tn.handlers.protocol.SentenceExtractionRequest;
import com.cfta.tn.handlers.protocol.SentenceExtractionResponse;
import com.cfta.tn.handlers.protocol.TokenExtractionRequest;
import com.cfta.tn.handlers.protocol.TokenExtractionResponse;
import com.google.gson.Gson;

// Sends request and returns responses from them
public class CFTARequest {

    private Gson gson = new Gson();

    // Handles Request to/from CFTA server
    public Object sendRequest(Object request, boolean httpSecure, String domain, int port) {
        try {
            String url;

            if (!httpSecure) {
                url = "http://" + domain + ":" + port;
            } else {
                url = "https://" + domain + ":" + port;
            }

            ApacheHttpClient client = new ApacheHttpClient();

            if (request instanceof PingRequest) {
                url += HandlerPaths.PING_PATH;
                String response = client.getResponse(url, gson.toJson(request, PingRequest.class));
                return gson.fromJson(response, PingResponse.class);
            } else if (request instanceof WebFetchRequest) {
                url += HandlerPaths.WEB_FETCH_PATH;
                String response = client.getResponse(url, gson.toJson(request, WebFetchRequest.class));
                return gson.fromJson(response, WebFetchResponse.class);
            } else if (request instanceof RSSFeedRequest) {
                url += HandlerPaths.RSS_FEED_PATH;
                String response = client.getResponse(url, gson.toJson(request, RSSFeedRequest.class));
                return gson.fromJson(response, RSSFeedResponse.class);
            } else if (request instanceof TwitterFeedRequest) {
                url += HandlerPaths.TWITTER_FEED_PATH;
                String response = client.getResponse(url, gson.toJson(request, TwitterFeedRequest.class));
                return gson.fromJson(response, TwitterFeedResponse.class);
            } else if (request instanceof TwitterFeedLinkRequest) {
                url += HandlerPaths.TWITTER_FEED_LINKS_PATH;
                String response = client.getResponse(url, gson.toJson(request, TwitterFeedLinkRequest.class));
                return gson.fromJson(response, TwitterFeedLinkResponse.class);
            } else if (request instanceof ContentExtractionRequest) {
                url += HandlerPaths.CONTENT_EXTRACT_PATH;
                String response = client.getResponse(url, gson.toJson(request, ContentExtractionRequest.class));
                return gson.fromJson(response, ContentExtractionResponse.class);
            } else if (request instanceof LanguageDetectionRequest) {
                url += HandlerPaths.LANGUAGE_DETECTION_PATH;
                String response = client.getResponse(url, gson.toJson(request, LanguageDetectionRequest.class));
                return gson.fromJson(response, LanguageDetectionResponse.class);
            } else if (request instanceof SentenceExtractionRequest) {
                url += HandlerPaths.SENTENCE_EXTRACTION_PATH;
                String response = client.getResponse(url, gson.toJson(request, SentenceExtractionRequest.class));
                return gson.fromJson(response, SentenceExtractionResponse.class);
            } else if (request instanceof TokenExtractionRequest) {
                url += HandlerPaths.TOKEN_EXTRACTION_PATH;
                String response = client.getResponse(url, gson.toJson(request, TokenExtractionRequest.class));
                return gson.fromJson(response, TokenExtractionResponse.class);
            } else if (request instanceof LemmatizationRequest) {
                url += HandlerPaths.LEMMATIZATION_PATH;
                String response = client.getResponse(url, gson.toJson(request, LemmatizationRequest.class));
                return gson.fromJson(response, LemmatizationResponse.class);
            } else if (request instanceof POSTagRequest) {
                url += HandlerPaths.POS_PATH;
                String response = client.getResponse(url, gson.toJson(request, POSTagRequest.class));
                return gson.fromJson(response, POSTagResponse.class);
            } else if (request instanceof NamedEntityRequest) {
                url += HandlerPaths.NER_PATH;
                String response = client.getResponse(url, gson.toJson(request, NamedEntityRequest.class));
                return gson.fromJson(response, NamedEntityResponse.class);
            } else if (request instanceof TextSummarizationRequest) {
                url += HandlerPaths.TEXTRANK_SUMMARIZE;
                String response = client.getResponse(url, gson.toJson(request, TextSummarizationRequest.class));
                return gson.fromJson(response, TextSummarizationResponse.class);
            } else if (request instanceof KeywordRequest) {
                url += HandlerPaths.TEXTRANK_KEYWORDS;
                String response = client.getResponse(url, gson.toJson(request, KeywordRequest.class));
                return gson.fromJson(response, KeywordResponse.class);
            } else if (request instanceof TextStatisticsRequest) {
                url += HandlerPaths.TEXT_STATISTICS_PATH;
                String response = client.getResponse(url, gson.toJson(request, TextStatisticsRequest.class));
                return gson.fromJson(response, TextStatisticsResponse.class);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
