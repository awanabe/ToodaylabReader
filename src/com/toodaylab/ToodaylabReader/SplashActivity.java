package com.toodaylab.ToodaylabReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SplashActivity.this.startActivity(new Intent(getApplication(), ItemListActivity.class));
                SplashActivity.this.finish();//自我销毁, 进入下一个页面之后返回不会进入这个页面
            }
        }, 2000);

    }
}
