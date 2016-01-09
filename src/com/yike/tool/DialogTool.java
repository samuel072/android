package com.yike.tool;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yike.R;
import com.yike.LoginActivity;
import com.yike.utils.Utils;
import com.yike.view.LodingDialog;

public class DialogTool {
	/**
	 * 未登陆状态提示框
	 * 
	 * @param context
	 * @return
	 */
	public static Dialog createLoginDialog(final Activity context, int stringID) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.dialog_login, null);// 得到加载view
		TextView textView = (TextView) v.findViewById(R.id.pop_login_tip);
		textView.setText(stringID);
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		final Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
		v.findViewById(R.id.pop_login_sure).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// ShareXmlTool xmlTool = new ShareXmlTool(context,
						// UserConst.USER_DATA_LOCAL);
						// String acount = xmlTool.getValue(UserConst.COUNT);
						// if (!GeneralTool.isEmpty(acount)) {
						context.startActivity(new Intent(context,
								LoginActivity.class));
						// }else {
						// context.startActivity(new
						// Intent(context,VerifyLogin.class));
						// }
						loadingDialog.dismiss();
					}
				});
		v.findViewById(R.id.pop_login_cancle).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						loadingDialog.dismiss();
					}
				});
		loadingDialog.setCancelable(false);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
		if (context != null && !context.isFinishing()) {
			// if(System.currentTimeMillis()-dialogShowTime>1000){
			// dialogShowTime=System.currentTimeMillis();
			loadingDialog.show();
			// }
		}
		return loadingDialog;
	}

	public static LodingDialog createLoadingDialog(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		// main.xml中的ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.anim_loding);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		// tipTextView.setText(R.string.loading_down);// 设置加载信息
		LodingDialog loadingDialog = new LodingDialog(context,
				R.style.loading_dialog);// 创建自定义样式dialog
		loadingDialog.setCancelable(true);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
		loadingDialog.setAnim(hyperspaceJumpAnimation, spaceshipImage);
		return loadingDialog;
	}

	/**
	 * 跳转登录界面
	 * 
	 * @param context
	 * @return
	 */
	public static Dialog createPayDialog(final Activity context,
			final String albumId) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.dialog_show_goumai, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		final Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
		// 稍后再说
		v.findViewById(R.id.pop_login_sure).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						loadingDialog.dismiss();
					}
				});
		// 现在购买
		v.findViewById(R.id.pop_login_cancle).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						loadingDialog.dismiss();
						//Utils.startPay(albumId, context);
					}
				});
		loadingDialog.setCancelable(true);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
		if (context != null && !context.isFinishing()) {
			// if(System.currentTimeMillis()-dialogShowTime>1000){
			// dialogShowTime=System.currentTimeMillis();
			loadingDialog.show();
			// }
		}
		return loadingDialog;
	}

	/**
	 * 跳转下载url;
	 * 
	 * @param context
	 * @return
	 */
	public static Dialog createCheckDialog(final Activity context,
			final String url) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.dialog_version_update, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		final Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
		// 稍后再说
		v.findViewById(R.id.pop_login_sure).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						loadingDialog.dismiss();
					}
				});
		// 现在购买
		v.findViewById(R.id.pop_login_cancle).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Uri uri = Uri.parse(url);
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						context.startActivity(intent);
						loadingDialog.dismiss();
					}
				});
		loadingDialog.setCancelable(true);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
		if (context != null && !context.isFinishing()) {
			loadingDialog.show();
		}
		return loadingDialog;
	}
}
