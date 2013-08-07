package com.toodaylab.ToodaylabReader;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import com.toodaylab.ToodaylabReader.rss.RssFeed;

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
            Log.i("tooday",rssFeed.toString());
        }
    }
}
