package com.cfta.log;

import com.cfta.cf.util.CFTASettings;

// Logger utility class, should move to use only standard Apache logging
@Deprecated public class CFTALog {

  // Stands for "log a line"
  public static void LL(String output) {
    if (CFTASettings.getDebug()) {
      System.out.println(output);
    }
  }
}
