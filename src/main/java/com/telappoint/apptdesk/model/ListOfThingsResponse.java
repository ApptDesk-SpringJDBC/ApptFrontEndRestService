package com.telappoint.apptdesk.model;

import com.telappoint.apptdesk.common.model.BaseResponse;

/**
 * 
 * @author Balaji N
 *
 */
public class ListOfThingsResponse extends BaseResponse {

	private String deplayText;
	private String listOfDocsTts;
	private String listOfDocsAudio;

	public String getDeplayText() {
		return deplayText;
	}

	public void setDeplayText(String deplayText) {
		this.deplayText = deplayText;
	}

	public String getListOfDocsTts() {
		return listOfDocsTts;
	}

	public void setListOfDocsTts(String listOfDocsTts) {
		this.listOfDocsTts = listOfDocsTts;
	}

	public String getListOfDocsAudio() {
		return listOfDocsAudio;
	}

	public void setListOfDocsAudio(String listOfDocsAudio) {
		this.listOfDocsAudio = listOfDocsAudio;
	}
}
