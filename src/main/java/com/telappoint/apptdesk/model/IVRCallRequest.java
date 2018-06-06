package com.telappoint.apptdesk.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.telappoint.apptdesk.common.model.BaseRequest;

/**
 * 
 * @author Balaji N
 *
 */

@JsonSerialize(include = Inclusion.NON_NULL)
public class IVRCallRequest extends BaseRequest {
	private Long customerId;
	private Integer locationId;
	private Integer resourceId;
	private Integer serviceId;
	private Long confNumber;
	private Integer apptType;
	
	
	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public Long getConfNumber() {
		return confNumber;
	}

	public void setConfNumber(Long confNumber) {
		this.confNumber = confNumber;
	}

	public Integer getApptType() {
		return apptType;
	}

	public void setApptType(Integer apptType) {
		this.apptType = apptType;
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
		return "IVRCallRequest [customerId=" + customerId + ", locationId=" + locationId + ", resourceId=" + resourceId + ", serviceId=" + serviceId + ", confNumber=" + confNumber
				+ ", apptType=" + apptType + "]";
	}
}
