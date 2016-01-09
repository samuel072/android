package com.yike.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.yike.R;
import com.yike.bean.TempmMuiscBean;
import com.yike.tool.BitmapTool;

/**
 * 
 * @author rendy 音乐节适配器
 * 
 * 
 */
public class RankListAdapter extends BaseAdapter {
	private List<TempmMuiscBean> list;
	private LayoutInflater inflater;
	private BitmapUtils bitmapTool;

	public RankListAdapter(List<TempmMuiscBean> list, LayoutInflater context) {
		super();
		this.list = list;
		this.inflater =context;
		bitmapTool = BitmapTool.getInstance().initAdapterUitl(context.getContext());

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
			convertView = inflater.inflate(R.layout.item_listview_rank, null);
			vh = new ViewHolder();
			vh.icon = (ImageView) convertView.findViewById(R.id.imageView1);
			vh.name = (TextView) convertView.findViewById(R.id.tv_video_name);
			vh.time = (TextView) convertView.findViewById(R.id.textView2);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		TempmMuiscBean listBean = list.get(position);
		vh.name.setText(listBean.getName());
		bitmapTool.display(vh.icon, listBean.getStandardPic());
		vh.time.setText("人气：" + listBean.getViewCount() + "人");
		return convertView;
	}

	class ViewHolder {
		ImageView icon;
		TextView name, time;
	}

}
