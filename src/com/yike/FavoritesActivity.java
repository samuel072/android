package com.yike;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.http.RequestParams;
import com.yike.action.ServiceAction;
import com.yike.adapter.FavAdapter;
import com.yike.bean.FavDataItem;
import com.yike.bean.UserBean;
import com.yike.service.FavPageService;
import com.yike.service.UserService;
import com.yike.tool.SendActtionTool;
import com.yike.tool.UrlTool;
import com.yike.utils.Contansts;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hmj on 2015/8/16.
 */
public class FavoritesActivity extends BaseActivity {

    private ImageView mBack;
    private ImageView mEdit_Cancel;
    private ListView mListView;
    private LinearLayout mBottomLayout;
    private ImageView mLeftButton;
    private ImageView mRightButton;
    private FavAdapter mFavAdapter;
    private boolean mEditMode;

    private static final int TYPE_EDITI = 1;
    private static final int TYPE_CANCEL = 2;

    private static final int CHECK_STATUS_TYPE_CHECKALL = 3;//全选
    private static final int CHECK_STATUS_TYPE_CANCEL_CHECKALL = 4;//取消全选

    @Override
    public void setContentView() {
        setContentView(R.layout.layout_favorites);
        loadFavData();
    }

    private void loadFavData() {
        UserBean bean = UserService.getInatance().getUserBean();
        RequestParams params = null;
        if (bean != null) {
            params = UrlTool.getParams(Contansts.USER_ID, bean.getId(), Contansts.PAGE, Contansts.UserParams.DEFAULT_FAV_PAGE,
                    Contansts.SIZE, Contansts.UserParams.DEFAULT_FAV_PAGE_SIZE);
            SendActtionTool.get(Contansts.UserParams.URL_FAVORITES, ServiceAction.Action_fav, null, this, params);
        } else {
            Toast.makeText(this, "请先登录！", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, Contansts.ResultConst.REQUEST_CODE_FAV_REQUEST_LOGIN);
        }
    }

    @Override
    public void findViewById() {
        initViews();
    }

    private void initViews() {
        mBack = (ImageView) findViewById(R.id.btn_return);
        mBack.setOnClickListener(this);

        mEdit_Cancel = (ImageView) findViewById(R.id.btn_edit);
        mEdit_Cancel.setTag(TYPE_EDITI);
        mEdit_Cancel.setOnClickListener(this);

        mListView = (ListView) findViewById(R.id.listview);

        mBottomLayout = (LinearLayout) findViewById(R.id.bottom_edit_layout);
        mBottomLayout.setVisibility(View.GONE);

        mLeftButton = (ImageView) findViewById(R.id.left_button);
        mLeftButton.setOnClickListener(this);
        mLeftButton.setTag(CHECK_STATUS_TYPE_CHECKALL);

        mRightButton = (ImageView) findViewById(R.id.right_button);
        mRightButton.setOnClickListener(this);

        List<FavDataItem> datas = FavPageService.getInstance().getFavDataItems();
        if (datas == null) {
            datas = new ArrayList<FavDataItem>();
        }
        mFavAdapter = new FavAdapter(datas, this);
        mListView.setAdapter(mFavAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);
        updateFavList();
    }

