package com.toodaylab.ToodaylabReader;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.toodaylab.ToodaylabReader.rss.RssItem;
import com.toodaylab.ToodaylabReader.adapter.ListViewRssAdapter;
import com.toodaylab.ToodaylabReader.rss.util.RssProvider;
import com.toodaylab.ToodaylabReader.util.StringUtils;
import com.toodaylab.ToodaylabReader.widget.PullToRefreshListView;

import java.util.ArrayList;
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
    private View lvRss_footer;
    private TextView lvRss_foot_more;
    private ProgressBar lvRss_progressbar;
    private List<RssItem> lvRssData = new ArrayList<RssItem>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);

        initItemListView();

    }

    private void initItemListView() {
        rssAdapter = new ListViewRssAdapter(this, getPageItems(0), R.layout.item_content);
        lvRss_footer = getLayoutInflater().inflate(R.layout.listview_footer, null);
        lvRss_foot_more = (TextView) lvRss_footer.findViewById(R.id.listview_foot_more);
        lvRss_progressbar = (ProgressBar) lvRss_footer.findViewById(R.id.listview_foot_progress);
        lvRss = (PullToRefreshListView) findViewById(R.id.item_list_fresh_view);
        lvRss.addFooterView(lvRss_footer);
        lvRss.setAdapter(rssAdapter);
        lvRss.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        lvRss.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                lvRss.onScrollStateChanged(view, scrollState);

                if(lvRssData.isEmpty()){
                    return;
                }

                boolean scrollEnd = false;
                try{
                    if(view.getPositionForView(lvRss_footer) == view.getLastVisiblePosition()){
                        scrollEnd = true;
                    }
                }catch (Exception e){
                    scrollEnd = false;
                }

                int lvDataState = StringUtils.toInt(lvRss.getTag());
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