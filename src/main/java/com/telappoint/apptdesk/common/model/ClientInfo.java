package com.telappoint.apptdesk.common.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonAutoDetect
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ClientInfo {
	private String clientCode;
	private String clientName;
	private String loginFirst;
	private String token;
	
	public String getLoginFirst() {
		return loginFirst;
	}
	public void setLoginFirst(String loginFirst) {
		this.loginFirst = loginFirst;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
}
