package com.abcbank.assignment.vo;

import javax.validation.constraints.NotNull;

public class AuthenticationRequest {

	@NotNull
	private String userName;
	@NotNull
	private String password;
	public AuthenticationRequest() {

	}
	public AuthenticationRequest(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
