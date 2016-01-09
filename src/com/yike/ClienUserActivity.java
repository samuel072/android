package com.yike;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.lidroid.xutils.http.RequestParams;
import com.yike.R;
import com.yike.action.PayAction;
import com.yike.action.ServiceAction;
import com.yike.action.UserAction;
import com.yike.adapter.BofangLeaveAdapter;
import com.yike.adapter.ShouCangAdapter;
import com.yike.adapter.YouHuiQuanListAdapter;
import com.yike.adapter.YuyueAdapter;
import com.yike.bean.BofangBean;
import com.yike.bean.ResultList;
import com.yike.bean.ShouCangBean;
import com.yike.bean.UserBean;
import com.yike.bean.YouHuiQuan;
import com.yike.bean.YuyueBean;
import com.yike.service.UserService;
import com.yike.tool.BitmapTool;
import com.yike.tool.SendActtionTool;
import com.yike.tool.UrlTool;
import com.yike.utils.Contansts;
import com.yike.utils.GeneralTool;
import com.yike.utils.LogUtils;
import com.yike.utils.Utils;
import com.yike.utils.Contansts.UserParams;
import com.yike.view.NoScroListView;

/**
 * 
 * @author rendy
 * 
 *         当前手机用户个人中心
 */
public class ClienUserActivity extends BaseActivity {
	// 播放记录 我的收藏 我的预约
	private View areaBofangLeave, areashouCang, areaYuyue, areaYouhuiQuan;
	private NoScroListView listViewYuyue, listViewBofang, listViewShoucang,
			listViewYouhuiQuan;
	private ImageView userImg;
	private TextView userNameTet;
	private UserBean userBean;
	private List<YuyueBean> dataYuyue;
	private List<BofangBean> dataBofang;
	private List<ShouCangBean> dataShouCangBeans;
	private List<YouHuiQuan> dataYouhuiQuan;

	@Override
	public void setContentView() {
		setContentView(R.layout.activity_user_client);
	}

	@Override
	public void findViewById() {
		userBean = UserService.getInatance().getUserBean();
		LogUtils.tiaoshi("clientUserActivity", userBean.getId());
		userImg = (ImageView) findViewById(R.id.client_userImg);
		userImg.setOnClickListener(this);
		userNameTet = (TextView) findViewById(R.id.client_userNameTet);
		((TextView) findViewById(R.id.tv_title)).setText(R.string._me);
		findViewById(R.id.client_setBtn).setOnClickListener(this);
		findViewById(R.id.img_search).setOnClickListener(this);
		getUrlData();
	}

	/**
	 * 获取网络数据
	 */
	private void getUrlData() {
		showLoginDialog();
		// 得到播放记录视屏
		SendActtionTool.get(UserParams.URL_PLAY_RECORD,
				ServiceAction.Action_User, UserAction.Action_See_Leave, this,
				UrlTool.getParams(UserParams.USER_ID, userBean.getId(),
						Contansts.PAGE, "1", Contansts.SIZE, "4"));
		// 得到我的收藏视屏
		SendActtionTool.get(UserParams.URL_COLLECT_LIST_VIDO,
				ServiceAction.Action_User, UserAction.Action_Shoucang, this,
				UrlTool.getParams(UserParams.USER_ID, userBean.getId(),
						Contansts.PAGE, "1", Contansts.SIZE, "4"));
		// 得到我的预约
		SendActtionTool.get(UserParams.URL_YUYUE_LIST,
				ServiceAction.Action_User, UserAction.Action_Yuyue_LIST, this,
				UrlTool.getParams(UserParams.USER_ID, userBean.getId(),
						Contansts.PAGE, "1", Contansts.SIZE, "4"));
		// 优惠券
		RequestParams params = UrlTool.getParams(Contansts.USER_ID,
				userBean.getId());
		SendActtionTool.post(UserParams.URL_YOUHUIQUAN,
				ServiceAction.Action_Pay, PayAction.Action_getYouHuiQuan, this,
				params);
	}

