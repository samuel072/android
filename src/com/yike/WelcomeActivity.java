package com.yike;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.yike.R;
import com.yike.action.ServiceAction;
import com.yike.manager.DavikActivityManager;
import com.yike.tool.BitmapTool;
import com.yike.tool.SendActtionTool;
import com.yike.utils.Contansts;
import com.yike.utils.MediaUtil;
import com.yike.utils.Contansts.UserParams;

/**
 * 开机欢迎界面
 * 
 * @author rendy
 * 
 */
public class WelcomeActivity extends BaseActivity {
	//private ImageView loadImageView;
	private TextView timeTet;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.welcome_btnPs:
			isWaiteInputJiaoYan = false;
			startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void setContentView() {
		setContentView(R.layout.activity_welcome);

	}

	@Override
	public void findViewById() {
		findViewById(R.id.welcome_btnPs).setOnClickListener(this);
		timeTet = (TextView) findViewById(R.id.welcome_time);
		//loadImageView = (ImageView) findViewById(R.id.image);
		//SendActtionTool.get(Contansts.URL_GET_START_AD, null, null, this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		isWaiteInputJiaoYan = true;
//		BitmapTool.getInstance().getAdapterUitl()
//				.display(loadImageView, MediaUtil.createAvdFile());
		startJishi();
	}

	@Override
	public void onSuccess(ServiceAction service, Object action, Object value) {
		super.onSuccess(service, action, value);
		JSONObject object = (JSONObject) value;
		try {

			String url = object.getJSONArray(UserParams.DATA).getJSONObject(0)
					.getString("bigImage");
			HttpUtils httpUtils = new HttpUtils();

//			httpUtils.download(url, MediaUtil.createAvdFile(),
//					new RequestCallBack<File>() {
//						@Override
//						public void onSuccess(ResponseInfo<File> arg0) {
//							BitmapTool
//									.getInstance()
//									.getAdapterUitl()
//									.display(loadImageView,
//											MediaUtil.createAvdFile());
//						}
//
//						@Override
//						public void onFailure(HttpException arg0, String arg1) {
//
//						}
//					});

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onFinish(ServiceAction service, Object action) {
		super.onFinish(service, action);
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
				timeTet.setText(time + "");
			}
		};
	};
	Timer timer;
	private boolean isWaiteInputJiaoYan = true;
	private int time = 3;

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
		timer.schedule(tt, 1000, 1000);
	}

	/**
	 * 结束计时
	 */
	private void stopJishi() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (isWaiteInputJiaoYan) {
			if (!DavikActivityManager.getScreenManager().isContentAty(
					MainActivity.class)) {
				startActivity(new Intent(getApplicationContext(),
						MainActivity.class));
				finish();
			}
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
