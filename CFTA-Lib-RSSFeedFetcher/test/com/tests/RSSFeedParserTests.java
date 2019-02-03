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

    @Test
    public void RSSFeedParser4Test() throws Exception {
        RSSFeedParser parser = new RSSFeedParser();
        RSSFeedResponse r = parser.parseFeedFromFile(getClass().getClassLoader().getResource("rssfeedparser_4.rss").getFile());

        Assert.assertTrue(r.feedTitle.equals("Cointelegraph.com News"));
        Assert.assertTrue(r.description.equals("Cointelegraph covers fintech, blockchain and Bitcoin bringing you the latest news and analyses on the future of money."));
        Assert.assertTrue(r.rssItems.size() == 30);
        Assert.assertTrue(r.errorCode == 0);

        Assert.assertTrue(r.rssItems.get(0).title.equals("CEO of Telecoms Giant Swisscom’s Blockchain Unit Steps Down"));
        Assert.assertTrue(r.rssItems.get(0).description.equals("<img src=\"https://images.cointelegraph.com/images/528_aHR0cHM6Ly9zMy5jb2ludGVsZWdyYXBoLmNvbS9zdG9yYWdlL3VwbG9hZHMvdmlldy84MWNlOTQ1MmVhMjRiOTdhYmIxNTZkZTUwNmQyZjQyYS5qcGc=.jpg\"><p>The CEO of the blockchain unit of Swiss state-owned telecoms company Swisscom has abruptly stepped down from his position</p>"));
        Assert.assertTrue(r.rssItems.get(0).links.get(0).equals("https://cointelegraph.com/news/ceo-of-telecoms-giant-swisscoms-blockchain-unit-steps-down"));

        Assert.assertTrue(r.rssItems.get(1).title.equals("CBOE Re-Applies With US SEC to List Bitcoin Exchange-Traded Fund"));
        Assert.assertTrue(r.rssItems.get(1).description.equals("<img src=\"https://images.cointelegraph.com/images/528_aHR0cHM6Ly9zMy5jb2ludGVsZWdyYXBoLmNvbS9zdG9yYWdlL3VwbG9hZHMvdmlldy83Yzc2NzM5NDFkNWY0YWE4MjdhZTU3YzExMTE5MWU0NS5qcGc=.jpg\"><p>The Chicago Board Options Exchange’s has re-submitted an application with the United States Securities and Exchange Commission for a rule change to list a Bitcoin ETF</p>"));
        Assert.assertTrue(r.rssItems.get(1).links.get(0).equals("https://cointelegraph.com/news/cboe-re-applies-with-us-sec-to-list-bitcoin-exchange-traded-fund"));

        Assert.assertTrue(r.rssItems.get(2).title.equals("New York Financial Regulators Grant BitLicense to Bitcoin ATM Operator"));
        Assert.assertTrue(r.rssItems.get(2).description.equals("<img src=\"https://images.cointelegraph.com/images/528_aHR0cHM6Ly9zMy5jb2ludGVsZWdyYXBoLmNvbS9zdG9yYWdlL3VwbG9hZHMvdmlldy85MzI2MGMzN2FjN2U3MzI2NDAxNGIyMWVlYWQzYjc3Yi5qcGc=.jpg\"><p>Bitcoin ATM operator Cottonwood Vending LLC has been granted a virtual currency license by the New York State Department of Financial Services</p>"));
        Assert.assertTrue(r.rssItems.get(2).links.get(0).equals("https://cointelegraph.com/news/new-york-financial-regulators-grant-bitlicense-to-bitcoin-atm-operator"));
    }

    @Test
    public void RSSFeedParser5Test() throws Exception {
        RSSFeedParser parser = new RSSFeedParser();
        RSSFeedResponse r = parser.parseFeedFromFile(getClass().getClassLoader().getResource("rssfeedparser_5.rss").getFile());

        Assert.assertTrue(r.feedTitle.equals("ETHNews"));
        Assert.assertTrue(r.description.equals("ETHNews is the authority in news, prices, analysis and information on Ethereum and its decentralized blockchain platform and cryptocurrency"));
        Assert.assertTrue(r.rssItems.size() == 19);
        Assert.assertTrue(r.errorCode == 0);

        Assert.assertTrue(r.rssItems.get(16).title.equals("<h1>Bithumb Joins Forces With Other South Korean Crypto Exchanges Against Money Laundering</h1>"));
        Assert.assertTrue(r.rssItems.get(16).description.equals("<img src=\"https://cdn.ethnews.com/images/1024x512/South-Korean-Crypto-Exchanges-Join-Forces-To-Combat-Money-Laundering-And-Protect-Investors-01-28-2019.jpg\" alt=\"bithumb-joins-forces-with-other-south-korean-crypto-exchanges-against-money-laundering\" ><p>South Korean crypto exchanges set up a “hotline” to prevent money laundering.</p>"));
        Assert.assertTrue(r.rssItems.get(16).links.get(0).equals("https://www.ethnews.com/bithumb-joins-forces-with-other-south-korean-crypto-exchanges-against-money-laundering"));

        Assert.assertTrue(r.rssItems.get(17).title.equals("<h1>BitTorrent Token Sells Out On Binance In 15 Minutes</h1>"));
        Assert.assertTrue(r.rssItems.get(17).description.equals("<img src=\"https://cdn.ethnews.com/images/1024x512/BitTorrent-Token-Sells-Out-In-18-Minutes-01-28-2019.jpg\" alt=\"bittorrent-token-sells-out-on-binance-in-15-minutes\" ><p>Six percent of BTT, the amount allocated for the public sale, was snatched up for around $7.2 million, says Binance.</p>"));
        Assert.assertTrue(r.rssItems.get(17).links.get(0).equals("https://www.ethnews.com/bittorrent-token-sells-outs-on-binance-in-15-minutes"));

        Assert.assertTrue(r.rssItems.get(18).title.equals("<h1>GörliCon 2k19: It’s Dappening In Berlin</h1>"));
        Assert.assertTrue(r.rssItems.get(18).description.equals("<img src=\"https://cdn.ethnews.com/images/1024x512/GorliCon-2k19-Its-Dappening-In-Berlin-01-28-2019.jpg\" alt=\"goerlicon-2k19-its-dappening-in-berlin\" ><p>Why does the Görli testnet need its own conference? Because it’s really about uniting the Ethereum open-source community.</p>"));
        Assert.assertTrue(r.rssItems.get(18).links.get(0).equals("https://www.ethnews.com/goerlicon-2k19-its-dappening-in-berlin"));

    }

    @Test
    public void RSSFeedParser6Test() throws Exception {
        RSSFeedParser parser = new RSSFeedParser();
        RSSFeedResponse r = parser.parseFeedFromFile(getClass().getClassLoader().getResource("rssfeedparser_6.rss").getFile());

        // This tests that module can parse files with content (web pages) encoded to it
        Assert.assertTrue(r.feedTitle.equals("Crypto-News.net"));
        Assert.assertTrue(r.description.equals("News from the world of cryptocurrencies"));
        Assert.assertTrue(r.rssItems.size() == 10);
        Assert.assertTrue(r.errorCode == 0);
    }

    @Test
    public void RSSFeedParser7Test() throws Exception {
        RSSFeedParser parser = new RSSFeedParser();
        RSSFeedResponse r = parser.parseFeedFromFile(getClass().getClassLoader().getResource("rssfeedparser_7.rss").getFile());

        // This tests that module can parse files with content (web pages) encoded to it
        Assert.assertTrue(r.feedTitle.equals("Crypto Briefing"));
        Assert.assertTrue(r.description.equals("Cryptocurrency News, Digital Asset Analysis, & Blockchain Updates"));
        Assert.assertTrue(r.rssItems.size() == 10);
        Assert.assertTrue(r.errorCode == 0);
    }
}
