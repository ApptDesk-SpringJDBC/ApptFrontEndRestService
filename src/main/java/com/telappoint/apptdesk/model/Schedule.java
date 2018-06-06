package com.telappoint.apptdesk.model;

/**
 * 
 * @author Balaji N
 *
 */
public class Schedule {
	private Long scheduleId;
	private Integer locationId;
	private Integer serviceId;
	private Long customerId;
	private String apptDateTime;
	private int status;
	
	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getApptDateTime() {
		return apptDateTime;
	}

	public void setApptDateTime(String apptDateTime) {
		this.apptDateTime = apptDateTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	
}
