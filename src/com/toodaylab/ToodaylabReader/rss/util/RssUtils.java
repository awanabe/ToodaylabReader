package com.toodaylab.ToodaylabReader.rss.util;

import android.content.Context;
import android.util.Log;
import com.toodaylab.ToodaylabReader.rss.RssFeed;
import com.toodaylab.ToodaylabReader.rss.RssItem;
import com.toodaylab.ToodaylabReader.rss.handler.RssHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * User: awanabe
 * Date: 13-8-7
 * Time: 下午11:55
 */
public class RssUtils {

    public static final String LOG_TAG = "TOODAY";

    public static final String rssUri = "http://www.toodaylab.com/feed";

    public static final int PAGE_SIZE = 10;

    private static final String HTML_TAG_REGEX = "<[^>]*>";

    private static final String HREF_REGEX = "<a [^>]*>|</a>";
    private static final String IMG_WIDTH_REGEX = "width=\"[0-9]*\" height=\"[0-9]*\"";
    private static final String IMG_WIDTH_STANDARD = "width=\"100%\"";
    /**
     * 获取Feed对象, 重新加载
     * @return RssFeed
     */
    private static RssFeed getFeed(){
        try {
            URL url = new URL(rssUri);
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
     * 重新刷新Feed, 新数据入库
     * @return num 新数据的条数
     */
    public static int refreshFeed(Context context){

        int newCount = 0;
        RssFeed feed = getFeed();
        if(feed != null){
            List<RssItem> list = feed.getItemList();
            if(list != null){
                Collections.reverse(list);

                RssProvider provider = new RssProvider(context);
                provider.open();
                //Get latest Item From DB
                RssItem latestItem = provider.getLatestItem();
                long latestTime = 0;
                if(latestItem!=null)
                    latestTime = latestItem.getPubdate();

                for(RssItem item : list){
                    if(item.getPubdate() > latestTime){
                        //Log.i("DEMO", "Insert:" + String.valueOf(item.getPubdate()));
                        newCount++;
                        provider.insertItem(item);
                    }
                }
                provider.close();
            }
        }
        return newCount;
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

    public static String formatRssItemDesc(RssItem item){
        if(item.getDesc() != null){
           return item.getDesc().replaceAll("\n","").replaceAll(HREF_REGEX,"").replaceAll(IMG_WIDTH_REGEX,IMG_WIDTH_STANDARD);
        }
        return null;
    }
}
