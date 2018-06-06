package com.telappoint.apptdesk.common.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonAutoDetect
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Department {
	private Integer departmentId;
	private String departmentName;
	private String departmentNameIvrTts;
	private String departmentNameIvrAudio;
	
	public Integer getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getDepartmentNameIvrTts() {
		return departmentNameIvrTts;
	}
	public void setDepartmentNameIvrTts(String departmentNameIvrTts) {
		this.departmentNameIvrTts = departmentNameIvrTts;
	}
	public String getDepartmentNameIvrAudio() {
		return departmentNameIvrAudio;
	}
	public void setDepartmentNameIvrAudio(String departmentNameIvrAudio) {
		this.departmentNameIvrAudio = departmentNameIvrAudio;
	}
}
