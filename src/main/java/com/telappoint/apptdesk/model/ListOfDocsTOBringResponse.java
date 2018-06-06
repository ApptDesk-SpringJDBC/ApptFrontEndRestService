package com.telappoint.apptdesk.model;

import com.telappoint.apptdesk.common.model.BaseResponse;

public class ListOfDocsTOBringResponse extends BaseResponse {
	private ListOfDocs listOfDocs;

	public ListOfDocs getListOfDocs() {
		return listOfDocs;
	}

	public void setListOfDocs(ListOfDocs listOfDocs) {
		this.listOfDocs = listOfDocs;
	}
}
