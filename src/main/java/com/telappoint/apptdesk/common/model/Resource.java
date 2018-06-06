package com.telappoint.apptdesk.common.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonAutoDetect
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Resource {
	private Integer resourceId;
	private String resourceName;
	private String resourceAudio;
	
	public Integer getResourceId() {
		return resourceId;
	}
	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getResourceAudio() {
		return resourceAudio;
	}
	public void setResourceAudio(String resourceAudio) {
		this.resourceAudio = resourceAudio;
	}
}
