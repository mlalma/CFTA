package com.cfta.rssfeed.data;

import java.util.ArrayList;
import java.util.List;

public class RSSFeedFolder {
    public final String title;
    
    public final List<RSSFeedFolder> folders;
    public final List<RSSFeed> feeds;
    
    // Constructor
    public RSSFeedFolder(String title) {
        this.title = title;
        folders = new ArrayList<>();
        feeds = new ArrayList<>();
    }
    
    public void print() {
        System.out.println("FOLDER: " + title);
        for (int i = 0; i < feeds.size(); i++) {
            System.out.println("FEED: " + feeds.get(i).title);
        }
        for (int i = 0;i < folders.size(); i++) {
           folders.get(i).print();
        }
    }
}
