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
import com.yike.bean.GridBean;

public class RankGridAdapter extends BaseAdapter {
	private List<GridBean> list;
	private Context context;
	private LayoutInflater inflater;

	public RankGridAdapter(List<GridBean> list, Context context, boolean b) {
		super();
		this.list = list;
		this.context = context;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (list == null) {
			return 0;
		}
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
			convertView = inflater
					.inflate(R.layout.item_gridview_ranking, null);
			vh = new ViewHolder();
			vh.icon = (ImageView) convertView.findViewById(R.id.imageView1);
			vh.img = (ImageView) convertView.findViewById(R.id.img_grid);
			vh.name = (TextView) convertView.findViewById(R.id.text_name);
			vh.time = (TextView) convertView.findViewById(R.id.textView2);
			vh.buy = (TextView) convertView.findViewById(R.id.textView4);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		GridBean listBean = list.get(position);
		vh.name.setText(listBean.getName());
		vh.time.setText(listBean.getTime());

		return convertView;
	}

	class ViewHolder {
		ImageView icon, img;
		TextView name, time, buy;
	}

}
