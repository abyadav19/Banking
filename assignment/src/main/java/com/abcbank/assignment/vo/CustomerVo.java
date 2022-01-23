package com.abcbank.assignment.vo;

import javax.validation.constraints.NotNull;

public class CustomerVo {

	@NotNull
	private Long accountNumber;
	@NotNull
	private int pin;
	private double amount;
	private Long targetAccountNumber;

	public CustomerVo() {
		
	}
	
	public Long getAccountNumber() {
		return accountNumber;
	}

	public int getPin() {
		return pin;
	}

	@Override
	public String toString() {
		return "CustomerVo [accountNumber=" + accountNumber + ", amount=" + amount
				+ ", targetAccountNumber=" + targetAccountNumber + "]";
	}

	public double getAmount() {
		return amount;
	}

	public Long getTargetAccountNumber() {
		return targetAccountNumber;
	}

}
