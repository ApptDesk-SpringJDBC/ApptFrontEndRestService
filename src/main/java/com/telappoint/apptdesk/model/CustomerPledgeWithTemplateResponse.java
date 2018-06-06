package com.telappoint.apptdesk.model;

import java.util.Map;

import com.telappoint.apptdesk.common.model.BaseResponse;

public class CustomerPledgeWithTemplateResponse extends BaseResponse {
	private Map<String, String> customerPledgeFundName;
	private String customerPledgeTemplate;
	private String errorFlag="N";
	private String errorMessage;
	
	public String getCustomerPledgeTemplate() {
		return customerPledgeTemplate;
	}
	public void setCustomerPledgeTemplate(String customerPledgeTemplate) {
		this.customerPledgeTemplate = customerPledgeTemplate;
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
	public Map<String, String> getCustomerPledgeFundName() {
		return customerPledgeFundName;
	}
	public void setCustomerPledgeFundName(Map<String, String> customerPledgeFundName) {
		this.customerPledgeFundName = customerPledgeFundName;
	}
}
