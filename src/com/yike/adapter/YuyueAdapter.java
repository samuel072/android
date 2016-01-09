package com.yike.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yike.R;
import com.yike.bean.FocusPictureModel;
import com.yike.bean.YuyueBean;
import com.yike.utils.LogUtils;

/**
 * 
 * @author rendy 音乐列表 预约适配器
 */
public class YuyueAdapter extends BaseAdapter {
	private List<YuyueBean> list;
	private LayoutInflater inflater;
	private OnClickListener listener;

	public YuyueAdapter(List<YuyueBean> list, LayoutInflater lay) {
		super();
		this.list = list;
		this.inflater = lay;
	}

	public void setOnClicklistener(OnClickListener listener) {
		this.listener = listener;
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

	public void setDatas(List<YuyueBean> list) {
		this.list = list;
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
			convertView = inflater.inflate(R.layout.item_listview, null);
			vh = new ViewHolder();
			vh.icon = (ImageView) convertView.findViewById(R.id.imageView1);
			vh.img = (ImageView) convertView.findViewById(R.id.imageView2);
			vh.name = (TextView) convertView.findViewById(R.id.tv_video_name);
			vh.type = (Button) convertView.findViewById(R.id.button1);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		YuyueBean listBean = list.get(position);
		vh.name.setText(listBean.getAlbumName());
		vh.type.setText(inflater.getContext().getText(R.string._already_yuyue));
		vh.type.setBackgroundResource(R.drawable.shape_oval_little_red);
		vh.type.setTag(listBean);
		if (listener != null) {
			vh.type.setOnClickListener(listener);
		}
		return convertView;
	}

	class ViewHolder {
		ImageView icon, img;
		TextView name;
		Button type;
	}
}
