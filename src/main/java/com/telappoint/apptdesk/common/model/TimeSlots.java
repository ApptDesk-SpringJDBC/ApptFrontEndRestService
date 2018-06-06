package com.telappoint.apptdesk.common.model;

public class TimeSlots {
	private String time;
	private String resourceId;
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	@Override
	public String toString() {
		return "Test [time=" + time + ", resourceId=" + resourceId + "]";
	}
	
	
}
