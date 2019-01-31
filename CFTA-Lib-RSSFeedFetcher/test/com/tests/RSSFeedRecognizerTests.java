package com.tests;

import com.cfta.rssfeed.parser.RSSFeedRecognizer;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Node;

import static com.tests.TestUtil.readTestResource;

public class RSSFeedRecognizerTests {

    @Test
    public void testTypeParser1() throws Exception {
        String s = readTestResource("rsstypeparser_1.rss");

        RSSFeedRecognizer feedRecognizer = new RSSFeedRecognizer();
        Node n = feedRecognizer.findFeedRootNode(s);
        Assert.assertTrue(feedRecognizer.recognizeFeedType(n) == RSSFeedRecognizer.RSSFeedType.eRSSFeed);

        s = readTestResource("rsstypeparser_2.rss");
        n = feedRecognizer.findFeedRootNode(s);
        Assert.assertTrue(feedRecognizer.recognizeFeedType(n) == RSSFeedRecognizer.RSSFeedType.eRSSFeed);

        s = readTestResource("rsstypeparser_3.rss");
        n = feedRecognizer.findFeedRootNode(s);
        Assert.assertTrue(feedRecognizer.recognizeFeedType(n) == RSSFeedRecognizer.RSSFeedType.eRSSFeed);

        s = readTestResource("rsstypeparser_4.rss");
        n = feedRecognizer.findFeedRootNode(s);
        Assert.assertTrue(feedRecognizer.recognizeFeedType(n) == RSSFeedRecognizer.RSSFeedType.eRSSFeed);
    }

    @Test
    public void testTypeParser2() throws Exception {
        String s = readTestResource("rssfeedparser_1.rss");

        RSSFeedRecognizer feedRecognizer = new RSSFeedRecognizer();
        Node n = feedRecognizer.findFeedRootNode(s);
        Assert.assertTrue(feedRecognizer.recognizeFeedType(n) == RSSFeedRecognizer.RSSFeedType.eRSSFeed);

        s = readTestResource("rssfeedparser_2.rss");
        n = feedRecognizer.findFeedRootNode(s);
        Assert.assertTrue(feedRecognizer.recognizeFeedType(n) == RSSFeedRecognizer.RSSFeedType.eRSSFeed);

        s = readTestResource("rssfeedparser_3.rss");
        n = feedRecognizer.findFeedRootNode(s);
        Assert.assertTrue(feedRecognizer.recognizeFeedType(n) == RSSFeedRecognizer.RSSFeedType.eRSSFeed);

        s = readTestResource("rssfeedparser_4.rss");
        n = feedRecognizer.findFeedRootNode(s);
        Assert.assertTrue(feedRecognizer.recognizeFeedType(n) == RSSFeedRecognizer.RSSFeedType.eRSSFeed);

        s = readTestResource("rssfeedparser_5.rss");
        n = feedRecognizer.findFeedRootNode(s);
        Assert.assertTrue(feedRecognizer.recognizeFeedType(n) == RSSFeedRecognizer.RSSFeedType.eRSSFeed);

        s = readTestResource("rssfeedparser_6.rss");
        n = feedRecognizer.findFeedRootNode(s);
        Assert.assertTrue(feedRecognizer.recognizeFeedType(n) == RSSFeedRecognizer.RSSFeedType.eRSSFeed);

        s = readTestResource("rssfeedparser_7.rss");
        n = feedRecognizer.findFeedRootNode(s);
        Assert.assertTrue(feedRecognizer.recognizeFeedType(n) == RSSFeedRecognizer.RSSFeedType.eRSSFeed);
    }
}
