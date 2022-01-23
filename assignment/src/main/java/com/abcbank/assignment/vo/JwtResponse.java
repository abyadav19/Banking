package com.abcbank.assignment.vo;

public class JwtResponse {

	private String jwt;

	public JwtResponse(String jwt) {
		super();
		this.jwt = jwt;
	}

	public String getJwt() {
		return jwt;
	}
	
}
