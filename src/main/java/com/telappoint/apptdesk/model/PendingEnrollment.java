package com.telappoint.apptdesk.model;

public class PendingEnrollment {
	private String apiName;
	private String jsonPayLoad;
	private long scheduleId;
	
	public String getApiName() {
		return apiName;
	}
	public void setApiName(String apiName) {
		this.apiName = apiName;
	}
	public String getJsonPayLoad() {
		return jsonPayLoad;
	}
	public void setJsonPayLoad(String jsonPayLoad) {
		this.jsonPayLoad = jsonPayLoad;
	}
	public long getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(long scheduleId) {
		this.scheduleId = scheduleId;
	}
}
