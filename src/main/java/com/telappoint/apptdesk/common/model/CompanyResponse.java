package com.telappoint.apptdesk.common.model;

import java.util.List;

import com.telappoint.apptdesk.model.Company;

public class CompanyResponse extends BaseResponse {
	private List<Company> companyList;

	public List<Company> getCompanyList() {
		return companyList;
	}

	public void setCompanyList(List<Company> companyList) {
		this.companyList = companyList;
	}
}
