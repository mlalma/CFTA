// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta;

import com.cfta.cf.handlers.ContentExtractionHandler;
import com.cfta.cf.handlers.RSSFeedRequestHandler;
import com.cfta.cf.handlers.TwitterLinkRequestHandler;
import com.cfta.cf.handlers.TwitterRequestHandler;
import com.cfta.cf.handlers.WebFetchRequestHandler;
import com.cfta.cf.util.CFTASettings;
import com.cfta.handlers.data.HandlerPaths;
import com.cfta.ping.HealthHandler;
import com.cfta.ping.PingRequestHandler;
import com.cfta.ta.handlers.LanguageDetectionHandler;
import com.cfta.ta.handlers.NamedEntityHandler;
import com.cfta.ta.handlers.POSHandler;
import com.cfta.ta.handlers.TextRankKeywordHandler;
import com.cfta.ta.handlers.TextRankSummarizationHandler;
import com.cfta.ta.handlers.TextStatisticsHandler;
import com.cfta.tn.handlers.SentenceExtractionHandler;
import com.cfta.tn.handlers.TextLemmatizationHandler;
import com.cfta.tn.handlers.TokenExtractionHandler;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import spark.Spark;

// CFTA front-end web server app
public class CFTAFrontEndApp {

    // Default port for the front-end
    static private int port = 8080;

    // Command-line options
    static private final String PORT_OPTION = "port";
    static private final String SETTINGS_OPTION = "settings";
    static private final String HELP_OPTION = "help";

    // Parses given command-line options
    private static void parseCommandLine(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption(PORT_OPTION, PORT_OPTION, true, "Used port on the server (default: 8080)");
        options.addOption(SETTINGS_OPTION, SETTINGS_OPTION, true, "Location of settings file");
        options.addOption(HELP_OPTION, HELP_OPTION, false, "Print this message");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption(PORT_OPTION)) {
            port = Integer.parseInt(cmd.getOptionValue(PORT_OPTION));
        }
        Spark.port(port);

        if (cmd.hasOption(SETTINGS_OPTION)) {
            CFTASettings.loadSettingsFile(cmd.getOptionValue(SETTINGS_OPTION));
        }

        if (cmd.hasOption(HELP_OPTION)) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("CFTA-FrontEnd-App", options);
            System.exit(0);
        }
    }

    // Initializes Spark handlers
    private static void initializeHandlers() {
        Spark.get(HandlerPaths.PING_PATH, new PingRequestHandler());
        Spark.get(HandlerPaths.HEALTH_PATH, new HealthHandler());
        Spark.post(HandlerPaths.WEB_FETCH_PATH, new WebFetchRequestHandler());
        Spark.post(HandlerPaths.RSS_FEED_PATH, new RSSFeedRequestHandler());
        Spark.post(HandlerPaths.TWITTER_FEED_PATH, new TwitterRequestHandler());
        Spark.post(HandlerPaths.TWITTER_FEED_LINKS_PATH, new TwitterLinkRequestHandler());
        Spark.post(HandlerPaths.CONTENT_EXTRACT_PATH, new ContentExtractionHandler(port));
        Spark.post(HandlerPaths.LANGUAGE_DETECTION_PATH, new LanguageDetectionHandler());
        Spark.post(HandlerPaths.SENTENCE_EXTRACTION_PATH, new SentenceExtractionHandler());
        Spark.post(HandlerPaths.TOKEN_EXTRACTION_PATH, new TokenExtractionHandler(port));
        Spark.post(HandlerPaths.LEMMATIZATION_PATH, new TextLemmatizationHandler(port));
        Spark.post(HandlerPaths.POS_PATH, new POSHandler());
        Spark.post(HandlerPaths.NER_PATH, new NamedEntityHandler());
        Spark.post(HandlerPaths.TEXTRANK_SUMMARIZE, new TextRankSummarizationHandler(port));
        Spark.post(HandlerPaths.TEXTRANK_KEYWORDS, new TextRankKeywordHandler(port));
        Spark.post(HandlerPaths.TEXT_STATISTICS_PATH, new TextStatisticsHandler(port));
    }

    // Entry point -- starts the CFTA front-end web server
    public static void main(String[] args) {
        try {
            parseCommandLine(args);
            initializeHandlers();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
