package com.telappoint.apptdesk.common.model;

/**
 * 
 * @author Balaji
 *
 */
public class EmailRequest {
	private String fromAddress;
	private String toAddress;
	private String replyAddress;
	private String [] ccAddresses;
	private String subject;
	private String emailBody;	
	private String emailType; // confirm or cancel
	
	// below fields for ics calendar email.
	private String method="REQUEST"; // REQUEST
	private String status; // CONFIRMED OR CANCELLED

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public String getReplyAddress() {
		return replyAddress;
	}

	public void setReplyAddress(String replyAddress) {
		this.replyAddress = replyAddress;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getEmailBody() {
		return emailBody;
	}

	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}

	public String getEmailType() {
		return emailType;
	}

	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}

	public String [] getCcAddresses() {
		return ccAddresses;
	}

	public void setCcAddresses(String [] ccAddresses) {
		this.ccAddresses = ccAddresses;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
