package com.yike;
///**
// * 
// */
//package com.modernsky.istv;
//
//import java.util.LinkedList;
//import java.util.List;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//
//import android.app.Activity;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.view.View;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.TypeReference;
//import com.lidroid.xutils.http.RequestParams;
//import com.modernsky.istv.action.PayAction;
//import com.modernsky.istv.action.ServiceAction;
//import com.modernsky.istv.bean.WeixinPayReq;
//import com.modernsky.istv.fragment.OrderFragment;
//import com.modernsky.istv.tool.SendActtionTool;
//import com.modernsky.istv.tool.UrlTool;
//import com.modernsky.istv.utils.Contansts;
//import com.modernsky.istv.utils.Contansts.UserParams;
//import com.modernsky.istv.utils.LogUtils;
//import com.modernsky.istv.utils.ThreeAppParams;
//import com.modernsky.istv.utils.Utils;
//import com.modernsky.istv.wxapi.MD5;
//import com.ta.utdid2.android.utils.NetworkUtils;
//import com.tencent.mm.sdk.modelpay.PayReq;
//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;
//
///**
// * @author： fengqingyun2008
// * @Email： fengqingyun2008@gmail.com
// * @version：1.0
// * @创建时间：2015-4-22 下午4:51:33
// * @类说明：
// */
//public class OrderActivity extends BaseActivity {
//	private static FragmentManager fMgr;
//
//	final IWXAPI msgApi = WXAPIFactory.createWXAPI(OrderActivity.this, null);
//	private String userId;
//	private String albumId;
//	StringBuffer sb;
//	private WeixinPayReq payReq;
//	PayReq req;
//
//	private OrderFragment orderFragment;
//
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//
//	}
//
//	/**
//	 * 初始化首个Fragment
//	 */
//	private void initFragment() {
//		FragmentTransaction ft = fMgr.beginTransaction();
//		orderFragment = new OrderFragment();
//		ft.add(R.id.fragmentRoot, orderFragment, "orderFragment");
//		ft.addToBackStack("orderFragment");
//		ft.commit();
//	}
//
//	public void setFragment(Fragment fragment, boolean withAnimation) {
//
//		FragmentTransaction transaction = fMgr.beginTransaction();
//		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//		if (withAnimation) {
//			transaction.setCustomAnimations(R.anim.push_right_in,
//					R.anim.push_left_out, R.anim.push_left_in,
//					R.anim.push_right_out);
//		}
//		transaction.replace(R.id.fragmentRoot, fragment).addToBackStack(null)
//				.commitAllowingStateLoss();
//	}
//
//	@Override
//	public void setContentView() {
//		setContentView(R.layout.activity_pay_order);
//		fMgr = getSupportFragmentManager();
//		Intent intent = getIntent();
//		setUserId(intent.getStringExtra(Contansts.USER_ID));
//		setAlbumId(intent.getStringExtra(Contansts.ALBUM_ID));
//
//		msgApi.registerApp(ThreeAppParams.WX_APP_ID);
//		sb = new StringBuffer();
//		req = new PayReq();
//	}
//
//	// 点击返回按钮
//	@Override
//	public void onBackPressed() {
//		if (fMgr.getBackStackEntryCount() <= 1) {
//			finish();
//		} else {
//			super.onBackPressed();
//		}
//	}
//
//	private void toPayByWX(WeixinPayReq payReq) {
//		req.appId = ThreeAppParams.WX_APP_ID;
//		req.partnerId = payReq.getMch_id();
//		req.prepayId = payReq.getPrepay_id();
//		req.packageValue = "Sign=WXPay";
//		req.nonceStr = payReq.getNonce_str();
//		req.timeStamp = String.valueOf(getTimeStamp());
//
//		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
//		signParams.add(new BasicNameValuePair("appid", req.appId));
//		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
//		signParams.add(new BasicNameValuePair("package", req.packageValue));
//		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
//		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
//		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
//
//		LogUtils.d("appid==" + req.appId);
//		LogUtils.d("noncestr==" + req.nonceStr);
//		LogUtils.d("package==" + req.packageValue);
//		LogUtils.d("partnerid==" + req.partnerId);
//		LogUtils.d("prepayid==" + req.prepayId);
//		LogUtils.d("timestamp==" + req.timeStamp);
//
//		req.sign = genAppSign(signParams);
//		msgApi.registerApp(ThreeAppParams.WX_APP_ID);
//
//		msgApi.sendReq(req);
//	}
//
//	@Override
//	public void findViewById() {
//		initFragment();
//		registerBoradcastReceiver();
//	}
//
//	/**
//	 * 生成签名
//	 */
//	private String genAppSign(List<NameValuePair> params) {
//		StringBuilder sb = new StringBuilder();
//
//		for (int i = 0; i < params.size(); i++) {
//			sb.append(params.get(i).getName());
//			sb.append('=');
//			sb.append(params.get(i).getValue());
//			sb.append('&');
//		}
//		sb.append("key=");
//		sb.append(ThreeAppParams.WX_PAY_KEY);
//
//		String appSign = MD5.getMessageDigest(sb.toString().getBytes())
//				.toUpperCase();
//		LogUtils.tiaoshi("orion", appSign);
//		return appSign;
//	}
//
//	public void getWXOrder(String body, String userId, String videoId,
//			String totalMoney, String voucherCode) {
//		RequestParams params = UrlTool.getParams(Contansts.BODY, body,
//				Contansts.USER_ID, userId, Contansts.VIDEO_ID, videoId,
//				Contansts.TOTAL_FEE, totalMoney, Contansts.CLIENT_IP, getIp(),
//				Contansts.TRADE_TYPE, Contansts.APP, Contansts.VOUCHER_CODE,
//				voucherCode);
//
//		SendActtionTool.postNoCheck(UserParams.URL_GET_WEIXIN_ORDER,
//				ServiceAction.Action_Pay, PayAction.Action_getWinXin_prepay_id,
//				this, params);
//		showLoginDialog();
//	}
//
//	/**
//	 * @return ip地址
//	 */
//	private String getIp() {
//		return NetworkUtils.getWifiIpAddress(getApplicationContext());
//	}
//
//	private long getTimeStamp() {
//		return System.currentTimeMillis() / 1000;
//	}
//
//	@Override
//	public void onFinish(ServiceAction service, Object action) {
//		super.onFinish(service, action);
//		dismissDialog();
//	}
//
//	@Override
//	public void onSuccess(ServiceAction service, Object action, Object value) {
//		super.onSuccess(service, action, value);
//		String string = value.toString();
//		LogUtils.tiaoshi("onSuccess---" + action.toString(), value.toString());
//		switch ((PayAction) action) {
//		case Action_getWinXin_prepay_id:
//			payReq = JSON.parseObject(string,
//					new TypeReference<WeixinPayReq>() {
//					});
//			if (orderFragment.getMoney() == 0) {
//				Utils.toast(this, R.string.pay_success);
//				Intent intent = new Intent(Contansts.ACTION_PAY_RESULT);
//				intent.putExtra(Contansts.ACTION_PAY_RESULT, 0);
//				OrderActivity.this.setResult(RESULT_OK, intent);
//				// 关闭Activity
//				OrderActivity.this.finish();
//			} else {
//				toPayByWX(payReq);
//				orderFragment.setPayBtnEnabled(true);
//			}
//			break;
//		case Action_sentWeixinBack:
//			break;
//
//		default:
//			break;
//		}
//
//	}
//
//	private String youhuiCode;
//
//	/**
//	 * @param code
//	 * @param position
//	 */
//	public void setYouHuiMa(String code, int position) {
//		orderFragment.setYouHuiMa(code, position);
//	}
//
//	public String getUserId() {
//		return userId;
//	}
//
//	public void setUserId(String userId) {
//		this.userId = userId;
//	}
//
//	public String getAlbumId() {
//		return albumId;
//	}
//
//	public void setAlbumId(String albumId) {
//		this.albumId = albumId;
//	}
//
//	public String getCode() {
//		return youhuiCode;
//	}
//
//	public void setCode(String code) {
//		this.youhuiCode = code;
//	}
//
//	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			String action = intent.getAction();
//			if (action.equals(Contansts.ACTION_PAY_RESULT)) {
//				dismissDialog();
//				int intExtra = intent.getIntExtra(Contansts.ACTION_PAY_RESULT,
//						-2);
//				toSentPayResult(payReq.getOut_trade_no(), intExtra);
//
//				if (intExtra == 0)
//					Utils.toast(context, R.string.pay_success);
//				else
//					Utils.toast(context, R.string.pay_fail);
//				OrderActivity.this.setResult(RESULT_OK, intent);
//				// 关闭Activity
//				OrderActivity.this.finish();
//			}
//		}
//
//	};
//
//	private void toSentPayResult(String out_trade_no, int intExtra) {
//		RequestParams params = UrlTool.getParams(Contansts.OUT_TRADE_NO,
//				out_trade_no, Contansts.TRADE_STATE, String.valueOf(intExtra));
//
//		SendActtionTool.postNoCheck(UserParams.URL_WEIXIN_BACK,
//				ServiceAction.Action_Pay, PayAction.Action_sentWeixinBack,
//				this, params);
//
//	}
//
//	@Override
//	protected void onDestroy() {
//		unregisterReceiver(mBroadcastReceiver);
//		super.onDestroy();
//	}
//
//	public void registerBoradcastReceiver() {
//		IntentFilter myIntentFilter = new IntentFilter();
//		myIntentFilter.addAction(Contansts.ACTION_PAY_RESULT);
//		// 注册广播
//		registerReceiver(mBroadcastReceiver, myIntentFilter);
//	}
//
//}
