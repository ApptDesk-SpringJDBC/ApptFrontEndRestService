package com.telappoint.apptdesk.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonSerialize(include = Inclusion.NON_NULL)
public class Service {
	private Integer serviceId;
	private String serviceNameOnline;
	private String serviceNameIvrTts;
	private String serviceNameIvrAudio;
	private Integer blocks;
	private Integer buffer;
	private String allowDuplAppt;
	private String skipDateTime;
	private String closed;
	private String closedMessage;
	private String closedAudio;
	private String closedTts;
	private String bucketStyle;
	private String sendReminder;
	private String closedLocationIds;
	private String sunOpen;
	private String monOpen;
	private String tueOpen;
	private String wedOpen;
	private String thuOpen;
	private String friOpen;
	private String satOpen;
	private String apptStartDate;
	private String apptEndDate;
	
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceNameOnline() {
		return serviceNameOnline;
	}
	public void setServiceNameOnline(String serviceNameOnline) {
		this.serviceNameOnline = serviceNameOnline;
	}

	public String getServiceNameIvrTts() {
		return serviceNameIvrTts;
	}
	public void setServiceNameIvrTts(String serviceNameIvrTts) {
		this.serviceNameIvrTts = serviceNameIvrTts;
	}
	public String getServiceNameIvrAudio() {
		return serviceNameIvrAudio;
	}
	public void setServiceNameIvrAudio(String serviceNameIvrAudio) {
		this.serviceNameIvrAudio = serviceNameIvrAudio;
	}
	public Integer getBlocks() {
		return blocks;
	}
	public void setBlocks(Integer blocks) {
		this.blocks = blocks;
	}
	public Integer getBuffer() {
		return buffer;
	}
	public void setBuffer(Integer buffer) {
		this.buffer = buffer;
	}
	public String getAllowDuplAppt() {
		return allowDuplAppt;
	}
	public void setAllowDuplAppt(String allowDuplAppt) {
		this.allowDuplAppt = allowDuplAppt;
	}
	public String getSkipDateTime() {
		return skipDateTime;
	}
	public void setSkipDateTime(String skipDateTime) {
		this.skipDateTime = skipDateTime;
	}
	public String getClosed() {
		return closed;
	}
	public void setClosed(String closed) {
		this.closed = closed;
	}
	public String getClosedMessage() {
		return closedMessage;
	}
	public void setClosedMessage(String closedMessage) {
		this.closedMessage = closedMessage;
	}
	public String getClosedAudio() {
		return closedAudio;
	}
	public void setClosedAudio(String closedAudio) {
		this.closedAudio = closedAudio;
	}
	public String getClosedTts() {
		return closedTts;
	}
	public void setClosedTts(String closedTts) {
		this.closedTts = closedTts;
	}
	public String getBucketStyle() {
		return bucketStyle;
	}
	public void setBucketStyle(String bucketStyle) {
		this.bucketStyle = bucketStyle;
	}
	public String getSendReminder() {
		return sendReminder;
	}
	public void setSendReminder(String sendReminder) {
		this.sendReminder = sendReminder;
	}
	public String getClosedLocationIds() {
		return closedLocationIds;
	}
	public void setClosedLocationIds(String closedLocationIds) {
		this.closedLocationIds = closedLocationIds;
	}
	
	public String getSunOpen() {
		return sunOpen;
	}
	public void setSunOpen(String sunOpen) {
		this.sunOpen = sunOpen;
	}
	public String getMonOpen() {
		return monOpen;
	}
	public void setMonOpen(String monOpen) {
		this.monOpen = monOpen;
	}
	public String getTueOpen() {
		return tueOpen;
	}
	public void setTueOpen(String tueOpen) {
		this.tueOpen = tueOpen;
	}
	public String getWedOpen() {
		return wedOpen;
	}
	public void setWedOpen(String wedOpen) {
		this.wedOpen = wedOpen;
	}
	public String getThuOpen() {
		return thuOpen;
	}
	public void setThuOpen(String thuOpen) {
		this.thuOpen = thuOpen;
	}
	public String getFriOpen() {
		return friOpen;
	}
	public void setFriOpen(String friOpen) {
		this.friOpen = friOpen;
	}
	public String getSatOpen() {
		return satOpen;
	}
	public void setSatOpen(String satOpen) {
		this.satOpen = satOpen;
	}
	public String getApptStartDate() {
		return apptStartDate;
	}
	public void setApptStartDate(String apptStartDate) {
		this.apptStartDate = apptStartDate;
	}
	public String getApptEndDate() {
		return apptEndDate;
	}
	public void setApptEndDate(String apptEndDate) {
		this.apptEndDate = apptEndDate;
	}
}
