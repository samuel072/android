package com.yike.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.yike.R;
import com.yike.bean.FocusPictureModel;
import com.yike.bean.HomePageItemInfo;
import com.yike.tool.BitmapTool;

import java.util.List;

/**
 * Created by wangwei-ps on 15-8-15.
 */
public class HomePageInfoAdapter extends BaseAdapter {
    private List<HomePageItemInfo> list;
    private LayoutInflater inflater;
    private boolean isShowType = true;

    public HomePageInfoAdapter(List<HomePageItemInfo> list, Context context) {
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
            convertView = inflater.inflate(R.layout.item_gridview, null);
            vh = new ViewHolder();
            vh.iconType = (ImageView) convertView.findViewById(R.id.imageView1);
            vh.img = (ImageView) convertView.findViewById(R.id.img_grid);
            vh.title = (TextView) convertView.findViewById(R.id.title_tv);
            vh.viewCount = (TextView) convertView.findViewById(R.id.view_count_tv);
            vh.collectCount = (TextView) convertView.findViewById(R.id.collect_count_tv);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        HomePageItemInfo listBean = list.get(position);
        vh.title.setText(listBean.getName());
        vh.viewCount.setText(String.valueOf(listBean.getViewCount()));
        vh.collectCount.setText(String.valueOf(listBean.getLikeCount()));
        try {
            int category = Integer.parseInt(listBean.getCategory());
            switch (category) {
                // 无
                case 0:
                    // 预约
                case 1:
                    vh.iconType.setVisibility(8);
                    break;
                // 热播
                case 2:
                    vh.iconType.setVisibility(0);
                    vh.iconType.setImageResource(R.drawable.hot);
                    break;
                // 独家
                case 3:
                    vh.iconType.setVisibility(0);
                    //vh.iconType.setImageResource(R.drawable.icon_exclusive);
                    break;
                // 付费
                case 4:
                    vh.iconType.setVisibility(0);
                    //vh.iconType.setImageResource(R.drawable.icon_jiaobiao_pay);
                    break;
                default:
                    break;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            vh.img.setVisibility(8);
        }
        if (!isShowType) {
            vh.iconType.setVisibility(8);
        }

        //vh.time.setText(listBean.getName());
        BitmapTool.getInstance().initAdapterUitl(inflater.getContext())
                .display(vh.img, listBean.getPic());
        return convertView;
    }

    class ViewHolder {
        ImageView iconType, img;
        TextView title, viewCount,collectCount;
    }
}
