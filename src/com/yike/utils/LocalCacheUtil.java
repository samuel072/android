package com.yike.utils;

import java.io.File;

import android.os.Environment;

/**
 * 本地缓存处理工具类
 * 
 * @author Christ
 */
public class LocalCacheUtil {
	private static String ROOT_NAME = "MordernSky";
	/** 根目录 **/
	public static File rootFilePath;
	/** 语音存储 **/
	public static File voiceFilePath;
	/** 图片储存 **/
	public static File pictureFilePath;
	/** 用户头像 **/
	public static File userIconFilePath;
	/** 软件下载 **/
	public static File apkDownLoadFilePath;
	/** 本地临时储存 **/
	public static File cacheFilePath;
	static {
		File rootFile = new File(Environment.getExternalStorageDirectory(),
				ROOT_NAME);
		if (!rootFile.exists()) {
			rootFile.mkdir();
		}
		rootFilePath = rootFile;
		initLocalCacheDir();
	}

	/**
	 * 初始化本地缓存目录
	 * 
	 * @param rootFileName
	 *            根目录文件名
	 */
	public static void initLocalCacheDir() {
		// 根目录创建
		File rootFile = new File(Environment.getExternalStorageDirectory(),
				ROOT_NAME);
		
		if (!rootFile.exists()) {
			rootFile.mkdir();
		}
		LogUtils.tiaoshi("initLocalCacheDir()",rootFile.getAbsoluteFile().toString());
		rootFilePath = rootFile;
		// 创建缓存子目录
		File voice = new File(rootFile, "voice");
		voice.mkdirs();
		voiceFilePath = voice;
		File picture = new File(rootFile, "picture");
		picture.mkdirs();
		pictureFilePath = picture;
		File images = new File(rootFile, "userIcon");
		images.mkdirs();
		userIconFilePath = images;
		File downloaded = new File(rootFile, "downloaded");
		downloaded.mkdirs();
		apkDownLoadFilePath = downloaded;

		File cahceFile = new File(rootFile, "cache");
		cahceFile.mkdirs();
		cacheFilePath = cahceFile;
	}
}
