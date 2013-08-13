package com.toodaylab.ToodaylabReader;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.toodaylab.ToodaylabReader.rss.RssItem;
import com.toodaylab.ToodaylabReader.adapter.ListViewRssAdapter;
import com.toodaylab.ToodaylabReader.rss.util.RssProvider;
import com.toodaylab.ToodaylabReader.widget.PullToRefreshListView;

import java.util.List;

/**
 * User: awanabe
 * Date: 13-8-10
 * Time: 上午12:41
 */
public class ItemListActivity extends Activity{

    private RssProvider provider = new RssProvider(this);
    private ListViewRssAdapter rssAdapter;
    private PullToRefreshListView lvRss;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);
        initItemListView();

    }

    private void initItemListView() {
        rssAdapter = new ListViewRssAdapter(this, getPageItems(0), R.layout.item_content);
        lvRss = (PullToRefreshListView) findViewById(R.id.item_list_fresh_view);
        lvRss.setAdapter(rssAdapter);
        lvRss.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        lvRss.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        lvRss.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
    }

    private List<RssItem> getPageItems(int page){
        provider.open();
        List<RssItem> list = provider.getOnePageItems(page);
        provider.close();
        return list;
    }


}