// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2013
package com.cfta.cf.feeds;

import com.cfta.cf.handlers.protocol.RSSFeedResponse;
import com.cfta.cf.httpfetch.ApacheHttpClientFetcher;
import com.cfta.cf.util.CFTASettings;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.pojava.datetime.DateTime;
import yarfraw.core.datamodel.CategorySubject;
import yarfraw.core.datamodel.ChannelFeed;
import yarfraw.core.datamodel.FeedFormat;
import yarfraw.core.datamodel.ItemEntry;
import yarfraw.core.datamodel.Link;
import yarfraw.io.FeedReader;
import yarfraw.utils.FeedFormatDetector;

// RSS feed parser main class, is not thread-safe
public class RSSFeedParser {    
    private FeedReader reader;
    private ChannelFeed cf;
    
    private final long TIMEOUT = 2 * CFTASettings.getHTTPTimeout() + CFTASettings.getRSSFeedParseTimeout();
    
    // Feed parsing thread
    private class FeedParseThread extends Thread {
        private String url = "";
        
        // Constructor for parse thread
        public FeedParseThread(String url) {
            this.url = url;
        }
        
        @Override
        // Fetches the feed and tries to parse it to container for later "proper" parsing
        public void run() {
            try {
                boolean ok = false;
                String testFetch;
                ApacheHttpClientFetcher fetcher = new ApacheHttpClientFetcher();
                testFetch = fetcher.getWebPage(url);
                if (testFetch.length() > 0) {                    
                    FeedFormat fformat = FeedFormatDetector.getFormat(IOUtils.toInputStream(testFetch), false);
                    if (fformat != FeedFormat.UNKNOWN) {
                        ok = true;
                    }
                }

                if (ok) {
                    reader = new FeedReader(url, false);
                    cf = reader.readChannel();
                }
            } catch (Exception ex) {
                if (CFTASettings.getDebug()) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    // Date formats themselves
    static final private String[] dateFormatStrings = {
        "EEE, dd MMM yyyy HH:mm:ss zzz",
        "dd MMM yyyy HH:mm:ss",
        "yyyy-MM-dd'T'HH:mm:ss",
        "EEE, dd MMM yyyy HH:mm:ss zzz",
        "dd MMM yyyy HH:mm:ss zzz",
        "dd.MM.yyyy HH:mm",
        "EEE, dd MMM yyyy HH:mm zzz",
        "EEE, dd MMM yyyy HH:mm zzz",
        "yyyy-MM-dd HH:mm:ss.S",
        "EEE, dd MMM yyyy HH:mm:ss zzz",
        "dd.MM.yyyy",
        "yyyy-MM-dd HH:mm:ss",
        "EEE, dd MMM yyyy HH:mm:ss",
        "MMM dd, yyyy HH:mm:ss a",
        "dd MMM yyyy HH:mm:ss",
        "EEE, dd MMM yyyy HH:mm:ss",
    };

    // Parses the date
    public Date parseDate(String date, Locale locale) {
        // If feed defines locale, try also using it
        if (locale != null) {
            for (int i = 0; i < dateFormatStrings.length; i++) {
                SimpleDateFormat format = new SimpleDateFormat(dateFormatStrings[i], locale);
                try {
                    Date d = new Date(format.parse(date).getTime());
                    return d;
                } catch (Exception ex) {                
                }
            }
        }
        
        // Otherwise try running date through English locale
        for (int i = 0; i < dateFormatStrings.length; i++) {
            SimpleDateFormat format = new SimpleDateFormat(dateFormatStrings[i], Locale.ENGLISH);
            try {
                Date d = new Date(format.parse(date).getTime());
                return d;
            } catch (Exception ex) {                
            }
        }
        
        // As a last-ditch resort, try using DateTime POJava class for parsing
        try {
            Date d = DateTime.parse(date).toDate();
            return d;
        } catch (Exception ex) {            
        }
        
        return null;
    }
    
    // Parses the feed
    public RSSFeedResponse parse() throws Exception {
        RSSFeedResponse f = new RSSFeedResponse();
                
        if (cf.getTitleText() != null) { f.feedTitle = StringEscapeUtils.unescapeHtml(cf.getTitleText().trim()); }
        if (cf.getLinks() != null && !cf.getLinks().isEmpty()) {
            for (Link l : cf.getLinks()) {
                f.links.add(l.getHref().trim());
            }
        }
        
        if (cf.getDescriptionOrSubtitleText() != null) { f.description = cf.getDescriptionOrSubtitleText(); }

        if (cf.getItems() != null && !cf.getItems().isEmpty()) {
            for (ItemEntry i : cf.getItems()) {
                RSSFeedResponse.RSSItem item = f.newItem();                               
                if (i.getTitleText() != null) { item.title = i.getTitleText().trim(); }
                
                if (i.getPubDate() != null) { item.date = parseDate(i.getPubDate().trim(), cf.getLangAsLocale()); }
                else if (i.getUpdatedDate() != null ) { item.date = parseDate(i.getUpdatedDate().trim(), cf.getLangAsLocale()); }

                if (i.getDescriptionOrSummaryText() != null) { item.description = i.getDescriptionOrSummaryText(); }
                if (i.getLinks() != null && !i.getLinks().isEmpty()) {
                    for (Link l : i.getLinks()) {
                        item.links.add(l.getHref().trim());
                    }
                }
                
                if (i.getCategorySubjects() != null && !i.getCategorySubjects().isEmpty()) {
                    for (CategorySubject c : i.getCategorySubjects()) {
                        item.categories.add(c.getCategoryOrSubjectOrTerm().trim());
                    }
                }
                
                f.rssItems.add(item);
            }
        }
        
        return f;
    }

    // Parses feed from file, will fail for ATOM feeds
    public RSSFeedResponse parseFeedFromFile(String file) throws Exception {
        reader = new FeedReader(new File(file));
        cf = reader.readChannel();
        return parse();
    }   

    // Parses feed directly from URL
    public RSSFeedResponse parseFeedFromUrl(String url) throws Exception {
        reader = null;
        cf = null;
        
        FeedParseThread fpt = new FeedParseThread(url);
        fpt.start();
        fpt.join(TIMEOUT);
        
        if (cf != null) {
            return parse();
        }
        return null;        
    }
    
    // Parses feed from given string
    public RSSFeedResponse parseFeedFromString(String feed) throws Exception {
        reader = new FeedReader(feed);
        cf = reader.readChannel();
        return parse();
    }    
}
