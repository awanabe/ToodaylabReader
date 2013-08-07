package com.toodaylab.ToodaylabReader.rss;

import java.util.ArrayList;
import java.util.List;

/**
 * User: awanabe
 * Date: 13-8-7
 * Time: 下午10:35
 */
public class RssItem {
    private String title;
    private StringBuilder desc;
    private String link;
    private List<String> category;
    private String pubdate;

    public RssItem() {
        desc = new StringBuilder();
        category = new ArrayList<String>();
    }

    public void addCategory(String cg){
        category.add(cg);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc.toString();
    }

    public void setDesc(String desc) {
        this.desc.append(desc);
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<String> getCategory() {
        return category;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }
}
