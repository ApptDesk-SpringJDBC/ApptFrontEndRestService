package com.telappoint.apptdesk.common.model;

import com.telappoint.apptdesk.model.AuthResponse;

/**
 * 
 * @author Balaji N
 *
 */
public class ExternalLogicResponse {
	private Long participantId;
	private Long houseHoldId;
	private AuthResponse authResponse;
	
	public Long getParticipantId() {
		return participantId;
	}
	public void setParticipantId(Long participantId) {
		this.participantId = participantId;
	}
	
	public Long getHouseHoldId() {
		return houseHoldId;
	}
	public void setHouseHoldId(Long houseHoldId) {
		this.houseHoldId = houseHoldId;
	}
	public AuthResponse getAuthResponse() {
		return authResponse;
	}
	public void setAuthResponse(AuthResponse authResponse) {
		this.authResponse = authResponse;
	}
}
