// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.textanalysis.textrank;

import com.cfta.ta.handlers.protocol.KeywordResponse;
import com.cfta.ta.handlers.protocol.POSTagResponse;
import com.cfta.ta.handlers.protocol.POSTagResponse.POSTag;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// TextRank algorithm (modified and adapted) implementation for finding keywords from text
public class TextRankKeywordFinder {

    private final int CO_OCCURENCE_WINDOW = 2;

    //private final double MULTIPART_KEYWORD_RANK_FACTOR = 0.375;
    //private final double MULTIPART_KEYWORD_ADD_FACTOR = 0.625;    
    private final double MULTIPART_KEYWORD_RANK_FACTOR = 0.5;
    private final double MULTIPART_KEYWORD_ADD_FACTOR = 0.5;

    private final int MAX_TAG_SIZE = 30;

    private final String POSSESSIVE_TREEBANK_TAG = "POS";
    private final String NEWLINE = "\n";
    private final String DETERMINER_TREEBANK_TAG = "DT";

    // Creates graph from POS-tagged words
    private TextRankGraph createGraph(List<POSTagResponse.POSTag> words) {
        TextRankGraph g = new TextRankGraph();

        int wordNum = 0;
        for (int i = 0; i < words.size(); i++) {
            POSTagResponse.POSTag t = words.get(i);
            if (t.tag.equalsIgnoreCase(POSSESSIVE_TREEBANK_TAG) && i > 0) {
                TextRankWordNode wordNode = (TextRankWordNode) g.graph.get(g.graph.size() - 1);
                wordNode.possessiveAdd.put(wordNode.posOnText, t.word.toLowerCase());
            } else if (t.word.equalsIgnoreCase(NEWLINE) && i > 0) {
                ((TextRankWordNode) g.graph.get(g.graph.size() - 1)).compoundEnd = true;
            } else if (t.tag.equalsIgnoreCase(DETERMINER_TREEBANK_TAG)) {
                continue;
            } else {
                TextRankWordNode wordNode = new TextRankWordNode();
                wordNode.posOnText = wordNum;
                wordNode.treeBankClass = t.tag;
                wordNode.word = t.word.toLowerCase();
                wordNode.lemma = t.lemma.toLowerCase();
                wordNode.possessiveAdd.put(wordNum, "");
                g.graph.add(wordNode);
                wordNum++;
            }
        }

        return g;
    }

    // Prints graph (debug)
    /*private void printGraph(TextRankGraph g) {
        for (int i = 0; i < g.graph.size(); i++) {
            TextRankWordNode w = (TextRankWordNode)g.graph.get(i);
            for (Integer j : w.allPositions) {
                System.out.print(j + "/");
            }
            System.out.print(w.word + "/" + w.lemma + "/" + w.treeBankClass + " ");
        }
    }*/

    // Filters out words that are not nouns or adjectives
    private void syntacticFilter(TextRankGraph g) {
        for (int i = 0; i < g.graph.size(); i++) {
            if (!((TextRankWordNode) g.graph.get(i)).isAdjective() && !((TextRankWordNode) g.graph.get(i)).isNoun()) {
                g.graph.remove(i);
                i--;
            }
        }
    }

    // Adds edges between words whose distance in text is within co-occurence window
    private void addEdges(TextRankGraph g, int coOccurenceWindow) {
        for (int i = 0; i < g.graph.size(); i++) {
            TextRankWordNode node = (TextRankWordNode) g.graph.get(i);
            for (int j = i + 1; j <= i + coOccurenceWindow; j++) {
                if (j < g.graph.size() - 1) {
                    TextRankWordNode node2 = (TextRankWordNode) g.graph.get(j);
                    if (node.posOnText + coOccurenceWindow >= node2.posOnText && !node.compoundEnd) {
                        if (!node.word.equalsIgnoreCase(node2.word)) {
                            node.setEdge(node2, 1.0);
                        }
                    }

                    if (node2.compoundEnd) {
                        break;
                    }
                }
            }
        }
    }

