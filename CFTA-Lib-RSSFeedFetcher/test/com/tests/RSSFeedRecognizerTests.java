package com.tests;

import com.cfta.rssfeed.parser.RSSFeedRecognizer;
import com.cfta.rssfeed.xmlparser.XMLNode;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
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
}
