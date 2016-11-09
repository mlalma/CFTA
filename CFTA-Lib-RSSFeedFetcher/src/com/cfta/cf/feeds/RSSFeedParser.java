package com.cfta.cf.feeds;

import com.cfta.rssfeed.parser.AtomTypeFeedParser;
import com.cfta.rssfeed.parser.FeedBurnerRSSTypeFeedParser;
import com.cfta.rssfeed.parser.RSSFeedRecognizer;
import com.cfta.rssfeed.parser.RSSTypeFeedParser;
import com.cfta.rssfeed.parser.RSSFeedRecognizer.RSSFeedType;
import com.cfta.rssfeed.xmlparser.XMLNode;
import com.cfta.rssfeed.xmlparser.XMLParserException;
import com.cfta.cf.handlers.protocol.RSSFeedResponse;
import com.cfta.cf.httpfetch.ApacheHttpClientFetcher;
import com.cfta.cf.util.CFTASettings;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.pojava.datetime.DateTime;

// RSS feed parser main class, is not thread-safe
public class RSSFeedParser {    
        
    private final RSSFeedRecognizer feedRecognizer = new RSSFeedRecognizer();
    private final RSSTypeFeedParser rssTypeFeedParser = new RSSTypeFeedParser();
    private final AtomTypeFeedParser atomTypeFeedParser = new AtomTypeFeedParser();
    private final FeedBurnerRSSTypeFeedParser feedBurnerTypeFeedParser = new FeedBurnerRSSTypeFeedParser();
    
    private final long TIMEOUT = 2 * CFTASettings.getHTTPTimeout() + CFTASettings.getRSSFeedParseTimeout();
    
    // Feed parsing thread
    private class FeedParseThread extends Thread {
        private String url = "";
        
        private String feed = "";
        
        // Constructor for parse thread
        public FeedParseThread(String url) {
            this.url = url;
        }
        
        @Override
        // Fetches the feed and tries to parse it to container for later "proper" parsing
        public void run() {
            try {
                ApacheHttpClientFetcher fetcher = new ApacheHttpClientFetcher();                
                feed = fetcher.getWebPage(url);                           
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
                        
        if (CFTASettings.getDebug()) {
            System.err.println("Could not parse date: " + date);
        }
        
        return null;
    }
    
    // Parses the feed
    public RSSFeedResponse parse(String feed) throws XMLParserException, IOException {       
        if (feed.length() > 0) {                    
            XMLNode rootFeedNode = feedRecognizer.findFeedRootNode(feed);
            RSSFeedType feedType = feedRecognizer.recognizeFeedType(rootFeedNode);

            if (feedType == RSSFeedRecognizer.RSSFeedType.eRSSFeed) {
                return rssTypeFeedParser.parseFeed(rootFeedNode);
            } else if (feedType == RSSFeedRecognizer.RSSFeedType.eAtom) {
                return atomTypeFeedParser.parseFeed(rootFeedNode);
            } else if (feedType == RSSFeedRecognizer.RSSFeedType.eFeedBurnerRSSFeed) {
                return feedBurnerTypeFeedParser.parseFeed(rootFeedNode);
            } else {
                // Unknown feed type
            }                    
        }   
        
        return null;
    }

    // Parses feed from file, will fail for ATOM feeds
    public RSSFeedResponse parseFeedFromFile(String file) throws Exception {
    	BufferedReader bf = new BufferedReader(new FileReader(file));
    	
    	String feed = "";
    	String line;
    	while ((line = bf.readLine()) != null) {
    		feed = line + "\n";
    	}
    	
    	bf.close();
    	
        return parse(feed);
    }   

    // Parses feed directly from URL
    public RSSFeedResponse parseFeedFromUrl(String url) throws InterruptedException, XMLParserException, IOException  {        
        FeedParseThread fpt = new FeedParseThread(url);
        fpt.start();
        fpt.join(TIMEOUT);
                
        return parse(fpt.feed);
    }
    
    // Parses feed from given string
    public RSSFeedResponse parseFeedFromString(String feed) throws Exception {
    	return parse(feed);
    }    
}