/**
 * 
 */
package com.yike.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.yike.R;
import com.yike.bean.Content;
import com.yike.utils.Contansts;
import com.yike.utils.LogUtils;

;

/**
 * @author： fengqingyun2008
 * @Email： fengqingyun2008@gmail.com
 * @version：1.0
 * @创建时间：2015-4-14 上午11:22:49
 * @类说明：
 */
public class PinglunDialog extends Dialog implements
		android.content.DialogInterface.OnCancelListener,
		android.content.DialogInterface.OnShowListener {

	public interface OnPinglunCompleteLisenter {
		void onComplete(List<Content> content);
	}

	private OnPinglunCompleteLisenter completeLisenter;
	private InputMethodManager mInputMethodManager;

	private Context mContext;

	private EditText mEditText;
	private String toUserName;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Dialog#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_pinglun);
		initView();
	}

	/**
	 * @param context
	 */
	public PinglunDialog(Context context) {
		this(context, 0, null);

	}

	/**
	 * @param activity
	 * @param dialogstylebottom
	 */
	public PinglunDialog(Context context, int theme,
			OnPinglunCompleteLisenter completeLisenter) {
		this(context, theme, null, completeLisenter);
	}

	/**
	 * @param activity
	 * @param dialogstylebottom
	 */
	public PinglunDialog(Context context, int theme, String toUserName,
			OnPinglunCompleteLisenter completeLisenter) {
		super(context, theme);
		this.mContext = context;
		this.completeLisenter = completeLisenter;
		this.toUserName = toUserName;
	}

	/**
	 * @param context
	 */
	private void initView() {
		mInputMethodManager = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		mEditText = (EditText) findViewById(R.id.ed_pinglun);
		if (!TextUtils.isEmpty(toUserName)) {
			mEditText.setHint("回复 " + toUserName);
		}
		findViewById(R.id.tv_complete).setOnClickListener(
				new android.view.View.OnClickListener() {

					@Override
					public void onClick(View v) {
						String string = mEditText.getText().toString();
						if (completeLisenter != null
								&& !TextUtils.isEmpty(string)) {
							List<Content> contents = new ArrayList<Content>();
							Content content = new Content();
							content.setType("1");
							content.setContent(string);
							contents.add(content);
							if (contents != null && contents.size() > 0) {
								completeLisenter.onComplete(contents);
							}
						}
						dismiss();
					}
				});
		findViewById(R.id.img_cancel).setOnClickListener(
				new android.view.View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dismiss();
					}
				});
		setOnCancelListener(this);
		setOnShowListener(this);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
		mInputMethodManager.showSoftInput(mEditText,
				InputMethodManager.SHOW_IMPLICIT);
		mEditText.requestFocus();

	}

	@Override
	public void onShow(DialogInterface dialog) {
		LogUtils.tiaoshi("onShow--", "onShow");
		mEditText.requestFocus();
		mInputMethodManager.showSoftInput(mEditText,
				InputMethodManager.SHOW_IMPLICIT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.content.DialogInterface.OnCancelListener#onCancel(android.content
	 * .DialogInterface)
	 */
	@Override
	public void onCancel(DialogInterface dialog) {
		finish();
	}

	public void finish() {
		if (mInputMethodManager != null)
			mInputMethodManager.hideSoftInputFromWindow(
					mEditText.getWindowToken(), 0);
	}

	/**
	 * 
	 */
	public void cleanText() {
		// TODO Auto-generated method stub
		if (mEditText != null) {
			mEditText.setText("");
		}
	}

}
