package com.telappoint.apptdesk.model;

import com.telappoint.apptdesk.common.model.BaseRequest;

public class CustomerInfoRequest extends BaseRequest {
	
	//fieldId~paramValue - format send by front end.
	private String inputParamValues;
	
	public String getInputParamValues() {
		return inputParamValues;
	}

	public void setInputParamValues(String inputParamValues) {
		this.inputParamValues = inputParamValues;
	}

	@Override
	public String toString() {
		return "CustomerInfoRequest [inputParamValues=" + inputParamValues + "]";
	}
}
