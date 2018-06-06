package com.telappoint.apptdesk.model;



/**
 * @author Balaji N
 * 
 */

public class CustomerPledgeData {
	private String customerPledgeId;
	private String pledateDate;
	private String totalPledgeAmount;
	
	private String []vendorNames;
	private String []vendorPledgePayments;
	private String []vendorNameTtsList;
	private String []vendorNameAudioList;
	
	private String fundName;
	private String fundNameTts;
	private String fundNameAudio;
	
	public String getCustomerPledgeId() {
		return customerPledgeId;
	}
	public void setCustomerPledgeId(String customerPledgeId) {
		this.customerPledgeId = customerPledgeId;
	}
	
	public String[] getVendorNames() {
		return vendorNames;
	}
	public void setVendorNames(String[] vendorNames) {
		this.vendorNames = vendorNames;
	}
	
	public String[] getVendorPledgePayments() {
		return vendorPledgePayments;
	}
	public void setVendorPledgePayments(String[] vendorPledgePayments) {
		this.vendorPledgePayments = vendorPledgePayments;
	}
	public String[] getVendorNameTtsList() {
		return vendorNameTtsList;
	}
	public void setVendorNameTtsList(String[] vendorNameTtsList) {
		this.vendorNameTtsList = vendorNameTtsList;
	}
	public String[] getVendorNameAudioList() {
		return vendorNameAudioList;
	}
	public void setVendorNameAudioList(String[] vendorNameAudioList) {
		this.vendorNameAudioList = vendorNameAudioList;
	}
	
	public String getFundNameTts() {
		return fundNameTts;
	}
	public void setFundNameTts(String fundNameTts) {
		this.fundNameTts = fundNameTts;
	}
	public String getFundNameAudio() {
		return fundNameAudio;
	}
	public void setFundNameAudio(String fundNameAudio) {
		this.fundNameAudio = fundNameAudio;
	}
	public String getTotalPledgeAmount() {
		return totalPledgeAmount;
	}
	public void setTotalPledgeAmount(String totalPledgeAmount) {
		this.totalPledgeAmount = totalPledgeAmount;
	}
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	public String getPledateDate() {
		return pledateDate;
	}
	public void setPledateDate(String pledateDate) {
		this.pledateDate = pledateDate;
	}
}
