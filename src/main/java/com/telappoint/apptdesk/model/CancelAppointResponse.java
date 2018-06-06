package com.telappoint.apptdesk.model;

import java.util.Map;

import com.telappoint.apptdesk.common.model.BaseResponse;
import com.telappoint.apptdesk.common.model.VerifyPageData;


public class CancelAppointResponse extends BaseResponse {
	private boolean isCancelled;
	private String displayKeys;
	private String displayValues;
	private VerifyPageData cancelApptDetails;
	
	// mobile
	private Map<String,String> pageData;
	private String errorMessage;
	
	public boolean isCancelled() {
		return isCancelled;
	}
	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}
	public String getDisplayKeys() {
		return displayKeys;
	}
	public void setDisplayKeys(String displayKeys) {
		this.displayKeys = displayKeys;
	}
	public String getDisplayValues() {
		return displayValues;
	}
	public void setDisplayValues(String displayValues) {
		this.displayValues = displayValues;
	}
	public VerifyPageData getCancelApptDetails() {
		return cancelApptDetails;
	}
	public void setCancelApptDetails(VerifyPageData cancelApptDetails) {
		this.cancelApptDetails = cancelApptDetails;
	}
	public Map<String,String> getPageData() {
		return pageData;
	}
	public void setPageData(Map<String,String> pageData) {
		this.pageData = pageData;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
