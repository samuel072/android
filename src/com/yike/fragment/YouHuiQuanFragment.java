package com.yike.fragment;
///**
// * 
// */
//package com.modernsky.istv.fragment;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.annotation.SuppressLint;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ListView;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.TypeReference;
//import com.alipay.sdk.util.Utils;
//import com.lidroid.xutils.http.RequestParams;
//import com.modernsky.istv.OrderActivity;
//import com.modernsky.istv.R;
//import com.modernsky.istv.action.PayAction;
//import com.modernsky.istv.action.ServiceAction;
//import com.modernsky.istv.adapter.YouHuiQuanListAdapter;
//import com.modernsky.istv.bean.ResultList;
//import com.modernsky.istv.bean.YouHuiQuan;
//import com.modernsky.istv.tool.SendActtionTool;
//import com.modernsky.istv.tool.UrlTool;
//import com.modernsky.istv.utils.Contansts;
//import com.modernsky.istv.utils.LogUtils;
//import com.modernsky.istv.utils.Contansts.UserParams;
//
///**
// * @author： fengqingyun2008
// * @Email： fengqingyun2008@gmail.com
// * @version：1.0
// * @创建时间：2015-4-26 下午12:18:44
// * @类说明：
// */
//public class YouHuiQuanFragment extends BaseFragment {
//	// -------------优惠券
//	private List<YouHuiQuan> youHuiQuans;
//	private YouHuiQuanListAdapter huiQuanListAdapter;
//	private ListView listView;
//	private String userId;
//	private OrderActivity activity;
//
//	public YouHuiQuanFragment(String userId) {
//		this.userId = userId;
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.img_back:
//			activity.onBackPressed();
//			break;
//
//		default:
//			break;
//		}
//	}
//
//	@SuppressLint("InflateParams")
//	@Override
//	public View setContentView(LayoutInflater inflater) {
//		activity = (OrderActivity) getActivity();
//
//		return inflater.inflate(R.layout.fragment_youhuiquan, null);
//	}
//
//	@Override
//	public void initView(View rootView) {
//		rootView.findViewById(R.id.img_back).setOnClickListener(this);
//		youHuiQuans = new ArrayList<YouHuiQuan>();
//		listView = (ListView) rootView.findViewById(R.id.listview_order);
//		huiQuanListAdapter = new YouHuiQuanListAdapter(youHuiQuans,
//				getActivity());
//		listView.setAdapter(huiQuanListAdapter);
//		listView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				YouHuiQuan youHuiQuan = youHuiQuans.get(position);
//				if (youHuiQuan.getIsUse() == 1) {
//					com.modernsky.istv.utils.Utils.toast(activity,
//							"您已经使用过此优惠券了。");
//				} else {
//					activity.setYouHuiMa(youHuiQuan.getCode(), position);
//					activity.onBackPressed();
//				}
//			}
//		});
//		if (TextUtils.isEmpty(userId))
//			userId = activity.getUserId();
//		LogUtils.tiaoshi("userId=", userId);
//		getYouHuiQuan(String.valueOf(userId));
//	}
//
//	private void getYouHuiQuan(String userId) {
//		RequestParams params = UrlTool.getParams(Contansts.USER_ID, userId);
//
//		SendActtionTool.post(UserParams.URL_YOUHUIQUAN,
//				ServiceAction.Action_Pay, PayAction.Action_getYouHuiQuan, this,
//				params);
//	}
//
//	@Override
//	public void onSuccess(ServiceAction service, Object action, Object value) {
//		super.onSuccess(service, action, value);
//		String string = value.toString();
//		switch ((PayAction) action) {
//		case Action_getYouHuiQuan:
//			ResultList<YouHuiQuan> resultList = JSON.parseObject(string,
//					new TypeReference<ResultList<YouHuiQuan>>() {
//					});
//			List<YouHuiQuan> tempYouHuiQuans = resultList.data;
//			if (tempYouHuiQuans != null && tempYouHuiQuans.size() > 0) {
//				youHuiQuans.clear();
//				youHuiQuans.addAll(tempYouHuiQuans);
//				huiQuanListAdapter.notifyDataSetChanged();
//			}
//			break;
//
//		default:
//			break;
//		}
//	}
//
//	@Override
//	public void onFaile(ServiceAction service, Object action, Object value) {
//		// TODO Auto-generated method stub
//		super.onFaile(service, action, value);
//	}
//
//	@Override
//	public void onException(ServiceAction service, Object action, Object value) {
//		// TODO Auto-generated method stub
//		super.onException(service, action, value);
//	}
//
//}
