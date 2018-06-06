package com.telappoint.apptdesk.common.model;

import com.telappoint.apptdesk.model.Service;

public class ServiceOption extends Service {
	private Integer serviceLocationId; //programId

	public Integer getServiceLocationId() {
		return serviceLocationId;
	}

	public void setServiceLocationId(Integer serviceLocationId) {
		this.serviceLocationId = serviceLocationId;
	}
}
