package com.founder.font.util;

import java.util.ArrayList;


public class StringUtil {

	 
	
	/**
	 * 是否为空
	 * 
	 * @Description:
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str) {
		if (str == null || str.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	
	
	/**
	 * @brief: 分割字符串
	 * @author: $Author: an
	 * @param str
	 *            String 原始字符串
	 * @param splitSign
	 *            String 分隔符
	 * @return String[] 分割后的字符串数组
	 */
	public static String[] strSplit(String str, String splitSign) {
		// 查找索引返回值
		int index;
		// 奇偶标志位
		int nFlag = 1;
		if (str == null || splitSign == null) {
			return null;
		}
		ArrayList<String> arrayList = new ArrayList<String>();
		while ((index = str.indexOf(splitSign)) != -1) {

			arrayList.add(str.substring(0, index));
			if (nFlag % 2 == 0) {
				// 索引标志位偶数次时将标志位接去掉
				str = str.substring(index + splitSign.length());
			} else {
				// 索引标志位奇数次时将标志位替换成“$”,便于下一步查找定位
				str = "$" + str.substring(index + splitSign.length());
			}
			++nFlag;
		}
		arrayList.add(str);
		return (String[]) arrayList.toArray(new String[0]);
	}
}
