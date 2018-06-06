package com.telappoint.apptdesk.common.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.telappoint.apptdesk.model.Customer;
import com.telappoint.apptdesk.model.LoginPageFields;

@JsonAutoDetect
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class PersonalInfo {
	private String clientCode;
	private String clientName;
	private List<LoginPageFields> mobilePageFields = new ArrayList<>();
	private Map<String,String> pageData;
	private Customer customer;
	
	public List<LoginPageFields> getMobilePageFields() {
		return mobilePageFields;
	}
	public void setMobilePageFields(List<LoginPageFields> mobilePageFields) {
		this.mobilePageFields = mobilePageFields;
	}
	public Map<String, String> getPageData() {
		return pageData;
	}
	public void setPageData(Map<String, String> pageData) {
		this.pageData = pageData;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
}
