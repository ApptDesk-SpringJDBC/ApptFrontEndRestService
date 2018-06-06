package com.telappoint.apptdesk.common.model;

import com.telappoint.apptdesk.model.CustomerInfoRequest;

public class ExternalLoginRequest extends CustomerInfoRequest {
	private int serviceId;
	private int locationId;

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	@Override
	public String toString() {
		super.toString();
		return "ExternalLoginRequest [serviceId=" + serviceId + ", locationId=" + locationId + "]";
	}
}
