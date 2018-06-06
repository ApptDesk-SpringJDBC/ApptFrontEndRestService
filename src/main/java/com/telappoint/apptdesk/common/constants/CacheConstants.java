package com.telappoint.apptdesk.common.constants;

/**
 * 
 * @author Balaji N
 *
 * Cache table by below key name with client code. [key|clientCode]= any object or Map
 */
public enum CacheConstants {

	// Master db table keys
	CLIENT("CLIENT"), 
	CLIENT_DEPLOYMENT_CONFIG("CLIENT_DEPLOYMENT_CONFIG"),
	ERROR_CONFIG("ERROR_CONFIG"),
	
	// client db table keys
	APPT_SYS_CONFIG("APPT_SYS_CONFIG"),
	SERVICES("SERVICES"),
	LOGIN_PARAM_CONFIG("LOGIN_PARAM_CONFIG"), 
	DISPLAY_TEMPLATE("DISPLAY_TEMPLATE"),

	DISPLAY_FIELD_LABEL("DISPLAY_FIELD_LABEL"),
	DISPLAY_PAGE_CONTENT("DISPLAY_PAGE_CONTENT"),
	EMAIL_TEMPLATE("EMAIL_TEMPLATE"),
	DISPLAY_BUTTON_NAMES("DISPLAY_BUTTON_NAMES"), 
	DISPLAY_ALIAES("DISPLAY_ALIAES"),
	DISPLAY_LANG("DISPLAY_LANG"),
	PROCEDURE_NO_MATCH("PROCEDURE_NO_MATCH"),
	IVR_VXML("IVR_VXML");

	private String value;

	private CacheConstants(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
