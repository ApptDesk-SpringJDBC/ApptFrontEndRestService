package com.telappoint.apptdesk.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonSerialize(include = Inclusion.NON_NULL)
public class Options {
	private String optionKey;
	private String optionValue;
	private String warningMsg;
	private String errorMsg;
	
	private String optionTTS;
	private String optionAudio;
	private String warningMsgTTS;
	private String warningMsgAudio;
	private String errorMsgTts;
	private String errorMsgAudio;

	public String getOptionKey() {
		return optionKey;
	}

	public void setOptionKey(String optionKey) {
		this.optionKey = optionKey;
	}

	public String getOptionValue() {
		return optionValue;
	}

	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}

	public String getOptionTTS() {
		return optionTTS;
	}

	public void setOptionTTS(String optionTTS) {
		this.optionTTS = optionTTS;
	}

	public String getOptionAudio() {
		return optionAudio;
	}

	public void setOptionAudio(String optionAudio) {
		this.optionAudio = optionAudio;
	}

	public String getWarningMsg() {
		return warningMsg;
	}

	public void setWarningMsg(String warningMsg) {
		this.warningMsg = warningMsg;
	}

	public String getWarningMsgTTS() {
		return warningMsgTTS;
	}

	public void setWarningMsgTTS(String warningMsgTTS) {
		this.warningMsgTTS = warningMsgTTS;
	}

	public String getWarningMsgAudio() {
		return warningMsgAudio;
	}

	public void setWarningMsgAudio(String warningMsgAudio) {
		this.warningMsgAudio = warningMsgAudio;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getErrorMsgTts() {
		return errorMsgTts;
	}

	public void setErrorMsgTts(String errorMsgTts) {
		this.errorMsgTts = errorMsgTts;
	}

	public String getErrorMsgAudio() {
		return errorMsgAudio;
	}

	public void setErrorMsgAudio(String errorMsgAudio) {
		this.errorMsgAudio = errorMsgAudio;
	}
}
