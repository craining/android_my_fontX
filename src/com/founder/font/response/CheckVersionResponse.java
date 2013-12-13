package com.founder.font.response;

import java.util.HashMap;

import com.founder.font.Debug;
import com.founder.font.util.PraseUtil;

public class CheckVersionResponse extends BaseResponse {

	
	private static final String STATE_IN = "1";//未过期
	private static final String STATE_OUT = "0";//已过期
	
	
	private static final String TAG = "CheckVersionResponse";
	private boolean stateIn;

	public boolean getStateIn() {
		return stateIn;
	}

	@Override
	public void initFeild(String response) {
		super.initFeild(response);
		// 解析返回数据

		Debug.i(TAG, "result=" + response);

		
		HashMap<String, String> result = PraseUtil.praseResult(response);
		if(result != null) {
			if(STATE_IN.equals(result.get("state"))) {
				stateIn = true;
			}
		}
	}

}
