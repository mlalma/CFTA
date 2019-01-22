// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.cf.feeds;

import org.jsoup.Jsoup;

// RSS feed cleaner to improve the feed parsing by encapsulating the key fields to CDATA-blocks
public class RSSFeedCleaner {

    // Default constructor
    public RSSFeedCleaner() {
    }

    // Cleans the feed string and cleans the feed from HTML encodings
    private String cleanFeedString(String s, String elementName) {
        int i = 0;
        String startTag = "<" + elementName + ">";
        String endTag = "</" + elementName + ">";

        String newStr = "";
        int oldEnd = 0;

        StringBuilder stringBuilder = new StringBuilder();

        while (s.indexOf(startTag, i) != -1) {
            int start = s.indexOf(startTag, i);
            int end = s.indexOf(endTag, start);

            String text = s.substring(start + startTag.length(), end);
            String tt = "<![CDATA[" + Jsoup.parse(text).text().trim() + "]]>";

            stringBuilder.append(s, oldEnd, start);
            oldEnd = end + endTag.length();

            stringBuilder.append(startTag).append(tt).append(endTag);

            i = s.indexOf(startTag, i) + startTag.length();
        }

        stringBuilder.append(s, oldEnd, s.length());
        return stringBuilder.toString();
    }

    // Tries to make feeds a bit more readable
    // Usually there are problems with added HTML encodings and "&", "<" and ">" chars inside certain feed attributes. Clean them!
    public String cleanFeedString(String s) {
        String newStr = cleanFeedString(s, "title");
        newStr = cleanFeedString(newStr, "description");
        newStr = cleanFeedString(newStr, "link");
        newStr = cleanFeedString(newStr, "category");
        return newStr;
    }
}
