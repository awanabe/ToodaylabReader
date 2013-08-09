package com.toodaylab.ToodaylabReader;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import com.toodaylab.ToodaylabReader.rss.RssItem;
import com.toodaylab.ToodaylabReader.util.RssProvider;

import java.util.List;

/**
 * User: awanabe
 * Date: 13-8-10
 * Time: 上午12:41
 */
public class ItemListActivity extends Activity {

    private RssProvider provider;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.item_list);

        RssProvider provider = new RssProvider(this);
        provider.open();
        List<RssItem> list = provider.getOnePageItems (0);


    }

}