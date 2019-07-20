// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.cf.twitter;

import com.cfta.cf.util.CFTASettings;
import org.jetbrains.annotations.NotNull;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

// Returns initialized twitter class instance
class TwitterResourceFactory {

  private static Twitter twitter = null;

  // Constructor
  private TwitterResourceFactory() {}

  // Returns twitter instance by checking from configuration settings
  static @NotNull Twitter getDefaultInstance() {
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

  // Returns Twitter4j instance based on the parameters
  static @NotNull Twitter getInstance(final String consumerKey,
      final String consumerSecret,
      final String accessToken,
      final String accessSecret) {
    if (twitter == null) {
      ConfigurationBuilder cb = new ConfigurationBuilder();
      cb.setOAuthConsumerKey(consumerKey)
          .setOAuthConsumerSecret(consumerSecret)
          .setOAuthAccessToken(accessToken)
          .setOAuthAccessTokenSecret(accessSecret);
      TwitterFactory tf = new TwitterFactory(cb.build());
      twitter = tf.getInstance();
    }
    return twitter;
  }
}
