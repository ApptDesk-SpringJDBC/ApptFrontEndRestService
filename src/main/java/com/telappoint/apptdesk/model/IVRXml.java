package com.telappoint.apptdesk.model;


/**
 * 
 * @author Balaji
 *
 */
public class IVRXml {
	private Long ivrXmlId;
	private String pageName;
	private String vxml;
	private String pageAudio;
	private String pageTTS;
	private String langCode;
	private String appCode;

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

	public String getLangCode() {
		return langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	public Long getIvrXmlId() {
		return ivrXmlId;
	}

	public void setIvrXmlId(Long ivrXmlId) {
		this.ivrXmlId = ivrXmlId;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getAppCode() {
		return appCode;
	}
}
