package com.telappoint.apptdesk.common.model;

import java.util.Map;

public class LocResSerResponse extends BaseResponse {
	private LocationResourceServiceInfo locResSerInfo = new LocationResourceServiceInfo();
	private Map<String, String> pageData;

	public LocationResourceServiceInfo getLocResSerInfo() {
		return locResSerInfo;
	}

	public void setLocResSerInfo(LocationResourceServiceInfo locResSerInfo) {
		this.locResSerInfo = locResSerInfo;
	}

	public Map<String, String> getPageData() {
		return pageData;
	}

	public void setPageData(Map<String, String> pageData) {
		this.pageData = pageData;
	}
}
