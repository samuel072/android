package com.yike.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.yike.R;
import com.yike.bean.SearchGuessLikeItem;
import com.yike.tool.BitmapTool;

import java.util.List;

/**
 * Created by hmj on 2015/8/16.
 */
public class GuessLikeAdapter extends BaseAdapter {
    private List<SearchGuessLikeItem> list;
    private LayoutInflater inflater;
    private boolean isShowType = true;

    public GuessLikeAdapter(List<SearchGuessLikeItem> list, Context context) {
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
            convertView = inflater.inflate(R.layout.search_item_gridview, null);
            vh = new ViewHolder();
            vh.img = (ImageView) convertView.findViewById(R.id.img_grid);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        SearchGuessLikeItem listBean = list.get(position);
        BitmapTool.getInstance().initAdapterUitl(inflater.getContext())
                .display(vh.img, listBean.getPic());
        return convertView;
    }

    static class ViewHolder {
        ImageView img;
    }
}
