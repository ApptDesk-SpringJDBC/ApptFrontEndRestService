package com.telappoint.apptdesk.common.model;
import java.net.URI;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonAutoDetect
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class BaseResponse {
	public boolean status = true;
	public String message;
	private URI resourceUri;
	private Long transId;
	
	// ivr
	private String pageName;
	private String vxml;
	private String pageAudio;
	private String pageTTS;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public URI getResourceUri() {
		return resourceUri;
	}

	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	public String getVxml() {
		return vxml;
	}
	public void setVxml(String vxml) {
		this.vxml = vxml;
	}
	public String getPageAudio() {
		return pageAudio;
	}
	public void setPageAudio(String pageAudio) {
		this.pageAudio = pageAudio;
	}
	public String getPageTTS() {
		return pageTTS;
	}
	public void setPageTTS(String pageTTS) {
		this.pageTTS = pageTTS;
	}
	public Long getTransId() {
		return transId;
	}
	public void setTransId(Long transId) {
		this.transId = transId;
	}	
}
