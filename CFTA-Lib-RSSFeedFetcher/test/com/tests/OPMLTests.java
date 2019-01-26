package com.tests;

import com.cfta.rssfeed.data.RSSFeedFolder;
import com.cfta.rssfeed.opml.OPMLParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

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
    public void opmlParserTestCnet() throws Exception {
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
    public void opmlParserTestDigg() throws Exception {
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
    public void opmlParserTestGoogleReader() throws Exception {
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
    public void opmlParserNPTech() throws Exception {
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
}
