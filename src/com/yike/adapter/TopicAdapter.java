package com.yike.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.yike.R;
import com.yike.bean.TopicBean;
import com.yike.tool.BitmapTool;
import com.yike.view.RoundAngleImageView;

/**
 * @author rendy
 *         专题列表适配器
 */
public class TopicAdapter extends BaseAdapter {
    private List<TopicBean> models;
    private LayoutInflater layout;
    private BitmapUtils bitmapTool;

    public TopicAdapter(List<TopicBean> datas, LayoutInflater lay) {
        bitmapTool = BitmapTool.getInstance().initAdapterUitl(lay.getContext());
        this.models = datas;
        this.layout = lay;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(List<TopicBean> models) {
        this.models = models;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Helper vh;
        if (convertView == null) {
            convertView = layout.inflate(R.layout.item_listview_topic, null);
            vh = new Helper();
            vh.imageView = (RoundAngleImageView) convertView
                    .findViewById(R.id.item_listview_topic_img);
            vh.nameTv = (TextView) convertView
                    .findViewById(R.id.topic_name);
            convertView.setTag(vh);
        } else {
            vh = (Helper) convertView.getTag();
        }
        TopicBean album = models.get(position);
        bitmapTool.display(vh.imageView, album.getStandardPic());
        vh.nameTv.setText(album.getName());
        return convertView;
    }

    private static class Helper {
        RoundAngleImageView imageView;
        TextView nameTv;
    }
}
