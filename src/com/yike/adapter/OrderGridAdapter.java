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

public class OrderGridAdapter extends BaseAdapter {
	private List<Ticket> list;
	private LayoutInflater inflater;

	public OrderGridAdapter(List<Ticket> list, Context context) {
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
			convertView = inflater.inflate(R.layout.item_gridview_order, null);
			vh = new ViewHolder();
			vh.name = (TextView) convertView.findViewById(R.id.tv_order_name);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		Ticket ticket = list.get(position);
		if (ticket != null) {
			vh.name.setText(ticket.getName());
			if (ticket.getIsPay() != 0) {
				vh.name.setEnabled(false);
			} else
				vh.name.setEnabled(true);

		}

		return convertView;
	}

	class ViewHolder {
		TextView name;
	}

}
