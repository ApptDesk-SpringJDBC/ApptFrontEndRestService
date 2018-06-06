package com.telappoint.apptdesk.common.model;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonAutoDetect
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class HomePageResponse extends BaseResponse {
	private Map<String, String> pageData;
	private Map<String, String> displayFlags;
	private List<ClientInfo> clientList;
	private String firstName;
	private String lastName;

	public Map<String, String> getPageData() {
		return pageData;
	}

	public void setPageData(Map<String, String> pageData) {
		this.pageData = pageData;
	}

	public Map<String, String> getDisplayFlags() {
		return displayFlags;
	}

	public void setDisplayFlags(Map<String, String> displayFlags) {
		this.displayFlags = displayFlags;
	}

	public List<ClientInfo> getClientList() {
		return clientList;
	}

	public void setClientList(List<ClientInfo> clientList) {
		this.clientList = clientList;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
