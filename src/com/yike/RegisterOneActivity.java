package com.yike;

import com.yike.R;
import com.yike.utils.CheckCode;
import com.yike.utils.Utils;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

/**
 * 
 * @author rendy 注册第一步
 */
public class RegisterOneActivity extends BaseActivity {
	private EditText count;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.registerOne_backBtn:
			finish();
			break;
		case R.id.registerOne_nextBtn:
			String value = count.getText().toString();
			Intent intent = new Intent(getApplicationContext(),
					RegisterActivity.class);
			intent.putExtra(RegisterOneActivity.class.getName(), value);
			if (CheckCode.isEmail(value)) {
				intent.putExtra(RegisterActivity.class.getName(), 2);
				startActivity(intent);
			} else if (CheckCode.isMobileNO(value)) {
				intent.putExtra(RegisterActivity.class.getName(), 1);
				startActivity(intent);
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
		setContentView(R.layout.activity_register_one);
	}

	@Override
	public void findViewById() {
		findViewById(R.id.registerOne_backBtn).setOnClickListener(this);
		findViewById(R.id.registerOne_nextBtn).setOnClickListener(this);
		count = (EditText) findViewById(R.id.registerOne_countEdt);
	}

}
