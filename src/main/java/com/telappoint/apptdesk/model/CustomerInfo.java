package com.telappoint.apptdesk.model;

import com.telappoint.apptdesk.common.model.BaseResponse;

public class CustomerInfo extends BaseResponse {
	private String javaRef;
	private String fieldNames;
	private String displayType;
    private Customer customer;

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getJavaRef() {
		return javaRef;
	}

	public void setJavaRef(String javaRef) {
		this.javaRef = javaRef;
	}

	public String getFieldNames() {
		return fieldNames;
	}

	public void setFieldNames(String fieldNames) {
		this.fieldNames = fieldNames;
	}

	public String getDisplayType() {
		return displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

	@Override
	public String toString() {
		return "CustomerInfo [javaRef=" + javaRef + ", fieldNames=" + fieldNames + ", displayType=" + displayType + ", customer=" + customer + "]";
	}
}
