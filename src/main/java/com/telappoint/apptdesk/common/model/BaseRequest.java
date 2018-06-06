package com.telappoint.apptdesk.common.model;
/**
 * 
 * @author Balaji N
 *
 */
public class BaseRequest {
	private String clientCode;
	private String langCode;
	private String device;
	private Long transId;
	private String token;
	
	// mobile app
	private String uuid;

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	
	public String getLangCode() {
		return langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	public Long getTransId() {
		return transId;
	}

	public void setTransId(Long transId) {
		this.transId = transId;
	}

	

	@Override
	public String toString() {
		return "BaseRequest [clientCode=" + clientCode + ", langCode=" + langCode + ", device=" + device + ", transId=" + transId + ", token=" + token + "]";
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
