package com.yike.utils;

import java.util.Date;

public class MediaUtil {
	private static String Tag = "yike";

	/**
	 * 
	 * @return String 
	 *         得到 唯一的图片文件路径
	 * 
	 */
	public static String createPictureFile() {
		String file = LocalCacheUtil.pictureFilePath.getAbsolutePath() + "/"
				+ getFinalString() + ".jpg";
		return file;
	}
	/**
	 * 
	 * @return String 
	 *         得到 开机首页广告
	 * 
	 */
	public static String createAvdFile() {
		String file = LocalCacheUtil.pictureFilePath.getAbsolutePath() + "avd" + ".jpg";
		return file;
	}

	/**
	 * 
	 * @return String 临时图片路径 后面要跟上文件的名字 + 扩展名
	 * 
	 */
	public static String createCacehFile() {
		String file = LocalCacheUtil.cacheFilePath.getAbsolutePath() + "/";
		return file;
	}

	private static String getFinalString() {
		return MD5Util.getMD5(Tag + new Date().getTime());
	}

}
