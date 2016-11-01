// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2013
package com.cfta.cf.twitter;

import com.cfta.cf.util.CFTASettings;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

// Returns initialized twitter class instance
class TwitterResourceFactory {
   
    private static Twitter twitter = null;

    private TwitterResourceFactory() {}
    
    // Returns twitter instance
    static Twitter getInstance() {
        if (twitter == null) {
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setOAuthConsumerKey(CFTASettings.getTwitterOAuthConsumerKey())
              .setOAuthConsumerSecret(CFTASettings.getTwitterOAuthConsumerSecret())
              .setOAuthAccessToken(CFTASettings.getTwitterAccessToken())
              .setOAuthAccessTokenSecret(CFTASettings.getTwitterAccessSecret());
            TwitterFactory tf = new TwitterFactory(cb.build());
            twitter = tf.getInstance();     
        }
        return twitter;
    }       
}
