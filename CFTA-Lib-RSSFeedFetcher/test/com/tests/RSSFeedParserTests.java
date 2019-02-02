package com.tests;

import com.cfta.cf.feeds.RSSFeedParser;
import com.cfta.cf.handlers.protocol.RSSFeedResponse;
import org.junit.Assert;
import org.junit.Test;

import static com.tests.TestUtil.readTestResource;
import static com.tests.TestUtil.getTestResourceFile;

public class RSSFeedParserTests {

    @Test
    public void RSSFeedParser1Test() throws Exception {
        String s = readTestResource("rssfeedparser_1.rss");
        RSSFeedParser parser = new RSSFeedParser();

        RSSFeedResponse r = parser.parseFeedFromString(s);

        Assert.assertTrue(r.feedTitle.equals("CoinDesk"));
        Assert.assertTrue(r.description.equals("Leader in blockchain news."));
        Assert.assertTrue(r.rssItems.size() == 20);
        Assert.assertTrue(r.errorCode == 0);

        Assert.assertTrue(r.rssItems.get(1).categories.contains("Business News"));
        Assert.assertTrue(r.rssItems.get(1).categories.contains("News"));
        Assert.assertTrue(r.rssItems.get(1).categories.contains("Prime Trust"));
        Assert.assertTrue(r.rssItems.get(1).categories.contains("custody"));
        Assert.assertTrue(r.rssItems.get(1).categories.contains("fees"));
        Assert.assertTrue(r.rssItems.get(0).description.equals("Cboe has re-filed the VanEck/SolidX bitcoin ETF proposal, which it previously withdrew due to the U.S. government shutdown."));
        Assert.assertTrue(r.rssItems.get(0).title.equals("Cboe Resubmits the VanEck/SolidX Bitcoin ETF Proposal for SEC Approval"));
        Assert.assertTrue(r.rssItems.get(19).links.get(0).equals("https://www.coindesk.com/bitcoin-eyes-minor-price-bounce-but-bear-trend-intact"));
        Assert.assertTrue(r.rssItems.get(18).links.get(0).equals("https://www.coindesk.com/crypto-exchange-gemini-passes-security-compliance-audit-conducted-by-deloitte"));
    }

    @Test
    public void RSSFeedParser2Test() throws Exception {
        String s = readTestResource("rssfeedparser_2.rss");
        RSSFeedParser parser = new RSSFeedParser();

        RSSFeedResponse r = parser.parseFeedFromString(s);

        Assert.assertTrue(r.feedTitle.equals("NYT > Home Page"));
        Assert.assertTrue(r.description.equals(""));
        Assert.assertTrue(r.rssItems.size() == 49);
        Assert.assertTrue(r.errorCode == 0);

        Assert.assertTrue(r.rssItems.get(0).title.equals("Ralph Northam Resists Calls to Resign as Virginia Governor Over Racist Yearbook Photo"));
        Assert.assertTrue(r.rssItems.get(1).title.equals("White House Memo: What Do You Learn About Trump in an 85-Minute Interview?"));
        Assert.assertTrue(r.rssItems.get(2).title.equals("Trump Sought a Loan During the 2016 Campaign. Deutsche Bank Said No."));
        Assert.assertTrue(r.rssItems.get(3).title.equals("White House Memo: In the Pale of Winter, Trump’s Tan Remains a State Secret"));
        Assert.assertTrue(r.rssItems.get(4).title.equals("Medicare for All Emerges as Early Policy Test for 2020 Democrats"));
        Assert.assertTrue(r.rssItems.get(5).title.equals("Japan’s Working Mothers: Record Responsibilities, Little Help from Dads"));
        Assert.assertTrue(r.rssItems.get(6).title.equals("Our Tokyo Bureau Chief on Where She Finds ‘Bolts of Insight’ (Hint: It’s Outside the Office)"));

        Assert.assertTrue(r.rssItems.get(5).links.get(0).equals("https://www.nytimes.com/2019/02/02/world/asia/japan-working-mothers.html?partner=rss&emc=rss"));
        Assert.assertTrue(r.rssItems.get(5).categories.contains("Working Hours"));
        Assert.assertTrue(r.rssItems.get(5).categories.contains("Women and Girls"));
        Assert.assertTrue(r.rssItems.get(5).categories.contains("Labor and Jobs"));
        Assert.assertTrue(r.rssItems.get(5).categories.contains("Japan"));

        Assert.assertTrue(r.rssItems.get(5).date.getTime() == 1549114803000L);
        Assert.assertTrue(r.rssItems.get(6).date.getTime() == 1549077795000L);
        Assert.assertTrue(r.rssItems.get(7).date.getTime() == 1549101609000L);
    }

    @Test
    public void RSSFeedParser3Test() throws Exception {
        RSSFeedParser parser = new RSSFeedParser();
        RSSFeedResponse r = parser.parseFeedFromFile(this.getClass().getClassLoader().getResource("rssfeedparser_3.rss").getFile());

        Assert.assertTrue(r.feedTitle.equals("cryptocurrencies – Bank Innovation"));
        Assert.assertTrue(r.description.equals("The Future of Fintech"));
        Assert.assertTrue(r.rssItems.size() == 25);
        Assert.assertTrue(r.errorCode == 0);

        Assert.assertTrue(r.rssItems.get(0).title.equals("Does it Matter if Cryptocurrencies Are Securities? (No)"));
        Assert.assertTrue(r.rssItems.get(0).description.equals("EXCLUSIVE— While the debate over whether cryptocurrencies are actually currencies, securities, or somehow a mix of the two drags on, there’s another question crypto enthusiasts should ask: does it matter? The question of whether to consider cryptos securities flared to life again last week, after former U.S. regulator Gary Gensler noted that in his mind, […]"));
        Assert.assertTrue(r.rssItems.get(1).title.equals("U.S. Banks Need to Upgrade Their Payments Infrastructure (But Not With Blockchain)"));
        Assert.assertTrue(r.rssItems.get(2).title.equals("Top 5 Hot Trends to Watch in 2018"));
        Assert.assertTrue(r.rssItems.get(3).title.equals("Japanese Cryptocurrency Exchange bitFlyer Launches in Europe"));
        Assert.assertTrue(r.rssItems.get(4).title.equals("Startup Ledger Raises $75M for Crypto Wallets"));

        Assert.assertTrue(r.rssItems.get(24).links.get(0).equals("https://bankinnovation.net/2017/10/ibm-to-use-blockchain-technology-for-crossborder-payments-for-banks/"));
        Assert.assertTrue(r.rssItems.get(24).categories.size() == 13);

    }
}
