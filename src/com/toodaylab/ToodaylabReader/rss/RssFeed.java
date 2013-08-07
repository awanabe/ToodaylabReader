package com.toodaylab.ToodaylabReader.rss;

import java.util.ArrayList;
import java.util.List;

/**
 * User: awanabe
 * Date: 13-8-7
 * Time: 下午10:24
 */
public class RssFeed {

    private String title;
    private String pubdate;
    private int itemCount;
    private List<RssItem> itemList;

    public RssFeed() {
        itemList = new ArrayList<RssItem>();
    }

    public void addItem(RssItem item){
        itemList.add(item);
        itemCount++;
    }

    public RssItem getItem(int location){
        if(location >=0 && location < itemCount){
            return itemList.get(location);
        }
        return null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public List<RssItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<RssItem> itemList) {
        this.itemList = itemList;
    }

}
