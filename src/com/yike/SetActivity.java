package com.yike;

import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yike.R;
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
import com.yike.manager.BaseApplication;
import com.yike.manager.DavikActivityManager;
import com.yike.service.UserService;
import com.yike.tool.DialogTool;
import com.yike.tool.SendActtionTool;
import com.yike.tool.UrlTool;
import com.yike.utils.LogUtils;
import com.yike.utils.ThreeAppParams;
import com.yike.utils.Utils;
import com.yike.utils.Contansts.UserParams;

/**
 * 
 * @author rendy 设置界面
 */
public class SetActivity extends BaseActivity {
	private TextView textVersion;
	private String uid;
	UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");
	private OpenInfoBean openInfo = new OpenInfoBean();
	private Button btnQQ, btnWX, btnWB;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.set_outBtn:
			//outPush();
			outUser();
			break;
		case R.id.img_live:
			finish();
			break;
		case R.id.img_search:
			startActivity(new Intent(getApplicationContext(),
					SearchActivity.class));
			break;
		case R.id.set_chenckVersionBtn:
			SendActtionTool.get(UserParams.URL_Check_VERSION,
					ServiceAction.Action_User, UserAction.Action_CHECK_VERSION,
					this);
			break;
		case R.id.set_qq:
			if (!UserService.getInatance().isBingQQ()) {
				login(SHARE_MEDIA.QQ);
			}
			break;
		case R.id.set_wb:
			if (!UserService.getInatance().isBingWB()) {
				login(SHARE_MEDIA.SINA);
			}
			break;
		case R.id.set_wx:
			if (!UserService.getInatance().isBingWX()) {
				getWeiXinInfo();
			}
			break;
		default:
			break;
		}
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
					Toast.makeText(SetActivity.this, "授权失败...",
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
		sendOpenInfo();
	}

	private void sendOpenInfo() {
		UserAction.Action_BINDING.value = openInfo.getSourse();
		SendActtionTool.post(
				UserParams.URL_USER_BINDING,
				ServiceAction.Action_User,
				UserAction.Action_BINDING,
				this,
				UrlTool.getParams(UserParams.USER_ID, UserService.getInatance()
						.getUserBean().getId(), UserParams.openId,
						openInfo.getOpenId(), UserParams.openName,
						openInfo.getName(), UserParams.faceUrl,
						openInfo.getUserFace(), UserParams.sex,
						openInfo.getSex(), UserParams.location,
						openInfo.getLocation(), UserParams.source,
						openInfo.getSourse()));
		showLoginDialog();
	}

	/**
	 * 得到微信 信息
	 */
	private void getWeiXinInfo() {
		mController.doOauthVerify(this, SHARE_MEDIA.WEIXIN,
				new UMAuthListener() {
					@Override
					public void onStart(SHARE_MEDIA platform) {
						Toast.makeText(SetActivity.this, "授权开始",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onError(SocializeException e,
							SHARE_MEDIA platform) {
						Toast.makeText(SetActivity.this, "授权错误",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onComplete(Bundle value, SHARE_MEDIA platform) {
						Toast.makeText(SetActivity.this, "授权完成",
								Toast.LENGTH_SHORT).show();
						// 获取相关授权信
						mController.getPlatformInfo(SetActivity.this,
								SHARE_MEDIA.WEIXIN, new UMDataListener() {
									@Override
									public void onStart() {
										Toast.makeText(SetActivity.this,
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
						Toast.makeText(SetActivity.this, "授权取消",
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
		sendOpenInfo();
	}

	private void outUser() {
		SendActtionTool.get(
				UserParams.URL_REGISTER_LOGOUT,
				ServiceAction.Action_User,
				UserAction.Action_Login_OUT,
				this,
				UrlTool.getParams(UserParams.USER_ID, UserService.getInatance()
						.getUserBean().getId()));
	}

	@Override
	public void setContentView() {
		setContentView(R.layout.activity_set);
	}

	@Override
	public void findViewById() {
		findViewById(R.id.set_outBtn).setOnClickListener(this);
		findViewById(R.id.img_live).setOnClickListener(this);
		findViewById(R.id.img_search).setOnClickListener(this);
		btnQQ = (Button) findViewById(R.id.set_qq);
		btnQQ.setOnClickListener(this);
		btnWB = (Button) findViewById(R.id.set_wb);
		btnWB.setOnClickListener(this);
		btnWX = (Button) findViewById(R.id.set_wx);
		btnWX.setOnClickListener(this);
		textVersion = (TextView) findViewById(R.id.set_chenckVersionBtn);
		textVersion.setOnClickListener(this);
		textVersion.setText("软件版本型号：   " + BaseApplication.mVersionName);
//		addQQPlatform();
//		SendActtionTool.post(
//				UserParams.URL_GET_ONE,
//				ServiceAction.Action_User,
//				UserAction.Action_CHECK_ONE,
//				this,
//				UrlTool.getParams(UserParams.USER_ID, UserService.getInatance()
//						.getUserBean().getId()));
//		showLoginDialog();
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

	@Override
	public void onSuccess(ServiceAction service, Object action, Object value) {
		switch (service) {
		case Action_User:
			anayUserback((UserAction) action, value);
			break;
		default:
			break;
		}
	}

	private void anayUserback(UserAction action, Object value) {
		switch (action) {
		case Action_Push_Acton_Out:
			LogUtils.tiaoshi("SetActivity.anayUserback()", "退出服务器推送");
			break;
		case Action_Login_OUT:
			UserService.getInatance().setUserBean(null);
			Utils.toast(getApplicationContext(), "退出成功");
			DavikActivityManager.getScreenManager().showTargetAty(
					MainActivity.class.getName());
			break;
		case Action_CHECK_VERSION:
			JSONObject object = (JSONObject) value;
			try {
				object = object.getJSONObject("androidVersion");
				String version = object.getString("version");
				String url = object.getString("url");
				if (!version.equals(BaseApplication.mVersionName)) {
					DialogTool.createCheckDialog(this, url);
				} else {
					LogUtils.tiaoshi("版本更新", "不用更新");
				}
			} catch (JSONException e) {
				LogUtils.tiaoshi("版本更新", e.toString());
				e.printStackTrace();
			}
			break;
		case Action_CHECK_ONE:
			JSONObject baseObj = (JSONObject) value;
			JSONArray openUser;
			try {
				openUser = baseObj.getJSONObject(UserParams.USER_ENTITY)
						.getJSONArray("openUser");
				if (openUser == null) {
					return;
				}
				for (int i = 0; i < openUser.length(); i++) {
					String source = openUser.getJSONObject(i).getString(
							UserParams.source);
					if (source.equals("QQ")) {
						UserService.getInatance().setBingQQ(true);
						btnQQ.setAlpha(1);
					} else if (source.equals("WX")) {
						UserService.getInatance().setBingWX(true);
						btnWX.setAlpha(1);
					} else if (source.equals("WB")) {
						UserService.getInatance().setBingWB(true);
						btnWB.setAlpha(1);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;
		case Action_BINDING:
			if ("QQ".equals(action.value)) {
				btnQQ.setAlpha(1);
				UserService.getInatance().setBingQQ(true);
				Utils.toast(getApplicationContext(), "QQ登录绑定成功");
			} else if ("WX".equals(action.value)) {
				btnWX.setAlpha(1);
				Utils.toast(getApplicationContext(), "微信登录绑定成功");
				UserService.getInatance().setBingWX(true);

			} else if ("WB".equals(action.value)) {
				btnWB.setAlpha(1);
				Utils.toast(getApplicationContext(), "微博绑定登录绑定成功");
				UserService.getInatance().setBingWB(true);
			}
			action.value=null;
			break;
		default:
			break;
		}
	}

	@Override
	public void onFaile(ServiceAction service, Object action, Object value) {
		Utils.toast(getApplicationContext(), value.toString());
	}

	@Override
	public void onFinish(ServiceAction service, Object action) {
		dismissDialog();
	}

}
