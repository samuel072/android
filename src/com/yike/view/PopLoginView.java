package com.yike.view;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.yike.R;
import com.yike.LoginActivity;

/**
 * 
 * @author   : Administrator
 *
 * @describe : 从新登录
 *
 * 2013-10-23
 */
public class PopLoginView extends PopupWindow{
	private View mMenuView;
	private Activity mcontext;
	public PopLoginView(Activity context) {
		super(context);
		this.mcontext = context ;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.dialog_login, null);
		mMenuView.findViewById(R.id.pop_login_sure).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v){
				mcontext.startActivity(new Intent(mcontext,LoginActivity.class));
				dismiss();
			}
		});
		mMenuView.findViewById(R.id.pop_login_cancle).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		//设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.MATCH_PARENT);
		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		//设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		//实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
				//设置SelectPicPopupWindow弹出窗体的背景
	    this.setBackgroundDrawable(dw);
	}
	/**
	 * 展示弹窗
	 * @param v
	 */
	public void showPopLogin(View v) {
		this.showAtLocation(v, Gravity.CLIP_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
	}
}
