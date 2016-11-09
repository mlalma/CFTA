package com.cfta.rssfeed.xmlparser;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Stack;

public class XMLFeedParser {    
    private static final int TEXT = 1;
    private static final int ENTITY = 2;
    private static final int OPEN_TAG = 3;
    private static final int CLOSE_TAG = 4;
    private static final int START_TAG = 5;
    private static final int ATTRIBUTE_LVALUE = 6;
    private static final int ATTRIBUTE_EQUAL = 9;
    private static final int ATTRIBUTE_RVALUE = 10;
    private static final int QUOTE = 7;
    private static final int IN_TAG = 8;
    private static final int SINGLE_TAG = 12;
    private static final int COMMENT = 13;
    private static final int DONE = 11;
    private static final int DOCTYPE = 14;
    private static final int PRE = 15;
    private static final int CDATA = 16;
    
    public XMLFeedParser() {    
    }
        
    private int popMode(Stack<Integer> st) {
        if (!st.empty()) {
            return ((int)st.pop());
        } else {
            return PRE;
        }
    }
    
    private void parserException(String s,int line,int col) throws XMLParserException {
        throw new XMLParserException(s + " near line " + line + ", column " + col);
    }
    
    private void sendTextToDoc(XMLParserHandler doc, StringBuilder sb) {
        String finalString = sb.toString().replace("\n", " ").replace("\r", "").replace("\t", "").replaceAll("( )+", " ").trim();
        if (finalString.length() > 0) {                                
            doc.text(finalString);    
        }                    
    }
    
