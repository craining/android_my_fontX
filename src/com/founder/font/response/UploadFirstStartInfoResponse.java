package com.founder.font.response;

import com.founder.font.Debug;


public class UploadFirstStartInfoResponse extends BaseResponse {

	private static final String TAG = "UploadFirstStartInfoResponse";
	private boolean result;

	@Override
	public void initFeild(String response) {
		super.initFeild(response);
		// 解析返回数据
		Debug.i(TAG, "result=" + response);
		result = true;//TODO 可能上传后服务器不返还任何数据
	}

	public boolean getResult() {
		return result;
	}
}
