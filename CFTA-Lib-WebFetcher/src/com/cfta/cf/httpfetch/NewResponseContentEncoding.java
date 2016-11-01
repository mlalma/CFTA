// CFTA -- Content Fetching & Text Analysis System
// Lassi Maksimainen, 2013
package com.cfta.cf.httpfetch;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Locale;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.protocol.ResponseContentEncoding;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.protocol.HttpContext;

// Overwrite basic ResponseContentEncoding class to handle non-standard redirections
public class NewResponseContentEncoding extends ResponseContentEncoding {
    
    // Apache HttpClient is a bit too strict -- we need to modify the way it handles non-standard content-encoding parts
    // (and while we are at it, take a look on redirection as well)
    public void process(
            final HttpResponse response,
            final HttpContext context) throws HttpException, IOException {       
        HttpEntity entity = response.getEntity();

        // Entity can be null in case of 304 Not Modified, 204 No Content or similar
        // Check for zero length entity.
        if (entity != null && entity.getContentLength() != 0) {
            Header ceheader = entity.getContentEncoding();           
            if (ceheader != null) {
                final HeaderElement[] codecs = ceheader.getElements();
                boolean uncompressed = false;
                for (final HeaderElement codec : codecs) {
                    final String codecname = codec.getName().toLowerCase(Locale.US);
                    if ("gzip".equals(codecname) || "x-gzip".equals(codecname)) {
                        //response.setEntity(new GzipDecompressingEntity(response.getEntity()));
                        //uncompressed = true;
                        break;
                    } else if ("deflate".equals(codecname)) {
                        //response.setEntity(new DeflateDecompressingEntity(response.getEntity()));
                        //uncompressed = true;
                        break;
                    } else if ("identity".equals(codecname)) {
                        return;
                    } else {
                        response.setHeader("Content-Encoding", "identity");                                                                        
                        Header hs[] = response.getAllHeaders();
                        for (Header hh : hs) {
                            if (hh.getName().equalsIgnoreCase("Location")) {
                                String urli = hh.getValue();                                
                                try {
                                    String sUrl = urli;
                                    URL url = new URL(sUrl);
                                    URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                                    String canonical = uri.toString();
                                    response.setHeader("Location", canonical);
                                } catch (Exception ex) {
                                }
                            }
                        }
                        
                        BasicHttpEntity httpEntity = new BasicHttpEntity();
                        httpEntity.setChunked(entity.isChunked());
                        httpEntity.setContentEncoding(response.getFirstHeader("Content-Encoding"));
                        httpEntity.setContent(entity.getContent());
                        httpEntity.setContentLength(entity.getContentLength());
                        httpEntity.setContentType(entity.getContentType());
                        response.setEntity(httpEntity);                        
                        return;
                    }
                }                
            }
        }
    }
}