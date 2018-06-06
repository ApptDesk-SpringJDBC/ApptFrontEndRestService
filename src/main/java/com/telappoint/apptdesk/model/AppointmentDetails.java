package com.telappoint.apptdesk.model;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonSerialize(include = Inclusion.NON_NULL)
public class AppointmentDetails {
	private String accountNumber;
	private String firstName;
	private String lastName;
	private String homePhone;
	private String cellPhone;
	private String workPhone;
	private String contactPhone;
	private String email;
	private String locationName;
	private String locationAddress;
	private String city;
	private String state;
	private String zip;
	private String timeZone;
	private String resourceName;
	private String serviceName;
	private String apptDateTime;
	private Long scheduleId;
	private Long confirmationNumber;
	private String serviceNameTts;
	private String serviceNameAudio;
	private String resourceNameAudio;
	private String locationNameTts;
	private String locationNameAudio;
	private String attrib1;
	private String attrib2;
	private String attrib3;
	private String attrib4;
	private String attrib5;
	private String attrib6;
	private String attrib7;
	private String attrib8;
	private String attrib9;
	private String attrib10;
	
	private String attrib11;
	private String attrib12;
	private String attrib13;
	private String attrib14;
	private String attrib15;
	private String attrib16;
	private String attrib17;
	private String attrib18;
	private String attrib19;
	private String attrib20;
	private List<TransScript> listDocsScript;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getHomePhone() {
		return homePhone;
	}
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}
	public String getCellPhone() {
		return cellPhone;
	}
	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
	public String getWorkPhone() {
		return workPhone;
	}
	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getLocationAddress() {
		return locationAddress;
	}
	public void setLocationAddress(String locationAddress) {
		this.locationAddress = locationAddress;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getApptDateTime() {
		return apptDateTime;
	}
	public void setApptDateTime(String apptDateTime) {
		this.apptDateTime = apptDateTime;
	}
	public Long getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}
	public Long getConfirmationNumber() {
		return confirmationNumber;
	}
	public void setConfirmationNumber(Long confirmationNumber) {
		this.confirmationNumber = confirmationNumber;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public String getServiceNameTts() {
		return serviceNameTts;
	}
	public void setServiceNameTts(String serviceNameTts) {
		this.serviceNameTts = serviceNameTts;
	}
	public String getServiceNameAudio() {
		return serviceNameAudio;
	}
	public void setServiceNameAudio(String serviceNameAudio) {
		this.serviceNameAudio = serviceNameAudio;
	}
	public String getResourceNameAudio() {
		return resourceNameAudio;
	}
	public void setResourceNameAudio(String resourceNameAudio) {
		this.resourceNameAudio = resourceNameAudio;
	}
	public String getLocationNameTts() {
		return locationNameTts;
	}
	public void setLocationNameTts(String locationNameTts) {
		this.locationNameTts = locationNameTts;
	}
	public String getLocationNameAudio() {
		return locationNameAudio;
	}
	public void setLocationNameAudio(String locationNameAudio) {
		this.locationNameAudio = locationNameAudio;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public String getAttrib1() {
		return attrib1;
	}
	public void setAttrib1(String attrib1) {
		this.attrib1 = attrib1;
	}
	public String getAttrib2() {
		return attrib2;
	}
	public void setAttrib2(String attrib2) {
		this.attrib2 = attrib2;
	}
	public String getAttrib3() {
		return attrib3;
	}
	public void setAttrib3(String attrib3) {
		this.attrib3 = attrib3;
	}
	public String getAttrib4() {
		return attrib4;
	}
	public void setAttrib4(String attrib4) {
		this.attrib4 = attrib4;
	}
	public String getAttrib5() {
		return attrib5;
	}
	public void setAttrib5(String attrib5) {
		this.attrib5 = attrib5;
	}
	public String getAttrib6() {
		return attrib6;
	}
	public void setAttrib6(String attrib6) {
		this.attrib6 = attrib6;
	}
	public String getAttrib7() {
		return attrib7;
	}
	public void setAttrib7(String attrib7) {
		this.attrib7 = attrib7;
	}
	public String getAttrib8() {
		return attrib8;
	}
	public void setAttrib8(String attrib8) {
		this.attrib8 = attrib8;
	}
	public String getAttrib9() {
		return attrib9;
	}
	public void setAttrib9(String attrib9) {
		this.attrib9 = attrib9;
	}
	public String getAttrib10() {
		return attrib10;
	}
	public void setAttrib10(String attrib10) {
		this.attrib10 = attrib10;
	}
	public String getAttrib11() {
		return attrib11;
	}
	public void setAttrib11(String attrib11) {
		this.attrib11 = attrib11;
	}
	public String getAttrib12() {
		return attrib12;
	}
	public void setAttrib12(String attrib12) {
		this.attrib12 = attrib12;
	}
	public String getAttrib13() {
		return attrib13;
	}
	public void setAttrib13(String attrib13) {
		this.attrib13 = attrib13;
	}
	public String getAttrib14() {
		return attrib14;
	}
	public void setAttrib14(String attrib14) {
		this.attrib14 = attrib14;
	}
	public String getAttrib15() {
		return attrib15;
	}
	public void setAttrib15(String attrib15) {
		this.attrib15 = attrib15;
	}
	public String getAttrib16() {
		return attrib16;
	}
	public void setAttrib16(String attrib16) {
		this.attrib16 = attrib16;
	}
	public String getAttrib17() {
		return attrib17;
	}
	public void setAttrib17(String attrib17) {
		this.attrib17 = attrib17;
	}
	public String getAttrib18() {
		return attrib18;
	}
	public void setAttrib18(String attrib18) {
		this.attrib18 = attrib18;
	}
	public String getAttrib19() {
		return attrib19;
	}
	public void setAttrib19(String attrib19) {
		this.attrib19 = attrib19;
	}
	public String getAttrib20() {
		return attrib20;
	}
	public void setAttrib20(String attrib20) {
		this.attrib20 = attrib20;
	}
	
	public List<TransScript> getListDocsScript() {
		return listDocsScript;
	}
	public void setListDocsScript(List<TransScript> listDocsScript) {
		this.listDocsScript = listDocsScript;
	}
}
