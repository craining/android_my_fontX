package com.founder.font.response;

import java.util.HashMap;

import com.founder.font.Debug;
import com.founder.font.util.PraseUtil;

public class GetFontLimitStateResponse extends BaseResponse {

	public static final int FONT_LIMIT_STATE_OK = 1;
	public static final int FONT_LIMIT_STATE_LIMIT = 2;
	
	
	private static final String TAG = "GetFontLimitStateResponse";
	private int state = FONT_LIMIT_STATE_OK;

	@Override
	public void initFeild(String response) {
		super.initFeild(response);
		// 解析返回数据

		Debug.i(TAG, "result=" + response);

		HashMap<String, String> result = PraseUtil.praseResult(response);
		if(result != null) {
			state = Integer.parseInt(result.get("state"));
		}
	}

	public int getState() {
		return state;
	}
}
