package com.yike;

import com.yike.R;

import android.content.Intent;
import android.view.View;

/**
 * 
 * @author rendy 手机找回密码
 */
public class FindPsdTypeActivity extends BaseActivity {

	@Override
	public void onClick(View v) {

	}

	@Override
	public void setContentView() {
		setContentView(R.layout.activity_find_psd);
	}

	@Override
	public void findViewById() {
	}

	public void byPhone(View view) {
		Intent intent = new Intent(getApplication(), ResetPsdActivity.class);
		intent.putExtra(ResetPsdActivity.class.getName(), 1);
		startActivity(intent);
	}

	public void byEmail(View view) {
		Intent intent = new Intent(getApplication(), ResetPsdActivity.class);
		intent.putExtra(ResetPsdActivity.class.getName(), 2);
		startActivity(intent);
	}

	public void backBtn(View view) {
		finish();
	}
	
}
