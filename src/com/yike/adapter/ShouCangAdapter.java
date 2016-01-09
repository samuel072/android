package com.yike.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.yike.R;
import com.yike.adapter.BofangLeaveAdapter.ViewHolder;
import com.yike.bean.ShouCangBean;
import com.yike.tool.BitmapTool;

/**
 * 
 * @author rendy 收藏adapter
 * 
 * 
 */
public class ShouCangAdapter extends BaseAdapter {
	private List<ShouCangBean> list;
	private LayoutInflater inflater;
	private OnClickListener listener;
	private int windowWidth;

	public ShouCangAdapter(List<ShouCangBean> list, LayoutInflater context,
			int width) {
		super();
		this.windowWidth = width;
		this.list = list;
		this.inflater = context;
	}

	public void setOnclickListener(OnClickListener listen) {
		this.listener = listen;
	}

	@Override
	public int getCount() {
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
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_shoucang, null);
			vh = new ViewHolder();
			vh.icon = (ImageView) convertView.findViewById(R.id.imageView1);
			vh.name = (TextView) convertView.findViewById(R.id.tv_video_name);
			vh.horiScrollView = (HorizontalScrollView) convertView;
			vh.deleteButton = (Button) convertView
					.findViewById(R.id.item_delete_shoucangBtn);
			vh.leftContent = convertView.findViewById(R.id.party_left_content);
			vh.horiScrollView.setOnTouchListener(touchlistener);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		LayoutParams params = vh.leftContent.getLayoutParams();
		params.width = windowWidth - 30;
		vh.leftContent.setLayoutParams(params);

		ShouCangBean listBean = list.get(position);
		// 添加删除 监听
		if (listener != null) {
			vh.deleteButton.setTag(listBean);
			vh.deleteButton.setOnClickListener(listener);
		}
		vh.name.setText(listBean.getAlbumName());
		BitmapTool.getInstance().getAdapterUitl()
				.display(vh.icon, listBean.getVideoPic());

		return convertView;
	}

	OnTouchListener touchlistener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_UP:
				// 获得ViewHolder
				ViewHolder viewHolder = (ViewHolder) v.getTag();

				// 获得HorizontalScrollView滑动的水平方向值.
				int scrollX = viewHolder.horiScrollView.getScrollX();
				// 获得操作区域的长度
				int actionW = viewHolder.deleteButton.getWidth();

				// 注意使用smoothScrollTo,这样效果看起来比较圆滑,不生硬
				// 如果水平方向的移动值<操作区域的长度的一半,就复原
				if (scrollX < actionW / 2) {
					viewHolder.horiScrollView.smoothScrollTo(0, 0);
				} else// 否则的话显示操作区域
				{
					viewHolder.horiScrollView.smoothScrollTo(actionW * 2, 0);
				}
				return true;
			}
			return false;
		}
	};

	class ViewHolder {
		ImageView icon;
		TextView name;
		HorizontalScrollView horiScrollView;
		View leftContent;
		Button deleteButton;
	}

}
