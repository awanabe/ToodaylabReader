package com.toodaylab.ToodaylabReader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.toodaylab.ToodaylabReader.R;
import com.toodaylab.ToodaylabReader.rss.RssItem;
import com.toodaylab.ToodaylabReader.rss.util.RssUtils;

import java.util.List;

/**
 * User: awanabe
 * Date: 13-8-10
 * Time: 下午11:32
 */
public class ListViewRssAdapter extends BaseAdapter {
    private Context context;
    private List<RssItem> list;
    private LayoutInflater listContainer;
    private int itemViewResource;

    static class ListItemView{
        public ImageView img;
        public TextView title;
        public TextView mini_content;
    }

    public ListViewRssAdapter(Context context, List<RssItem> list, int itemViewResource) {
        this.context = context;
        this.list = list;
        this.itemViewResource = itemViewResource;
        this.listContainer = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemView itemView = null;

        if(convertView == null){
            convertView = listContainer.inflate(this.itemViewResource, null);
            itemView = new ListItemView();
            itemView.title = (TextView) convertView.findViewById(R.id.title);
            itemView.mini_content = (TextView)convertView.findViewById(R.id.mini_content);
            itemView.img = (ImageView)convertView.findViewById(R.id.img);

            convertView.setTag(itemView);
        }else {
            itemView = (ListItemView) convertView.getTag();
        }

        RssItem item = list.get(position);
        itemView.title.setText(item.getTitle());
        itemView.mini_content.setText(RssUtils.getDescWithoutPic(item));

        return convertView;
    }
}
