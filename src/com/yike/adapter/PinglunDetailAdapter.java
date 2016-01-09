package com.yike.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.yike.R;
import com.yike.bean.Content;
import com.yike.bean.Huifu;
import com.yike.bean.UserEntity;
import com.yike.tool.BitmapTool;
import com.yike.tool.TimeTool;

public class PinglunDetailAdapter extends BaseAdapter {
	private List<Huifu> list;
	private BitmapUtils bitTools;
	private Context context;
	private LayoutInflater inflater;
	private String headUserId;
	private ForegroundColorSpan blueSpan;

	public PinglunDetailAdapter(List<Huifu> list2, String headUserId,
			Context context) {
		super();
		this.list = list2;
		this.headUserId = headUserId;
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.bitTools = BitmapTool.getInstance().getAdapterUitl();
		blueSpan = new ForegroundColorSpan(context.getResources().getColor(
				R.color.blue_green));
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
			convertView = inflater.inflate(
					R.layout.item_listview_pinglun_detail, null);
			vh = new ViewHolder();
			vh.img = (ImageView) convertView.findViewById(R.id.img_grid);
			vh.name = (TextView) convertView.findViewById(R.id.tv_video_name);
			vh.time = (TextView) convertView
					.findViewById(R.id.item_pinglun_detial_time);
			vh.text = (TextView) convertView.findViewById(R.id.textView2);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		Huifu pinglun = list.get(position);
		List<Content> contents = pinglun.getContent();
		if (pinglun != null) {
			vh.time.setText(TimeTool.getTimeString(pinglun.getBuildTime(),
					context));

			UserEntity userEntity = pinglun.getUserEntity();
			UserEntity toUserEntity = pinglun.getToUserEntity();
			boolean showHuifu = false;
			if (userEntity != null) {
				vh.name.setText(userEntity.getUserName());
				bitTools.display(vh.img, userEntity.getFaceUrl());
				if (toUserEntity != null) {
					if ((!toUserEntity.getId().equals(userEntity.getId()))
							&& (!toUserEntity.getId().equals(headUserId))) {
						showHuifu = true;
					}
				}
			}
			if (contents != null && contents.size() > 0) {
				Content mContent = contents.get(0);
				if (showHuifu) {
					String content = "回复 " + toUserEntity.getUserName() + " "
							+ mContent.getContent();
					Spannable word = new SpannableString(content);

					word.setSpan(blueSpan, 3, 3 + toUserEntity.getUserName()
							.length(),

					Spannable.SPAN_INCLUSIVE_INCLUSIVE);

					vh.text.setText(word);
				} else
					vh.text.setText(mContent.getContent());
			}
		}

		return convertView;
	}

	class ViewHolder {
		TextView name, time, text;
		ImageView img;
	}

}
