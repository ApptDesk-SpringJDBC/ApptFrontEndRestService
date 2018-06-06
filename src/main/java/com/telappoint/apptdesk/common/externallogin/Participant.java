package com.telappoint.apptdesk.common.externallogin;

import java.util.List;

public class Participant {
	private long participantID;
	private String firstName;
	private String lastName;
	private String ssn;
	private String dob;
	private List<HouseHold> householdList;
	
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
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	
	public long getParticipantID() {
		return participantID;
	}
	public void setParticipantID(long participantID) {
		this.participantID = participantID;
	}
	public List<HouseHold> getHouseholdList() {
		return householdList;
	}
	public void setHouseholdList(List<HouseHold> householdList) {
		this.householdList = householdList;
	}
	@Override
	public String toString() {
		return "Participant [participantID=" + participantID + ", firstName=" + firstName + ", lastName=" + lastName + ", ssn=" + ssn + ", dob=" + dob + ", householdList="
				+ householdList + "]";
	}
	
	

}
