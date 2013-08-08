package com.toodaylab.ToodaylabReader;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import com.toodaylab.ToodaylabReader.rss.RssFeed;
import com.toodaylab.ToodaylabReader.rss.RssItem;
import com.toodaylab.ToodaylabReader.util.RssProvider;
import com.toodaylab.ToodaylabReader.util.RssUtils;

import java.util.Collections;
import java.util.List;

public class MyActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        new FeedParserTask().execute(RssUtils.rssUri);
    }

    private class FeedParserTask extends AsyncTask<String, Void, RssFeed>{

        @Override
        protected RssFeed doInBackground(String... uris) {
            return RssUtils.getFeed(uris[0]);
        }

        @Override
        protected void onPostExecute(RssFeed rssFeed) {
            RssProvider provider = new RssProvider(getBaseContext());
            List<RssItem> list;
            Collections.reverse(list = rssFeed.getItemList());

            provider.open();
//            for(RssItem item : list){
//                Log.i("DEMO", item.getPubdate().toString());
//                long id = provider.insertItem(item);
//                Log.i("DEMO", String.valueOf(id));
//            }
            List<RssItem> pageItems = provider.getOnePageItems(2);
            provider.close();
        }
    }
}