    // Combines words together (not using lemmas, doing direct one-to-one word comparison)
    private void combineWords(TextRankGraph g) {
        for (int i = 0; i < g.graph.size(); i++) {
            TextRankWordNode node = (TextRankWordNode) g.graph.get(i);
            for (int j = i + 1; j < g.graph.size(); j++) {
                TextRankWordNode node2 = (TextRankWordNode) g.graph.get(j);
                if (node.word.equalsIgnoreCase(node2.word) && node.treeBankClass.equalsIgnoreCase(node2.treeBankClass)) {
                    // Combine these two together
                    for (TextRankNode e : node2.edges.keySet()) {
                        node.setEdge(e, 1.0);
                        e.edges.remove(node2);
                    }
                    node.allPositions.add(node2.posOnText);
                    node.possessiveAdd.putAll(node2.possessiveAdd);
                    node2.edges.clear();
                }
            }
        }

        // Removing all "empty" nodes that are added to another nodes
        for (int i = 0; i < g.graph.size(); i++) {
            TextRankWordNode node = (TextRankWordNode) g.graph.get(i);
            if (node.edges.isEmpty()) {
                //System.out.println("Removing node: " + node.word + "/" + node.posOnText);
                g.graph.remove(i);
                i--;
            } else {
                node.allPositions.add(node.posOnText);
                //System.out.println("Node exists: " + node.word + "/" + node.posOnText);                
            }
        }
    }

    // Sorts keyword candidates based on their calculated TextRank value
    private void sortKeywordCandidates(TextRankGraph g) {
        g.graph.sort((o1, o2) -> {
            TextRankWordNode p1 = (TextRankWordNode) o1;
            TextRankWordNode p2 = (TextRankWordNode) o2;
            return -Double.compare(p1.rank, p2.rank);
        });
    }

    // Sets new multipart node with values from two nodes
    private void setupMultipartNode(TextRankWordNode newNode, TextRankWordNode node, TextRankWordNode node2, TextRankGraph g, int k, int l) {
        newNode.rank = (node.rank + node2.rank) * MULTIPART_KEYWORD_RANK_FACTOR;
        if (!node.multipart) {
            newNode.allPositions.add(node.allPositions.get(k));
        } else {
            newNode.allPositions.addAll(node.allPositions);
        }
        if (!node2.multipart) {
            newNode.allPositions.add(node2.allPositions.get(l));
        } else {
            newNode.allPositions.addAll(node2.allPositions);
        }
        newNode.multipart = true;
        newNode.setEdge(newNode, 1.0);

        /*Integer ii =*/
        node.allPositions.remove(k);
        /*ii =*/
        node2.allPositions.remove(l);

        if (node.allPositions.isEmpty() || node.multipart) {
            /*boolean ok =*/
            g.graph.remove(node);
        }
        if (node2.allPositions.isEmpty() || node2.multipart) {
            /*boolean ok =*/
            g.graph.remove(node2);
        }

        g.graph.add(newNode);
    }

