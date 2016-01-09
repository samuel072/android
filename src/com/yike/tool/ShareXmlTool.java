package com.yike.tool;

import android.app.Activity;
import android.content.SharedPreferences;
/**
 * 
 * @author   Administrator
 * 
 * @destribe 本地Xml文件储存 
 * 
 *       2014-2-21
 */
public class ShareXmlTool {
	private SharedPreferences sharePre;
	private SharedPreferences.Editor shareEdit;
	public ShareXmlTool(Activity context,String xmlname){
		sharePre=context.getSharedPreferences(xmlname,context.MODE_WORLD_READABLE);
		shareEdit=sharePre.edit();
	}
	public String getValue(String key){
		return sharePre.getString(key,null);
	}
	public void putValue(String key,String value){
		 shareEdit.putString(key, value);
		 shareEdit.commit();
	}
}
