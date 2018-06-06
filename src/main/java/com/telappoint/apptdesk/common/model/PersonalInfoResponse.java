package com.telappoint.apptdesk.common.model;

import java.util.List;

public class PersonalInfoResponse extends BaseResponse {
	private PersonalInfo personalInfo;
	private List<PersonalInfo> personalInfoList;
	
	public PersonalInfo getPersonalInfo() {
		return personalInfo;
	}

	public void setPersonalInfo(PersonalInfo personalInfo) {
		this.personalInfo = personalInfo;
	}

	public List<PersonalInfo> getPersonalInfoList() {
		return personalInfoList;
	}

	public void setPersonalInfoList(List<PersonalInfo> personalInfoList) {
		this.personalInfoList = personalInfoList;
	}
}
