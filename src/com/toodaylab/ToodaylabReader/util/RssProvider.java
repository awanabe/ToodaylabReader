package com.toodaylab.ToodaylabReader.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.toodaylab.ToodaylabReader.rss.RssItem;

import java.util.ArrayList;
import java.util.List;

/**
 * User: awanabe
 * Date: 13-8-8
 * Time: 下午9:28
 */
public class RssProvider {
    /*
    DB KEYS
     */
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESC = "desc";
    private static final String KEY_LINK = "link";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_PUBDATE = "pubdate";

    private static final String TABLE_NAME = "item";

    private static final int PAGE_SIZE = 10;

    private static final String[] DB_KEY_ORDER = new String[]{KEY_ID, KEY_TITLE, KEY_DESC, KEY_LINK, KEY_CATEGORY, KEY_PUBDATE};
    private static final String DB_DEFAULT_ORDER = "id DESC";

    private static final String DB_RSS_CREATE_SQL = "create table item (id integer primary key autoincrement, " +
            "title text not null, desc text not null, link text not null, " +
            "category text not null, pubdate text not null)";

    final Context context;
    RssDatabaseHelper dbHelper;
    SQLiteDatabase db;

    public RssProvider(Context context) {
        this.context = context;
        dbHelper = new RssDatabaseHelper(context);
    }

    public RssProvider open(){
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    /**
     * add one item into db
     * @param item
     * @return
     */
    public long insertItem(RssItem item){
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, item.getTitle().trim());
        values.put(KEY_DESC, item.getDesc().trim());
        values.put(KEY_LINK, item.getLink().trim());
        values.put(KEY_CATEGORY, item.getCategoryForDB());
        values.put(KEY_PUBDATE, item.getPubdate());
        return db.insert(TABLE_NAME, null, values);
    }

    /**
     * get one page items
     * @param page
     * @return
     */
    public List<RssItem> getOnePageItems(int page){
        List<RssItem> list = new ArrayList<RssItem>();
        int offset = page * PAGE_SIZE;
        Cursor c = db.query(
                true,
                TABLE_NAME,
                DB_KEY_ORDER,
                null,
                null,
                null,
                null,
                DB_DEFAULT_ORDER,
                offset + ", 10"
        );

        if(c.moveToFirst()){
            do{
                list.add(this.parseItemFromDB(c));
            }while (c.moveToNext());
        }
        return list;
    }

    /**
     * get newest item
     * @return
     */
    public RssItem getLatestItem(){
        Cursor c = db.query(
                true,
                TABLE_NAME,
                DB_KEY_ORDER,
                null,
                null,
                null,
                null,
                DB_DEFAULT_ORDER,
                "0,1"
        );
        if(c.moveToFirst()){
            return this.parseItemFromDB(c);
        }
        return null;
    }



    private RssItem parseItemFromDB(Cursor c){
        RssItem item = new RssItem();
        item.setId(c.getInt(0));
        item.setTitle(c.getString(1));
        item.setDesc(c.getString(2));
        item.setLink(c.getString(3));
        item.setCategoryFromDB(c.getString(4));
        item.setPubdateFromDB(c.getString(5));
        return item;
    }


    private static class RssDatabaseHelper extends SQLiteOpenHelper{


        public RssDatabaseHelper(Context context) {
            super(context, "toodaylab", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(DB_RSS_CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS item");
            onCreate(sqLiteDatabase);
        }
    }
}
