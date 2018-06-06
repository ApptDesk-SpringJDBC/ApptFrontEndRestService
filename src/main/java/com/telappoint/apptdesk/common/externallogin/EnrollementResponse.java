package com.telappoint.apptdesk.common.externallogin;

public class EnrollementResponse {
	private boolean eligible;
	private String enrollmentId;
	public boolean isEligible() {
		return eligible;
	}
	public void setEligible(boolean eligible) {
		this.eligible = eligible;
	}
	public String getEnrollmentId() {
		return enrollmentId;
	}
	public void setEnrollmentId(String enrollmentId) {
		this.enrollmentId = enrollmentId;
	}
	@Override
	public String toString() {
		return "EnrollementResponse [eligible=" + eligible + ", enrollmentId=" + enrollmentId + "]";
	}
}
