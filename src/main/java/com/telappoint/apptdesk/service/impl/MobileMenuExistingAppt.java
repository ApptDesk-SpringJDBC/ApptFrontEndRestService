package com.telappoint.apptdesk.service.impl;

import java.util.List;

import com.telappoint.apptdesk.model.AppointmentDetails;

public class MobileMenuExistingAppt {
	private String clientName;
	private List<AppointmentDetails> apptList;
	
	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public List<AppointmentDetails> getApptList() {
		return apptList;
	}

	public void setApptList(List<AppointmentDetails> apptList) {
		this.apptList = apptList;
	}
}
