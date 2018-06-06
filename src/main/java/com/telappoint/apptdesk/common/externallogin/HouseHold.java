package com.telappoint.apptdesk.common.externallogin;

import java.math.BigInteger;

public class HouseHold {
	private BigInteger householdID;
	private String householdName;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String postalCode;
	public BigInteger getHouseholdID() {
		return householdID;
	}
	public void setHouseholdID(BigInteger householdID) {
		this.householdID = householdID;
	}
	public String getHouseholdName() {
		return householdName;
	}
	public void setHouseholdName(String householdName) {
		this.householdName = householdName;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
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
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	@Override
	public String toString() {
		return "HouseHold [householdID=" + householdID + ", householdName=" + householdName + ", address1=" + address1 + ", address2=" + address2 + ", city=" + city + ", state="
				+ state + ", postalCode=" + postalCode + "]";
	}
	
	
}
