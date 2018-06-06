package com.telappoint.apptdesk.model;

import java.util.Map;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.telappoint.apptdesk.common.model.BaseResponse;

/**
 * @author Balaji Nandarapu
 */

@JsonSerialize(include = Inclusion.NON_NULL)
public class AuthResponse extends BaseResponse {
	private boolean isAuthSuccess;
	private String authMessage="";
	private long customerId=(long)-1;
	private String blocked ="N";
	private String blockedMessage;
	private String eligible;
	private String eligibleMessage;
	private Integer resourceId;
	
	public boolean isAuthSuccess() {
		return isAuthSuccess;
	}

	public void setAuthSuccess(boolean isAuthSuccess) {
		this.isAuthSuccess = isAuthSuccess;
	}

	public String getAuthMessage() {
		return authMessage;
	}

	public void setAuthMessage(String authMessage) {
		this.authMessage = authMessage;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getBlocked() {
		return blocked;
	}

	public void setBlocked(String blocked) {
		this.blocked = blocked;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getBlockedMessage() {
		return blockedMessage;
	}

	public void setBlockedMessage(String blockedMessage) {
		this.blockedMessage = blockedMessage;
	}
	
	public String getEligible() {
		return eligible;
	}

	public void setEligible(String eligible) {
		this.eligible = eligible;
	}

	public String getEligibleMessage() {
		return eligibleMessage;
	}

	public void setEligibleMessage(String eligibleMessage) {
		this.eligibleMessage = eligibleMessage;
	}

	public Integer getResourceId() {
		return resourceId;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}
}
