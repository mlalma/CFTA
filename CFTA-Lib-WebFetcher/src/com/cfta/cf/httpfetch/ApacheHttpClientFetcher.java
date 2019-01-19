// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package com.cfta.cf.httpfetch;

import com.cfta.cf.util.CFTASettings;

import java.io.InputStream;
import java.net.URI;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.mozilla.universalchardet.UniversalDetector;

// Fetches web page contents using Apache's HttpClient, is not thread-safe
public class ApacheHttpClientFetcher implements HttpFetcherBase {

    private CloseableHttpClient httpClient;
    private HttpClientContext context;

    private String url = "";
    private String output = "";

    private final int RESPONSE_OK = 200;

    // Constructor
    public ApacheHttpClientFetcher() {
        LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();
        httpClient = HttpClients.custom().setUserAgent(CFTASettings.getHTTPUserAgent()).addInterceptorFirst(new NewResponseContentEncoding()).setRedirectStrategy(redirectStrategy).build();
        context = HttpClientContext.create();
    }

    // Fetcher thread to get the timeouts working properly
    private class FetchThread extends Thread {
        private String startUrl;

        // Constructor
        FetchThread(final String startUrl) {
            this.startUrl = startUrl;
        }

        @Override
        // Reads web page, tries to detected the charset and use it
        public void run() {
            url = startUrl;
            try {
                HttpGet httpget = new HttpGet(url);
                RequestConfig requestConfig = RequestConfig.custom()
                        .setSocketTimeout((int) CFTASettings.getHTTPTimeout())
                        .setConnectTimeout((int) CFTASettings.getHTTPConnectTimeout())
                        .build();
                httpget.setConfig(requestConfig);

                try (CloseableHttpResponse response = httpClient.execute(httpget, context)) {
                    if (response.getStatusLine().getStatusCode() == RESPONSE_OK) {
                        HttpEntity entity = response.getEntity();
                        if (entity != null) {
                            try (InputStream inStream = entity.getContent()) {
                                byte[] bytes = IOUtils.toByteArray(inStream);
                                UniversalDetector detector = new UniversalDetector(null);
                                detector.handleData(bytes, 0, bytes.length);
                                detector.dataEnd();
                                String encoding = detector.getDetectedCharset();

                                if (encoding != null) {
                                    output = new String(bytes, encoding);
                                } else {
                                    output = new String(bytes);
                                }
                            }
                        }
                    } else {
                        output = "";
                    }

                    HttpHost target = context.getTargetHost();
                    List<URI> redirectLocations = context.getRedirectLocations();
                    URI location = URIUtils.resolve(httpget.getURI(), target, redirectLocations);
                    url = location.toASCIIString();
                } catch (Exception ex) {
                    if (CFTASettings.getDebug()) {
                        ex.printStackTrace();
                    }
                }
            } catch (Exception ex) {
                if (CFTASettings.getDebug()) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    // Fetches web page and returns its contents
    public String getWebPage(final String url) throws Exception {
        output = "";
        FetchThread fetcher = new FetchThread(url);
        fetcher.start();
        fetcher.join(CFTASettings.getHTTPTimeout() + CFTASettings.getHTTPConnectTimeout() + 500);
        if (fetcher.isAlive()) {
            fetcher.interrupt();
        }
        return output;
    }
}
