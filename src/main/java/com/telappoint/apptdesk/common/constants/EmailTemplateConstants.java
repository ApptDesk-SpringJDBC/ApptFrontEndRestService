package com.telappoint.apptdesk.common.constants;

/**
 * @author Balaji
 */
public enum EmailTemplateConstants {

	EMAIL_APPT_CONFIRM_SUBJECT("ONLINE_EMAIL_APPT_CONF_SUBJECT"),
	EMAIL_APPT_CONFIRM_BODY("ONLINE_EMAIL_APPT_CONF_BODY"),
	EMAIL_APPT_CANCEL_SUBJECT("ONLINE_EMAIL_APPT_CANCEL_SUBJECT"),
	EMAIL_APPT_CANCEL_BODY("ONLINE_EMAIL_APPT_CANCEL_BODY"),
	EMAIL_ADDITIONAL_FILEUPLOAD_SUBJECT("EMAIL_ADDITIONAL_FILEUPLOAD_SUBJECT"),
	EMAIL_ADDITIONAL_FILEUPLOAD_BODY("EMAIL_ADDITIONAL_FILEUPLOAD_BODY");

	private String value;

	private EmailTemplateConstants(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
