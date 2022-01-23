package com.abcbank.assignment.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.abcbank.assignment.account.Account;
import com.abcbank.assignment.account.ConsumerAccount;
import com.abcbank.assignment.exceptions.CustomerAlreadyExistExeception;
import com.abcbank.assignment.vo.Person;
import com.abcbank.assignment.vo.Transaction;

@Service
public class BankService implements IBankService {

	
	private Map<Long, Account> accounts;
	private Map<Long, List<Transaction>> transactions;
	private Map<String, Account> customerDetails;
	AtomicLong accountNumber;
	Random random = new Random();

	public BankService() {
		this.customerDetails = new LinkedHashMap<String, Account>();
		this.accounts = new LinkedHashMap<Long, Account>();
		this.transactions = new LinkedHashMap<Long, List<Transaction>>();
		this.accountNumber = new AtomicLong(random.nextInt(1000000000));
	}

	public Account getAccount(Long accountNumber) {
		return accounts.get(accountNumber);
	}

	public Account openConsumerAccount(Person person, int pin)
			throws CustomerAlreadyExistExeception {
		String id = person.getIdDetails();
		if (customerDetails.containsKey(id)) {
			Transaction transaction = new Transaction("Duplicate AccountCreation", person.getOpeningAmount(), "Failure");
			transactions.get(customerDetails.get(id).getAccountNumber()).add(transaction);
			throw new CustomerAlreadyExistExeception("Customer id " + id + " is already exists!!!");
		}
		accountNumber.incrementAndGet();
		ConsumerAccount consumerAccount = new ConsumerAccount(person, accountNumber.get(), pin, person.getOpeningAmount());
		customerDetails.put(id, consumerAccount);
		accounts.put(accountNumber.get(), consumerAccount);
		transactions.put(accountNumber.get(), new ArrayList<Transaction>());
		Transaction transaction = new Transaction("AccountCreation", person.getOpeningAmount(), "Success");
		transactions.get(accountNumber.get()).add(transaction);
		return consumerAccount;
	}

	public boolean authenticateUser(Long accountNumber, int pin) {
		Account account = getAccount(accountNumber);
		if (null == account) {
			return false;
		}
		return account.validatePin(pin);
	}
	
	public double getBalance(Long accountNumber) {
		Account account = getAccount(accountNumber);
		return account.getBalance();
	}

	public double credit(Long accountNumber, double amount)  {
		return credit(accountNumber, amount, "Credit");
	}

	public double credit(Long accountNumber, double amount, String transactionType) {
		Account account = getAccount(accountNumber);
		Transaction transaction = new Transaction(transactionType, amount, "Success");
		transactions.get(accountNumber).add(transaction);
		return account.creditAccount(amount);
	}

	public boolean debit(Long accountNumber, double amount) {
		Account account = getAccount(accountNumber);
		boolean result = account.debitAccount(amount);
		Transaction transaction = new Transaction("Debit", amount, result ? "Success" : "Failure");
		transactions.get(accountNumber).add(transaction);
		return result;
	}
	
	public boolean isCustomerAccountExists(Long accountNumber) {
		return null != getAccount(accountNumber);
	}

	public List<Transaction> getTransationStatement(Long accountNumber) {
		return transactions.get(accountNumber);
	}
}
