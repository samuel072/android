package com.yike.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yike.R;
import com.yike.bean.TopicDetailBean;
import com.yike.tool.TimeTool;

/**
 * @author rendy
 *         专题列表适配器
 */
public class TopicDetailAdapter extends BaseAdapter {
    private List<TopicDetailBean> models;
    private LayoutInflater layout;
    private Context mContext;

    public TopicDetailAdapter(List<TopicDetailBean> datas, LayoutInflater lay, Context context) {
        this.models = datas;
        this.layout = lay;
        this.mContext = context;
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

    public void setData(List<TopicDetailBean> models) {
        this.models = models;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Helper vh;
        if (convertView == null) {
            convertView = layout.inflate(R.layout.topic_detail_item, null);
            vh = new Helper();
            vh.updateTimeTv = (TextView) convertView
                    .findViewById(R.id.update_time);
            vh.nameTv = (TextView) convertView
                    .findViewById(R.id.title);
            convertView.setTag(vh);
        } else {
            vh = (Helper) convertView.getTag();
        }
        TopicDetailBean album = models.get(position);
        vh.nameTv.setText(album.getName());
        vh.updateTimeTv.setText(TimeTool.getTimeStr(album.getModifyTime(), mContext));
        return convertView;
    }

    private static class Helper {
        TextView nameTv;
        TextView updateTimeTv;
    }
}
