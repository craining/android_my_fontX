package com.founder.font.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.founder.font.FontCoolConstant;
import com.founder.font.response.GetFontLimitStateResponse;

public class TimeUtil {

	
	
	// 以下数据为时间转换的规则
		// yearMap 年 ， mouthMap 月 ，dayMap 日。
		public static final String[] yearMap = { "d", "c", "f", "t", "r", "y", "u", "j", "h", "n", "m", "b", "v", "x", "z",
				"q", "o", "l", "p", "a", "s", "e", "g", "i", "k", "w" };
		public static final String[] mouthMap = { "g", "k", "o", "p", "l", "m", "w", "b", "v", "n", "j", "c", "d", "x",
				"t", "r", "z", "e", "f", "s", "y", "q", "a", "u", "i", "h" };
		public static final String[] dayMap = { "k", "n", "a", "v", "t", "d", "z", "l", "e", "g", "i", "f", "r", "j", "m",
				"o", "q", "b", "s", "h", "u", "w", "c", "x", "p", "y" };
	
	
	private static String DATE_STRING_FORMAT = "yyyyMMdd";//

	
	/**
	 * 上传装机信息时的时间格式
	 * @return
	 */
	public static String getCurrentTimeStringToUpload() {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_STRING_FORMAT);
		Date dt = new Date(System.currentTimeMillis());
		return sdf.format(dt);
	}

	
	/**
	 * 校验内置免费时间
	 * @return
	 */
	public static int getFontLimitState() {
		int result = GetFontLimitStateResponse.FONT_LIMIT_STATE_OK;
		Calendar clNow = Calendar.getInstance();
		Calendar clLimit = Calendar.getInstance();
		int nYear = Integer.parseInt(FontCoolConstant.Limit_Date.substring(0, 4));
		int nMonth = Integer.parseInt(FontCoolConstant.Limit_Date.substring(4, 6));
		int nDay = Integer.parseInt(FontCoolConstant.Limit_Date.substring(6, 8));
		clLimit.set(nYear, nMonth, nDay);
		if (clNow.after(clLimit)) {
			result = GetFontLimitStateResponse.FONT_LIMIT_STATE_LIMIT;
		}
		return result;
	}
}
