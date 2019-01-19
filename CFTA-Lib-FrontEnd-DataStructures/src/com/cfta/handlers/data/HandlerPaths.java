// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.handlers.data;

// Defines handler paths
public class HandlerPaths {
    static public final String PING_PATH = "/cfta/ping";
    static public final String HEALTH_PATH = "/health";

    static public final String WEB_FETCH_PATH = "/cf/web_fetch";
    static public final String RSS_FEED_PATH = "/cf/rss_feed";
    static public final String TWITTER_FEED_PATH = "/cf/twitter_feed";
    static public final String TWITTER_FEED_LINKS_PATH = "/cf/twitter_feed_links";
    static public final String CONTENT_EXTRACT_PATH = "/cf/content_extract";

    static public final String LANGUAGE_DETECTION_PATH = "/ta/lang_detect";
    static public final String POS_PATH = "/ta/pos_tag";
    static public final String NER_PATH = "/ta/named_entity_extract";
    static public final String TEXTRANK_SUMMARIZE = "/ta/textrank_summarize";
    static public final String TEXTRANK_KEYWORDS = "/ta/textrank_find_keywords";
    static public final String TEXT_STATISTICS_PATH = "/ta/text_statistics";

    static public final String SENTENCE_EXTRACTION_PATH = "/tn/sentence_extract";
    static public final String TOKEN_EXTRACTION_PATH = "/tn/token_extract";
    static public final String LEMMATIZATION_PATH = "/tn/lemmatize";

    private HandlerPaths() {
    }
}
