package com.yike.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yike.R;
import com.yike.bean.ExploreCategoryInfo;
import com.yike.bean.FocusPictureModel;
import com.yike.tool.BitmapTool;

/**
 * @author rendy
 *         视屏信息分类 适配器
 */
public class ExploreTypeAdapter extends BaseAdapter {
    private List<ExploreCategoryInfo> list;
    private LayoutInflater inflater;
    private boolean isShowType = true;

    public ExploreTypeAdapter(List<ExploreCategoryInfo> list, Context context) {
        super();
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    public void setIsShow(boolean type) {
        this.isShowType = type;
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
            convertView = inflater.inflate(R.layout.item_gridview_explore, null);
            vh = new ViewHolder();
            vh.img = (ImageView) convertView.findViewById(R.id.img_grid);
            vh.name = (TextView) convertView.findViewById(R.id.name);
            vh.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        ExploreCategoryInfo listBean = list.get(position);

        vh.time.setText(String.valueOf(listBean.getStartTime().getMonth()) + "-" + String.valueOf(listBean.getStartTime().getDate()));
        vh.name.setText(listBean.getTitle());
        BitmapTool.getInstance().initAdapterUitl(inflater.getContext())
                .display(vh.img, listBean.getThumbnail());
        return convertView;
    }

    class ViewHolder {
        ImageView img;
        TextView name, time;
    }

}
