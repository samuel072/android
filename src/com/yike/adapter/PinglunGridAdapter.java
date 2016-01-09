package com.yike.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.yike.R;
import com.yike.bean.Content;
import com.yike.tool.BitmapTool;
import com.yike.utils.Contansts;

public class PinglunGridAdapter extends BaseAdapter {
	private List<Content> list;
	private LayoutInflater inflater;
	private BitmapUtils bitmapTool;

	public PinglunGridAdapter(List<Content> contents, Context context) {
		super();
		this.list = contents;
		this.inflater = LayoutInflater.from(context);
		bitmapTool = BitmapTool.getInstance().initAdapterUitl(context);

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
			convertView = inflater
					.inflate(R.layout.item_gridview_pinglun, null);
			vh = new ViewHolder();
			vh.imageView = (ImageView) convertView.findViewById(R.id.img_grid);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		Content map = list.get(position);
		bitmapTool.display(vh.imageView, map.getContent());

		return convertView;
	}

	class ViewHolder {
		ImageView imageView;
	}

}
