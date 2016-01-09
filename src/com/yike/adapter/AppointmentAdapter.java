package com.yike.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yike.R;
import com.yike.bean.AppointmentBean;
import com.yike.tool.TimeTool;

/**
 * @author rendy
 *         专题列表适配器
 */
public class AppointmentAdapter extends BaseAdapter {
    private List<AppointmentBean> models;
    private LayoutInflater layout;
    private Context mContext;

    public AppointmentAdapter(List<AppointmentBean> datas, LayoutInflater lay, Context context) {
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

    public void setData(List<AppointmentBean> models) {
        this.models = models;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Helper vh;
        if (convertView == null) {
            convertView = layout.inflate(R.layout.item_appointment_list, null);
            vh = new Helper();
            vh.updateTimeTv = (TextView) convertView
                    .findViewById(R.id.appointment_time);
            vh.nameTv = (TextView) convertView
                    .findViewById(R.id.alum_name);
            convertView.setTag(vh);
        } else {
            vh = (Helper) convertView.getTag();
        }
        AppointmentBean album = models.get(position);
        vh.nameTv.setText(album.getAlbumName());
        vh.updateTimeTv.setText(TimeTool.getTime(album.getBuildTime()));
        return convertView;
    }

    private static class Helper {
        TextView nameTv;
        TextView updateTimeTv;
    }
}
