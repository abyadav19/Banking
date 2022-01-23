package com.abcbank.assignment.account;

public abstract class AccountHolder {

	private String idDetails;

	public AccountHolder() {
		
	}
	public AccountHolder(String idDetails) {
		this.idDetails = idDetails;
	}

	public String getIdDetails() {
		return idDetails;
	}

}