	/**
	 * 填充个人信息
	 */
	private void initUserInfo() {
		UserBean temp = UserService.getInatance().getUserBean();
		userNameTet.setText(temp.getUserName());
		if (!GeneralTool.isEmpty(temp.getFaceUrl())) {
			BitmapTool.getInstance().getAdapterUitl()
					.display(userImg, temp.getFaceUrl());
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		initUserInfo();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 跳转设置界面
		case R.id.client_setBtn:
			startActivity(new Intent(getApplicationContext(), SetActivity.class));
			break;
		case R.id.client_userImg:
			startActivity(new Intent(getApplicationContext(),
					SetUserInfoActivity.class));
			break;
		case R.id.img_search:
			startActivity(new Intent(getApplicationContext(),
					SearchActivity.class));
			break;
		// 预约取消发送按钮
		case R.id.button1:
			YuyueBean model = (YuyueBean) v.getTag();
			// 取消预约
			UserAction.Action_Yuyue_Cancle.value = model;
			SendActtionTool.get(UserParams.URL_YUYUE_DEL,
					ServiceAction.Action_User, UserAction.Action_Yuyue_Cancle,
					this, UrlTool.getParams(UserParams.USER_ID,
							userBean.getId(), UserParams.ALBUM_ID,
							String.valueOf(model.getAlbumId())));
			showLoginDialog();
			// 添加预约
			break;
		// 删除 观看纪录
		case R.id.item_delete:
			BofangBean bean = (BofangBean) v.getTag();
			UserAction.Action_See_Leave_DEL.value = bean;
			SendActtionTool.get(UserParams.URL_PLAY_RECORD_DEL,
					ServiceAction.Action_User, UserAction.Action_See_Leave_DEL,
					this, UrlTool.getParams(UserParams.USER_ID,
							userBean.getId(), UserParams.VIDEO_ID,
							String.valueOf(bean.getVideoId())));
			showLoginDialog();
			break;
		// 删除收藏的视屏
		case R.id.item_delete_shoucangBtn:
			ShouCangBean shoucang = (ShouCangBean) v.getTag();
			UserAction.Action_Shoucang_Del.value = shoucang;
			SendActtionTool.post(UserParams.URL_Dell_Collect_VIDEO,
					ServiceAction.Action_User, UserAction.Action_Shoucang_Del,
					this, UrlTool.getParams(UserParams.USER_ID,
							userBean.getId(), UserParams.VIDEO_ID,
							String.valueOf(shoucang.getVideoId())));
			showLoginDialog();

			break;
		default:
			break;
		}
	}

