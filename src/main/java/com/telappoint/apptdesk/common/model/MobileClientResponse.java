package com.telappoint.apptdesk.common.model;

import java.util.List;
import java.util.Map;

public class MobileClientResponse extends BaseResponse {
	private List<IPhoneClientInfo> iphoneClientInfo;
	private Map<String,String> pageData;

	public List<IPhoneClientInfo> getIphoneClientInfo() {
		return iphoneClientInfo;
	}

	public void setIphoneClientInfo(List<IPhoneClientInfo> iphoneClientInfo) {
		this.iphoneClientInfo = iphoneClientInfo;
	}

	public Map<String,String> getPageData() {
		return pageData;
	}

	public void setPageData(Map<String,String> pageData) {
		this.pageData = pageData;
	}
}
