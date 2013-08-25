package com.toodaylab.ToodaylabReader.util;

import android.app.Activity;
import android.content.Intent;

/**
 * User: awanabe
 * Date: 13-8-15
 * Time: 上午1:11
 */
public class UIUtils {
    //列表一些数据状态
    public final static int LISTVIEW_DATA_MORE = 0x01;
    public final static int LISTVIEW_DATA_LOADING = 0x02;
    public final static int LISTVIEW_DATA_FULL = 0x03;
    public final static int LISTVIEW_DATA_EMPTY = 0x04;
    //列表的一些动作
    public final static int LISTVIEW_ACTION_INIT = 0x01;
    public final static int LISTVIEW_ACTION_REFRESH = 0x02;
    public final static int LISTVIEW_ACTION_SCROLL = 0x03;

    public static void showShareMore(Activity context, final String title, final String url){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享 | 理想生活实验室 |" + title);
        intent.putExtra(Intent.EXTRA_TEXT,"理想生活实验室 | " + title + " " + url);
        context.startActivity(Intent.createChooser(intent,"选择分享"));
    }

}
