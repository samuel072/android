package com.yike;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.yike.R;
import com.yike.action.ServiceAction;
import com.yike.action.UserAction;
import com.yike.manager.DavikActivityManager;
import com.yike.tool.SendActtionTool;
import com.yike.tool.UrlTool;
import com.yike.utils.CheckCode;
import com.yike.utils.GeneralTool;
import com.yike.utils.Utils;
import com.yike.utils.Contansts.UserParams;

/**
 * 
 * @author rendy 找回密码
 */
public class ResetPsdActivity extends BaseActivity {
	private TextView tetTitle, tetTime;
	private EditText edtPhone, edtCode, edtPhonePsd, edtEmail;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backBtn:
			finish();
			break;
		// 获取校验码
		case R.id.reset_codeBtn:
			if (isWaiteInputJiaoYan) {
				return;
			}
			String mobile = edtPhone.getText().toString();
			if (!CheckCode.isMobileNO(mobile)) {
				Utils.toast(getApplicationContext(), "请输入正确的手机号");
				return;
			}
			RequestParams request = UrlTool
					.getParams(UserParams.EMAIL_PHONE, mobile);
			SendActtionTool.get(UserParams.URL_GET_REGISTER_CODE, null,
					UserAction.Action_Regiser_Phone_Code, this, request);
			break;
		// 手机找回密码
		case R.id.reset_phoneBtn:
			String phone = edtPhone.getText().toString();
			String phoneCode = edtCode.getText().toString();
			String phonePsd = edtPhonePsd.getText().toString();

			if (!CheckCode.isMobileNO(phone)) {
				Utils.toast(getApplicationContext(), "请输入正确的手机号");
				return;
			}

			if (GeneralTool.isEmpty(phoneCode) || phoneCode.length() != 6) {
				Utils.toast(getApplicationContext(), "请输入正确的校验码");
				return;
			}

			if (GeneralTool.isEmpty(phonePsd) || phonePsd.length() < 6
					|| phonePsd.length() > 20) {
				Utils.toast(getApplicationContext(), R.string.input_psd);
				return;
			}

			RequestParams phoneRequest = UrlTool.getParams(UserParams.MOBILE,
					phone, UserParams.PASSWORD, phonePsd, UserParams.CODE,
					phoneCode);
			SendActtionTool.get(UserParams.URL_USER_UPDATE_Phone_Psd, null,
					UserAction.Action_FindPsd_Phone, this, phoneRequest);
			showLoginDialog();
			break;
		// 邮箱 找回密码
		case R.id.reset_emialBtn:
			String email = edtEmail.getText().toString();
			if (!CheckCode.isEmail(email)) {
				Utils.toast(getApplicationContext(), "请输入正确的邮箱号");
				return;
			}
			RequestParams emailRequest = UrlTool.getParams(UserParams.EMAIL,
					email);
			SendActtionTool.get(UserParams.URL_USER_UPDATE_EMAIL_Psd, null,
					UserAction.Action_FindPsd_EMAIL, this, emailRequest);
			showLoginDialog();
			break;
		default:
			break;
		}
	}

	@Override
	public void setContentView() {
		setContentView(R.layout.activity_reset_psd);
	}

	@Override
	public void findViewById() {
		findViewById(R.id.backBtn).setOnClickListener(this);
		tetTime = (TextView) findViewById(R.id.reset_codeBtn);
		tetTime.setOnClickListener(this);
		findViewById(R.id.reset_phoneBtn).setOnClickListener(this);
		findViewById(R.id.reset_emialBtn).setOnClickListener(this);

		edtCode = (EditText) findViewById(R.id.rest_codeEdt);
		edtPhone = (EditText) findViewById(R.id.reset_phoneEdt);
		edtPhonePsd = (EditText) findViewById(R.id.reset_phonePsdEdt);
		edtEmail = (EditText) findViewById(R.id.reset_emailEdt);

		tetTitle = (TextView) findViewById(R.id.reset_titleTet);

		int type = getIntent().getIntExtra(ResetPsdActivity.class.getName(), 1);
		if (type == 1) {
			findViewById(R.id.reset_phoneArea).setVisibility(0);
			findViewById(R.id.reset_emailArea).setVisibility(8);
			tetTitle.setText("手机重置密码");

		} else {
			findViewById(R.id.reset_phoneArea).setVisibility(8);
			findViewById(R.id.reset_emailArea).setVisibility(0);
			tetTitle.setText("邮箱重置密码");

		}
	}

	/**
	 * 更新计时器
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			time--;
			if (time <= 0) {
				stopJishi();
			} else {
				tetTime.setText(time
						+ getResources().getString(R.string._regetTime));
			}
		};
	};
	Timer timer;
	private boolean isWaiteInputJiaoYan;
	private int time = 56;

	/**
	 * 开始计时
	 */
	private void startJishi() {
		if (timer == null) {
			timer = new Timer();
		}
		TimerTask tt = new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(0);
			}
		};
		tetTime.setText(time + getResources().getString(R.string._regetTime));
		isWaiteInputJiaoYan = true;
		timer.schedule(tt, 1000, 1000);
	}

	/**
	 * 结束计时
	 */
	private void stopJishi() {
		isWaiteInputJiaoYan = false;
		if (timer != null) {
			timer.cancel();
			timer = null;
			tetTime.setText(R.string._getRegisterCode);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopJishi();
	}

	@Override
	public void onSuccess(ServiceAction service, Object action, Object value) {
		switch ((UserAction) action) {
		// 得到验证码
		case Action_Regiser_Phone_Code:
			startJishi();
			break;
		case Action_FindPsd_EMAIL:
			Utils.toast(getApplicationContext(), "请到邮箱修改密码");
			DavikActivityManager.getScreenManager().showTargetAty(
					MainActivity.class.getName());
			break;
		case Action_FindPsd_Phone:
			stopJishi();
			Utils.toast(getApplicationContext(), "修改密码成功");
			DavikActivityManager.getScreenManager().showTargetAty(
					MainActivity.class.getName());
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
