package com.tests;

import com.cfta.cf.feeds.RSSFeedFinder;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static com.tests.TestUtil.readTestResource;

import com.cfta.cf.feeds.RSSFeedFinder.RSSFeedSource;

public class RSSFeedFinderTest {

    @Test
    public void RSSFeedFinderFound1Test() throws Exception {
        String s = readTestResource("rssfeedfinder_1.html");
        RSSFeedFinder finder = new RSSFeedFinder();
        List<RSSFeedSource> sources = finder.parseHtmlPageForSources(s);

        Assert.assertTrue(sources.size() == 1);
        Assert.assertTrue(sources.get(0).name.equalsIgnoreCase("Engadget"));
        Assert.assertTrue(sources.get(0).sourceUrl.equalsIgnoreCase("//www.engadget.com/rss.xml"));
    }

    @Test
    public void RSSFeedFinderFound2Test() throws Exception {
        String s = readTestResource("rssfeedfinder_2.html");
        RSSFeedFinder finder = new RSSFeedFinder();
        List<RSSFeedSource> sources = finder.parseHtmlPageForSources(s);

        Assert.assertTrue(sources.size() == 1);
        System.out.println(sources.get(0).name);
        Assert.assertTrue(sources.get(0).name.equalsIgnoreCase("RSS"));
        Assert.assertTrue(sources.get(0).sourceUrl.equalsIgnoreCase("https://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml"));
    }

    @Test
    public void RSSFeedFinderNotFound1Test() throws Exception {
        String s = readTestResource("rssfeedfinder_3.html");
        RSSFeedFinder finder = new RSSFeedFinder();
        List<RSSFeedSource> sources = finder.parseHtmlPageForSources(s);

        Assert.assertTrue(sources.size() == 0);
    }

    @Test
    public void RSSFeedFinderNotFound2Test() throws Exception {
        String s = readTestResource("rssfeedfinder_4.html");
        RSSFeedFinder finder = new RSSFeedFinder();
        List<RSSFeedSource> sources = finder.parseHtmlPageForSources(s);

        Assert.assertTrue(sources.size() == 0);
    }

    @Test
    public void RSSFeedFinderFetchTest1() throws Exception {
        RSSFeedFinder finder = new RSSFeedFinder();
        List<RSSFeedSource> sources = finder.findRSSSources("https://www.google.com/");

        Assert.assertTrue(sources.size() == 0);
    }

    @Test
    public void RSSFeedFinderFetchTest2() throws Exception {
        RSSFeedFinder finder = new RSSFeedFinder();
        List<RSSFeedSource> sources = finder.findRSSSources("https://www.engadget.com/");

        Assert.assertTrue(sources.size() == 1);
        Assert.assertTrue(sources.get(0).name.equalsIgnoreCase("Engadget"));
        Assert.assertTrue(sources.get(0).sourceUrl.equalsIgnoreCase("//www.engadget.com/rss.xml"));
    }
}
