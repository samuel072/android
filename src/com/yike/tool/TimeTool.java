package com.yike.tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.yike.R;

import android.content.Context;
import android.util.Log;

public class TimeTool {
	/**
	 * 
	 * @param oldTime
	 * 
	 * @return
	 * 
	 *         根据接受的时间与当前时间匹配，返回需要的字符信息
	 */
	public static String getTimeStr(Date oldTime, Context context) {

		long time1 = new Date().getTime();

		long time2 = oldTime.getTime();

		long time = (time1 - time2) / 1000;

		if (time >= 0 && time < 60) {
			return context.getString(R.string.just_now);
		} else if (time >= 60 && time < 3600) {
			return time / 60 + context.getString(R.string.minute_before);
		} else if (time >= 3600 && time < 3600 * 24) {
			return time / 3600 + context.getString(R.string.hour_before);
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.format(oldTime);
		}
	}

	   /**
     * 
     * @param oldTime
     * 
     * @return
     * 
     *         根据接受的时间与当前时间匹配，返回需要的字符信息
     */
    public static String getTimeStr(long oldTime, Context context) {

        long time1 = new Date().getTime();

        long time2 = oldTime;

        long time = (time1 - time2) / 1000;

        if (time >= 0 && time < 60) {
            return context.getString(R.string.just_now);
        } else if (time >= 60 && time < 3600) {
            return time / 60 + context.getString(R.string.minute_before);
        } else if (time >= 3600 && time < 3600 * 24) {
            return time / 3600 + context.getString(R.string.hour_before);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(oldTime);
        }
    }
	/**
	 * 
	 * @param time
	 * @return 00:00:00
	 */
	public static String getTimeStr(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
		return sdf.format(time);
	}

	/**
	 * 
	 * @param str
	 * @return 显示客户端时间 String
	 */
	public static String strToString(String str, Context context) {
		// sample：Tue May 31 17:46:55 +0800 2011
		// E：周 MMM：字符串形式的月，如果只有两个M，表示数值形式的月 Z表示时区（＋0800）
		SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy",
				Locale.CHINA);
		Date result = null;
		try {
			result = sdf.parse(str);
		} catch (Exception e) {
		}
		return getTimeStr(result, context);
	}

	/**
	 * php服务器 返回的是毫秒
	 * 
	 * 返回 时间 提示 字符窜
	 * 
	 * **/
	public static String getTimeString(long time, Context context) {
		return getTimeStr(new Date(time), context);
	}

	/**
	 * 
	 * @param time
	 *            yyyy-MM-dd HH:mm:ss
	 * @return millenSeconds
	 */
	public static long getMillTime(String time) {
		SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdff.parse(time).getTime();
		} catch (ParseException e) {
			Log.e("_______errar", e.toString());
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 当日的日期字符串形式例如：20140327
	 * 
	 * @return
	 */
	public static String getCurrentDate(long time) {
		SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
		return sFormat.format(new Date(time));
	}

	/**
	 * ß
	 * 
	 * @param time
	 * @return String yyyy-MM-dd HH:mm:ss
	 */
	public static String getFormaTime(long time) {
		if (time <= 0)
			return null;
		SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdff.format(new Date(time));
	}

	/**
	 * 
	 * @param time
	 * @return String MM-dd HH:mm
	 */
	public static String getTime(long time) {
		if (time <= 0)
			return null;
		SimpleDateFormat sdff = new SimpleDateFormat("MM-dd HH:mm");
		return sdff.format(new Date(time));
	}

	/**
	 * 
	 */
	public static String getDayTime(long time) {
		if (time <= 0)
			return null;
		SimpleDateFormat sdff = new SimpleDateFormat("yyyy年MM月dd日");
		return sdff.format(new Date(time));
	}
}
