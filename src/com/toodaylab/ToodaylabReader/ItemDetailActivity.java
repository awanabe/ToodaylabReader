package com.toodaylab.ToodaylabReader;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;
import com.toodaylab.ToodaylabReader.rss.RssItem;
import com.toodaylab.ToodaylabReader.rss.util.RssProvider;
import com.toodaylab.ToodaylabReader.rss.util.RssUtils;

import java.net.URLEncoder;

/**
 * User: awanabe
 * Date: 13-8-16
 * Time: 上午12:39
 */
public class ItemDetailActivity extends Activity {

    private int postId;
    private TextView itemTitle;
    private TextView itemCategory;
    private WebView itemDetail;
    private RssProvider provider = new RssProvider(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);
        this.initView();
        this.initData();
    }

    private void initData() {
        if(postId <= 0){
            finish();
        }

        provider.open();
        RssItem item = provider.getItemById(postId);
        provider.close();
        if(item == null){
            finish();
        }

        itemTitle.setText(item.getTitle());
        itemCategory.setText(item.getCategoryForDB());

        //webView注入
        itemDetail.getSettings().setDefaultTextEncodingName("utf-8");
        itemDetail.loadDataWithBaseURL(null, RssUtils.formatRssItemDesc(item), "text/html", "utf-8", null);
    }

    private void initView() {
        postId = getIntent().getIntExtra("post_id", 0);
        itemTitle = (TextView)findViewById(R.id.item_title);
        itemCategory = (TextView)findViewById(R.id.detail_category);
        itemDetail = (WebView)findViewById(R.id.detail_detail);
    }
}
