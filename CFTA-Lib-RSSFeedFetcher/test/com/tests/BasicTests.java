package com.tests;

import com.cfta.rssfeed.data.RSSFeedFolder;
import com.cfta.rssfeed.opml.OPMLParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class BasicTests {

    private String readTestResource(String name) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(name).getFile());
        System.out.println(file.getAbsolutePath());

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
    public void opmlParserTests() throws Exception {
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
}
