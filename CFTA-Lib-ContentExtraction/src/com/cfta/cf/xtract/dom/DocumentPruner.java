// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.cf.xtract.dom;

// Fixes values with known HTML glitches
public class DocumentPruner {

    // Tries to do clear-fixing of nodes with wrong height due to float
    private void compareAndSetWidthHeight(HtmlNode n) {
        if (n.parent != null) {
            int parent_height = n.parent.height;
            int height = n.height;
            if (!n.isHidden() && height > parent_height) {
                n.parent.height = height;
            }
        }
    }

    // Does clear fix
    public void doClearFix(HtmlNode n) {
        if (n.docNode.getNodeType() == 1 && !n.nodeName.equalsIgnoreCase("br")) {
            for (int i = 0; i < n.childNodes.size(); i++) {
                doClearFix(n.childNodes.get(i));
                compareAndSetWidthHeight(n);
            }
        }
    }
}
