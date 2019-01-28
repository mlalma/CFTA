package com.tests;

import com.cfta.rssfeed.data.RSSFeedFolder;
import com.cfta.rssfeed.opml.OPMLParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

// Unit tests for OPML functionality
public class OPMLTests {

    private String readTestResource(String name) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(name).getFile());

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String s;
            while ((s = br.readLine()) != null) {
                sb.append(s).append("\r\n");
            }
        } catch (FileNotFoundException fne) {
            fne.printStackTrace();
            Assert.assertTrue(false);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            Assert.assertTrue(false);
        }
        return sb.toString();
    }

    @Test
    public void testOpmlParserTestCnet() throws Exception {
        String s = readTestResource("opml_cnet100.opml");

        OPMLParser opmlParser = new OPMLParser();
        RSSFeedFolder feedFolder = opmlParser.parseOPMLDoc(s);

        Assert.assertTrue(feedFolder.folders.size() == 10);
        Assert.assertTrue(feedFolder.title.equalsIgnoreCase("CNET News.com Blog 100"));
        Assert.assertTrue(feedFolder.feeds.size() == 0);

        Assert.assertTrue(feedFolder.folders.get(0).title.equalsIgnoreCase("Cutting Edge"));
        Assert.assertTrue(feedFolder.folders.get(0).folders.size() == 0);
        Assert.assertTrue(feedFolder.folders.get(0).feeds.size() == 9);
        Assert.assertTrue(feedFolder.folders.get(0).feeds.get(0).xmlUrl.equalsIgnoreCase("http://www.scienceblog.com/cms/rss.xml"));

        Assert.assertTrue(feedFolder.folders.get(1).title.equalsIgnoreCase("Digital lifestyle"));
        Assert.assertTrue(feedFolder.folders.get(1).folders.size() == 0);
        Assert.assertTrue(feedFolder.folders.get(1).feeds.size() == 13);
        Assert.assertTrue(feedFolder.folders.get(1).feeds.get(0).xmlUrl.equalsIgnoreCase("http://www.engadget.com/rss.xml"));

        Assert.assertTrue(feedFolder.folders.get(2).title.equalsIgnoreCase("Law, politics"));
        Assert.assertTrue(feedFolder.folders.get(2).folders.size() == 0);
        Assert.assertTrue(feedFolder.folders.get(2).feeds.size() == 3);
        Assert.assertTrue(feedFolder.folders.get(2).feeds.get(0).xmlUrl.equalsIgnoreCase("http://www.lessig.org/blog/index.xml"));

        Assert.assertTrue(feedFolder.folders.get(3).title.equalsIgnoreCase("Mac nation"));
        Assert.assertTrue(feedFolder.folders.get(3).folders.size() == 0);
        Assert.assertTrue(feedFolder.folders.get(3).feeds.size() == 7);
        Assert.assertTrue(feedFolder.folders.get(3).feeds.get(0).xmlUrl.equalsIgnoreCase("http://applematters.com/index.php/section/rss_2.0/"));

        Assert.assertTrue(feedFolder.folders.get(4).title.equalsIgnoreCase("Open source"));
        Assert.assertTrue(feedFolder.folders.get(4).folders.size() == 0);
        Assert.assertTrue(feedFolder.folders.get(4).feeds.size() == 6);
        Assert.assertTrue(feedFolder.folders.get(4).feeds.get(0).xmlUrl.equalsIgnoreCase("http://jeremy.zawodny.com/blog/atom.xml"));

        Assert.assertTrue(feedFolder.folders.get(5).title.equalsIgnoreCase("Search, Media"));
        Assert.assertTrue(feedFolder.folders.get(5).folders.size() == 0);
        Assert.assertTrue(feedFolder.folders.get(5).feeds.size() == 15);
        Assert.assertTrue(feedFolder.folders.get(5).feeds.get(0).xmlUrl.equalsIgnoreCase("http://scobleizer.wordpress.com/feed/"));

        Assert.assertTrue(feedFolder.folders.get(6).title.equalsIgnoreCase("Security, Threats"));
        Assert.assertTrue(feedFolder.folders.get(6).folders.size() == 0);
        Assert.assertTrue(feedFolder.folders.get(6).feeds.size() == 8);
        Assert.assertTrue(feedFolder.folders.get(6).feeds.get(0).xmlUrl.equalsIgnoreCase("http://www.schneier.com/blog/index.rdf"));

        Assert.assertTrue(feedFolder.folders.get(7).title.equalsIgnoreCase("Software"));
        Assert.assertTrue(feedFolder.folders.get(7).folders.size() == 0);
        Assert.assertTrue(feedFolder.folders.get(7).feeds.size() == 15);
        Assert.assertTrue(feedFolder.folders.get(7).feeds.get(0).xmlUrl.equalsIgnoreCase("http://blogs.msdn.com/ie/Rss.aspx"));

        Assert.assertTrue(feedFolder.folders.get(8).title.equalsIgnoreCase("Tech business"));
        Assert.assertTrue(feedFolder.folders.get(8).folders.size() == 0);
        Assert.assertTrue(feedFolder.folders.get(8).feeds.size() == 17);
        Assert.assertTrue(feedFolder.folders.get(8).feeds.get(0).xmlUrl.equalsIgnoreCase("http://www.businessweek.com/the_thread/blogspotting/atom.xml"));

        Assert.assertTrue(feedFolder.folders.get(9).title.equalsIgnoreCase("Web culture"));
        Assert.assertTrue(feedFolder.folders.get(9).folders.size() == 0);
        Assert.assertTrue(feedFolder.folders.get(9).feeds.size() == 7);
        Assert.assertTrue(feedFolder.folders.get(9).feeds.get(0).xmlUrl.equalsIgnoreCase("http://www.kuro5hin.org/backend.rdf"));
    }

    @Test
    public void testOpmlParserTestDigg() throws Exception {
        String s = readTestResource("opml_digg.opml");

        OPMLParser opmlParser = new OPMLParser();
        RSSFeedFolder feedFolder = opmlParser.parseOPMLDoc(s);

        Assert.assertTrue(feedFolder.folders.size() == 8);
        Assert.assertTrue(feedFolder.title.equalsIgnoreCase("Digg"));
        Assert.assertTrue(feedFolder.feeds.size() == 8);

        Assert.assertTrue(feedFolder.folders.get(0).title.equalsIgnoreCase("Technology"));
        Assert.assertTrue(feedFolder.folders.get(0).folders.size() == 0);
        Assert.assertTrue(feedFolder.folders.get(0).feeds.size() == 12);
        Assert.assertTrue(feedFolder.folders.get(0).feeds.get(0).xmlUrl.equalsIgnoreCase("http://digg.com/rss/containertechnology.xml"));

        Assert.assertTrue(feedFolder.folders.get(1).title.equalsIgnoreCase("World & Business"));
        Assert.assertTrue(feedFolder.folders.get(1).folders.size() == 0);
        Assert.assertTrue(feedFolder.folders.get(1).feeds.size() == 6);
        Assert.assertTrue(feedFolder.folders.get(1).feeds.get(0).xmlUrl.equalsIgnoreCase("http://digg.com/rss/containerworld_business.xml"));

        Assert.assertTrue(feedFolder.folders.get(2).title.equalsIgnoreCase("Science"));
        Assert.assertTrue(feedFolder.folders.get(2).folders.size() == 0);
        Assert.assertTrue(feedFolder.folders.get(2).feeds.size() == 4);
        Assert.assertTrue(feedFolder.folders.get(2).feeds.get(0).xmlUrl.equalsIgnoreCase("http://digg.com/rss/containerscience.xml"));

        Assert.assertTrue(feedFolder.folders.get(3).title.equalsIgnoreCase("Gaming"));
        Assert.assertTrue(feedFolder.folders.get(3).folders.size() == 0);
        Assert.assertTrue(feedFolder.folders.get(3).feeds.size() == 7);
        Assert.assertTrue(feedFolder.folders.get(3).feeds.get(0).xmlUrl.equalsIgnoreCase("http://digg.com/rss/containergaming.xml"));

        Assert.assertTrue(feedFolder.folders.get(4).title.equalsIgnoreCase("Lifestyle"));
        Assert.assertTrue(feedFolder.folders.get(4).folders.size() == 0);
        Assert.assertTrue(feedFolder.folders.get(4).feeds.size() == 7);
        Assert.assertTrue(feedFolder.folders.get(4).feeds.get(0).xmlUrl.equalsIgnoreCase("http://digg.com/rss/containerlifestyle.xml"));

        Assert.assertTrue(feedFolder.folders.get(5).title.equalsIgnoreCase("Entertainment"));
        Assert.assertTrue(feedFolder.folders.get(5).folders.size() == 0);
        Assert.assertTrue(feedFolder.folders.get(5).feeds.size() == 6);
        Assert.assertTrue(feedFolder.folders.get(5).feeds.get(0).xmlUrl.equalsIgnoreCase("http://digg.com/rss/containerentertainment.xml"));

        Assert.assertTrue(feedFolder.folders.get(6).title.equalsIgnoreCase("Sports"));
        Assert.assertTrue(feedFolder.folders.get(6).folders.size() == 0);
        Assert.assertTrue(feedFolder.folders.get(6).feeds.size() == 11);
        Assert.assertTrue(feedFolder.folders.get(6).feeds.get(0).xmlUrl.equalsIgnoreCase("http://digg.com/rss/containersports.xml"));

        Assert.assertTrue(feedFolder.folders.get(7).title.equalsIgnoreCase("Offbeat"));
        Assert.assertTrue(feedFolder.folders.get(7).folders.size() == 0);
        Assert.assertTrue(feedFolder.folders.get(7).feeds.size() == 5);
        Assert.assertTrue(feedFolder.folders.get(7).feeds.get(0).xmlUrl.equalsIgnoreCase("http://digg.com/rss/containeroffbeat.xml"));

    }

    @Test
    public void testOpmlParserTestGoogleReader() throws Exception {
        String s = readTestResource("opml_google-reader.opml");

        OPMLParser opmlParser = new OPMLParser();
        RSSFeedFolder feedFolder = opmlParser.parseOPMLDoc(s);

        Assert.assertTrue(feedFolder.folders.size() == 16);
        Assert.assertTrue(feedFolder.title.equalsIgnoreCase("Jason subscriptions in Google Reader"));
        Assert.assertTrue(feedFolder.feeds.size() == 227);

        Assert.assertTrue(feedFolder.feeds.get(0).title.equalsIgnoreCase(".::output >> /dev/null::."));
        Assert.assertTrue(feedFolder.feeds.get(4).title.equalsIgnoreCase("Africa &#038; Middle East - International Herald Tribune"));
        Assert.assertTrue(feedFolder.feeds.get(20).title.equalsIgnoreCase("Blue Bus todo mundo lê"));
        Assert.assertTrue(feedFolder.feeds.get(225).title.equalsIgnoreCase("读书新闻-文化新闻"));
        Assert.assertTrue(feedFolder.feeds.get(226).title.equalsIgnoreCase("资讯 / 首页 - 挖新闻 - 17Tech.COM"));
    }

    @Test
    public void testOpmlParserNPTech() throws Exception {
        String s = readTestResource("opml_nptech.opml");

        OPMLParser opmlParser = new OPMLParser();
        RSSFeedFolder feedFolder = opmlParser.parseOPMLDoc(s);

        Assert.assertTrue(feedFolder.folders.size() == 8);
        Assert.assertTrue(feedFolder.title.equalsIgnoreCase("Bloglines Subscriptions"));
        Assert.assertTrue(feedFolder.feeds.size() == 0);

        Assert.assertTrue(feedFolder.folders.get(0).title.equalsIgnoreCase("Advocacy"));
        Assert.assertTrue(feedFolder.folders.get(0).folders.size() == 0);
        Assert.assertTrue(feedFolder.folders.get(0).feeds.size() == 3);
        Assert.assertTrue(feedFolder.folders.get(0).feeds.get(0).xmlUrl.equalsIgnoreCase("http://www.audioactivism.org/index.rdf"));

        Assert.assertTrue(feedFolder.folders.get(1).title.equalsIgnoreCase("Communications"));
        Assert.assertTrue(feedFolder.folders.get(1).folders.size() == 0);
        Assert.assertTrue(feedFolder.folders.get(1).feeds.size() == 2);
        Assert.assertTrue(feedFolder.folders.get(1).feeds.get(0).xmlUrl.equalsIgnoreCase("http://feeds.feedburner.com/commoncraft?q=index.rdf"));

        Assert.assertTrue(feedFolder.folders.get(2).title.equalsIgnoreCase("Fundraising"));
        Assert.assertTrue(feedFolder.folders.get(2).folders.size() == 0);
        Assert.assertTrue(feedFolder.folders.get(2).feeds.size() == 3);
        Assert.assertTrue(feedFolder.folders.get(2).feeds.get(0).xmlUrl.equalsIgnoreCase("http://betsysblog.blogspot.com/atom.xml"));

        Assert.assertTrue(feedFolder.folders.get(3).title.equalsIgnoreCase("general - to be sorted/deleted"));
        Assert.assertTrue(feedFolder.folders.get(3).folders.size() == 0);
        Assert.assertTrue(feedFolder.folders.get(3).feeds.size() == 1);
        Assert.assertTrue(feedFolder.folders.get(3).feeds.get(0).xmlUrl.equalsIgnoreCase("http://www.bloggercorps.org/index.rdf"));

        Assert.assertTrue(feedFolder.folders.get(4).title.equalsIgnoreCase("Internet/Web"));
        Assert.assertTrue(feedFolder.folders.get(4).folders.size() == 0);
        Assert.assertTrue(feedFolder.folders.get(4).feeds.size() == 0);

        Assert.assertTrue(feedFolder.folders.get(6).title.equalsIgnoreCase("Subsector Specific"));
        Assert.assertTrue(feedFolder.folders.get(6).folders.size() == 0);
        Assert.assertTrue(feedFolder.folders.get(6).feeds.size() == 7);
        Assert.assertTrue(feedFolder.folders.get(6).feeds.get(1).title.equalsIgnoreCase("Doug Varone & Dancers: <i>Deconstructing English</i>"));
        Assert.assertTrue(feedFolder.folders.get(6).feeds.get(0).xmlUrl.equalsIgnoreCase("http://www.artsjournal.com/artfulmanager/feed.xml"));
    }

    @Test
    public void testOpmlParserPodcast() throws Exception {
        String s = readTestResource("opml_podcast.opml");

        OPMLParser opmlParser = new OPMLParser();
        RSSFeedFolder feedFolder = opmlParser.parseOPMLDoc(s);

        Assert.assertTrue(feedFolder.folders.size() == 0);
        Assert.assertTrue(feedFolder.title.equalsIgnoreCase("Top Podcasts"));
        Assert.assertTrue(feedFolder.feeds.size() == 22);

        Assert.assertTrue(feedFolder.feeds.get(0).xmlUrl.equalsIgnoreCase("http://feeds.feedburner.com/web20Show"));
        Assert.assertTrue(feedFolder.feeds.get(17).xmlUrl.equalsIgnoreCase("http://www.theonion.com/content/feeds/radionews"));
        Assert.assertTrue(feedFolder.feeds.get(14).xmlUrl.equalsIgnoreCase("http://dawnanddrew.podshow.com/wp-rss2.php"));
        Assert.assertTrue(feedFolder.feeds.get(10).xmlUrl.equalsIgnoreCase("http://podcasts.engadget.com/rss.xml"));

        Assert.assertTrue(feedFolder.feeds.get(5).title.equalsIgnoreCase("Diggnation w/Kevin Rose & Alex Albrecht"));
        Assert.assertTrue(feedFolder.feeds.get(20).title.equalsIgnoreCase("On The Media from NPR/WNYC"));
    }

    @Test
    public void testOpmlParserScoble() throws Exception {
        String s = readTestResource("opml_scoble.opml");

        OPMLParser opmlParser = new OPMLParser();
        RSSFeedFolder feedFolder = opmlParser.parseOPMLDoc(s);

        Assert.assertTrue(feedFolder.folders.size() == 3);
        Assert.assertTrue(feedFolder.title.equalsIgnoreCase("Bloglines Subscriptions"));
        Assert.assertTrue(feedFolder.feeds.size() == 498);

        Assert.assertTrue(feedFolder.folders.get(0).feeds.size() == 0);
        Assert.assertTrue(feedFolder.folders.get(1).feeds.size() == 54);
        Assert.assertTrue(feedFolder.folders.get(2).feeds.size() == 0);
    }

    @Test
    public void testOpmlTop100() throws Exception {
        String s = readTestResource("opml_top100.opml");

        OPMLParser opmlParser = new OPMLParser();
        RSSFeedFolder feedFolder = opmlParser.parseOPMLDoc(s);

        Assert.assertTrue(feedFolder.folders.size() == 0);
        Assert.assertTrue(feedFolder.title.equalsIgnoreCase("Top 100 Feeds"));
        Assert.assertTrue(feedFolder.feeds.size() == 100);
    }

    @Test
    public void testOpmlVolume() throws Exception {
        String s = readTestResource("opml_volume.opml");

        OPMLParser opmlParser = new OPMLParser();
        RSSFeedFolder feedFolder = opmlParser.parseOPMLDoc(s);

        Assert.assertTrue(feedFolder.folders.size() == 0);
        Assert.assertTrue(feedFolder.title.equalsIgnoreCase("<unknown OPML feed>"));
        Assert.assertTrue(feedFolder.feeds.size() == 119);

        Assert.assertTrue(feedFolder.feeds.get(118).title.equalsIgnoreCase(""));
        Assert.assertTrue(feedFolder.feeds.get(118).xmlUrl.equalsIgnoreCase("http://feeds.mixx.com/MixxFun"));

        Assert.assertTrue(feedFolder.feeds.get(109).title.equalsIgnoreCase(""));
        Assert.assertTrue(feedFolder.feeds.get(109).xmlUrl.equalsIgnoreCase("http://feeds.mixx.com/MixxPopular"));
    }

    @Test
    public void testOpmlVolume20() throws Exception {
        String s = readTestResource("opml_volume_20.opml");

        OPMLParser opmlParser = new OPMLParser();
        RSSFeedFolder feedFolder = opmlParser.parseOPMLDoc(s);

        Assert.assertTrue(feedFolder.folders.size() == 0);
        Assert.assertTrue(feedFolder.title.equalsIgnoreCase("<unknown OPML feed>"));
        Assert.assertTrue(feedFolder.feeds.size() == 20);

        Assert.assertTrue(feedFolder.feeds.get(16).title.equalsIgnoreCase(""));
        Assert.assertTrue(feedFolder.feeds.get(16).xmlUrl.equalsIgnoreCase("http://feeds.mixx.com/MixxPopular"));

        Assert.assertTrue(feedFolder.feeds.get(19).title.equalsIgnoreCase(""));
        Assert.assertTrue(feedFolder.feeds.get(19).xmlUrl.equalsIgnoreCase("http://feeds.mixx.com/MixxEntertainment"));
    }

    @Test
    public void testOpmlWomen() throws Exception {
        String s = readTestResource("opml_women.opml");

        OPMLParser opmlParser = new OPMLParser();
        RSSFeedFolder feedFolder = opmlParser.parseOPMLDoc(s);

        Assert.assertTrue(feedFolder.folders.size() == 1);
        Assert.assertTrue(feedFolder.title.equalsIgnoreCase("FeedDemon Subscriptions"));
        Assert.assertTrue(feedFolder.feeds.size() == 0);

        Assert.assertTrue(feedFolder.folders.get(0).title.equalsIgnoreCase("[My Feeds]"));
        Assert.assertTrue(feedFolder.folders.get(0).folders.size() == 1);
        Assert.assertTrue(feedFolder.folders.get(0).folders.get(0).title.equalsIgnoreCase("misbehaving"));
        Assert.assertTrue(feedFolder.folders.get(0).folders.get(0).feeds.size() == 107);

        Assert.assertTrue(feedFolder.folders.get(0).folders.get(0).feeds.get(7).xmlUrl.equalsIgnoreCase("http://feeds.smallbiztrends.com/parsers/feedParser?feedId=220"));
        Assert.assertTrue(feedFolder.folders.get(0).folders.get(0).feeds.get(9).xmlUrl.equalsIgnoreCase("http://purselipsquarejaw.org/blogger_rss.xml"));
        Assert.assertTrue(feedFolder.folders.get(0).folders.get(0).feeds.get(106).htmlUrl.equalsIgnoreCase("http://windsormedia.blogs.com/"));


    }
}
