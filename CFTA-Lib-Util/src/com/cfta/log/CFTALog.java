// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.log;

import com.cfta.cf.util.CFTASettings;

// Logger utility class
public class CFTALog {

    // Stands for "log a line"
    static public void LL(String output) {
        if (CFTASettings.getDebug()) {
            System.out.println(output);
        }
    }
}
