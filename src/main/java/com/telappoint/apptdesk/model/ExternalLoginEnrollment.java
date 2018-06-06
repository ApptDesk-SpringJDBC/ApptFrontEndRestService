package com.telappoint.apptdesk.model;

public class ExternalLoginEnrollment {
	private long participantId;
	private int programId;
	private Long programInstanceId;
	private long householdId;
	private String appointmentDate;
	private String firstName;
	private String lastName;
	private String ssn;
	private String phoneNumber;
	private String note;
	private String transaction;
	private String prospectiveClientID="0";
	private String dob;
	private String EnrollmentStartDate;
	private String EnrollmentEndDate;
	
	public String getEnrollmentStartDate() {
		return EnrollmentStartDate;
	}
	public void setEnrollmentStartDate(String enrollmentStartDate) {
		EnrollmentStartDate = enrollmentStartDate;
	}
	public String getEnrollmentEndDate() {
		return EnrollmentEndDate;
	}
	public void setEnrollmentEndDate(String enrollmentEndDate) {
		EnrollmentEndDate = enrollmentEndDate;
	}
	public long getParticipantId() {
		return participantId;
	}
	public void setParticipantId(long participantId) {
		this.participantId = participantId;
	}
	public int getProgramId() {
		return programId;
	}
	public void setProgramId(int programId) {
		this.programId = programId;
	}
	public Long getProgramInstanceId() {
		return programInstanceId;
	}
	public void setProgramInstanceId(Long programInstanceId) {
		this.programInstanceId = programInstanceId;
	}
	public long getHouseholdId() {
		return householdId;
	}
	public void setHouseholdId(long householdId) {
		this.householdId = householdId;
	}
	public String getAppointmentDate() {
		return appointmentDate;
	}
	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}
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
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getTransaction() {
		return transaction;
	}
	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}
	public String getProspectiveClientID() {
		return prospectiveClientID;
	}
	public void setProspectiveClientID(String prospectiveClientID) {
		this.prospectiveClientID = prospectiveClientID;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
}
