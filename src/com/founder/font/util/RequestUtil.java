package com.founder.font.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.founder.font.Debug;
import com.founder.font.response.CheckNewVersionResponse;
import com.founder.font.response.CheckVersionResponse;
import com.founder.font.response.GetFontLimitStateResponse;
import com.founder.font.response.GetFontUsersResponse;
import com.founder.font.response.UploadFirstStartInfoResponse;

public class RequestUtil {

	private static final String REQUEST_URL = "http://diy.foundertype.com/efont/GetMessage.aspx";
	private static final boolean IS_POST = true;
	// 单例
	private static RequestUtil mRequest = null;
	private static HttpUtil mHttpUtil;

	private RequestUtil() {
	}

	public static RequestUtil getInstance() {
		if (mRequest == null) {
			mRequest = new RequestUtil();
		}
		if (mHttpUtil == null) {
			mHttpUtil = HttpUtil.getInstence();
		}
		return mRequest;
	}

	/**
	 * 版本验证
	 * @param IS_POST
	 * @return
	 */
	public CheckVersionResponse checkVersion() {
		

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// 请求参数
		params.add(new BasicNameValuePair("act", "sctime"));
		params.add(new BasicNameValuePair("id", ""));
		params.add(new BasicNameValuePair("start", ""));
		params.add(new BasicNameValuePair("check", null));
		CheckVersionResponse response = new CheckVersionResponse();
		// 执行
		mHttpUtil.sendRequest(response, REQUEST_URL, params, IS_POST);
		return response;
	}
	
	/**
	 * 请求下载人数
	 * @param IS_POST
	 * @return
	 */
	public GetFontUsersResponse getFontUsers() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// 请求参数
		params.add(new BasicNameValuePair("act", "gftusers"));
		GetFontUsersResponse response = new GetFontUsersResponse();
		// 执行
		mHttpUtil.sendRequest(response, REQUEST_URL, params, IS_POST);
		return response;
	}
	
	/**
	 * 上传装机量统计信息
	 * @param IS_POST
	 * @return
	 */
	public UploadFirstStartInfoResponse uploadFirstStartInfo(final String sys, final String mbd, final String time, final String ptype, final String clientSW) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// 请求参数
		params.add(new BasicNameValuePair("act", "customex"));
		params.add(new BasicNameValuePair("uid", "android0"));
		params.add(new BasicNameValuePair("sys", sys));
		params.add(new BasicNameValuePair("mbd", mbd));
		params.add(new BasicNameValuePair("time", time));
		params.add(new BasicNameValuePair("ptype", ptype));
		params.add(new BasicNameValuePair("clientSW", clientSW));
		
		UploadFirstStartInfoResponse response = new UploadFirstStartInfoResponse();
		// 执行
		mHttpUtil.sendRequest(response, REQUEST_URL, params, IS_POST);
		return response;
	}
	
	/**
	 * 检测新版本
	 * @param IS_POST
	 * @return
	 */
	public CheckNewVersionResponse checkNewVersion(String nowVersion) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// 请求参数
		params.add(new BasicNameValuePair("act", "gupdate"));
		params.add(new BasicNameValuePair("platform", "adr"));
		params.add(new BasicNameValuePair("sfver", nowVersion));
		
		Debug.v("", "checkNewVersion sfver=" + nowVersion);
		
		CheckNewVersionResponse response = new CheckNewVersionResponse();
		// 执行
		mHttpUtil.sendRequest(response, REQUEST_URL, params, IS_POST);
		return response;
	}
	
	
	/**
	 * 检测新版本
	 * @param IS_POST
	 * @return
	 */
	public GetFontLimitStateResponse getFontLimitState(int fontId) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// 请求参数
		params.add(new BasicNameValuePair("act", "glimit"));
		params.add(new BasicNameValuePair("fontid", "" + fontId));
		
		GetFontLimitStateResponse response = new GetFontLimitStateResponse();
		// 执行
		mHttpUtil.sendRequest(response, REQUEST_URL, params, IS_POST);
		return response;
	}
}
