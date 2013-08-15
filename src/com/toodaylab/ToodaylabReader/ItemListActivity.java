package com.toodaylab.ToodaylabReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.toodaylab.ToodaylabReader.rss.RssItem;
import com.toodaylab.ToodaylabReader.adapter.ListViewRssAdapter;
import com.toodaylab.ToodaylabReader.rss.util.RssProvider;
import com.toodaylab.ToodaylabReader.rss.util.RssUtils;
import com.toodaylab.ToodaylabReader.util.StringUtils;
import com.toodaylab.ToodaylabReader.util.UIUtils;
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
    private List<RssItem> lvRss_data = new ArrayList<RssItem>();
    private int current_page_index;
    private Handler lvRss_handler;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);
        current_page_index = 0;
        initItemListView();
        initItemListData();
    }

    private void initItemListData() {
        lvRss_handler = getLvHandler(lvRss, rssAdapter, lvRss_foot_more, lvRss_progressbar);
        loadRssItemData(0, lvRss_handler, UIUtils.LISTVIEW_ACTION_INIT);
    }

    private void initItemListView() {
        //获取数据
        rssAdapter = new ListViewRssAdapter(this, lvRss_data, R.layout.item_content);
        //增加foot
        lvRss_footer = getLayoutInflater().inflate(R.layout.listview_footer, null);
        lvRss_foot_more = (TextView) lvRss_footer.findViewById(R.id.listview_foot_more);
        lvRss_progressbar = (ProgressBar) lvRss_footer.findViewById(R.id.listview_foot_progress);
        lvRss = (PullToRefreshListView) findViewById(R.id.item_list_fresh_view);
        lvRss.addFooterView(lvRss_footer);
        //设置数据
        lvRss.setAdapter(rssAdapter);
        lvRss.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0 || view == lvRss_footer){
                    return;
                }
                Intent detailIntent = new Intent(getBaseContext(), ItemDetailActivity.class);
                detailIntent.putExtra("post_id", lvRss_data.get(position-1).getId());//pos从1开始, 真是不够程序员的!
                startActivity(detailIntent);
            }
        });
        lvRss.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                lvRss.onScrollStateChanged(view, scrollState);

                if(lvRss_data.isEmpty()){
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
                if(scrollEnd && lvDataState == UIUtils.LISTVIEW_DATA_MORE){
                    lvRss.setTag(UIUtils.LISTVIEW_DATA_MORE);
                    lvRss_foot_more.setText(R.string.load_ing);
                    lvRss_progressbar.setVisibility(View.VISIBLE);
                    current_page_index += 1;
                    loadRssItemData(current_page_index, lvRss_handler, UIUtils.LISTVIEW_ACTION_SCROLL);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lvRss.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        });
        lvRss.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRssItemData(0, lvRss_handler, UIUtils.LISTVIEW_ACTION_REFRESH);
            }
        });
    }


    /**
     * 从db中获取list操作
     * @param page
     * @return
     */
    private List<RssItem> getPageItems(int page){
        provider.open();
        List<RssItem> list = provider.getOnePageItems(page);
        provider.close();
        return list;
    }

    /**
     * 载入数据是开启线程异步操作, 需要通过handler来处理消息
     * @param lv
     * @param adapter
     * @param more
     * @param progress
     * @return
     */
    private Handler getLvHandler(final PullToRefreshListView lv, final BaseAdapter adapter,
                                 final TextView more, final ProgressBar progress){
        return new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what < RssUtils.PAGE_SIZE){
                    //说明已经没有数据了
                    lv.setTag(UIUtils.LISTVIEW_DATA_FULL);
                    adapter.notifyDataSetChanged();
                    more.setText(R.string.load_full);
                }else if(msg.what == RssUtils.PAGE_SIZE){
                    lv.setTag(UIUtils.LISTVIEW_DATA_MORE);
                    adapter.notifyDataSetChanged();
                    more.setText(R.string.load_more);
                }
                if(adapter.getCount() == 0){
                    lv.setTag(UIUtils.LISTVIEW_DATA_EMPTY);
                    more.setText(R.string.load_empty);
                }
                progress.setVisibility(ProgressBar.GONE);
                lvRss_progressbar.setVisibility(ProgressBar.GONE);
                if(msg.arg1 == UIUtils.LISTVIEW_ACTION_REFRESH){
                    lv.onRefreshComplete();
                    lv.setSelection(0);
                }
            }
        };
    }

    /**
     * 开启线程载入数据, 载入完毕通过handler发送消息
     * @param page
     * @param handler
     * @param action
     */
    private void loadRssItemData(final int page, final Handler handler, final int action){
        lvRss_progressbar.setVisibility(View.VISIBLE);
        new Thread(){
            @Override
            public void run() {
                Message msg = new Message();
                List<RssItem> list = null;
                if(action == UIUtils.LISTVIEW_ACTION_REFRESH
                        || action == UIUtils.LISTVIEW_ACTION_INIT){
                    int count = RssUtils.refreshFeed(getBaseContext());//重新刷新Feed
                    list = getPageItems(0);
                    lvRss_data.clear();
                    lvRss_data.addAll(list);
                }else {
                    list = getPageItems(page);
                    lvRss_data.addAll(list);
                }
                msg.what = list.size();
                msg.obj = list;
                msg.arg1 = action;
                handler.sendMessage(msg);
            }
        }.start();
    }

}