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

/**
 * 
 * @author rendy 音乐列表 预约适配器
 */
public class PageYuyueAdapter extends BaseAdapter {
	private List<FocusPictureModel> list;
	private Activity context;
	private LayoutInflater inflater;
	private OnClickListener listener;
	private Map<Integer, View> views = new HashMap<Integer, View>();

	public PageYuyueAdapter(List<FocusPictureModel> list, Activity context) {
		super();
		this.list = list;
		this.context = context;
		this.inflater = LayoutInflater.from(context);
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

	public void setDatas(List<FocusPictureModel> list) {
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
		convertView = views.get(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_listview, null);
			views.put(position, convertView);
			vh = new ViewHolder();
			vh.icon = (ImageView) convertView.findViewById(R.id.imageView1);
			vh.img = (ImageView) convertView.findViewById(R.id.imageView2);
			vh.name = (TextView) convertView.findViewById(R.id.tv_video_name);
			vh.type = (Button) convertView.findViewById(R.id.button1);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		FocusPictureModel listBean = list.get(position);
		vh.name.setText(listBean.getName());
		if (listBean.getIsSubscribe() == 1) {
			// vh.img.setVisibility(View.VISIBLE);
			// LogUtils.tiaoshi("已预约", listBean.getName());
			vh.type.setText("已预约");
			vh.type.setBackgroundResource(R.drawable.shape_oval_little_red);
		} else {
			vh.type.setText("预约");
			// LogUtils.tiaoshi("预约", listBean.getName());
			// vh.img.setVisibility(View.GONE);
			vh.type.setBackgroundResource(R.drawable.shape_oval_default_color);
		}
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
