package com.yike.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Log;
/**
 * 
 * @Description 
 *   网络文件的设置
 * @Author 
 *   chuang
 * @version 1.0
 *  2013-4-24
 */
public class MD5Util {
	public static String getMD5(String content){
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(content.getBytes());
			String temp=getHashString(digest);
			Log.e("_________:",temp);
					return temp;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
    private static String getHashString(MessageDigest digest) {
        StringBuilder builder = new StringBuilder();
        for (byte b : digest.digest()) {
            builder.append(Integer.toHexString((b >> 4) & 0xf));
            builder.append(Integer.toHexString(b & 0xf));
        }
        return builder.toString();
    }
}