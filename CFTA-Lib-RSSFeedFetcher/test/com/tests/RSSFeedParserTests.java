package com.tests;

import com.cfta.cf.feeds.RSSFeedParser;
import com.cfta.cf.handlers.protocol.RSSFeedResponse;
import org.junit.Assert;
import org.junit.Test;

import static com.tests.TestUtil.readTestResource;

public class RSSFeedParserTests {

    @Test
    public void RSSFeedParser1Test() throws Exception {
        String s = readTestResource("rssfeedparser_1.rss");
        RSSFeedParser parser = new RSSFeedParser();

        RSSFeedResponse r = parser.parseFeedFromString(s);

        Assert.assertTrue(r.feedTitle.equalsIgnoreCase("CoinDesk"));
        Assert.assertTrue(r.description.equalsIgnoreCase("Leader in blockchain news."));
        Assert.assertTrue(r.rssItems.size() == 20);
        Assert.assertTrue(r.errorCode == 0);
    }
}
