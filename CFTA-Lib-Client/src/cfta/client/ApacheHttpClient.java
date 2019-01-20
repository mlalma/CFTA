// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2019
package cfta.client;

import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.mozilla.universalchardet.UniversalDetector;

// Fetches web page contens using Apache's HttpClient, is not thread-safe
class ApacheHttpClient {

    private CloseableHttpClient httpClient;
    private HttpClientContext context;

    private String url = "";
    private String output = "";
    private String input = "";

    private final int RESPONSE_OK = 200;

    private final int READ_TIMEOUT = 90 * 1000;
    private final int CONNECT_TIMEOUT = 5 * 1000;

    // Constructor
    ApacheHttpClient() {
        LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();
        httpClient = HttpClients.custom().setUserAgent("CFTA-Java-Client").setRedirectStrategy(redirectStrategy).build();
        context = HttpClientContext.create();
    }

    // Fetcher thread to get the timeouts working properly
    private class FetchThread extends Thread {
        private String startUrl;

        // Constructor
        FetchThread(String startUrl) {
            this.startUrl = startUrl;
        }

        @Override
        // Reads web page, tries to detected the charset and use it
        public void run() {
            url = startUrl;
            try {
                HttpPost httppost = new HttpPost(url);

                StringEntity inputEntity = new StringEntity(input, Charset.forName("UTF-8"));
                inputEntity.setContentType("application/x-www-form-urlencoded");
                inputEntity.setChunked(true);
                httppost.setEntity(inputEntity);

                RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(READ_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).build();
                httppost.setConfig(requestConfig);

                try (CloseableHttpResponse response = httpClient.execute(httppost, context)) {
                    if (response.getStatusLine().getStatusCode() == RESPONSE_OK) {
                        HttpEntity entity = response.getEntity();
                        if (entity != null) {
                            try (InputStream instream = entity.getContent()) {
                                byte[] bytes = IOUtils.toByteArray(instream);
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
                        System.out.println("Error completing request: " + response.getStatusLine().getStatusCode());
                    }

                    HttpHost target = context.getTargetHost();
                    List<URI> redirectLocations = context.getRedirectLocations();
                    URI location = URIUtils.resolve(httppost.getURI(), target, redirectLocations);
                    url = location.toASCIIString();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // Gets response and return it
    String getResponse(String url, String input) throws Exception {
        this.output = "";
        this.input = input;
        FetchThread fetcher = new FetchThread(url);
        fetcher.start();
        fetcher.join(CONNECT_TIMEOUT + READ_TIMEOUT + 500);
        if (fetcher.isAlive()) {
            fetcher.interrupt();
        }
        return output.trim();
    }
}
