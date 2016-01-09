package com.yike;

import com.yike.R;
import com.yike.utils.CheckCode;
import com.yike.utils.Utils;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
/**
 * 
 * @author rendy 输入用户信息
 */
public class InputContectActivity extends BaseActivity {
	private EditText count;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.registerOne_backBtn:
			finish();
			break;
		case R.id.registerOne_nextBtn:
			String value = count.getText().toString();
			Intent intent = getIntent();
			if (CheckCode.isEmail(value)||CheckCode.isMobileNO(value)) {
				intent.putExtra(InputContectActivity.class.getName(), value);
				setResult(RESULT_OK,intent);
				finish();
			} else {
				Utils.toast(getApplicationContext(), "请正确输入手机或者邮箱");
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void setContentView() {
		setContentView(R.layout.activity_input_user_contect);
	}

	@Override
	public void findViewById() {
		findViewById(R.id.registerOne_backBtn).setOnClickListener(this);
		findViewById(R.id.registerOne_nextBtn).setOnClickListener(this);
		count = (EditText) findViewById(R.id.registerOne_countEdt);
	}

}
