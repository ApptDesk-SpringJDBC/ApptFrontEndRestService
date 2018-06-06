package com.telappoint.apptdesk.common.model;

import java.util.Map;

public class VerifyPageResponse extends BaseResponse {
	private VerifyPageData verifyPageData;
	private Map<String,String> pageData;

	public VerifyPageData getVerifyPageData() {
		return verifyPageData;
	}

	public void setVerifyPageData(VerifyPageData verifyPageData) {
		this.verifyPageData = verifyPageData;
	}

	public Map<String,String> getPageData() {
		return pageData;
	}

	public void setPageData(Map<String,String> pageData) {
		this.pageData = pageData;
	}
}
