package com.yike.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.yike.R;
import com.yike.bean.Content;
import com.yike.bean.UserEntity;
import com.yike.bean.VideoPinglun;
import com.yike.fragment.PlayFragment;
import com.yike.fragment.PlayFragment.OnDianZanListener;
import com.yike.tool.BitmapTool;
import com.yike.tool.TimeTool;
import com.yike.utils.Utils;
import com.yike.view.MyGridView;
import com.yike.view.RoundAngleImageView;

public class PlayPinglunListAdapter extends BaseAdapter implements
		OnScrollListener {
	private List<VideoPinglun> list;
	private LayoutInflater inflater;
	private BitmapUtils bitmapTool;
	private Context mContext;
	private PlayFragment fragment;
	private boolean isScroll;

	public PlayPinglunListAdapter(List<VideoPinglun> pingluns,
			Activity context, PlayFragment fragment) {
		super();
		this.list = pingluns;
		this.mContext = context;
		this.inflater = context.getLayoutInflater();
		bitmapTool = BitmapTool.getInstance().getAdapterUitl();
		this.fragment = fragment;

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
					.inflate(R.layout.item_listview_pinglun, null);
			vh = new ViewHolder();
			vh.imageView = (RoundAngleImageView) convertView
					.findViewById(R.id.img_grid);
			vh.name = (TextView) convertView.findViewById(R.id.tv_video_name);
			vh.text = (TextView) convertView.findViewById(R.id.textView2);
			vh.time = (TextView) convertView.findViewById(R.id.time);
			vh.good = (TextView) convertView.findViewById(R.id.pinlun_good);
			vh.pinglun = (TextView) convertView.findViewById(R.id.pinlun_count);
			vh.myGridView = (MyGridView) convertView
					.findViewById(R.id.gridview_paly_pinglun);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		VideoPinglun videoPinglun = list.get(position);
		setViewHolder(vh, videoPinglun, position);

		return convertView;
	}

	private void setViewHolder(final ViewHolder vh,
			final VideoPinglun videoPinglun, final int position) {
		final UserEntity userEntity = videoPinglun.getUserEntity();
		if (!isScroll) {
			bitmapTool.display(vh.imageView, userEntity.getFaceUrl());
		}
		vh.name.setText(userEntity.getUserName());
		vh.time.setText(TimeTool.getTimeString(videoPinglun.getBuildTime(),
				mContext));
		vh.good.setText("赞(" + videoPinglun.getPraiseCount() + ")");

		vh.pinglun.setText("评论(" + videoPinglun.getCommentCount() + ")");

		vh.good.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				vh.good.setEnabled(false);
				if (videoPinglun.getIsPraise() == 1) {
					Utils.toast(mContext, "您已经过点赞了！");
				} else {
					fragment.dianZan(videoPinglun.getId(), userEntity.getId(),
							new OnDianZanListener() {

								@Override
								public void onCompletion(String commentId,
										int count) {
									if (commentId.equals(videoPinglun.getId())) {
										vh.good.setText("赞(" + count + ")");
										videoPinglun.setPraiseCount(count);
									}

								}
							});
				}
			}
		});

		List<Content> contents = videoPinglun.getContent();
		List<Content> tempContents = new ArrayList<Content>();
		if (contents == null) {
			vh.myGridView.setVisibility(View.GONE);
		} else {
			for (int i = contents.size() - 1; i >= 0; i--) {
				Content map = contents.get(i);
				if (map.getType().equals("1")) {
					vh.text.setText(map.getContent());
				} else {
					tempContents.add(map);
				}
			}
			if (contents.size() > 0) {
				vh.myGridView.setVisibility(View.VISIBLE);
				PinglunGridAdapter pinglunGridAdapter = new PinglunGridAdapter(
						tempContents, mContext);
				vh.myGridView.setAdapter(pinglunGridAdapter);
				// Utils.setGridViewHeightBasedOnChildren(vh.myGridView, 3);
			}

		}
	}

	class ViewHolder {
		RoundAngleImageView imageView;
		TextView name, text, time, good, pinglun;
		MyGridView myGridView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AbsListView.OnScrollListener#onScrollStateChanged(android
	 * .widget.AbsListView, int)
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		switch (scrollState) {
		case OnScrollListener.SCROLL_STATE_FLING:
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
			isScroll = true;
			break;
		case OnScrollListener.SCROLL_STATE_IDLE:
			isScroll = false;
			break;

		default:
			break;

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.AbsListView.OnScrollListener#onScroll(android.widget.
	 * AbsListView, int, int, int)
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

	}

}
