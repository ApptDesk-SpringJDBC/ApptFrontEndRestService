package com.telappoint.apptdesk.model;


public class UpdateCustomerInfoRequest extends CustomerInfoRequest {
	private Long customerId;

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	@Override
	public String toString() {
		return "UpdateCustomerInfoRequest [customerId=" + customerId + "]";
	}
}
