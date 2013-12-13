package com.founder.font.response;

import java.util.HashMap;

import com.founder.font.Debug;
import com.founder.font.util.PraseUtil;


public class CheckNewVersionResponse extends BaseResponse {

	
	private static final String HAS_NEW = "0";
	private static final String TAG = "CheckNewVersionResponse";
	private boolean hasNew;//有新版本
	private String downLoadUrl = "";//下载链接

	@Override
	public void initFeild(String response) {
		super.initFeild(response);
		// 解析返回数据
		Debug.i(TAG, "result=" + response);
		HashMap<String, String> result = PraseUtil.praseResult(response);
		if(result != null) {
			if(HAS_NEW.equals(result.get("state"))) {
				hasNew = true;
				downLoadUrl = result.get("url");
			}
		}
	}

	public String getDownLoadUrl() {
		return downLoadUrl;
	}
	public boolean getHasNew() {
		return hasNew;
	}
}
