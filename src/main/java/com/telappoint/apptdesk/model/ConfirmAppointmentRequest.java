package com.telappoint.apptdesk.model;

import com.telappoint.apptdesk.common.model.BaseRequest;

public class ConfirmAppointmentRequest extends BaseRequest {
	private Long scheduleId;
	private Long customerId;
	
	// mobile
	private String token;

	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	@Override
	public String toString() {
		return "ConfirmAppointmentRequest [scheduleId=" + scheduleId + ", customerId=" + customerId + "]";
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
