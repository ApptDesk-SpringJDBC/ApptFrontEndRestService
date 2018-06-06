package com.telappoint.apptdesk.model;

import java.util.Map;

import com.telappoint.apptdesk.common.model.BaseResponse;
import com.telappoint.apptdesk.common.model.VerifyPageData;

public class ConfirmAppointmentResponse extends BaseResponse {
	private Long confirmation;
	private String displayKeys;
	private String displayValues;
	
	
	//mobile
	private VerifyPageData confirmApptData;
	private Map<String,String> pageData;

	public Long getConfirmation() {
		return confirmation;
	}
	public void setConfirmation(Long confirmation) {
		this.confirmation = confirmation;
	}
	
	public String getDisplayValues() {
		return displayValues;
	}
	public void setDisplayValues(String displayValues) {
		this.displayValues = displayValues;
	}
	public String getDisplayKeys() {
		return displayKeys;
	}
	public void setDisplayKeys(String displayKeys) {
		this.displayKeys = displayKeys;
	}
	public Map<String,String> getPageData() {
		return pageData;
	}
	public void setPageData(Map<String,String> pageData) {
		this.pageData = pageData;
	}
	public VerifyPageData getConfirmApptData() {
		return confirmApptData;
	}
	public void setConfirmApptData(VerifyPageData confirmApptData) {
		this.confirmApptData = confirmApptData;
	}
}
