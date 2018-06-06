package com.telappoint.apptdesk.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.telappoint.apptdesk.common.model.BaseResponse;
import com.telappoint.apptdesk.common.model.ServiceOption;

@JsonAutoDetect
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServiceResponse extends BaseResponse {
	private List<Service> services;
	private List<ServiceOption> serviceList;

	public List<Service> getServices() {
		return services;
	}


	public void setServices(List<Service> services) {
		this.services = services;
	}


	@Override
	public String toString() {
		return "ServiceResponse [services=" + services + "]";
	}


	public List<ServiceOption> getServiceList() {
		return serviceList;
	}


	public void setServiceList(List<ServiceOption> serviceList) {
		this.serviceList = serviceList;
	}
}
