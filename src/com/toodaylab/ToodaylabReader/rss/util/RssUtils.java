package com.toodaylab.ToodaylabReader.rss.util;

import android.util.Log;
import com.toodaylab.ToodaylabReader.rss.RssFeed;
import com.toodaylab.ToodaylabReader.rss.RssItem;
import com.toodaylab.ToodaylabReader.rss.handler.RssHandler;
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

    public static final String LOG_TAG = "TOODAY";

    public static final String rssUri = "http://www.toodaylab.com/feed";

    private static final String HTML_TAG_REGEX = "<[^>]*>";

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
            Log.e(LOG_TAG, "getFeed", e);
            return null;
        }

    }

    /**
     * 从文章中获取没有图片的文字
     * @return
     */
    public static String getDescWithoutPic(RssItem item){
        if(item.getDesc() != null){
            return item.getDesc().replaceAll("\n","").replaceAll(HTML_TAG_REGEX,"");
        }
        return "";
    }

    /**
     * 获取第一张图片
     * @return
     */
    public static String getFirstPicUrlInDesc(RssItem item){
        if(item.getDesc() != null){
            String str = item.getDesc();
            int start = str.indexOf("\"", str.indexOf("src", str.indexOf("<img"))) + 1;
            int end = str.indexOf("\"", start);
            return str.substring(start, end);
        }
        return null;
    }
}
