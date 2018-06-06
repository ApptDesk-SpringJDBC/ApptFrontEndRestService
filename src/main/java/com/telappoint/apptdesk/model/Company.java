package com.telappoint.apptdesk.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonSerialize(include = Inclusion.NON_NULL)
public class Company {
	private Integer companyId;
	private String companyNameOnline;
	private String companyNameTts;
	private String companyNameAudio;
	
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public String getCompanyNameOnline() {
		return companyNameOnline;
	}
	public void setCompanyNameOnline(String companyNameOnline) {
		this.companyNameOnline = companyNameOnline;
	}
	public String getCompanyNameTts() {
		return companyNameTts;
	}
	public void setCompanyNameTts(String companyNameTts) {
		this.companyNameTts = companyNameTts;
	}
	public String getCompanyNameAudio() {
		return companyNameAudio;
	}
	public void setCompanyNameAudio(String companyNameAudio) {
		this.companyNameAudio = companyNameAudio;
	}
}
