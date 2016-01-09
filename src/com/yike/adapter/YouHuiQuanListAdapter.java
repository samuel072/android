package com.yike.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yike.R;
import com.yike.bean.Ticket;
import com.yike.bean.YouHuiQuan;
import com.yike.tool.TimeTool;

public class YouHuiQuanListAdapter extends BaseAdapter {
	private List<YouHuiQuan> list;
	private LayoutInflater inflater;

	public YouHuiQuanListAdapter(List<YouHuiQuan> list, Context context) {
		super();
		this.list = list;
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
			convertView = inflater.inflate(R.layout.item_listview_youhuiquan,
					null);
			vh = new ViewHolder();
			vh.name = (TextView) convertView.findViewById(R.id.tv_video_name);
			vh.youhuima = (TextView) convertView.findViewById(R.id.tv_youhuima);
			vh.isUse = (TextView) convertView.findViewById(R.id.tv_shiyong);
			vh.endTime = (TextView) convertView.findViewById(R.id.textView2);
			vh.youhuiMoney = (TextView) convertView
					.findViewById(R.id.textView4);
			vh.moeny = (TextView) convertView
					.findViewById(R.id.tv_youhui_moeny);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		YouHuiQuan ticket = list.get(position);
		if (ticket != null) {
			String name = getName(ticket);

			vh.name.setText(name);
			vh.youhuima.setText("优惠码：" + ticket.getCode());
			if (ticket.getIsUse() == 0) {
				vh.isUse.setText("未使用");
			} else
				vh.isUse.setText("已使用");
			vh.endTime.setText("使用有效期："
					+ TimeTool.getDayTime(ticket.getEndTime()));
			if (ticket.getType() == 3) {
				vh.youhuiMoney.setVisibility(0);
				vh.moeny.setVisibility(0);
				vh.moeny.setText(ticket.getMoney() + "元");
			} else {
				vh.youhuiMoney.setVisibility(8);
				vh.moeny.setVisibility(8);
			}
		}

		return convertView;
	}

	private String getName(YouHuiQuan ticket) {
		String name = "";
		switch (ticket.getVideoType()) {
		case 1:
			name = "全场通用";
			break;
		case 100:
			name = "音乐节";
			break;
		case 101:
			name = "演唱会";
			break;
		case 102:
			name = "Live House";
			break;
		case 103:
			name = "MTV";
			break;

		default:
			break;
		}
		switch (ticket.getType()) {
		case 1:
			name = name + "单日抵用券";
			break;
		case 2:
			name = name + "通票抵用券";
			break;
		case 3:
			name = name + "优惠券";
			break;

		default:
			break;
		}
		return name;
	}

	class ViewHolder {
		TextView name, youhuima, isUse, endTime, moeny, youhuiMoney;
	}

}
