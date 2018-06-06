package com.telappoint.apptdesk.common.sp.constants;

/**
 * @author Balaji
 */
public enum SPConstants {
	
	TIME_ZONE("TIME_ZONE"),
	LOCATION_ID("LOC_ID"),
	PROCEDURE_ID("PROC_ID"),
	TRANS_ID("trans_id"),
	RESOURCE_ID("RES_ID"),
	DEPARTMENT_ID("DEPT_ID"),
	SERVICE_ID("SER_ID"),
	DEVICE("DEVICE"),
	LANG_CODE("LANG_CODE"),
	BLOCK_TIME_IN_MINS("BLOCK_TIME_IN_MINS"),
	RESULT_LIST("RESULT_LIST"),
	DISPLAY_KEYS("display_keys"),
	DISPLAY_VALUES("display_values"),
	BLOCKS("BLOCKS"),
	APPT_DATE_TIME("APPT_DATE_TIME"),
	SCHEDULE_ID("SCHED_ID"),
	CUSTOMER_ID("CUST_ID"),
	APPT_METHOD("APPT_METHOD"),
	STATUS_RESULT("status_result"),
	CANCEL_METHOD("CANCEL_METHOD"),
	
	
	// out parameters
	AVAILABLE_DATE_TIMES("avail_date_times"),
	RESULT_STR("result_str"),
	AVAILABLE_DATES("avail_dates"),
	AVAILABLE_DATE("avail_date"),
	HOLD_ID("hold_id"),
	RETURN_SCHEDULE_ID("sched_id"),
	AVAILABILITY("availability"),
	DISPLAY_DATETIME("display_datetime"),
	CONFIRM_NUMBER("conf_number"),
	SUCCESS("success"),
	RESULT("result"),
	ERROR_MESSAGE("error_msg");

	private String value;

	private SPConstants(String value) {
		this.setValue(value);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
