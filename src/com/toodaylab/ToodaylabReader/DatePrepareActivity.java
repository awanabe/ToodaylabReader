package com.toodaylab.ToodaylabReader;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import com.toodaylab.ToodaylabReader.rss.RssFeed;
import com.toodaylab.ToodaylabReader.rss.RssItem;
import com.toodaylab.ToodaylabReader.util.RssProvider;
import com.toodaylab.ToodaylabReader.util.RssUtils;

import java.util.Collections;
import java.util.List;

public class DatePrepareActivity extends Activity
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
            String errMsg = null;
            try{
                //get items in feed, then reverse it, order by time
                if(rssFeed == null){
                    errMsg = getString(R.string.errMsgNoFeed);
                    throw new Exception(errMsg);
                }
                List<RssItem> list = rssFeed.getItemList();
                if(list != null){
                    Collections.reverse(list);

                    RssProvider provider = new RssProvider(getBaseContext());
                    provider.open();
                    //Get latest Item From DB
                    RssItem latestItem = provider.getLatestItem();
                    long latestTime = 0;
                    if(latestItem!=null)
                        latestTime = latestItem.getPubdate();

                    for(RssItem item : list){
                        if(item.getPubdate() > latestTime){
                            //Log.i("DEMO", "Insert:" + String.valueOf(item.getPubdate()));
                            provider.insertItem(item);
                        }
                    }
                    provider.close();
                }
            }catch (Exception ex){
                Log.e(RssUtils.LOG_TAG, errMsg, ex);
            }
            startActivity(new Intent(getApplication(), ItemListActivity.class));
            finish();//自我销毁, 进入下一个页面之后返回不会进入这个页面
        }
    }
}
