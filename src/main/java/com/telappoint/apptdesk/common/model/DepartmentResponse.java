package com.telappoint.apptdesk.common.model;

import java.util.List;

public class DepartmentResponse extends BaseResponse {
	private List<Department> departmentList;

	public List<Department> getDepartmentList() {
		return departmentList;
	}

	public void setDepartmentList(List<Department> departmentList) {
		this.departmentList = departmentList;
	}
}
