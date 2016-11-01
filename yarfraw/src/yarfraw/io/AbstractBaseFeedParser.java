package yarfraw.io;

import com.cfta.cf.feeds.RSSFeedCleaner;
import com.cfta.cf.httpfetch.ApacheHttpClientFetcher;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import org.apache.commons.io.IOUtils;

import yarfraw.core.datamodel.FeedFormat;
import yarfraw.core.datamodel.YarfrawException;
import yarfraw.utils.FeedFormatDetector;
/**
 * Provides a set of function to facilitate parsing of a RSS feed.
 * @author jliang
 *
 */
abstract class AbstractBaseFeedParser extends AbstractBaseIO{  
   
    protected String _origUrl = "";
    protected String _source = null;
    protected String _rssStreamText = null;

    public AbstractBaseFeedParser(File file, FeedFormat format){
        super(file, format);
    }
  
    public AbstractBaseFeedParser(String pathName, FeedFormat format){
        super(new File(pathName), format);
    }
  
    public AbstractBaseFeedParser(URI uri, FeedFormat format){
        super(new File(uri), format);
    } 
  
    public AbstractBaseFeedParser(File file){
        super(file);
    }
  
    public AbstractBaseFeedParser(String rssStreamText) throws YarfrawException{
        _rssStreamText = rssStreamText;
        _format = FeedFormatDetector.getFormat(IOUtils.toInputStream(_rssStreamText));
    }
  
    public AbstractBaseFeedParser(URI uri) throws YarfrawException, IOException{
        _origUrl = uri.toString();
        _format = FeedFormatDetector.getFormat(getStream());
    }
      
    public AbstractBaseFeedParser(URI uri, boolean checkFormat) throws YarfrawException, IOException{
        _origUrl = uri.toString();               
        if (checkFormat) {
            _format = FeedFormatDetector.getFormat(getStream());
        }
    }

    /**
     * Is the reader reading the feed from a remote http link.
     * @return true if reading remotely<br/>
     * false if reading from local file
     */
    public boolean isRemoteRead(){
      return _origUrl.length() > 0;
    }
      
    protected InputStream getStream() throws IOException{    
        InputStream stream;
        if (_rssStreamText != null) {
            stream = IOUtils.toInputStream(_rssStreamText);
        } else if (isRemoteRead()){            
            ApacheHttpClientFetcher f = new ApacheHttpClientFetcher();
            RSSFeedCleaner c = new RSSFeedCleaner();

            String s = "";
            try {                
                s = f.getWebPage(_origUrl);
                s = c.cleanFeedString(s);
            } catch (Exception ex) {
                ex.printStackTrace();
            }      
            stream = IOUtils.toInputStream(s);      
        } else{
            return new FileInputStream(_file);
        }

        return stream; 
    }  
}