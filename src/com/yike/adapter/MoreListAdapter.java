package com.yike.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.yike.R;
import com.yike.MainActivity;
import com.yike.bean.FocusPictureModel;
import com.yike.service.FirstPageService;
import com.yike.tool.BitmapTool;
import com.yike.utils.Utils;

/**
 * 
 * @author rendy 首页更多界面
 */
public class MoreListAdapter extends BaseAdapter {
	private List<FocusPictureModel> list;
	private LayoutInflater inflater;
	private BitmapUtils bitmapTool;
	private MainActivity aty;

	public MoreListAdapter(List<FocusPictureModel> list, MainActivity context) {
		this.list = list;
		this.inflater = context.getLayoutInflater();
		this.aty = context;
		bitmapTool = BitmapTool.getInstance().getAdapterUitl();
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
		FocusPictureModel model = list.get(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_morelistview, null);
			vh = new ViewHolder();
			vh.title = (TextView) convertView.findViewById(R.id.music_type);
			vh.img = (ImageView) convertView.findViewById(R.id.img_grid);
			vh.img.setOnClickListener(listener);
			vh.name = (Button) convertView.findViewById(R.id.more);
			vh.name.setOnClickListener(listener);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		vh.img.setTag(position);
		vh.name.setText(model.getName());
		bitmapTool.display(vh.img, model.getPic());
		return convertView;
	}

	class ViewHolder {
		ImageView img;
		TextView time, title;
		Button name;
	}

	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.img_grid:
				FocusPictureModel model = FirstPageService.getInstance()
						.getMoreDatas().getData().get((Integer) v.getTag());
				Utils.playVideo(aty, model.getVideoId(), model.getName(),
						model.getAlbumId());
				break;
			case R.id.more:
				aty.changeToyinYuejie();
				break;
			default:
				break;
			}
		}
	};
}
