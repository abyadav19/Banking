package com.abcbank.assignment.service;

import com.abcbank.assignment.exceptions.InvalidCustomerException;

public class TransactionImpl implements ITransaction {

	private Long accountNumber;
	private IBankService bank;

	public TransactionImpl(Long accountNumber, IBankService bank, int pin) throws Exception {
		super();
		this.accountNumber = accountNumber;
		this.bank = bank;
		if (!bank.authenticateUser(accountNumber, pin)) {
			throw new Exception("Account validation failed");
		}
	}

	public double getBalance() {
		return bank.getBalance(accountNumber);
	}

	public void credit(double amount) throws InvalidCustomerException {
		bank.credit(accountNumber, amount);

	}

	public boolean debit(double amount) {
		return bank.debit(accountNumber, amount);
	}

}
