package com.telappoint.apptdesk.model;

import com.telappoint.apptdesk.common.model.BaseResponse;

public class PledgeLetterData extends BaseResponse {
	public String pledgeLetterBody;

	public String getPledgeLetterBody() {
		return pledgeLetterBody;
	}

	public void setPledgeLetterBody(String pledgeLetterBody) {
		this.pledgeLetterBody = pledgeLetterBody;
	}
}
