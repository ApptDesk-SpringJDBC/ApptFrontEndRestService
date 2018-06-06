package com.telappoint.apptdesk.common.externallogin;

public class Eligibility {
  private boolean eligible;

	public boolean isEligible() {
		return eligible;
	}
	
	public void setEligible(boolean eligible) {
		this.eligible = eligible;
	}

	@Override
	public String toString() {
		return "Eligibility [eligible=" + eligible + "]";
	}
}
