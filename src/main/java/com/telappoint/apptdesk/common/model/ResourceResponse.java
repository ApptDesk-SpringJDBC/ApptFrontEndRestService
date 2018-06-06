package com.telappoint.apptdesk.common.model;

import java.util.List;

public class ResourceResponse extends BaseResponse {
	private List<Resource> resourceList;

	public List<Resource> getResourceList() {
		return resourceList;
	}

	public void setResourceList(List<Resource> resourceList) {
		this.resourceList = resourceList;
	}
} 
