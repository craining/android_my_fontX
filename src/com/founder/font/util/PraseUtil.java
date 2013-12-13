package com.founder.font.util;

import java.util.HashMap;

public class PraseUtil {
	
	/**
	 * 解析服务器返回数据
	 * @param result
	 * @return
	 */
	public static HashMap<String, String> praseResult(String result) {
		if (!StringUtil.isNull(result)) {
			String[] keyValue = result.split("#");
			HashMap<String, String> map = new HashMap<String, String>();
			int length = keyValue.length;
			if (length > 0) {
				for (int i = 0; i < length; i++) {
					keyValue[i] = keyValue[i].replace("&", "");
					String[] pair = keyValue[i].split("=");
					if (pair.length == 1) {
						if (pair[0] != "") {
							map.put(pair[0], "");
						}
					} else {
						map.put(pair[0], pair[1]);
					}
				}
				return map;
			}
		}
		return null;
	}
}
