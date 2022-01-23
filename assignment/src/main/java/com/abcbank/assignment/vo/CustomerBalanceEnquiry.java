package com.abcbank.assignment.vo;

import javax.validation.constraints.NotNull;

public class CustomerBalanceEnquiry{

	@NotNull
	public Long accountNumber;
	@NotNull
	public int pin;
	
	public CustomerBalanceEnquiry() {
		
	}

	public Long getAccountNumber() {
		return accountNumber;
	}

	public int getPin() {
		return pin;
	}

	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}

	@Override
	public String toString() {
		return "CustomerBalanceEnquiry [accountNumber=" + accountNumber + "]";
	}
	
}
