package com.telappoint.apptdesk.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonSerialize(include = Inclusion.NON_NULL)
public class Procedure {
	private Integer procedureId;
	private String procedureNameOnline;
	private String procedureNameTts;
	private String procedureNameAudio;

	public Integer getProcedureId() {
		return procedureId;
	}

	public void setProcedureId(Integer procedureId) {
		this.procedureId = procedureId;
	}

	public String getProcedureNameOnline() {
		return procedureNameOnline;
	}

	public void setProcedureNameOnline(String procedureNameOnline) {
		this.procedureNameOnline = procedureNameOnline;
	}

	public String getProcedureNameTts() {
		return procedureNameTts;
	}

	public void setProcedureNameTts(String procedureNameTts) {
		this.procedureNameTts = procedureNameTts;
	}

	public String getProcedureNameAudio() {
		return procedureNameAudio;
	}

	public void setProcedureNameAudio(String procedureNameAudio) {
		this.procedureNameAudio = procedureNameAudio;
	}
}
