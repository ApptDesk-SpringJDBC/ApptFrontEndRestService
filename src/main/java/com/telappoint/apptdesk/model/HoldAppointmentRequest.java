package com.telappoint.apptdesk.model;

import com.telappoint.apptdesk.common.model.BaseRequest;

public class HoldAppointmentRequest extends BaseRequest {
	private Long customerId;
	private Integer companyId;
	private Integer procedureId;
	private Integer locationId;
	private Integer departmentId;
	private Integer resourceId;
	private Integer serviceId;
	
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public Integer getProcedureId() {
		return procedureId;
	}
	public void setProcedureId(Integer procedureId) {
		this.procedureId = procedureId;
	}
	public Integer getLocationId() {
		return locationId;
	}
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}
	public Integer getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	
	public Integer getResourceId() {
		return resourceId;
	}
	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}
	
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	@Override
	public String toString() {
		return "HoldAppointmentRequest Data: [customerId=" + customerId + ", companyId=" + companyId + ", procedureId=" + procedureId + ", locationId=" + locationId + ", departmentId="
				+ departmentId + ", resourceId=" + resourceId + ", serviceId=" + serviceId + "]";
	}
}
