package com.toodaylab.ToodaylabReader.util;

import com.toodaylab.ToodaylabReader.handler.RssHandler;
import com.toodaylab.ToodaylabReader.rss.RssFeed;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.net.URL;

/**
 * User: awanabe
 * Date: 13-8-7
 * Time: 下午11:55
 */
public class RssUtils {

    public static final String rssUri = "http://www.toodaylab.com/feed";

    public static RssFeed getFeed(String uri){
        try {
            URL url = new URL(uri);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            RssHandler handler = new RssHandler();
            reader.setContentHandler(handler);
            InputSource in = new InputSource(url.openStream());
            reader.parse(in);
            return handler.getFeed();
        } catch (Exception e) {
            return null;
        }

    }
}