    // Creates a single multipard keynode and jumps out or returns false if cannot anymore create new multipart keywords
    private boolean createMultipartKeywords(TextRankGraph g) {
        for (int i = 0; i < g.graph.size(); i++) {
            TextRankWordNode node = (TextRankWordNode) g.graph.get(i);
            for (int j = i + 1; j < g.graph.size(); j++) {
                TextRankWordNode node2 = (TextRankWordNode) g.graph.get(j);
                if (node != node2) {
                    for (int k = 0; k < node.allPositions.size(); k++) {
                        for (int l = 0; l < node2.allPositions.size(); l++) {
                            if ((node.allPositions.get(k) == node2.allPositions.get(l) - 1) && !node.compoundEnd) {
                                String wordPart1;
                                if (node.possessiveAdd.get(node.allPositions.get(k)) != null) {
                                    wordPart1 = node.word + node.possessiveAdd.get(node.allPositions.get(k));
                                } else {
                                    wordPart1 = node.word;
                                }

                                String wordPart2;
                                if (node2.possessiveAdd.get(node2.allPositions.get(l)) != null) {
                                    wordPart2 = node2.word + node2.possessiveAdd.get(node2.allPositions.get(l));
                                } else {
                                    wordPart2 = node2.word;
                                }

                                String part = wordPart1 + " " + wordPart2;
                                if (part.length() > MAX_TAG_SIZE) {
                                    node.compoundEnd = true;
                                    continue;
                                }
                                TextRankWordNode newNode = new TextRankWordNode();
                                newNode.word = part.trim();
                                newNode.posOnText = node.posOnText;
                                newNode.treeBankClass = node.treeBankClass + node2.treeBankClass;
                                if (node2.compoundEnd) {
                                    newNode.compoundEnd = true;
                                }
                                setupMultipartNode(newNode, node, node2, g, k, l);
                                return true;
                            } else if ((node2.allPositions.get(l) == node.allPositions.get(k) - 1) && !node2.compoundEnd) {
                                String wordPart1;
                                if (node.possessiveAdd.get(node.allPositions.get(k)) != null) {
                                    wordPart1 = node.word + node.possessiveAdd.get(node.allPositions.get(k));
                                } else {
                                    wordPart1 = node.word;
                                }

                                String wordPart2;
                                if (node2.possessiveAdd.get(node2.allPositions.get(l)) != null) {
                                    wordPart2 = node2.word + node2.possessiveAdd.get(node2.allPositions.get(l));
                                } else {
                                    wordPart2 = node2.word;
                                }

                                String part = wordPart2 + " " + wordPart1;
                                if (part.length() > MAX_TAG_SIZE) {
                                    node2.compoundEnd = true;
                                    continue;
                                }

                                TextRankWordNode newNode = new TextRankWordNode();
                                newNode.word = part.trim();
                                newNode.posOnText = node2.posOnText;
                                newNode.treeBankClass = node.treeBankClass + node2.treeBankClass;
                                if (node.compoundEnd) {
                                    newNode.compoundEnd = true;
                                }
                                setupMultipartNode(newNode, node, node2, g, k, l);
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    // Collapses duplicate multipart keywords to a single multipart keyword instance
    private boolean collapseDuplicates(TextRankGraph g) {
        for (int i = 0; i < g.graph.size(); i++) {
            TextRankWordNode node = (TextRankWordNode) g.graph.get(i);
            for (int j = i + 1; j < g.graph.size(); j++) {
                TextRankWordNode node2 = (TextRankWordNode) g.graph.get(j);
                if (node.word.equalsIgnoreCase(node2.word) && node.treeBankClass.equalsIgnoreCase(node2.treeBankClass)) {
                    // Combine these two together
                    node.rank = (node.rank + node2.rank) * MULTIPART_KEYWORD_ADD_FACTOR;
                    g.graph.remove(node2);
                    return true;
                }
            }
        }

        return false;
    }

    // Removes non-ok keyword candidates (plain adjectives)
    private void removeNonOkKeywordCandidates(TextRankGraph g) {
        for (int i = 0; i < g.graph.size(); i++) {
            TextRankWordNode t = (TextRankWordNode) g.graph.get(i);
            if (!t.multipart && t.isAdjective()) {
                g.graph.remove(t);
                i--;
            }
        }
    }

    // Calculates z-score for each keyword
    private void calcNormalizedValues(TextRankGraph g, KeywordResponse response) {
        double totalRank = 0.0;
        for (TextRankNode t : g.graph) {
            totalRank += t.rank;
        }

        int N = g.graph.size() - 1;
        if (N > 0) {
            double avg = totalRank / g.graph.size();
            double variance_tot = 0.0;
            for (TextRankNode t : g.graph) {
                variance_tot += (t.rank - avg) * (t.rank - avg);
            }
            variance_tot /= (double) N;

            double std_dev = Math.sqrt(variance_tot);
            for (TextRankNode t : g.graph) {
                t.normalizedRank = (t.rank - avg) / std_dev;
            }

            response.keywordAvgValue = avg;
            response.keywordStdDev = std_dev;
        }
    }

    // Creates keywords from list of POS tags
    public KeywordResponse getKeywords(List<POSTag> words, int keywordNum) {
        TextRankGraph g = createGraph(words);
        syntacticFilter(g);
        addEdges(g, CO_OCCURENCE_WINDOW);
        combineWords(g);
        g.doPageRank();

        while (createMultipartKeywords(g)) {
            continue;
        }

        while (collapseDuplicates(g)) {
            continue;
        }

        KeywordResponse response = new KeywordResponse();

        sortKeywordCandidates(g);
        removeNonOkKeywordCandidates(g);
        calcNormalizedValues(g, response);

        if (keywordNum == -1) {
            keywordNum = g.graph.size();
        }

        for (int i = 0; i < Math.min(keywordNum, g.graph.size()); i++) {
            KeywordResponse.Keyword k = response.newKeyword();
            k.word = ((TextRankWordNode) g.graph.get(i)).word;
            k.value = g.graph.get(i).rank;
            k.normalizedValue = g.graph.get(i).normalizedRank;
            response.keywords.add(k);
        }

        return response;
    }
}
