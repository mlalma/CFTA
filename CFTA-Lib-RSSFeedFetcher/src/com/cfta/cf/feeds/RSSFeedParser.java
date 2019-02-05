package com.cfta.cf.feeds;

import com.cfta.rssfeed.parser.AtomTypeFeedParser;
import com.cfta.rssfeed.parser.FeedBurnerRSSTypeFeedParser;
import com.cfta.rssfeed.parser.RSSFeedRecognizer;
import com.cfta.rssfeed.parser.RSSTypeFeedParser;
import com.cfta.rssfeed.parser.RSSFeedRecognizer.RSSFeedType;
import com.cfta.cf.handlers.protocol.RSSFeedResponse;
import com.cfta.cf.httpfetch.ApacheHttpClientFetcher;
import com.cfta.cf.util.CFTASettings;

import java.io.*;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Node;

// RSS feed parser main class, is not thread-safe
public class RSSFeedParser {

  private final RSSFeedRecognizer feedRecognizer = new RSSFeedRecognizer();
  private final RSSTypeFeedParser rssTypeFeedParser = new RSSTypeFeedParser();
  private final AtomTypeFeedParser atomTypeFeedParser = new AtomTypeFeedParser();
  private final FeedBurnerRSSTypeFeedParser feedBurnerTypeFeedParser =
      new FeedBurnerRSSTypeFeedParser();

  private final long TIMEOUT =
      2 * CFTASettings.getHTTPTimeout() + CFTASettings.getRSSFeedParseTimeout();

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

  // Parses the feed
  private RSSFeedResponse parse(@NotNull final String feed) {
    if (feed.length() > 0) {
      Node rootFeedNode = feedRecognizer.findFeedRootNode(feed);
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
  public RSSFeedResponse parseFeedFromFile(@NotNull final String file) {
    StringBuilder sb = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      String s;
      while ((s = br.readLine()) != null) {
        sb.append(s).append("\r\n");
      }
    } catch (FileNotFoundException fne) {
      fne.printStackTrace();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    return parse(sb.toString());
  }

  // Parses feed directly from URL
  public RSSFeedResponse parseFeedFromUrl(@NotNull final String url) throws InterruptedException {
    FeedParseThread fpt = new FeedParseThread(url);
    fpt.start();
    fpt.join(TIMEOUT);

    return parse(fpt.feed);
  }

  // Parses feed from given string
  public RSSFeedResponse parseFeedFromString(@NotNull final String feed) {
    return parse(feed);
  }
}
