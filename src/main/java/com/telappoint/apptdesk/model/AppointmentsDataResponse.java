package com.telappoint.apptdesk.model;

import java.util.List;
import java.util.Map;

import com.telappoint.apptdesk.common.model.BaseResponse;
import com.telappoint.apptdesk.service.impl.MobileMenuExistingAppt;

public class AppointmentsDataResponse extends BaseResponse {
	private String clientName;
	private List<AppointmentDetails> apptDetails;
	private List<MobileMenuExistingAppt> mainMenuApptDetails;
	private String message;
	
	// mobile
	private Map<String,String> pageData;

	public List<AppointmentDetails> getBookedAppts() {
		return apptDetails;
	}

	public void setBookedAppts(List<AppointmentDetails> bookedAppts) {
		this.apptDetails = bookedAppts;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String,String> getPageData() {
		return pageData;
	}

	public void setPageData(Map<String,String> pageData) {
		this.pageData = pageData;
	}

	public List<MobileMenuExistingAppt> getMainMenuApptDetails() {
		return mainMenuApptDetails;
	}

	public void setMainMenuApptDetails(List<MobileMenuExistingAppt> mainMenuApptDetails) {
		this.mainMenuApptDetails = mainMenuApptDetails;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
}
