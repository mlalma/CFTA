package com.cfta.rssfeed.xmlparser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import org.apache.commons.io.IOUtils;

public class XMLTreeParser implements XMLParserHandler {

    private XMLNode rootNode;
    private XMLNode currentNode;
    //private int depth = 0;
    
    private final XMLFeedParser parser = new XMLFeedParser();
    
    @Override
    public void startDocument() {
        rootNode = new XMLNode(XMLNode.TYPE_NODE, "doc", null, null, null);
        currentNode = rootNode;
        //depth = 0;
    }
 
    @Override
    public void endDocument() {
        if (currentNode != rootNode) {
            System.err.println("Potentially malformatted XML");
            throw new RuntimeException("Malformatted XML");
        }                
    }
 
    @Override
    public void startElement(final String nodeName, final HashMap<String, String> attributes) {
        XMLNode node = new XMLNode(XMLNode.TYPE_NODE, nodeName.trim().toLowerCase(), attributes, currentNode, null);
        currentNode.childNodes.add(node);
        currentNode = node;
        //for (int i = 0; i < depth * 2; i++) { System.out.print(" "); }
        //System.out.println("<" + nodeName + ">");
        //depth++;
    }
  
    @Override
    public void endElement(final String nodeName) {
        currentNode = currentNode.parent;        
        //depth--;        
        //for (int i = 0; i < depth * 2; i++) { System.out.print(" "); }
        //System.out.println("</" + nodeName + ">");
    }
  
    @Override
    public void text(final String text) {
        XMLNode node = new XMLNode(XMLNode.TYPE_TEXT, XMLNode.TEXT_NODE_NAME, null, currentNode, text.trim());
        currentNode.childNodes.add(node);               
            
        //for (int i = 0; i < depth * 2; i++) { System.out.print(" "); }
        //System.out.println(text);
    }
          
    public XMLNode createXMLTreeFromFile(String file) throws IOException, FileNotFoundException, XMLParserException {       
        try (FileReader fr = new FileReader(file)) {
            parser.parse(this, fr);
            return rootNode;
        }
    }
    
    public XMLNode createXMLTreeFromString(String str) throws XMLParserException, IOException {
        parser.parse(this, new BufferedReader(new InputStreamReader(IOUtils.toInputStream(str))));
        return rootNode;
    }
}