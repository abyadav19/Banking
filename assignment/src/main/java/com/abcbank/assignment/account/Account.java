package com.abcbank.assignment.account;

import com.abcbank.assignment.vo.JwtResponse;

public abstract class Account {

	private AccountHolder accountHolder;
	private Long accountNumber;
	private int pin;
	private double balance;
	private JwtResponse jwtResponse;
	private String userName;
	private String password;

	
	protected Account(AccountHolder accountHolder, Long accountNumber, int pin, double balance) {
		this.accountHolder = accountHolder;
		this.accountNumber = accountNumber;
		this.pin = pin;
		this.balance = balance;
	}

	public AccountHolder getAccountHolder() {
		return accountHolder;
	}

	public Long getAccountNumber() {
		return accountNumber;
	}

	public int getPin() {
		return pin;
	}

	public double getBalance() {
		return balance;
	}

	public String getIdNumber() {
		return accountHolder.getIdDetails();
	}

	public boolean validatePin(int attempedPin) {
		return pin == attempedPin;
	}

	public double creditAccount(double amount) {
		return balance = balance + amount;
	}

	public boolean debitAccount(double amount) {
		if (balance >= amount) {
			balance = balance - amount;
			return true;
		}
		return false;
	}
	
	public JwtResponse getJwtResponse() {
		return jwtResponse;
	}

	public void setJwtResponse(JwtResponse jwtResponse) {
		this.jwtResponse = jwtResponse;
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

	@Override
	public String toString() {
		return "Account [accountNumber=" + accountNumber + ", balance=" + balance + ", ID=" + accountHolder.getIdDetails() + "]";
	}

}
