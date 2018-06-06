package com.telappoint.apptdesk.model;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.telappoint.apptdesk.common.model.BaseResponse;

@JsonSerialize(include = Inclusion.NON_NULL)
public class ProcedureResponse extends BaseResponse {
	private Integer procedureId;
	
	// for procedure drop down
	private List<Procedure> procedureList;

	public Integer getProcedureId() {
		return procedureId;
	}

	public void setProcedureId(Integer procedureId) {
		this.procedureId = procedureId;
	}

	public List<Procedure> getProcedureList() {
		return procedureList;
	}

	public void setProcedureList(List<Procedure> procedureList) {
		this.procedureList = procedureList;
	}
}
