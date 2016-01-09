package com.yike.utils;


import android.util.Log;

public class LogUtils {
    public static boolean debug = true;
    public static String tag = "yike";
    public static void d(String tag, String msg) {
        if (debug)
            Log.d(tag, msg);
    }
    public static void tiaoshi(String local, String msg) {
    	if (debug)
    		Log.d(tag,local+"\n"+msg);
    }

    public static void d(String msg) {
        if (debug)
            Log.d(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (debug)
            Log.i(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (debug)
            Log.e(tag, msg);
    }

    public static void i(String msg) {
        if (debug)
            Log.i(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (debug)
            Log.v(tag, msg);
    }

    public static void v(String msg) {
        if (debug)
            Log.v(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (debug)
            Log.w(tag, msg);
    }

    public static void w(String msg) {
        if (debug)
            Log.w(tag, msg);
    }

    public static void e(String msg) {
        if (debug)
            Log.e(tag, msg);
    }

    public static void printStack() {
        Throwable throwable = new Throwable();
        Log.w(tag, Log.getStackTraceString(throwable));
    }

}
