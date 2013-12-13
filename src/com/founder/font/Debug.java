package com.founder.font;

import android.util.Log;

/**
 * 日志管理类。主要负责5个级别的日志输出。
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 */
public class Debug {

	// 发布时关闭log
	public static final boolean LOG = true;
	//数据库是在sd卡上创建，便于没有root的手机调试
	public static final boolean DB_SAVE_SDCARD = true;
	
	
	
	// 用来防止msg为空时的异常
	private static final String NULL_STR = "msg is null!";

	public static void d(String tag, String msg) {
		if (LOG)
			Log.d(tag, msg != null ? msg : NULL_STR);
	}

	public static void d(String tag, String msg, Throwable tr) {
		if (LOG)
			Log.d(tag, msg != null ? msg : NULL_STR, tr);
	}

	public static void e(String tag, String msg) {
		if (LOG)
			Log.e(tag, msg != null ? msg : NULL_STR);
	}

	public static void e(String tag, String msg, Throwable tr) {
		if (LOG)
			Log.e(tag, msg != null ? msg : NULL_STR, tr);
	}

	public static void v(String tag, String msg) {
		if (LOG)
			Log.v(tag, msg != null ? msg : NULL_STR);

	}

	public static void v(String tag, String msg, Throwable tr) {
		if (LOG)
			Log.v(tag, msg != null ? msg : NULL_STR, tr);
	}

	public static void i(String tag, String msg) {
		if (LOG)
			Log.i(tag, msg != null ? msg : NULL_STR);
	}

	public static void i(String tag, String msg, Throwable tr) {
		if (LOG)
			Log.i(tag, msg != null ? msg : NULL_STR, tr);
	}

	public static void w(String tag, String msg) {
		if (LOG)
			Log.w(tag, msg != null ? msg : NULL_STR);
	}

	public static void w(String tag, String msg, Throwable tr) {
		if (LOG)
			Log.w(tag, msg != null ? msg : NULL_STR, tr);
	}
}
