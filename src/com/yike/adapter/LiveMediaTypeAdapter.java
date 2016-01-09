package com.yike.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.yike.R;
import com.yike.bean.LiveItemInfo;
import com.yike.tool.BitmapTool;

import java.util.List;

/**
 * Created by wangwei-ps on 15-8-15.
 */
public class LiveMediaTypeAdapter extends BaseAdapter {
    private List<LiveItemInfo> list;
    private LayoutInflater inflater;

    public LiveMediaTypeAdapter(List<LiveItemInfo> list, Context context) {
        super();
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.zhengzai_zhibo_item, null);
            vh = new ViewHolder();
            vh.img = (ImageView) convertView.findViewById(R.id.img_grid);
             vh.time = (TextView) convertView.findViewById(R.id.tv_time);
            vh.name = (TextView) convertView.findViewById(R.id.live_name);
            vh.type_name = (TextView) convertView.findViewById(R.id.tv_type_name);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        LiveItemInfo info = list.get(position);
        vh.time.setText(info.getShowtime());
        vh.name.setText(info.getName());

        //TODO 目前接口返回数据有问题,待核对
        vh.type_name.setText(info.getName());

        BitmapTool.getInstance().initAdapterUitl(inflater.getContext())
                .display(vh.img, info.getPic());
        return convertView;
    }

    class ViewHolder {
        ImageView img;
        TextView name, time, type_name;
    }
}
