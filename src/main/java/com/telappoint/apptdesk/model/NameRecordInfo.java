package com.telappoint.apptdesk.model;


import com.telappoint.apptdesk.common.model.BaseRequest;

public class NameRecordInfo extends BaseRequest {
	private Long scheduleId;
	private Long customerId;
	private String mp3FilePath;
	private Integer duration;
	private String fileName;
	
	
	public Long getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public String getMp3FilePath() {
		return mp3FilePath;
	}
	public void setMp3FilePath(String mp3FilePath) {
		this.mp3FilePath = mp3FilePath;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	@Override
	public String toString() {
		return "NameRecordInfo [scheduleId=" + scheduleId + ", customerId=" + customerId + ", mp3FilePath=" + mp3FilePath + ", duration=" + duration + ", fileName=" + fileName
				+ "]";
	}
}
