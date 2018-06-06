package com.telappoint.apptdesk.common.model;

public class TempHoldAppt {
	private Long holdId;
	private String errorMessage;
	private String displayDateTime;
	
	public Long getHoldId() {
		return holdId;
	}
	public void setHoldId(Long holdId) {
		this.holdId = holdId;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String getDisplayDateTime() {
		return displayDateTime;
	}
	public void setDisplayDateTime(String displayDateTime) {
		this.displayDateTime = displayDateTime;
	}
	
	@Override
	public String toString() {
		return "TempHoldAppt [holdId=" + holdId + ", errorMessage=" + errorMessage + ", displayDateTime=" + displayDateTime + "]";
	}
}
