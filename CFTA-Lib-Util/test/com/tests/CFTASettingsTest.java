package com.tests;

import com.cfta.cf.util.CFTASettings;
import org.junit.Assert;
import org.junit.Test;
import com.tests.TestUtil;

public class CFTASettingsTest {

  @Test
  public void testSettings() {
    String fileName = TestUtil.getTestResourceFile("test.properties").getAbsolutePath();

    CFTASettings.loadSettingsFile(fileName);

    Assert.assertTrue(CFTASettings.getHTTPConnectTimeout() == 100);
    Assert.assertTrue(CFTASettings.getHTTPTimeout() == 101);
    Assert.assertTrue(CFTASettings.getHTTPUserAgent().equals("TestUserAgent"));
    Assert.assertTrue(CFTASettings.getRSSFeedParseTimeout() == 102);
    Assert.assertTrue(CFTASettings.getDebug());
    Assert.assertTrue(CFTASettings.getTwitterOAuthConsumerKey().equals("OAuthCKey"));
    Assert.assertTrue(CFTASettings.getTwitterOAuthConsumerSecret().equals("OAuthConsumerSecret"));
    Assert.assertTrue(CFTASettings.getTwitterAccessSecret().equals("OAuthAccessSecret"));
    Assert.assertTrue(CFTASettings.getTwitterAccessToken().equals("OAuthAccessToken"));
    Assert.assertTrue(CFTASettings.getLangDetectProfileDir().equals("/lang/profile/dir"));
    Assert.assertTrue(CFTASettings.getStopwordListDir().equals("/stopword/list/dir"));
  }
}
