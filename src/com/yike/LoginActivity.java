package com.yike;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.lidroid.xutils.http.RequestParams;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.yike.action.ServiceAction;
import com.yike.action.UserAction;
import com.yike.bean.OpenInfoBean;
import com.yike.bean.UserBean;
import com.yike.manager.DavikActivityManager;
import com.yike.service.UserService;
import com.yike.tool.SendActtionTool;
import com.yike.tool.UrlTool;
import com.yike.utils.*;
import com.yike.utils.Contansts.UserParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author rendy 登录注册
 */
public class LoginActivity extends BaseActivity {
	// 创建
	private EditText edtName, edtPsd;
	private boolean index = true;
	private String uid;
	private OpenInfoBean openInfo;
	UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");

	@Override
	public void setContentView() {
		setContentView(R.layout.activity_login);
		addQQPlatform();
	}

	@Override
	public void findViewById() {
		findViewById(R.id.login_backBtn).setOnClickListener(this);
		findViewById(R.id.login_loginBtn).setOnClickListener(this);
		findViewById(R.id.login_fastRegsiterBtn).setOnClickListener(this);
		findViewById(R.id.login_qqBtn).setOnClickListener(this);
		findViewById(R.id.login_weixinBtn).setOnClickListener(this);
		findViewById(R.id.login_weiboBtn).setOnClickListener(this);
		findViewById(R.id.login_shoujiduanxinBtn).setOnClickListener(this);
		edtName = (EditText) findViewById(R.id.login_countEdt);
		edtPsd = (EditText) findViewById(R.id.login_psdEdt);
		openInfo = new OpenInfoBean();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 返回
		case R.id.login_backBtn:
			finish();
			break;
		// 登录
		case R.id.login_loginBtn:
			String count = edtName.getText().toString();
			String psd = edtPsd.getText().toString();
			if (!CheckCode.isEmail(count) && !CheckCode.isMobileNO(count)) {
				Utils.toast(getApplicationContext(), "请正确输入用户名");
				break;
			}
			if (GeneralTool.isEmpty(psd) || psd.length() < 6
					|| psd.length() > 20) {
				Utils.toast(getApplicationContext(), R.string.input_psd);
				return;
			}
			if (index) {
				index = false;
				RequestParams params = UrlTool
						.getParams(UserParams.EMAIL_PHONE, count,
								UserParams.PASSWORD, psd);
				SendActtionTool.get(UserParams.URL_GET_LOGIN, null,
						UserAction.Action_login, this, params);
				showLoginDialog();
			}
			break;
		// 快速注册
		case R.id.login_fastRegsiterBtn:
			Intent  intent = new Intent(getApplicationContext(),
					RegisterActivity.class);
			intent.putExtra(RegisterActivity.class.getName(), 1);
			startActivityForResult(intent, Contansts.ResultConst.REQUEST_CODE_REGISTER_SUCCESS);
			break;
		// 手机短信 登录
		case R.id.login_shoujiduanxinBtn:
			Intent it = new Intent(getApplication(), ResetPsdActivity.class);
			it.putExtra(ResetPsdActivity.class.getName(), 1);
			startActivity(it);
			break;
		case R.id.login_qqBtn:
			login(SHARE_MEDIA.QQ);
			break;
		case R.id.login_weiboBtn:
			login(SHARE_MEDIA.SINA);
			break;
		case R.id.login_weixinBtn:
			getWeiXinInfo();
			break;

		default:
			break;
		}
	}

	private void addQQPlatform() {
		// // 添加QQ支持, 并且设置QQ分享内容的target url
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this,
				ThreeAppParams.QQ_APP_ID, ThreeAppParams.QQ_APP_KEY);
		qqSsoHandler.setTargetUrl("http://www.umeng.com");
		qqSsoHandler.addToSocialSDK();
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(this, ThreeAppParams.WX_APP_ID,
				ThreeAppParams.WX_APP_KEY);
		wxHandler.addToSocialSDK();

	}

	/**
	 * 授权。如果授权成功，则获取用户信息</br>
	 */
	private void login(final SHARE_MEDIA platform) {
		mController.doOauthVerify(this, platform, new UMAuthListener() {

			@Override
			public void onStart(SHARE_MEDIA platform) {
			}

			@Override
			public void onError(SocializeException e, SHARE_MEDIA platform) {
			}

			@Override
			public void onComplete(Bundle value, SHARE_MEDIA platform) {
				uid = value.getString("uid");
				if (!TextUtils.isEmpty(uid)) {
					getUserInfo(platform);
				} else {
					Toast.makeText(LoginActivity.this, "授权失败...",
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onCancel(SHARE_MEDIA platform) {
			}
		});
	}

	/**
	 * 获取授权平台的用户信息</br>
	 */
	private void getUserInfo(final SHARE_MEDIA platform) {
		mController.getPlatformInfo(this, platform, new UMDataListener() {
			@Override
			public void onStart() {

			}

			@Override
			public void onComplete(int status, Map<String, Object> info) {
				// String showText = "";
				// if (status == StatusCode.ST_CODE_SUCCESSED) {
				// showText = "用户名：" + info.get("screen_name").toString();
				// Log.d("#########", "##########" + info.toString());
				// } else {
				// showText = "获取用户信息失败";
				// }
				if (info != null) {
					LogUtils.tiaoshi("getUseInfo", info.toString());
					sendUserInfo(platform, info);
				}
			}

		});
	}

	private void sendUserInfo(SHARE_MEDIA platform, Map<String, Object> info) {
		String sex = null;
		String city = null;
		switch (platform) {
		case QQ:
			sex = info.get("gender") + "";
			city = info.get("city") + "";
			if (sex.equals("男")) {
				sex = "1";
			} else {
				sex = "0";
			}
			openInfo.setSourse("QQ");
			openInfo.setSex(sex);
			openInfo.setLocation(city);
			openInfo.setName(info.get("screen_name").toString());
			openInfo.setUserFace(info.get("profile_image_url").toString());
			openInfo.setOpenId(uid);

			break;
		case SINA:
			sex = info.get("gender") + "";
			city = info.get("location") + "";
			openInfo.setSourse("WB");
			openInfo.setSex(sex);
			openInfo.setLocation(city);
			openInfo.setName(info.get("screen_name").toString());
			openInfo.setUserFace(info.get("profile_image_url").toString());
			openInfo.setOpenId(uid);

			break;
		default:
			break;
		}
		checkNeedUserInfo(uid, openInfo.getSourse());
	}

	/**
	 * 得到微信 信息
	 */
	private void getWeiXinInfo() {
		mController.doOauthVerify(this, SHARE_MEDIA.WEIXIN,
				new UMAuthListener() {
					@Override
					public void onStart(SHARE_MEDIA platform) {
						Toast.makeText(LoginActivity.this, "授权开始",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onError(SocializeException e,
							SHARE_MEDIA platform) {
						Toast.makeText(LoginActivity.this, "授权错误",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onComplete(Bundle value, SHARE_MEDIA platform) {
						Toast.makeText(LoginActivity.this, "授权完成",
								Toast.LENGTH_SHORT).show();
						// 获取相关授权信
						mController.getPlatformInfo(LoginActivity.this,
								SHARE_MEDIA.WEIXIN, new UMDataListener() {
									@Override
									public void onStart() {
										Toast.makeText(LoginActivity.this,
												"获取平台数据开始...",
												Toast.LENGTH_SHORT).show();
									}

									@Override
									public void onComplete(int status,
											Map<String, Object> info) {
										if (status == 200 && info != null) {
											StringBuilder sb = new StringBuilder();
											Set<String> keys = info.keySet();
											for (String key : keys) {
												sb.append(key
														+ "="
														+ info.get(key)
																.toString()
														+ "\r\n");
											}
											LogUtils.tiaoshi("TestData",
													sb.toString());
											sendWinxininfo(info);
										} else {
											LogUtils.tiaoshi("TestData",
													"发生错误：" + status);
										}
									}

								});
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						Toast.makeText(LoginActivity.this, "授权取消",
								Toast.LENGTH_SHORT).show();
					}
				});
	}

	/**
	 * 
	 * @param info
	 *            发送微信信息
	 */
	private void sendWinxininfo(Map<String, Object> info) {
		Object city = info.get("city");
		city = city == null ? "" : city;
		String sex = info.get("sex").toString() + "";
		openInfo.setSourse("WX");
		openInfo.setSex(sex);
		openInfo.setLocation(city.toString());
		openInfo.setName(info.get("nickname").toString());
		openInfo.setUserFace(info.get("headimgurl").toString());
		openInfo.setOpenId(info.get("openid").toString());
		checkNeedUserInfo(uid, openInfo.getSourse());
	}

	// protected void onActivityResult(int requestCode, int resultCode,
	// android.content.Intent data);
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case 5:
			String value = data.getStringExtra(InputContectActivity.class
					.getName());
			openInfo.setEmailOrPhone(value);
			sendOpenInfo(value);
			break;
			case Contansts.ResultConst.REQUEST_CODE_REGISTER_SUCCESS:
				finish();
				break;
		default:
			break;
		}
	}

	@Override
	public void onSuccess(ServiceAction service, Object action, Object value) {
		JSONObject obj = (JSONObject) value;
		switch ((UserAction) action) {
		case Action_OPEN_LOGIN:
		case Action_login:
			Utils.toast(getApplicationContext(), "登录成功");
			try {
				String datas = obj.getJSONObject(UserParams.USER_ENTITY)
						.toString();
				UserService.getInatance().setUserBean(
						JsonUtils.parse(datas, UserBean.class));

			} catch (JSONException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			ThreeAppParams.isNeedToken = true;
			//sendPush();
			DavikActivityManager.getScreenManager().showTargetAty(
					MainActivity.class.getName());
			break;
		case Action_Push_Acton:

			break;
		case Action_OPEN_CHECK:
			try {
				if ("1".equals(obj.getJSONObject(UserParams.DATA).getString(
						"isNeed"))) {
					startActivityForResult(new Intent(this,
							InputContectActivity.class), 5);
				} else {
					sendOpenInfo("");
				}
			} catch (JSONException e1) {
				e1.printStackTrace();
			}

			break;
		default:
			break;
		}

	}

	/**
	 * 
	 * @param openid
	 * @param source
	 *            检测需不需要登录
	 */
	private void checkNeedUserInfo(String openid, String source) {
		SendActtionTool.get(UserParams.URL_OPEN_Check,
				ServiceAction.Action_User, UserAction.Action_OPEN_CHECK, this,
				UrlTool.getParams(UserParams.openId, openid, UserParams.source,
						source));

	}

	/**
	 * 登录数据信息
	 * 
	 * @param value
	 *            已经登录 value=""; email / phone
	 */
	private void sendOpenInfo(String value) {
		SendActtionTool.post(UserParams.URL_OPEN_LOGIN,
				ServiceAction.Action_User, UserAction.Action_OPEN_LOGIN, this,
				UrlTool.getParams(UserParams.openId, openInfo.getOpenId(),
						UserParams.openName, openInfo.getName(),
						UserParams.faceUrl, openInfo.getUserFace(),
						UserParams.sex, openInfo.getSex(), UserParams.location,
						openInfo.getLocation(), UserParams.source,
						openInfo.getSourse(), UserParams.EMAIL_PHONE, value));
		showLoginDialog();
	}

	@Override
	public void onFinish(ServiceAction service, Object action) {
		super.onFinish(service, action);
		index = true;
		dismissDialog();
	}

	@Override
	public void onFaile(ServiceAction service, Object action, Object value) {
		Utils.toast(getApplicationContext(), value.toString());
	}

	@Override
	public void onException(ServiceAction service, Object action, Object value) {

	}
}
