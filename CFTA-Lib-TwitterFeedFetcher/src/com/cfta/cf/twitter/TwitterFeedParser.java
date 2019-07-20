// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.cf.twitter;

import com.cfta.cf.handlers.protocol.TwitterFeedLinkResponse;
import com.cfta.cf.handlers.protocol.TwitterFeedResponse;
import com.cfta.cf.httpfetch.NewResponseContentEncoding;
import com.cfta.cf.util.CFTASettings;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jetbrains.annotations.NotNull;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

// Parses twitter feeds
public class TwitterFeedParser {

  // Twitter4j instance
  private final Twitter twitter;

  // Constructor
  public TwitterFeedParser(@NotNull Twitter twitter) {
    if (twitter == null) {
      this.twitter = TwitterResourceFactory.getDefaultInstance();
    } else {
      this.twitter = twitter;
    }
  }

  // Initializes TwitterFeed item based on user data
  private @NotNull TwitterFeedResponse initializeFeed(final User u) {
    TwitterFeedResponse feed = new TwitterFeedResponse();
    feed.favouritesCount = u.getFavouritesCount();
    feed.followersCount = u.getFollowersCount();
    feed.friendsCount = u.getFriendsCount();
    feed.location = u.getLocation();
    feed.profileImageUrl = u.getProfileImageURL();
    feed.screenName = u.getScreenName();
    feed.userName = u.getName();
    feed.userId = u.getId();
    return feed;
  }

  // Initializes tweet
  private @NotNull TwitterFeedResponse.Tweet initializeTweet(final Status s,
      final TwitterFeedResponse feed) {
    TwitterFeedResponse.Tweet t = feed.newTweet();
    t.creationTime = s.getCreatedAt().getTime();
    t.favouriteCount = s.getFavoriteCount();
    t.retweetCount = s.getRetweetCount();
    t.isRetweet = s.isRetweet();
    t.id = s.getId();
    t.replyToId = s.getInReplyToStatusId();
    t.replyToUser = s.getInReplyToScreenName();
    t.text = s.getText();
    if (t.isRetweet) {
      t.retweetId = s.getRetweetedStatus().getId();
      t.retweetUserScreenName = s.getRetweetedStatus().getUser().getScreenName();
      t.text = s.getRetweetedStatus().getText();
    }
    return t;
  }

  // Returns latest maxNumOfTweets amount of tweets and that have been sent since sinceMessageId's
  // tweet was sent (or -1)
  public @NotNull TwitterFeedResponse getTwitterFeed(@NotNull final String userName,
      final int maxNumOfTweets,
      final long sinceMessageId) throws TwitterException {
    User u = twitter.showUser(userName);
    TwitterFeedResponse feed = initializeFeed(u);
    ResponseList<Status> tweets;
    if (sinceMessageId <= 0) {
      tweets = twitter.getUserTimeline(feed.userId, new Paging(1, maxNumOfTweets));
    } else {
      tweets = twitter.getUserTimeline(feed.userId, new Paging(1, maxNumOfTweets, sinceMessageId));
    }
    feed.limit = u.getRateLimitStatus().getLimit();
    feed.remaining = u.getRateLimitStatus().getRemaining();

    for (Status s : tweets) {
      TwitterFeedResponse.Tweet t = initializeTweet(s, feed);
      feed.tweets.add(t);
    }

    return feed;
  }

  // Convenience method
  public @NotNull TwitterFeedResponse getTwitterFeed(@NotNull final String userName,
      final int maxNumOfTweets)
      throws TwitterException {
    return getTwitterFeed(userName, maxNumOfTweets, -1);
  }

  // Gets all links from the text
  private @NotNull List<String> getAllLinks(final String text, final String urlStart) {
    List<String> links = new ArrayList<>();

    int i = 0;
    while (text.indexOf(urlStart, i) != -1) {
      int linkStart = text.indexOf(urlStart, i);
      int linkEnd = text.indexOf(" ", linkStart);
      if (linkEnd != -1) {
        String link = text.substring(linkStart, linkEnd);
        links.add(link);
      } else {
        String link = text.substring(linkStart);
        links.add(link);
      }
      i = text.indexOf(urlStart, i) + urlStart.length();
    }

    return links;
  }

  // Parses only links that can be found from Twitter feed
  public @NotNull TwitterFeedLinkResponse getTwitterFeedLinks(
      final String userName, final int maxNumOfTweets, final long sinceMessageId)
      throws TwitterException, IOException {
    TwitterFeedResponse feed = getTwitterFeed(userName, maxNumOfTweets, sinceMessageId);
    TwitterFeedLinkResponse links = new TwitterFeedLinkResponse();

    CloseableHttpClient httpclient =
        HttpClients.custom()
            .setUserAgent("CFTA-Twitter-Link-Expander")
            .addInterceptorFirst(new NewResponseContentEncoding())
            .build();
    HttpClientContext context = HttpClientContext.create();
    RequestConfig requestConfig =
        RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();

    for (TwitterFeedResponse.Tweet t : feed.tweets) {
      List<String> urls = getAllLinks(t.text, "http://");
      urls.addAll(getAllLinks(t.text, "https://"));

      for (String s : urls) {
        TwitterFeedLinkResponse.TweetLink l = links.newLink();
        String url = s;

        // Try to figure the expaneded link url
        HttpGet httpget = new HttpGet(url);
        httpget.setConfig(requestConfig);
        try {
          try (CloseableHttpResponse response = httpclient.execute(httpget, context)) {
            HttpHost target = context.getTargetHost();
            List<URI> redirectLocations = context.getRedirectLocations();
            URI location = URIUtils.resolve(httpget.getURI(), target, redirectLocations);
            url = location.toASCIIString();
          }
        } catch (Exception ex) {
          if (CFTASettings.getDebug()) {
            ex.printStackTrace();
          }
        }

        l.linkUrl = url;
        l.favouriteCount = t.favouriteCount;
        l.retweetCount = t.retweetCount;
        l.creationTime = t.creationTime;
        links.links.add(l);
      }
    }

    httpclient.close();

    return links;
  }

  // Convenience method
  public @NotNull TwitterFeedLinkResponse getTwitterFeedLinks(final String userName,
      final int maxNumOfTweets)
      throws TwitterException, IOException {
    return getTwitterFeedLinks(userName, maxNumOfTweets, -1);
  }
}
