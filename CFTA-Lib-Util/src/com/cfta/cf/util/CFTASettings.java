// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.cf.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

// Utility settings for CFTA library
public class CFTASettings {

    static private Properties propertiesFile = null;

    private static final long DEFAULT_HTTP_CONNECT_TIMEOUT = 2500;
    private static final long DEFAULT_HTTP_TIMEOUT = 10 * 1000;
    private static final String DEFAULT_HTTP_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.69 Safari/537.36";
    private static final long DEFAULT_RSS_PARSING_TIMEOUT = 5 * 1000;
    private static final String DEFAULT_DEBUG = "true";

    // Set the proper language profile directory via settings file in order to be able to user language detection
    private static final String LANG_DETECT_PROF_DIRECTORY = "..\\CFTA-Lib-TextAnalysis\\res\\profiles";

    // Set the proper stopword list directory for different languages via settings file in order to be able to use stopword filtering
    private static final String STOPWORD_LIST_DIRECTORY = "";

    // Register your own Twitter app to get consumer & access key and secret
    private static final String OAUTH_CONSUMER_KEY = "";
    private static final String OAUTH_CONSUMER_SECRET = "";
    private static final String OAUTH_ACCESS_TOKEN = "";
    private static final String OAUTH_ACCESS_SECRET = "";

    // Private constructor, this class should never be instantiated
    private CFTASettings() {
    }

    // Returns value from configuration file
    static private String getVal(String valueName) {
        if (propertiesFile == null) {
            return null;
        }

        return propertiesFile.getProperty(valueName).trim();
    }

    // Returns value formatted as long number
    static private long returnLong(String paramName, long defaultValue) {
        try {
            String s;
            if ((s = getVal(paramName)) != null) {
                return Long.parseLong(s);
            } else {
                return defaultValue;
            }
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    // Returns value formatted as a string
    static private String returnString(String paramName, String defaultValue) {
        try {
            String s;
            if ((s = getVal(paramName)) != null) {
                return s;
            } else {
                return defaultValue;
            }
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    // Returns value formatted as a boolean
    static private boolean returnBoolean(String paramName, String defaultValue) {
        try {
            String s;
            if ((s = getVal(paramName)) != null) {
                return s.equalsIgnoreCase("true");
            } else {
                return defaultValue.equalsIgnoreCase("true");
            }
        } catch (Exception ex) {
            return defaultValue.equalsIgnoreCase("true");
        }
    }

    // Loads settings file -- call this at the application start-up
    static public void loadSettingsFile(String fileName) {
        try {
            propertiesFile = new Properties();
            propertiesFile.load(new FileInputStream(fileName));
        } catch (IOException ex) {
            propertiesFile = null;
            ex.printStackTrace();
        }
    }

    /* Getters to Return the various configuration values from settings file or from defaults */

    static public long getHTTPConnectTimeout() {
        return returnLong("httpConnectTimeout", DEFAULT_HTTP_CONNECT_TIMEOUT);
    }

    static public long getHTTPTimeout() {
        return returnLong("httpTimeout", DEFAULT_HTTP_TIMEOUT);
    }

    static public String getHTTPUserAgent() {
        return returnString("httpUserAgent", DEFAULT_HTTP_USER_AGENT);
    }

    static public long getRSSFeedParseTimeout() {
        return returnLong("rssFeedParseTimeout", DEFAULT_RSS_PARSING_TIMEOUT);
    }

    static public boolean getDebug() {
        return returnBoolean("debug", DEFAULT_DEBUG);
    }

    static public String getTwitterOAuthConsumerKey() {
        return returnString("twitterOauthConsumerKey", OAUTH_CONSUMER_KEY);
    }

    static public String getTwitterOAuthConsumerSecret() {
        return returnString("twitterOauthConsumerSecret", OAUTH_CONSUMER_SECRET);
    }

    static public String getTwitterAccessToken() {
        return returnString("twitterOauthAccessToken", OAUTH_ACCESS_TOKEN);
    }

    static public String getTwitterAccessSecret() {
        return returnString("twitterOauthAccessSecret", OAUTH_ACCESS_SECRET);
    }

    static public String getLangDetectProfileDir() {
        return returnString("langDetectProfileDir", LANG_DETECT_PROF_DIRECTORY);
    }

    static public String getStopwordListDir() {
        return returnString("stopwordListDir", STOPWORD_LIST_DIRECTORY);
    }
}
