package com.telappoint.apptdesk.model;

import java.util.List;

import com.telappoint.apptdesk.common.model.BaseResponse;

public class MessageResponse extends BaseResponse {
	private List<String> messages;

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
}
