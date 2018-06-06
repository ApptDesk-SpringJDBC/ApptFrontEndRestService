package com.telappoint.apptdesk.common.externallogin;

public class Login {
	private String Token;

	public String getToken() {
		return Token;
	}

	public void setToken(String token) {
		Token = token;
	}

	@Override
	public String toString() {
		return "Login [Token=" + Token + "]";
	}
	
	
}
