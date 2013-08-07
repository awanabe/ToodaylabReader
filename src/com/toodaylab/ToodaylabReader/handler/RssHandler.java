package com.toodaylab.ToodaylabReader.handler;

import android.util.Log;
import com.toodaylab.ToodaylabReader.rss.RssFeed;
import com.toodaylab.ToodaylabReader.rss.RssItem;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

/**
 * User: awanabe
 * Date: 13-8-7
 * Time: 下午10:51
 */
public class RssHandler extends DefaultHandler2{

    RssFeed feed;
    RssItem item;

    static final int RSS_TITLE = 1;
    static final int RSS_LINK = 2;
    static final int RSS_DESCRIPTION = 3;
    static final int RSS_CATEGORY = 4;
    static final int RSS_PUBDATE = 5;

    static final String RSS_CHANNEL_TAG = "channel";
    static final String RSS_ITEM_TAG = "item";
    static final String RSS_TITLE_TAG = "title";
    static final String RSS_DESC_TAG = "description";
    static final String RSS_LINK_TAG = "link";
    static final String RSS_CATEGORY_TAG = "category";
    static final String RSS_PUBDATE_TAG = "pubDate";

    static final int DEPTH_FEED = 3;

    int depth = 0;
    int currentState = 0;

    public RssFeed getFeed(){
        return feed;
    }

    @Override
    public void startDocument() throws SAXException {
        feed = new RssFeed();
        item = new RssItem();
    }

    @Override
    public void endDocument() throws SAXException {
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        depth++;
        if (localName.equals(RSS_CHANNEL_TAG)){
            currentState = 0;
            return;
        }
        if (localName.equals(RSS_ITEM_TAG)){
            item = new RssItem();
            return;
        }
        if (localName.equals(RSS_TITLE_TAG)){
            currentState = RSS_TITLE;
            return;
        }
        if (localName.equals(RSS_DESC_TAG)){
            currentState = RSS_DESCRIPTION;
            return;
        }
        if (localName.equals(RSS_LINK_TAG)){
            currentState = RSS_LINK;
            return;
        }
        if (localName.equals(RSS_CATEGORY_TAG)){
            currentState = RSS_CATEGORY;
            return;
        }
        if (localName.equals(RSS_PUBDATE_TAG)){
            currentState = RSS_PUBDATE;
            return;
        }
        currentState = 0;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        depth--;
        if (localName.equals("item")){
            feed.addItem(item);
        }
        //End of element makes status init, or keep the status
        //the desc sometimes contains "\n\t" so parser cannot read all chars at once
        if(localName.equals(RSS_DESC_TAG)
                ||localName.equals(RSS_TITLE_TAG)
                ||localName.equals(RSS_LINK_TAG)
                ||localName.equals(RSS_CATEGORY_TAG)
                ||localName.equals(RSS_PUBDATE_TAG)) {
            currentState = 0;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String str = new String(ch, start, length);
        Log.i("tooday", str);

        switch (currentState){
            case RSS_TITLE:
                if(depth == DEPTH_FEED)
                    feed.setTitle(str);
                else
                    item.setTitle(str);
                break;
            case RSS_LINK:
                if (depth == DEPTH_FEED){
                    //Feed parsing
                }else{
                    item.setLink(str);
                }
                break;
            case RSS_DESCRIPTION:
                if (depth == DEPTH_FEED){
                    //Feed parsing
                }else{
                    item.setDesc(str);
                }
                break;
            case RSS_CATEGORY:
                item.addCategory(str);
                break;
            case RSS_PUBDATE:
                item.setPubdate(str);
                break;
            default:
                return;
        }
    }
}