    public void parse(XMLParserHandler doc, Reader r) throws XMLParserException, IOException {
        Stack<Integer> st = new Stack<Integer>();        
        int mode = PRE;
        int c = 0;
        int quotec = '"';
        
        StringBuilder sb = new StringBuilder();
        StringBuilder etag = new StringBuilder();
        
        String tagName = null;
        String lvalue = null;
        String rvalue = null;        
        HashMap<String, String> attrs = null;
        
        st = new Stack<Integer>();
        doc.startDocument();
        
        int line = 1, col = 0;
        boolean eol = false;
        
        // Parsing loop
        while((c = r.read()) != -1) {
            if (c == '\n' && eol) {
                eol = false;
                continue;
            } else if (eol) {
                eol = false;
            } else if (c == '\n') {
                line++;
                col = 0;
            } else if (c == '\r') {
                eol = true;
                c = '\n';
                line++;
                col = 0;
            } else {
                col++;
            }
            
            if (mode == DONE) {
                doc.endDocument();
                return;
            } else if (mode == TEXT) {
                if (c == '<') {
                    st.push(mode);
                    mode = START_TAG;
                    if (sb.length() > 0) {
                        sendTextToDoc(doc, sb);
                        sb.setLength(0);
                    }
                } else if (c == '&') {
                    st.push(mode);
                    mode = ENTITY;
                    etag.setLength(0);
                } else {                    
                    sb.append((char)c);
                }
            } else if (mode == CLOSE_TAG) {
                if (c == '>') {
                    mode = popMode(st);
                    tagName = sb.toString();
                    sb.setLength(0);
                                   
                    doc.endElement(tagName);
                } else {
                    sb.append((char)c);
                }
            } else if (mode == CDATA) {
                if (c == '>' && sb.toString().endsWith("]]")) {
                    sb.setLength(sb.length() - 2);
                    doc.text(sb.toString());
                    sb.setLength(0);
                    mode = popMode(st);
                } else {
                    sb.append((char)c);
                }
            } else if (mode == COMMENT) {
                if(c == '>' && sb.toString().endsWith("--")) {
                    sb.setLength(0);
                    mode = popMode(st);
                } else {
                    sb.append((char)c);
                }
            } else if (mode == PRE) {
                if (c == '<') {
                    mode = TEXT;
                    st.push(mode);
                    mode = START_TAG;
                }
            } else if (mode == DOCTYPE) {
                if (c == '>') {
                    mode = popMode(st);
                    if (mode == TEXT) {
                        mode = PRE;
                    }
                }
            } else if (mode == START_TAG) {
                mode = popMode(st);
                if (c == '/') {
                    st.push(mode);
                    mode = CLOSE_TAG;
                } else if (c == '?') {
                    mode = DOCTYPE;
                } else {
                    st.push(mode);
                    mode = OPEN_TAG;
                    tagName = null;
                    attrs = new HashMap<>();
                    sb.append((char)c);
                }
            } else if(mode == ENTITY) {
                if (c == ';') {
                    mode = popMode(st);
                    String cent = etag.toString();
                                       
                    etag.setLength(0);
                    if (cent.equals("lt"))
                        sb.append('<');
                    else if (cent.equals("gt"))
                        sb.append('>');
                    else if (cent.equals("amp"))
                        sb.append('&');
                    else if (cent.equals("quot"))
                        sb.append('"');
                    else if (cent.equals("apos"))
                        sb.append('\'');                                        
                    else if (cent.equals("#xa"))
                        sb.append('\n');
                    else if (cent.equals("#xd"))
                        sb.append('\n');
                    else if (cent.equals("#x9"))
                        sb.append('\t');
                    else if (cent.equals("auml"))
                        sb.append("ä");
                    else if (cent.equals("Auml"))
                        sb.append("Ä");
                    else if (cent.equals("ouml"))
                        sb.append("ö");
                    else if (cent.equals("Ouml"))
                        sb.append("Ö");
                    else if (cent.equals("#8211"))
                        sb.append("-");
                    else if (cent.equals("#8230"))
                        sb.append("...");
                    else if (cent.equals("#8217"))
                        sb.append("'");
                    else if (cent.equals("#38"))
                        sb.append("&");
                    else if (cent.equals("#62"))
                        sb.append(">");
                    else if(cent.startsWith("#"))
                        try { sb.append((char)Integer.parseInt(cent.substring(1))); } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    // Insert custom entity definitions here
                    else
                        parserException("Unknown entity: &" + cent + ";", line, col);
                } else {
                    etag.append((char)c);
                }
            } else if (mode == SINGLE_TAG) {
                if (tagName == null) {
                    tagName = sb.toString();
                }
                
                if(c != '>') {
                    parserException("Expected > for tag: <" + tagName + "/>", line, col);
                }
                
                doc.startElement(tagName, attrs);
                doc.endElement(tagName);
                                
                sb.setLength(0);
                attrs = new HashMap<>();
                tagName = null;
                mode = popMode(st);
            } else if (mode == OPEN_TAG) {
                if (c == '>') {
                    if (tagName == null) {
                        tagName = sb.toString();
                    }            
                    sb.setLength(0);
                    doc.startElement(tagName, attrs);                    
                    mode = popMode(st);
                    if (tagName.toLowerCase().trim().equalsIgnoreCase("script")) {
                        mode = CDATA;
                    }
                    tagName = null;
                    attrs = new HashMap<>();
                } else if (c == '/') {
                    mode = SINGLE_TAG;
                } else if (c == '-' && sb.toString().equals("!-")) {
                    mode = COMMENT;
                } else if (c == '[' && sb.toString().equals("![CDATA")) {
                    mode = CDATA;
                    sb.setLength(0);
                } else if (c == 'E' && sb.toString().equals("!DOCTYP")) {
                    sb.setLength(0);
                    mode = DOCTYPE;
                } else if (Character.isWhitespace((char)c)) {
                    tagName = sb.toString();
                    sb.setLength(0);
                    mode = IN_TAG;
                } else {
                    sb.append((char)c);
                }
            } else if(mode == QUOTE) {
                if (c == quotec) {
                    rvalue = sb.toString();
                    sb.setLength(0);
                    attrs.put(lvalue, rvalue);
                    mode = IN_TAG;
                } else if(" \r\n\u0009".indexOf(c)>=0) {
                    sb.append(' ');
                } else if(c == '&') {
                    st.push(mode);
                    mode = ENTITY;
                    etag.setLength(0);
                } else {
                    sb.append((char)c);
                }
            } else if (mode == ATTRIBUTE_RVALUE) {
                if (c == '"' || c == '\'') {
                    quotec = c;
                    mode = QUOTE;
                } else if(Character.isWhitespace((char)c)) {
                    ;
                } else {
                    parserException("Error in attribute processing", line, col);
                }
            } else if (mode == ATTRIBUTE_LVALUE) {
                if(Character.isWhitespace((char)c)) {
                    lvalue = sb.toString();
                    sb.setLength(0);
                    mode = ATTRIBUTE_EQUAL;
                } else if (c == '=') {
                    lvalue = sb.toString();
                    sb.setLength(0);
                    mode = ATTRIBUTE_RVALUE;
                } else {
                    sb.append((char)c);
                }
            } else if(mode == ATTRIBUTE_EQUAL) {
                if(c == '=') {
                    mode = ATTRIBUTE_RVALUE;
                } else if(Character.isWhitespace((char)c)) {
                    ;
                } else {
                    parserException("Error in attribute processing", line, col);
                }
            } else if(mode == IN_TAG) {
                if (c == '>') {
                    mode = popMode(st);
                    doc.startElement(tagName, attrs);
                    tagName = null;
                    attrs = new HashMap<>();
                } else if (c == '/') {
                    mode = SINGLE_TAG;
                } else if(Character.isWhitespace((char)c)) {
                    ;
                } else {
                    mode = ATTRIBUTE_LVALUE;
                    sb.append((char)c);
                }
            }
        }
    
        if(mode == DONE || c == -1) {
            doc.endDocument();
        } else {
            parserException("Missing end tag", line, col);
        }
    }
}
