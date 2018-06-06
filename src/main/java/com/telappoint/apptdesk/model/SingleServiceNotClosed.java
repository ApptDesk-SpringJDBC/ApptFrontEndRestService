package com.telappoint.apptdesk.model;

import com.telappoint.apptdesk.common.model.BaseResponse;

public class SingleServiceNotClosed extends BaseResponse {
	private Integer serviceId;

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
}
