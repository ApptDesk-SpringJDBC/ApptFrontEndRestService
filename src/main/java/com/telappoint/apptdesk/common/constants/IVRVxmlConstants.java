package com.telappoint.apptdesk.common.constants;

/**
 * @author Balaji
 */
public enum IVRVxmlConstants {

	WELCOME("WELCOME", "welcome"),
	LOGIN("LOGIN", "login"), //FOR IVR AUDIO FOR BOOKING
	LOGIN_CANCEL("LOGIN_CANCEL","login_cancel"), // IVR LOGIN CANCEL.
	SINGLE_PROCEDURE("SINGLE_PROCEDURE", "single_procedure"),
	PROCEDURE("PROCEDURE", "procedure"),
	SINGLE_LOCATION("SINGLE_LOCATION", "single_location"),
	LOCATION("LOCATION", "location"),
	SINGLE_DEPARTMENT("SINGLE_DEPARTMENT", "single_department"),
	DEPARTMENT("DEPARTMENT", "department");
	
	private String pageKey;
	private String pageValue;

	private IVRVxmlConstants(String pageKey, String pageValue) {
		this.pageKey = pageKey;
		this.pageValue = pageValue;
	}

	public String getPageKey() {
		return pageKey;
	}

	public void setPageKey(String pageKey) {
		this.pageKey = pageKey;
	}

	public String getPageValue() {
		return pageValue;
	}

	public void setPageValue(String pageValue) {
		this.pageValue = pageValue;
	}

}
