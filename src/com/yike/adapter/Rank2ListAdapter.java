package com.yike.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.yike.R;
import com.yike.bean.RankBean;
import com.yike.bean.TempmMuiscBean;
import com.yike.tool.BitmapTool;
import com.yike.tool.TimeTool;

/**
 * @author rendy
 *         排行适配器
 */
public class Rank2ListAdapter extends BaseAdapter {
    private List<RankBean> list;
    private LayoutInflater inflater;
    private BitmapUtils bitmapTool;

    public Rank2ListAdapter(List<RankBean> list, LayoutInflater layout) {
        super();
        this.list = list;
        this.inflater = layout;
        bitmapTool = BitmapTool.getInstance().initAdapterUitl(
                layout.getContext());

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

    public void setDatas(List<RankBean> datas) {
        this.list = datas;

    }

    public void cleanDatas() {
        this.list.clear();

    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_listview_rank, null);
            vh = new ViewHolder();
            vh.icon = (ImageView) convertView.findViewById(R.id.imageView1);
            vh.name = (TextView) convertView.findViewById(R.id.tv_video_name);
            vh.persion = (TextView) convertView.findViewById(R.id.textView2);
            vh.date = (TextView) convertView.findViewById(R.id.textView3);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        RankBean listBean = list.get(position);
        vh.name.setText(listBean.getName());
        bitmapTool.display(vh.icon, listBean.getStandardPic());
        vh.persion.setText("人气：" + listBean.getViewCount() + "人");
        vh.date.setText(TimeTool.getCurrentDate(listBean.getTime().getTime()));
        return convertView;
    }

    class ViewHolder {
        ImageView icon;
        TextView name, persion,date;
    }

}
