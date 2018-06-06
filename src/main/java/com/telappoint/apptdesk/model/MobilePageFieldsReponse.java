package com.telappoint.apptdesk.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.telappoint.apptdesk.common.model.BaseResponse;

public class MobilePageFieldsReponse extends BaseResponse {
	private List<LoginPageFields> mobilePageFields = new ArrayList<>();
	private Map<String,String> pageData;

	public List<LoginPageFields> getMobilePageFields() {
		return mobilePageFields;
	}

	public void setMobilePageFields(List<LoginPageFields> mobilePageFields) {
		this.mobilePageFields = mobilePageFields;
	}

	public Map<String,String> getPageData() {
		return pageData;
	}

	public void setPageData(Map<String,String> pageData) {
		this.pageData = pageData;
	}
}
