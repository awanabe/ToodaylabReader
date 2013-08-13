package com.toodaylab.ToodaylabReader.rss;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * User: awanabe
 * Date: 13-8-7
 * Time: 下午10:35
 */
public class RssItem {

    private int id;
    private String title;
    private StringBuilder desc;
    private String link;
    private List<String> category;
    private long pubdate;

    public RssItem() {
        desc = new StringBuilder();
        category = new ArrayList<String>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    /**
     * for xml parser add category one by one
     * @param cg
     */
    public void addCategory(String cg){
        category.add(cg);
    }

    /**
     * gen category string for db
     * @return
     */
    public String getCategoryForDB(){
        StringBuilder sb = new StringBuilder();
        for(String cate : category){
            sb.append(cate.toString().trim()).append(";");
        }
        String cgStr = sb.toString();
        if(cgStr.endsWith(";"))
            cgStr = cgStr.substring(0, cgStr.length()-1);
        return  cgStr;
    }

    /**
     * gen category list from db
     * @param categoryStr
     */
    public void setCategoryFromDB(String categoryStr){
        String[] cgArray = categoryStr.split(";");
        category = Arrays.asList(cgArray);
    }

    public long getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = new Date(pubdate.trim()).getTime();
    }

    public Date getRealPubdate(){
        return new Date(pubdate);
    }

    public void setPubdateFromDB(String time){
        this.pubdate = new Date(Long.valueOf(time)).getTime();
    }

}
