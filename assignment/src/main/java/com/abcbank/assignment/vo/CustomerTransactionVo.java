package com.abcbank.assignment.vo;

import javax.validation.constraints.NotNull;

public class CustomerTransactionVo extends CustomerBalanceEnquiry{

	
	@NotNull
	protected double amount;

	public CustomerTransactionVo() {
		
	}
	
	public double getAmount() {
		return amount;
	}
	
	public void setAmount(double amount) {
		this.amount= amount;
	}

	@Override
	public String toString() {
		return "CustomerTransactionVo [amount=" + amount + ", accountNumber=" + accountNumber + "]";
	}
	
	
}
