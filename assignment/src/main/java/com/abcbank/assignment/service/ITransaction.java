package com.abcbank.assignment.service;

import com.abcbank.assignment.exceptions.InvalidCustomerException;

public interface ITransaction {

	public double getBalance();

	public void credit(double amount) throws InvalidCustomerException;

	public boolean debit(double amount);
}