	/**
	 * 更新播放记录
	 */
	private void updateboFangleave() {
		if (areaBofangLeave == null) {
			areaBofangLeave = findViewById(R.id.client_bofangLeaveArea);
			listViewBofang = (NoScroListView) findViewById(R.id.client_listviewJiLu);
			listViewBofang.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					BofangBean bean = dataBofang.get(position);
					Utils.playVideo(ClienUserActivity.this,
							String.valueOf(bean.getVideoId()),
							bean.getVideoName(),
							String.valueOf(bean.getAlbumId()));
				}
			});
		}
		if (dataBofang == null || dataBofang.size() == 0) {
			areaBofangLeave.setVisibility(8);
		} else {
			areaBofangLeave.setVisibility(0);
		}
		BofangLeaveAdapter adapter = new BofangLeaveAdapter(dataBofang,
				getLayoutInflater(), getWindowManager().getDefaultDisplay()
						.getWidth());
		adapter.setOnclickListener(this);
		listViewBofang.setAdapter(adapter);
		// Utils.setListViewHeightBasedOnChildren(listViewBofang);

	}

	/**
	 * 更新收藏
	 */
	private void updateShowcang() {
		if (areashouCang == null) {
			areashouCang = findViewById(R.id.client_shouCangArea);
			listViewShoucang = (NoScroListView) findViewById(R.id.client_listviewShouCang);
			listViewShoucang.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					ShouCangBean bean = dataShouCangBeans.get(position);
					// 打开 播放器
					Utils.playAlbum(ClienUserActivity.this,
							String.valueOf(bean.getAlbumId()),
							bean.getAlbumName());
				}
			});
		}
		if (dataShouCangBeans == null || dataShouCangBeans.size() == 0) {
			areashouCang.setVisibility(8);
		} else {
			areashouCang.setVisibility(0);
		}
		ShouCangAdapter adapter = new ShouCangAdapter(dataShouCangBeans,
				getLayoutInflater(), getWindowManager().getDefaultDisplay()
						.getWidth());
		adapter.setOnclickListener(this);
		listViewShoucang.setAdapter(adapter);
		// Utils.setListViewHeightBasedOnChildren(listViewShoucang);
	}

	private void updateYouhuiquna() {
		if (areaYouhuiQuan == null) {
			areaYouhuiQuan = findViewById(R.id.client_youhuiArea);
			listViewYouhuiQuan = (NoScroListView) findViewById(R.id.client_listviewYouhui);
		}
		if (dataYouhuiQuan != null && dataYouhuiQuan.size() > 0) {
			areaYouhuiQuan.setVisibility(0);
			YouHuiQuanListAdapter huiQuanListAdapter = new YouHuiQuanListAdapter(
					dataYouhuiQuan, this);
			listViewYouhuiQuan.setAdapter(huiQuanListAdapter);
		} else {
			areaYouhuiQuan.setVisibility(8);
		}

	}

	/**
	 * 更新预约
	 */
	private void updateyuYue() {
		if (areaYuyue == null) {
			areaYuyue = findViewById(R.id.client_yuyueArea);
			listViewYuyue = (NoScroListView) findViewById(R.id.client_listviewYuyue);
		}
		if (dataYuyue == null || dataYuyue.size() == 0) {
			areaYuyue.setVisibility(8);
			return;
		} else {
			areaYuyue.setVisibility(0);
		}
		YuyueAdapter adapter = new YuyueAdapter(dataYuyue, getLayoutInflater());
		adapter.setOnClicklistener(this);
		listViewYuyue.setAdapter(adapter);
		// Utils.setListViewHeightBasedOnChildren(listViewYuyue);
	}

	@Override
	public void onSuccess(ServiceAction service, Object action, Object value) {
		super.onSuccess(service, action, value);
		JSONObject baseObj = (JSONObject) value;
		switch (service) {
		case Action_User:
			anaUserData(baseObj, action);
			break;
		case Action_Pay:
			anaYouhuiQuan(baseObj, action);
			break;
		default:
			break;
		}

	}
	private void anaYouhuiQuan(JSONObject baseObj, Object action) {
		switch ((PayAction) action) {
		case Action_getYouHuiQuan:
			ResultList<YouHuiQuan> resultList = JSON.parseObject(
					baseObj.toString(),
					new TypeReference<ResultList<YouHuiQuan>>() {
					});
			List<YouHuiQuan> tempYouHuiQuans = resultList.data;
			if (tempYouHuiQuans != null && tempYouHuiQuans.size() > 0) {
				if (dataYouhuiQuan == null) {
					dataYouhuiQuan = new ArrayList<YouHuiQuan>();
				}
				dataYouhuiQuan.clear();
				dataYouhuiQuan.addAll(tempYouHuiQuans);
				updateYouhuiquna();
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onFaile(ServiceAction service, Object action, Object value) {
		super.onFaile(service, action, value);
		Utils.toast(getApplicationContext(), value.toString());
	}

	@Override
	public void onFinish(ServiceAction service, Object action) {
		super.onFinish(service, action);
		dismissDialog();
	}

	private void anaUserData(JSONObject baseObj, Object action) {
		UserAction userAction = (UserAction) action;
		switch (userAction) {
		// 取消预约
		case Action_Yuyue_Cancle:
			Utils.toast(getApplicationContext(), R.string._cancle_yuyue);
			if (UserAction.Action_Yuyue_Cancle.value != null) {
				if (dataYuyue.contains(UserAction.Action_Yuyue_Cancle.value)) {
					dataYuyue.remove(UserAction.Action_Yuyue_Cancle.value);
					updateyuYue();
				}
			}
			break;
		// 获取预约列表
		case Action_Yuyue_LIST:
			try {
				dataYuyue = JSON.parseArray(baseObj.getString(UserParams.DATA),
						YuyueBean.class);
				updateyuYue();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;
		// 光看记录列表
		case Action_See_Leave:
			try {
				dataBofang = JSON.parseArray(
						baseObj.getString(UserParams.DATA), BofangBean.class);
				updateboFangleave();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;
		// 删除观看记录
		case Action_See_Leave_DEL:
			Utils.toast(getApplicationContext(), R.string._del_success);
			if (UserAction.Action_See_Leave_DEL.value != null) {
				if (dataBofang.contains(UserAction.Action_See_Leave_DEL.value)) {
					dataBofang.remove(UserAction.Action_See_Leave_DEL.value);
					updateboFangleave();
				}
			}
			break;
		// 收藏列表
		case Action_Shoucang:
			try {
				dataShouCangBeans = JSON.parseArray(
						baseObj.getString(UserParams.DATA), ShouCangBean.class);
				updateShowcang();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;
		// 删除收藏
		case Action_Shoucang_Del:
			Utils.toast(getApplicationContext(), R.string._del_success);
			if (UserAction.Action_Shoucang_Del.value != null) {
				if (dataShouCangBeans
						.contains(UserAction.Action_Shoucang_Del.value)) {
					dataShouCangBeans
							.remove(UserAction.Action_See_Leave_DEL.value);
					updateShowcang();
				}
			}
			break;
		default:
			break;
		}
		userAction.value = null;
	}
}
