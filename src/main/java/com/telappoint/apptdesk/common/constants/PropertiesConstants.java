package com.telappoint.apptdesk.common.constants;

/**
 * 
 * @author Balaji N
 *
 */
public enum PropertiesConstants {

	APPT_SERVICE_REST_WS_PROP("apptRestService.properties");
	
	private String propertyFileName;
	
	private PropertiesConstants(String propertyFileName) {
		this.setPropertyFileName(propertyFileName);
	}

	public String getPropertyFileName() {
		return propertyFileName;
	}

	public void setPropertyFileName(String propertyFileName) {
		this.propertyFileName = propertyFileName;
	}
}
