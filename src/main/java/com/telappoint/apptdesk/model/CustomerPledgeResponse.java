package com.telappoint.apptdesk.model;

import java.util.List;

import com.telappoint.apptdesk.common.model.BaseResponse;

public class CustomerPledgeResponse extends BaseResponse {
	private List<CustomerPledge> customerPledgeList;
	private String errorFlag="N";
	private String errorMessage;
	
	public List<CustomerPledge> getCustomerPledgeList() {
		return customerPledgeList;
	}
	public void setCustomerPledgeList(List<CustomerPledge> customerPledgeList) {
		this.customerPledgeList = customerPledgeList;
	}
	public String getErrorFlag() {
		return errorFlag;
	}
	public void setErrorFlag(String errorFlag) {
		this.errorFlag = errorFlag;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
