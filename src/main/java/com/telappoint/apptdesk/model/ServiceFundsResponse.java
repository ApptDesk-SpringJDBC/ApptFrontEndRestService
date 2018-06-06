package com.telappoint.apptdesk.model;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.telappoint.apptdesk.common.model.BaseResponse;

@JsonSerialize(include = Inclusion.NON_NULL)
public class ServiceFundsResponse extends BaseResponse {
	private Integer serviceFundsRcvdId;
	private Integer customerTypeId;
	private Integer utilityTypeId;
	private Integer serviceId;
	private String errorFlag="N";
	private String errorPage;
	private String warningFlag="N";
	private String warningPage;
	private String categoryType;
	private List<Integer> ids;
	private List<String> values;
	private List<String> ttsList;
	private List<String> audioList;
	public Integer getServiceFundsRcvdId() {
		return serviceFundsRcvdId;
	}
	public void setServiceFundsRcvdId(Integer serviceFundsRcvdId) {
		this.serviceFundsRcvdId = serviceFundsRcvdId;
	}
	public Integer getCustomerTypeId() {
		return customerTypeId;
	}
	public void setCustomerTypeId(Integer customerTypeId) {
		this.customerTypeId = customerTypeId;
	}
	public Integer getUtilityTypeId() {
		return utilityTypeId;
	}
	public void setUtilityTypeId(Integer utilityTypeId) {
		this.utilityTypeId = utilityTypeId;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public String getErrorFlag() {
		return errorFlag;
	}
	public void setErrorFlag(String errorFlag) {
		this.errorFlag = errorFlag;
	}
	public String getErrorPage() {
		return errorPage;
	}
	public void setErrorPage(String errorPage) {
		this.errorPage = errorPage;
	}
	public String getWarningFlag() {
		return warningFlag;
	}
	public void setWarningFlag(String warningFlag) {
		this.warningFlag = warningFlag;
	}
	public String getWarningPage() {
		return warningPage;
	}
	public void setWarningPage(String warningPage) {
		this.warningPage = warningPage;
	}
	public String getCategoryType() {
		return categoryType;
	}
	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}
	public List<Integer> getIds() {
		return ids;
	}
	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}
	public List<String> getValues() {
		return values;
	}
	public void setValues(List<String> values) {
		this.values = values;
	}
	public List<String> getTtsList() {
		return ttsList;
	}
	public void setTtsList(List<String> ttsList) {
		this.ttsList = ttsList;
	}
	public List<String> getAudioList() {
		return audioList;
	}
	public void setAudioList(List<String> audioList) {
		this.audioList = audioList;
	}
}
