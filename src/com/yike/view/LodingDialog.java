package com.yike.view;

import android.app.Dialog;
import android.content.Context;
import android.view.animation.Animation;
import android.widget.ImageView;

/**
 * 
 * @author youxin_android
 * @destribe 自定义加载 动画 2014-9-23
 */
public class LodingDialog extends Dialog {
	private Animation animaTion;
	private ImageView vView;

	public LodingDialog(Context context, int theme) {
		super(context, theme);
	}

	protected LodingDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public LodingDialog(Context context) {
		super(context);
	}

	public void setAnim(Animation anim, ImageView view) {
		this.animaTion = anim;
		this.vView = view;
	}

	@Override
	public void show() {
		super.show();
		if (animaTion != null) {
			this.vView.startAnimation(animaTion);
		}
	}

	@Override
	public void dismiss() {
		super.dismiss();
		if (animaTion != null) {
			animaTion.reset();
		}
	}
}
