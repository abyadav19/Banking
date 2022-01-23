package com.abcbank.assignment.service;

import java.util.List;

import com.abcbank.assignment.account.Account;
import com.abcbank.assignment.exceptions.CustomerAlreadyExistExeception;
import com.abcbank.assignment.vo.Person;
import com.abcbank.assignment.vo.Transaction;

public interface IBankService {

	public Account openConsumerAccount(Person person, int pin)
			throws CustomerAlreadyExistExeception;

	public boolean authenticateUser(Long accountNumner, int pin);

	public double getBalance(Long accountNumner);

	public double credit(Long accountNumner, double amount);

	public double credit(Long accountNumber, double amount, String transactionType);

	public boolean debit(Long accountNumner, double amount);

	boolean isCustomerAccountExists(Long accountNumber);
	
	public List<Transaction> getTransationStatement(Long accountNumber);
	
	public Account getAccount(Long accountNumber);
}
