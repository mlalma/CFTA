package com.cfta.rssfeed.util;

import com.cfta.cf.util.CFTASettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pojava.datetime.DateTime;
import org.w3c.dom.Node;

import java.text.SimpleDateFormat;
import java.util.*;

// Common utilities used to find / parse node trees
public class NodeTools {

  public static final String EMPTY_STRING = "";

  // Returns attribute value or empty string if none
  public static String getAttributeValueOrEmpty(@NotNull final Node node, @NotNull final String attrName) {
    if (node.getAttributes() != null) {
      for (int i = 0; i < node.getAttributes().getLength(); i++) {
        if (node.getAttributes().item(i).getNodeName().trim().equalsIgnoreCase(attrName.trim())) {
          return node.getAttributes().item(i).getNodeValue();
        }
      }
    }
    return EMPTY_STRING;
  }

  // Returns first child node with given name
  @Nullable
  public static Node childNode(@NotNull final Node n, @NotNull final String nodeName) {
    for (int i = 0; i < n.getChildNodes().getLength(); i++) {
      Node child = n.getChildNodes().item(i);
      if (child.getNodeName() != null && child.getNodeName().trim().equalsIgnoreCase(nodeName)) {
        return child;
      }
    }
    return null;
  }

  // Returns first child with given name from the tree
  @Nullable
  public static Node childNodeFromTree(@NotNull final Node n, @NotNull final String nodeName) {
    LinkedList<Node> nodeQueue = new LinkedList<>();
    for (int i = 0; i < n.getChildNodes().getLength(); i++) {
      nodeQueue.add(n.getChildNodes().item(i));
    }

    while (!nodeQueue.isEmpty()) {
      Node candidateNode = nodeQueue.pop();
      if (candidateNode.getNodeName() != null
          && candidateNode.getNodeName().trim().equalsIgnoreCase(nodeName)) {
        return candidateNode;
      }

      for (int i = 0; i < candidateNode.getChildNodes().getLength(); i++) {
        nodeQueue.add(candidateNode.getChildNodes().item(i));
      }
    }
    return null;
  }

  // Returns all child node with given name
  public static List<Node> childNodes(@NotNull final Node n, @NotNull final String nodeName) {
    ArrayList<Node> childList = new ArrayList<>();
    for (int i = 0; i < n.getChildNodes().getLength(); i++) {
      Node child = n.getChildNodes().item(i);
      if (child.getNodeName() != null && child.getNodeName().trim().equalsIgnoreCase(nodeName)) {
        childList.add(child);
      }
    }
    return childList;
  }

  // Date formats themselves
  private static final String[] dateFormatStrings = {
    "EEE, dd MMM yyyy HH:mm:ss zzz",
    "dd MMM yyyy HH:mm:ss",
    "yyyy-MM-dd'T'HH:mm:ss",
    "EEE, dd MMM yyyy HH:mm:ss zzz",
    "dd MMM yyyy HH:mm:ss zzz",
    "dd.MM.yyyy HH:mm",
    "EEE, dd MMM yyyy HH:mm zzz",
    "EEE, dd MMM yyyy HH:mm zzz",
    "yyyy-MM-dd HH:mm:ss.S",
    "EEE, dd MMM yyyy HH:mm:ss zzz",
    "dd.MM.yyyy",
    "yyyy-MM-dd HH:mm:ss",
    "EEE, dd MMM yyyy HH:mm:ss",
    "MMM dd, yyyy HH:mm:ss a",
    "dd MMM yyyy HH:mm:ss",
    "EEE, dd MMM yyyy HH:mm:ss",
  };

  // Parses the date
  @Nullable
  public static Date parseDate(@NotNull String date, @NotNull Locale locale) {
    // If feed defines locale, try also using it
    if (locale != null) {
      for (int i = 0; i < dateFormatStrings.length; i++) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormatStrings[i], locale);
        try {
          return new Date(format.parse(date).getTime());
        } catch (Exception ex) {
        }
      }
    }

    // Otherwise try running date through English locale
    for (int i = 0; i < dateFormatStrings.length; i++) {
      SimpleDateFormat format = new SimpleDateFormat(dateFormatStrings[i], Locale.ENGLISH);
      try {
        return new Date(format.parse(date).getTime());
      } catch (Exception ex) {
      }
    }

    // As a last-ditch resort, try using DateTime POJava class for parsing
    try {
      return DateTime.parse(date).toDate();
    } catch (Exception ex) {
    }

    if (CFTASettings.getDebug()) {
      System.err.println("Could not parse date: " + date);
    }

    return null;
  }

  // Private constructor, this class can never be instantiated
  private NodeTools() {}
}
