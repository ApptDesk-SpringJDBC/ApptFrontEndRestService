package com.telappoint.apptdesk.model;

import java.util.List;

import com.telappoint.apptdesk.common.model.BaseResponse;

public class DirectAccessNumberResponse extends BaseResponse {
	
	private List<String> accessNumber;

	public List<String> getAccessNumber() {
		return accessNumber;
	}

	public void setAccessNumber(List<String> accessNumber) {
		this.accessNumber = accessNumber;
	}
}
