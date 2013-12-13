package com.founder.font.response;

 
public class BaseResponse {

	private String responseData = "";

	public String getResponseData() {
		return responseData;
	}

	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}

	public void initFeild(String response) {
		responseData = response;
	}

}
