package com.telappoint.apptdesk.common.constants;

/**
 * @author Balaji
 */
public enum DisplayPageContentConstants {

	LEFT_SIDE_TOP_WELCOME_HEADER("LEFT_SIDE_TOP_WELCOME_HEADER"),
	LANDING_PAGE_CONTENT("LANDING_PAGE_CONTENT"),
	SCHEDULER_CLOSED_HEADER("SCHEDULER_CLOSED_HEADER"),
	SCHEDULER_CLOSED_BODY("SCHEDULER_CLOSED_BODY"),
	NO_FUNDING_HEADER("NO_FUNDING_HEADER"),
	NO_FUNDING_BODY("NO_FUNDING_BODY");
	
	private String value;
	private DisplayPageContentConstants(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
