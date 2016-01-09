package com.yike.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yike.R;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yike.utils.Contansts;
import com.yike.utils.LogUtils;
import com.yike.utils.ThreeAppParams;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_result);

		api = WXAPIFactory.createWXAPI(this, ThreeAppParams.WX_APP_ID);

		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	public void ClickOK(View v) {
		finish();
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		LogUtils.tiaoshi("onResp", "onPayFinish, errCode = " + resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.app_tip);
			if (resp.errCode == 0) {
				builder.setMessage("支付成功");
			} else {
				builder.setMessage("支付失败");
			}
			// builder.setMessage(getString(R.string.pay_result_callback_msg,
			// resp.errStr + ";code=" + String.valueOf(resp.errCode)));
			builder.show();
			Intent intent = new Intent(Contansts.ACTION_PAY_RESULT);
			intent.putExtra(Contansts.ACTION_PAY_RESULT, resp.errCode);
			sendBroadcast(intent);
			finish();
		}
	}
}