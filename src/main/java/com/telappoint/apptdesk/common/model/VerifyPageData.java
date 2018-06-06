package com.telappoint.apptdesk.common.model;

import com.telappoint.apptdesk.model.Customer;

public class VerifyPageData extends Customer  {
	private String confNumber;
	private String apptDateTime;
	private String procedureName;
	private String locationName;
	private String departmentName;
	private String resourceName;
	private String serviceName;
	private String listOfDocsToBring;
	private String timeZone;

	public String getApptDateTime() {
		return apptDateTime;
	}

	public void setApptDateTime(String apptDateTime) {
		this.apptDateTime = apptDateTime;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getListOfDocsToBring() {
		return listOfDocsToBring;
	}

	public void setListOfDocsToBring(String listOfDocsToBring) {
		this.listOfDocsToBring = listOfDocsToBring;
	}

	public String getConfNumber() {
		return confNumber;
	}

	public void setConfNumber(String confNumber) {
		this.confNumber = confNumber;
	}

	public String getProcedureName() {
		return procedureName;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
}
