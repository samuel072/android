package com.yike.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.yike.R;
import com.yike.bean.SearchResultAlbum;

import java.util.List;

public class AlbumListAdapter extends BaseAdapter {
    private List<SearchResultAlbum> list;
    private LayoutInflater inflater;
    private BitmapUtils utils;
    private BitmapDisplayConfig config;

    public AlbumListAdapter(List<SearchResultAlbum> list, Context context) {
        super();
        this.list = list;
        this.inflater = LayoutInflater.from(context);
        utils = new BitmapUtils(context);
        config = new BitmapDisplayConfig();
//        config.setLoadingDrawable(context.getResources().getDrawable(
//                R.drawable.logo));
//        config.setLoadFailedDrawable(context.getResources().getDrawable(
//                R.drawable.logo));
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (list == null)
            return 0;

        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            convertView = inflater.inflate(
                    R.layout.item_listview_search_ablumlist, null);
            vh = new ViewHolder();
            vh.img = (ImageView) convertView.findViewById(R.id.imageView1);
            vh.name = (TextView) convertView.findViewById(R.id.tv_video_name);
            vh.time = (TextView) convertView.findViewById(R.id.textView2);
            vh.count = (TextView) convertView.findViewById(R.id.textView3);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        SearchResultAlbum album = list.get(position);
        utils.display(vh.img, album.getStandardPic());
        vh.name.setText(album.getName());
        vh.time.setText(album.getCreateTime());
        vh.count.setText(album.getScore());

        return convertView;
    }

    class ViewHolder {
        ImageView img;
        TextView name, time, count;
    }

}
