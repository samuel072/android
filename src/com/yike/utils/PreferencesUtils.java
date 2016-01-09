package com.yike.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zxm on 2015/1/25.
 */
public class PreferencesUtils {
	private static final String APP_INFO = "IsTV";
	public static String TYPE_FIRST_START = "first_start";
	public static String TYPE_LOCK_SCREEN = "lock_screen";

	public static String getPreferences(Context context, String stringType) {
		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(APP_INFO, Context.MODE_PRIVATE);
		return pref.getString(stringType, "");
	}

	public static boolean savePreferences(Context context, String stringType,
			String info) {
		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(APP_INFO, Context.MODE_PRIVATE);
		return pref.edit().putString(stringType, info).commit();
	}

	public static Boolean getBooleanPreferences(Context context,
			String stringType) {
		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(APP_INFO, Context.MODE_PRIVATE);
		return pref.getBoolean(stringType, false);
	}

	public static boolean saveBooleanPreferences(Context context,
			String stringType, Boolean info) {
		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(APP_INFO, Context.MODE_PRIVATE);
		return pref.edit().putBoolean(stringType, info).commit();
	}

	public static boolean setFirstStartInfo(Context context, boolean isFirst) {
		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(APP_INFO, Context.MODE_PRIVATE);
		return pref.edit().putBoolean(TYPE_FIRST_START, isFirst).commit();
	}

	public static boolean getFirstStartInfo(Context context) {
		SharedPreferences pref = context.getApplicationContext()
				.getSharedPreferences(APP_INFO, Context.MODE_PRIVATE);
		return pref.getBoolean(TYPE_FIRST_START, false);
	}
}
