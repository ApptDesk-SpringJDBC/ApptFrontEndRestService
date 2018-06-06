package com.telappoint.apptdesk.model;

import java.util.Map;
import com.telappoint.apptdesk.common.model.BaseResponse;

public class PageValidationResponse extends BaseResponse {
	private Map<String,String> pageValidationMap;

	public Map<String, String> getPageValidationMap() {
		return pageValidationMap;
	}

	public void setPageValidationMap(Map<String, String> pageValidationMap) {
		this.pageValidationMap = pageValidationMap;
	}
}
