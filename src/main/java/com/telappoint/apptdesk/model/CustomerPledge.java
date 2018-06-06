package com.telappoint.apptdesk.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;


/**
 * @author Balaji N
 * 
 */
@JsonAutoDetect
@JsonSerialize(include = Inclusion.NON_NULL)
public class CustomerPledge extends Customer {
	private String customerPledgeId;
	private String fundId;
	private String fundName;
	private String applyToAppt;
	private String totalPledgeAmt;
	private String pledgeStatusId;
	private String pledgeStatus;
	private String vendor1Id;
	private String vendor1Name;
	private String vendor1Payment;
	private String vendor2Id;
	private String vendor2Name;
	private String vendor2Payment;
	private String vendor3Id;
	private String vendor3Name;
	private String vendor3Payment;
	private String pmtUpdateBy;
	private String pmtUpdateByName;
	private String pledgeDateTime;
	private String locationId;
	private String resourceId;
	private String serviceId;
	private String scheduleId="-1";
	private String liheapFund;
	private String pseHelpFund;
	private String apptDateTime;
	
	private String vendor1NameTts;
	private String vendor2NameTts;
	private String vendor3NameTts;
	
	private String vendor1NameAudio;
	private String vendor2NameAudio;
	private String vendor3NameAudio;
	
	private String fundNameTts;
	private String fundNameAudio;
	
	
	@JsonIgnore
	private int vendorCount;

	public String getPmtUpdateBy() {
		return pmtUpdateBy;
	}
	public void setPmtUpdateBy(String pmtUpdateBy) {
		this.pmtUpdateBy = pmtUpdateBy;
	}
	
	
	
	public String getFundId() {
		return fundId;
	}
	public void setFundId(String fundId) {
		this.fundId = fundId;
	}
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	public String getApplyToAppt() {
		return applyToAppt;
	}
	public void setApplyToAppt(String applyToAppt) {
		this.applyToAppt = applyToAppt;
	}
	public String getTotalPledgeAmt() {
		return totalPledgeAmt;
	}
	public void setTotalPledgeAmt(String totalPledgeAmt) {
		this.totalPledgeAmt = totalPledgeAmt;
	}
	public String getPledgeStatusId() {
		return pledgeStatusId;
	}
	public void setPledgeStatusId(String pledgeStatusId) {
		this.pledgeStatusId = pledgeStatusId;
	}
	public String getPledgeStatus() {
		return pledgeStatus;
	}
	public void setPledgeStatus(String pledgeStatus) {
		this.pledgeStatus = pledgeStatus;
	}
	public String getVendor1Id() {
		return vendor1Id;
	}
	public void setVendor1Id(String vendor1Id) {
		this.vendor1Id = vendor1Id;
	}
	public String getVendor1Name() {
		return vendor1Name;
	}
	public void setVendor1Name(String vendor1Name) {
		this.vendor1Name = vendor1Name;
	}
	public String getVendor1Payment() {
		return vendor1Payment;
	}
	public void setVendor1Payment(String vendor1Payment) {
		this.vendor1Payment = vendor1Payment;
	}
	public String getVendor2Id() {
		return vendor2Id;
	}
	public void setVendor2Id(String vendor2Id) {
		this.vendor2Id = vendor2Id;
	}
	public String getVendor2Name() {
		return vendor2Name;
	}
	public void setVendor2Name(String vendor2Name) {
		this.vendor2Name = vendor2Name;
	}
	public String getVendor2Payment() {
		return vendor2Payment;
	}
	public void setVendor2Payment(String vendor2Payment) {
		this.vendor2Payment = vendor2Payment;
	}
	public String getVendor3Id() {
		return vendor3Id;
	}
	public void setVendor3Id(String vendor3Id) {
		this.vendor3Id = vendor3Id;
	}
	public String getVendor3Name() {
		return vendor3Name;
	}
	public void setVendor3Name(String vendor3Name) {
		this.vendor3Name = vendor3Name;
	}
	public String getVendor3Payment() {
		return vendor3Payment;
	}
	public void setVendor3Payment(String vendor3Payment) {
		this.vendor3Payment = vendor3Payment;
	}

	public String getPmtUpdateByName() {
		return pmtUpdateByName;
	}
	public void setPmtUpdateByName(String pmtUpdateByName) {
		this.pmtUpdateByName = pmtUpdateByName;
	}
	public String getCustomerPledgeId() {
		return customerPledgeId;
	}
	public void setCustomerPledgeId(String customerPledgeId) {
		this.customerPledgeId = customerPledgeId;
	}
	public String getPledgeDateTime() {
		return pledgeDateTime;
	}
	public void setPledgeDateTime(String pledgeDateTime) {
		this.pledgeDateTime = pledgeDateTime;
	}
	
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}
	public String getLiheapFund() {
		return liheapFund;
	}
	public void setLiheapFund(String liheapFund) {
		this.liheapFund = liheapFund;
	}
	public String getPseHelpFund() {
		return pseHelpFund;
	}
	public void setPseHelpFund(String pseHelpFund) {
		this.pseHelpFund = pseHelpFund;
	}

	public String getApptDateTime() {
		return apptDateTime;
	}
	public void setApptDateTime(String apptDateTime) {
		this.apptDateTime = apptDateTime;
	}
	
	public int getVendorCount() {
		return vendorCount;
	}
	public void setVendorCount(int vendorCount) {
		this.vendorCount = vendorCount;
	}
	public String getVendor1NameTts() {
		return vendor1NameTts;
	}
	public void setVendor1NameTts(String vendor1NameTts) {
		this.vendor1NameTts = vendor1NameTts;
	}
	public String getVendor2NameTts() {
		return vendor2NameTts;
	}
	public void setVendor2NameTts(String vendor2NameTts) {
		this.vendor2NameTts = vendor2NameTts;
	}
	public String getVendor3NameTts() {
		return vendor3NameTts;
	}
	public void setVendor3NameTts(String vendor3NameTts) {
		this.vendor3NameTts = vendor3NameTts;
	}
	public String getVendor1NameAudio() {
		return vendor1NameAudio;
	}
	public void setVendor1NameAudio(String vendor1NameAudio) {
		this.vendor1NameAudio = vendor1NameAudio;
	}
	public String getVendor2NameAudio() {
		return vendor2NameAudio;
	}
	public void setVendor2NameAudio(String vendor2NameAudio) {
		this.vendor2NameAudio = vendor2NameAudio;
	}
	public String getVendor3NameAudio() {
		return vendor3NameAudio;
	}
	public void setVendor3NameAudio(String vendor3NameAudio) {
		this.vendor3NameAudio = vendor3NameAudio;
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
}
