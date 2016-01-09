package com.yike.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yike.R;
import com.yike.bean.LatestVideo;
import com.yike.bean.VideoPlayInfo;

public class PlayListAdapter extends BaseAdapter {
	private List<LatestVideo<VideoPlayInfo>> list;
	private Context context;
	private LayoutInflater inflater;

	public PlayListAdapter(List<LatestVideo<VideoPlayInfo>> latestVideos,
			Context context) {
		super();
		this.list = latestVideos;
		this.context = context;
		this.inflater = LayoutInflater.from(context);
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
			convertView = inflater.inflate(R.layout.item_twoline, null);
			vh = new ViewHolder();
			vh.name = (TextView) convertView.findViewById(R.id.tv_video_name);
			vh.time = (TextView) convertView.findViewById(R.id.textView2);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		LatestVideo<VideoPlayInfo> latestVideo = list.get(position);
		vh.name.setText(latestVideo.getName());

		return convertView;
	}

	class ViewHolder {
		TextView name, time;
	}

}