    public void setEditMode(boolean edit) {
        mEditMode = edit;
        mFavAdapter.setEditMode(edit);
        if (mEditMode) {
            mBottomLayout.setVisibility(View.VISIBLE);
            mEdit_Cancel.setImageResource(R.drawable.fav_cancel_button);
            mEdit_Cancel.setTag(TYPE_CANCEL);
            checkAllCheckStatus();
            checkDeletebuttonStatus();
        } else {
            mEdit_Cancel.setImageResource(R.drawable.edit_button);
            mEdit_Cancel.setTag(TYPE_EDITI);
            mBottomLayout.setVisibility(View.GONE);
        }
    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            if (mEditMode) {
                mFavAdapter.changeSelectedStatus(position);
                checkAllCheckStatus();
                checkDeletebuttonStatus();
            } else {
                // do nothing
            }
        }
    };

    private void checkAllCheckStatus() {
        boolean selectedAll = mFavAdapter.isSelectedAll();
        if (selectedAll) {
            mLeftButton.setImageResource(R.drawable.cancel_check_all);
            mLeftButton.setTag(CHECK_STATUS_TYPE_CANCEL_CHECKALL);
        } else {
            mLeftButton.setImageResource(R.drawable.check_all);
            mLeftButton.setTag(CHECK_STATUS_TYPE_CHECKALL);
        }
    }

    private void checkDeletebuttonStatus() {
        boolean hasSelected = mFavAdapter.isHasSelectedItems();
        if (hasSelected) {
            mRightButton.setImageResource(R.drawable.delete_button_click);
        } else {
            mRightButton.setImageResource(R.drawable.delete_button);
        }
    }

    @Override
    public void onBackPressed() {
        if (mEditMode) {
            setEditMode(false);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        if (view == mBack) {
            if (mEditMode) {
                setEditMode(false);
            } else {
                finish();
            }
        } else if (view == mEdit_Cancel) {
            int type = TYPE_EDITI;
            try {
                type = ((Integer) mEdit_Cancel.getTag()).intValue();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (type == TYPE_EDITI) {
                setEditMode(true);
            } else if (type == TYPE_CANCEL) {
                setEditMode(false);
            }
        } else if (view == mLeftButton) {
            int type = ((Integer)mLeftButton.getTag()).intValue();
            if (type == CHECK_STATUS_TYPE_CHECKALL) {
                mFavAdapter.resetSelectedStatus(true);
            } else if (type == CHECK_STATUS_TYPE_CANCEL_CHECKALL) {
                mFavAdapter.resetSelectedStatus(false);
            }
            checkAllCheckStatus();
            checkDeletebuttonStatus();
        } else if (view == mRightButton) {
            doDelete();
        }
    }

    private void doDelete() {
        boolean hasSelected = mFavAdapter.isHasSelectedItems();
        if (!hasSelected) {
            //do nothing
            return;
        }
        List<FavDataItem> selectedItems = mFavAdapter.getSelectedItems();
        if (selectedItems == null || selectedItems.size() == 0) {
            //do nothing
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (FavDataItem item : selectedItems) {
            sb.append(item.getVideoId());
            sb.append(",");
        }
        int length = sb.length();
        int index = sb.lastIndexOf(",");
        if (index == length - 1) {
            sb.deleteCharAt(index);
        }
        String key = sb.toString();

        UserBean bean = UserService.getInatance().getUserBean();
        RequestParams params = null;
        if (bean != null) {
            //TODO:由于接口没有完善,暂时采取循环删除
            //params = UrlTool.getParams(Contansts.USER_ID, bean.getId(), Contansts.VIDEO_ID, key);
            //SendActtionTool.get(Contansts.UserParams.URL_DELETE_FAVORITES, ServiceAction.Action_Delete_Fav, null, this, params);

            for (FavDataItem item : selectedItems) {
                params = UrlTool.getParams(Contansts.USER_ID, bean.getId(), Contansts.VIDEO_ID, item.getVideoId()+"");
                SendActtionTool.get(Contansts.UserParams.URL_DELETE_FAVORITES, ServiceAction.Action_Delete_Fav, null, this, params);
            }
            mFavAdapter.removeDatas(selectedItems);
            setEditMode(false);
        } else {
            Toast.makeText(this, "请先登录！", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, Contansts.ResultConst.REQUEST_CODE_FAV_REQUEST_LOGIN);
        }
    }

    @Override
    public void onSuccess(ServiceAction service, Object action, Object value) {
        JSONObject obj = (JSONObject) value;
        switch (service) {
            case Action_fav:
                Log.e("ww", "fav =" + obj.toString());
                parseFavData(obj);
                break;

            case Action_Delete_Fav:
                Log.e("ww", "delete fav status=" + obj.toString());
                int status = obj.optInt("status", -1);
                if (status == 1) {
                    Toast.makeText(FavoritesActivity.this, R.string._del_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FavoritesActivity.this, R.string.delete_fav_fail, Toast.LENGTH_SHORT).show();
                    //重新加载新数据
                    loadFavData();
                }
                Intent intent = new Intent(Contansts.FAV_DATA_CHANGED_RECEIVER);
                FavoritesActivity.this.sendBroadcast(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onFaile(ServiceAction service, Object action, Object value) {
        super.onFaile(service, action, value);
        switch (service) {
            case Action_fav:
                break;
            case Action_Delete_Fav:
                Log.e("ww", "delete fav fail msg=" + value.toString());
                Toast.makeText(FavoritesActivity.this, R.string.delete_fav_fail, Toast.LENGTH_SHORT).show();
                //重新加载新数据
                loadFavData();
                break;
            default:
                break;
        }
    }

    private void parseFavData(JSONObject object) {
        try {
            JSONArray array = object.getJSONArray(Contansts.DATA);
            List<FavDataItem> datas = JSON.parseArray(array.toString(), FavDataItem.class);
            FavPageService.getInstance().setFavDataItems(datas);
            updateFavList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateFavList() {
        List<FavDataItem> datas = FavPageService.getInstance().getFavDataItems();

        //test ========start ============
        //if (datas == null || datas.size() == 0) {
        //    if (datas == null) {
        //        datas = new ArrayList<FavDataItem>();
        //    }
        //    int len = 1;
        //    for (int i = 0; i < len; i++) {
        //        FavDataItem item = new FavDataItem();
        //        item.setAlbumName("北大演讲《梦想还是要有的》");
        //        item.setVideoId(1);
        //        datas.add(item);
        //    }
        //}
        //test =========end =============
        if (datas == null || datas.size() == 0) {
            mListView.setVisibility(View.GONE);
        }
        if (datas != null && datas.size() > 0) {
            mListView.setVisibility(View.VISIBLE);
            mFavAdapter.setDatas(datas);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //登录成功后返回，重新拉取数据
        if (requestCode == Contansts.ResultConst.REQUEST_CODE_FAV_REQUEST_LOGIN) {
            loadFavData();
        }
    }
}
