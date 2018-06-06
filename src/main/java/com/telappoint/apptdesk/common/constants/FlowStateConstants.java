package com.telappoint.apptdesk.common.constants;

/**
 * @author Balaji
 */
public enum FlowStateConstants {
	DB_ERROR("9999"),
	
	GET_CLIENT("GET_CLIENT"), 
	GET_LOGIN_INFO("GET_LOGIN_INFO");
	
	private String value;
	
	private FlowStateConstants(String value) {
		this.setValue(value);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
