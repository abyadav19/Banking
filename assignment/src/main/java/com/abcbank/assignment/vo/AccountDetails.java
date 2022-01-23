package com.abcbank.assignment.vo;

import com.abcbank.assignment.account.AccountHolder;

public class AccountDetails {

	private AccountHolder accountHoler;
	private int pin;
	private double depostedAmount;
	private Long accountNumber;

	public AccountHolder getAccountHoler() {
		return accountHoler;
	}

	public void setAccountHoler(AccountHolder accountHoler) {
		this.accountHoler = accountHoler;
	}

	public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}

	public double getDepostedAmount() {
		return depostedAmount;
	}

	public void setDepostedAmount(double depostedAmount) {
		this.depostedAmount = depostedAmount;
	}

	public Long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}

}
