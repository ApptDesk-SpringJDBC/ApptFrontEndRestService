package com.telappoint.apptdesk.common.constants;

/**
 * @author Balaji
 */
public enum DisplayFieldLabelConstants {

	LOC_NO_APPTS("LOC_NO_APPTS"),
	NO_BOOKED_APPTS("NO_BOOKED_APPTS"),
	LOGOUT_URL("LOGOUT_URL");

	private String value;

	private DisplayFieldLabelConstants(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
