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
        Assert.assertTrue(r.rssItems.get(1).categories.contains("Business News"));
        Assert.assertTrue(r.rssItems.get(1).categories.contains("News"));
        Assert.assertTrue(r.rssItems.get(1).categories.contains("Prime Trust"));
        Assert.assertTrue(r.rssItems.get(1).categories.contains("custody"));
        Assert.assertTrue(r.rssItems.get(1).categories.contains("fees"));
        Assert.assertTrue(r.rssItems.get(0).description.equalsIgnoreCase("Cboe has re-filed the VanEck/SolidX bitcoin ETF proposal, which it previously withdrew due to the U.S. government shutdown."));
        Assert.assertTrue(r.rssItems.get(0).title.equalsIgnoreCase("Cboe Resubmits the VanEck/SolidX Bitcoin ETF Proposal for SEC Approval"));
        Assert.assertTrue(r.rssItems.get(19).links.get(0).equalsIgnoreCase("https://www.coindesk.com/bitcoin-eyes-minor-price-bounce-but-bear-trend-intact"));
        Assert.assertTrue(r.rssItems.get(18).links.get(0).equalsIgnoreCase("https://www.coindesk.com/crypto-exchange-gemini-passes-security-compliance-audit-conducted-by-deloitte"));
    }
}
