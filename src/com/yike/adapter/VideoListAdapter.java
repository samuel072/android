package com.yike.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.yike.R;
import com.yike.bean.SearchResultVideo;

import java.util.List;

public class VideoListAdapter extends BaseAdapter {
    private List<SearchResultVideo> list;
    private Context context;
    private LayoutInflater inflater;
    private BitmapDisplayConfig config;

    public VideoListAdapter(List<SearchResultVideo> list, Context context) {
        super();
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
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
                    R.layout.item_listview_search_videolist, null);
            vh = new ViewHolder();
            vh.name = (TextView) convertView.findViewById(R.id.tv_video_name);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        SearchResultVideo album = list.get(position);
        vh.name.setText(album.getName());

        return convertView;
    }

    class ViewHolder {
        TextView name;
    }

}
