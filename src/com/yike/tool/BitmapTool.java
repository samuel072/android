package com.yike.tool;

import android.content.Context;

import com.lidroid.xutils.BitmapUtils;

/**
 * 
 * @author zhaoweiChuang
 * 
 * @2015年1月22日
 * 
 * @descripte
 * 
 *            图片加载器
 * 
 */
public class BitmapTool {
	private BitmapUtils bitmapUtils;
	static BitmapTool TOOL;

	private BitmapTool() {
	}

	/**
	 * 得到实例
	 */
	public static BitmapTool getInstance() {
		return TOOL = TOOL == null ? new BitmapTool() : TOOL;
	}

	/**
	 * 
	 * @param context
	 * @return 加载图片工具 对象
	 */
	public BitmapUtils initAdapterUitl(Context context) {
		initBitmapUtils(context);
		return bitmapUtils;
	}

	/**
	 * 展示网络网络图片
	 * 
	 * @return
	 */
	public BitmapUtils getAdapterUitl() {
		return bitmapUtils;
	}

	/**
	 * 初始化对象
	 * 
	 * @param context
	 */
	private void initBitmapUtils(Context context) {
		bitmapUtils = bitmapUtils == null ? new BitmapUtils(context)
				: bitmapUtils;
	}
}
