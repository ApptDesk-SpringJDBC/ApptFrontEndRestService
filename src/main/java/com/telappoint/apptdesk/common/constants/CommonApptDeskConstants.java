package com.telappoint.apptdesk.common.constants;

/**
 * 
 * @author Balaji N
 *
 */
public enum CommonApptDeskConstants {
	VERSION("version"),
	EMPTY_STRING(""),
	ONLINE("online"),
	IVRAUDIO("ivr"),
	MOBILE("mobile"),
	ADMIN("admin"),
	COMMA(",");
	
	private String value;

	private CommonApptDeskConstants(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
