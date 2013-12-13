package com.founder.font.response;

import java.util.ArrayList;
import java.util.HashMap;

import com.founder.font.Debug;
import com.founder.font.bean.Font;
import com.founder.font.util.PraseUtil;
import com.founder.font.util.StringUtil;

public class GetFontUsersResponse extends BaseResponse {

	private static final String TAG = "GetFontUsersResponse";
	private ArrayList<Font> result;

	@Override
	public void initFeild(String response) {
		super.initFeild(response);
		// 解析返回数据

		Debug.i(TAG, "result=" + response);

		// HashMap<String, String> result = PraseUtil.praseResult(response);
		// if(result != null) {
		// }

		// 暂不调用PraseUtil.praseResult，效率不高
		if (StringUtil.isNull(response)) {
			String[] keyValue = response.split("#");

			result = new ArrayList<Font>();
			int length = keyValue.length;
			if (length > 0) {
				for (int i = 0; i < length; i++) {
					keyValue[i] = keyValue[i].replace("&", "");
					String[] pair = keyValue[i].split("=");
					if (pair.length == 1) {
						if (pair[0] != "") {
							Font font = new Font();
							font.id = Integer.parseInt(pair[0]);
							font.users = 0;
							result.add(font);
						}
					} else {
						Font font = new Font();
						font.id = Integer.parseInt(pair[0]);
						font.users = Integer.parseInt(pair[1]);
						result.add(font);
					}
				}
			}
		}

	}

	public ArrayList<Font> getResult() {
		return result;
	}
}
