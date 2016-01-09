package com.yike.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.lidroid.xutils.BitmapUtils;
import com.yike.R;
import com.yike.bean.LiveItemInfo;
import com.yike.tool.BitmapTool;

import java.util.List;

/**
 * Created by wangwei-ps on 15-8-15.
 */
public class SoonZhiBoAdapter extends BaseAdapter {
    private List<LiveItemInfo> list;
    private LayoutInflater inflater;
    private BitmapUtils bitmapTool;

    public static final int TYPE_RESERVATION_CLICK = 1;

    public SoonZhiBoAdapter(List<LiveItemInfo> list, Context context) {
        this.list = list;
        bitmapTool = BitmapTool.getInstance().initAdapterUitl(context);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        }
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
        LiveItemInfo model = list.get(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_live_gohome, null);
            vh = new ViewHolder();
            vh.img = (ImageView) convertView.findViewById(R.id.item_live_pic);
            vh.iconType = (ImageView) convertView.findViewById(R.id.imageView1);
            vh.name = (TextView) convertView.findViewById(R.id.tv_name);
            vh.reservation = (ImageView) convertView.findViewById(R.id.img_reservation);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.name.setText(model.getName());
        try {
            int category = Integer.parseInt(model.getCategory());
            switch (category) {
                // 无
                case 0:
                    // 预约
                case 1:
                    vh.iconType.setVisibility(View.GONE);
                    break;
                // 热播
                case 2:
                    vh.iconType.setVisibility(View.VISIBLE);
                    vh.iconType.setImageResource(R.drawable.hot);
                    break;
                // 独家
                case 3:
                    vh.iconType.setVisibility(View.VISIBLE);
                    //vh.iconType.setImageResource(R.drawable.icon_exclusive);
                    break;
                // 付费
                case 4:
                    vh.iconType.setVisibility(View.VISIBLE);
                    //vh.iconType.setImageResource(R.drawable.icon_jiaobiao_pay);
                    break;
                default:
                    break;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        vh.reservation.setOnClickListener(new OnItemViewClickListener((AdapterView) parent, convertView, position, position + TYPE_RESERVATION_CLICK));
        bitmapTool.display(vh.img, model.getPic());
        return convertView;
    }

    class ViewHolder {
        ImageView iconType, img;
        TextView name;
        ImageView reservation;
    }

    private class OnItemViewClickListener implements View.OnClickListener {

        private int mPosition;
        private AdapterView mAdapterView;
        private View mParent;
        private int mId;

        public OnItemViewClickListener(AdapterView view, View parent, int position, int id) {
            mPosition = position;
            mAdapterView = view;
            mParent = parent;
            mId = id;
        }

        @Override
        public void onClick(View v) {
            AdapterView.OnItemClickListener listener = mAdapterView.getOnItemClickListener();
            if (listener != null) {
                listener.onItemClick(mAdapterView, mParent, mPosition, mId);
            }
        }

    }
}
