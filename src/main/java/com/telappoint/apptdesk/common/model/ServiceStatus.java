package com.telappoint.apptdesk.common.model;

public class ServiceStatus extends BaseResponse {
	private boolean isClosed;
	private String closedMessage;
	private String closedAudio;
	private String closedTts;
	
	public boolean isClosed() {
		return isClosed;
	}
	public void setClosed(boolean isClosed) {
		this.isClosed = isClosed;
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
}
