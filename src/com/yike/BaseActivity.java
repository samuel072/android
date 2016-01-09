package com.yike;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View.OnClickListener;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.IUmengUnregisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import com.yike.action.ServiceAction;
import com.yike.action.UserAction;
import com.yike.bean.UserBean;
import com.yike.listener.CommonListener;
import com.yike.manager.DavikActivityManager;
import com.yike.service.UserService;
import com.yike.tool.DialogTool;
import com.yike.tool.SendActtionTool;
import com.yike.tool.UrlTool;
import com.yike.utils.LogUtils;
import com.yike.utils.ThreeAppParams;
import com.yike.utils.Contansts.UserParams;
import com.yike.view.LodingDialog;


public abstract class BaseActivity extends FragmentActivity implements
		CommonListener, OnClickListener {
	private LodingDialog lodingDialog;
	/** 友盟推送 **/
	PushAgent mPushAgent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView();
		findViewById();
		DavikActivityManager.getScreenManager().pushActivity(this);
		initUM();
		MobclickAgent.updateOnlineConfig(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		//sendPush();
	}

	/**
	 * 初始化友盟推送
	 */
	protected void initUM() {
		if (ThreeAppParams.umToken == null) {
			mPushAgent = PushAgent.getInstance(this);
			mPushAgent.onAppStart();
			LogUtils.tiaoshi("BaseActivity.initUM()", "初始化");
			mPushAgent.enable(mRegisterCallback);
		} else {
			LogUtils.tiaoshi("BaseActivity.initUM()", "初始化UM成功");

		}
	}

	/**
	 * 关闭友盟推送
	 */
	protected void closePush() {
		if (mPushAgent.isEnabled() || UmengRegistrar.isRegistered(this)) {
			mPushAgent.disable(mUnregisterCallback);
		}
	}

	/**
	 * 打开友盟推送
	 */
	protected void startPush() {
		mPushAgent.enable(mRegisterCallback);
	}

	public Handler baseHandler = new Handler();
	/**
	 * 开启友盟推送
	 */
	public IUmengRegisterCallback mRegisterCallback = new IUmengRegisterCallback() {
		@Override
		public void onRegistered(String registrationId) {
			// TODO Auto-generated method stub
			baseHandler.post(new Runnable() {
				@Override
				public void run() {
					ThreeAppParams.umToken = mPushAgent.getRegistrationId();
					LogUtils.tiaoshi("---mRegisterCallback-----", "执行");
					ThreeAppParams.umToken = UmengRegistrar
							.getRegistrationId(BaseActivity.this);
					updateStatus();
				}
			});
		}
	};

	/**
	 * 注册推送到服务器
	 */
	protected void sendPush() {
		ThreeAppParams.umToken = UmengRegistrar.getRegistrationId(this);
		if (ThreeAppParams.isNeedToken) {
			UserBean bean = UserService.getInatance().getUserBean();
			if (bean != null && ThreeAppParams.umToken != null) {
				LogUtils.tiaoshi("BaseActivity.sendPush()",
						ThreeAppParams.umToken);
				SendActtionTool.post(UserParams.URL_ADD_PUSH_TOKEN,
						ServiceAction.Action_User,
						UserAction.Action_Push_Acton, this, UrlTool.getParams(
								UserParams.USER_ID, bean.getId(), "source",
								"android", "token", ThreeAppParams.umToken));
			}
		}
	}

	/**
	 * 推出自己的服务器推送
	 */
	protected void outPush() {
		UserBean bean = UserService.getInatance().getUserBean();
		if (bean != null) {
			LogUtils.tiaoshi("BaseActivity.outPush()", ThreeAppParams.umToken);
			SendActtionTool.post(UserParams.URL_ADD_PUSH_TOKEN,
					ServiceAction.Action_User,
					UserAction.Action_Push_Acton_Out, this, UrlTool.getParams(
							UserParams.USER_ID, bean.getId(), "source",
							"android", "token", ""));
		}
	}

	/**
	 * 关闭友盟推送
	 */
	private IUmengUnregisterCallback mUnregisterCallback = new IUmengUnregisterCallback() {

		@Override
		public void onUnregistered(String registrationId) {
			baseHandler.post(new Runnable() {

				@Override
				public void run() {
					updateStatus();
				}
			});
		}
	};

	/**
	 * 更新推送状态
	 */
	private void updateStatus() {
		String info = String.format(
				"enabled:%s  isRegistered:%s  DeviceToken:%s",
				mPushAgent.isEnabled(), mPushAgent.isRegistered(),
				mPushAgent.getRegistrationId());
		LogUtils.tiaoshi("BaseActivity.updateStatus()_更新友盟推送", info);
	}

	public abstract void setContentView();

	public abstract void findViewById();

	/**
	 * 展示加加载信心等待 dialog
	 */
	public void showLoginDialog() {
		if (lodingDialog == null) {
			lodingDialog = DialogTool.createLoadingDialog(this);
		}
		if (lodingDialog.isShowing()) {
			return;
		} else {
			lodingDialog.show();
		}
	}

	/**
	 * 隐藏加载信息 dialog
	 */
	public void dismissDialog() {
		if (lodingDialog == null) {
			return;
		}
		if (lodingDialog.isShowing()) {
			lodingDialog.dismiss();
		}
	}

	@Override
	public void onSuccess(ServiceAction service, Object action, Object value) {

	}

	@Override
	public void onFaile(ServiceAction service, Object action, Object value) {

	}

	@Override
	public void onException(ServiceAction service, Object action, Object value) {

	}

	@Override
	public void onStart(ServiceAction service, Object action) {

	}
	@Override
	public void onFinish(ServiceAction service, Object action) {

	}
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		dismissDialog();
		DavikActivityManager.getScreenManager().popActivity(this);

	}
}
