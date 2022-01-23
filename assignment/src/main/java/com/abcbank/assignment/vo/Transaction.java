package com.abcbank.assignment.vo;

import java.util.Date;
import java.util.Random;

public class Transaction {

	private String transactionType;
	private double amount;
	private String transactionStatus;
	private String date;
	Random random = new Random();
	private Long trasactionId;
	public Transaction(String transactionType, double amount, String transactionStatus) {
		super();
		this.transactionType = transactionType;
		this.amount = amount;
		this.transactionStatus = transactionStatus;
		this.date = new Date(System.currentTimeMillis()).toLocaleString();
		this.trasactionId = new Long(random.nextLong());
	}

	public String getTransactionType() {
		return transactionType;
	}

	public double getAmount() {
		return amount;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}
	
	public String getDate() {
		return date;
	}
	
	public Long getTrasactionId() {
		return trasactionId;
	}

}
