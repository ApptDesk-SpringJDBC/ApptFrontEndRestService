package com.telappoint.apptdesk.model;

import com.telappoint.apptdesk.common.model.BaseResponse;

public class FirstAvailableDateAnyLocationResponse extends BaseResponse {
	private Location location = new Location();
	private String date;
	private String displayDateTime;
	private String time;
	private Service service = new Service();
	
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Service getService() {
		return service;
	}
	public void setService(Service service) {
		this.service = service;
	}
	public String getDisplayDateTime() {
		return displayDateTime;
	}
	public void setDisplayDateTime(String displayDateTime) {
		this.displayDateTime = displayDateTime;
	}
}
