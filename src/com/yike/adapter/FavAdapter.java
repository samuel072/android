package com.yike.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.yike.R;
import com.yike.bean.FavDataItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hmj on 2015/8/16.
 */
public class FavAdapter extends BaseAdapter {

    private List<FavDataItem> list;
    private Activity context;
    private LayoutInflater inflater;
    private boolean isEdit;
    private boolean[] selectedItems;

    public FavAdapter(List<FavDataItem> list, Activity context) {
        super();
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setEditMode(boolean edit) {
        isEdit = edit;
        if (!isEdit) {
            resetSelectedStatus(false);
        }
    }

    public boolean isEditMode() {
        return isEdit;
    }

    public void changeSelectedStatus(int position) {
        if (selectedItems == null) {
            selectedItems = new boolean[getCount()];
        }
        if (position >= 0 && position < getCount()) {
            selectedItems[position] = !selectedItems[position];
        }
        notifyDataSetChanged();
    }

    public void resetSelectedStatus(boolean checked) {
        if (selectedItems == null) {
            selectedItems = new boolean[getCount()];
        }
        for (int i = 0; i < selectedItems.length; i++) {
            selectedItems[i] = checked;
        }
        notifyDataSetChanged();
    }

    public boolean isSelectedAll() {
        if (!isEdit) {
            return false;
        }
        if (selectedItems == null) {
            return false;
        }
        for (int i = 0; i < selectedItems.length; i++) {
            if (!selectedItems[i]) {
                return false;
            }
        }
        return true;
    }

    public boolean isSelectedByPosition(int position) {
        if (selectedItems == null) {
            return false;
        }
        return selectedItems[position];
    }

    public boolean isHasSelectedItems() {
        if (selectedItems == null) {
            return false;
        }
        for (int i = 0; i < selectedItems.length; i++) {
            if (selectedItems[i]) {
                return true;
            }
        }
        return false;
    }

    public List<FavDataItem> getSelectedItems() {
        if (selectedItems == null) {
            return null;
        }
        ArrayList<FavDataItem> items = new ArrayList<FavDataItem>();
        for (int i = 0; i < selectedItems.length; i++) {
            if (selectedItems[i]) {
                items.add(getItem(i));
            }
        }
        return items;
    }

    @Override
    public int getCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    @Override
    public FavDataItem getItem(int position) {
        return list.get(position);
    }

    public void setDatas(List<FavDataItem> list) {
        this.list = list;
        selectedItems = null;
        notifyDataSetChanged();
    }

    public void removeDatas(List<FavDataItem> infos) {
        list.removeAll(infos);
        selectedItems = null;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fav_item, null);
            //views.put(position, convertView);
            vh = new ViewHolder();
            vh.state = (ImageView) convertView.findViewById(R.id.mark_select_status);
            vh.name = (TextView) convertView.findViewById(R.id.fav_name);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        FavDataItem info = list.get(position);
        vh.name.setText(info.getAlbumName());
        if (isEdit) {
            vh.state.setVisibility(View.VISIBLE);
            boolean checked = isSelectedByPosition(position);
            vh.state.setImageResource(checked ? R.drawable.fav_selected : R.drawable.fav_unselected);
        } else {
            vh.state.setVisibility(View.GONE);
            vh.state.setImageResource(R.drawable.fav_unselected);
        }
        //convertView.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Toast.makeText(context, "position="+position, Toast.LENGTH_SHORT).show();
        //    }
        //});
        return convertView;
    }

    class ViewHolder {
        ImageView state;
        TextView name;
    }
}
