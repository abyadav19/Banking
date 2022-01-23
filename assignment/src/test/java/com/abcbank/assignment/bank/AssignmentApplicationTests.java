package com.abcbank.assignment.bank;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.abcbank.assignment.account.Account;
import com.abcbank.assignment.exceptions.CustomerAlreadyExistExeception;
import com.abcbank.assignment.exceptions.InvalidCustomerException;
import com.abcbank.assignment.service.BankService;
import com.abcbank.assignment.service.IBankService;
import com.abcbank.assignment.service.ITransaction;
import com.abcbank.assignment.service.TransactionImpl;
import com.abcbank.assignment.vo.Person;

@SpringBootTest
public class AssignmentApplicationTests {

	@Test
	public void contextLoads() {
	}
	
	IBankService bank;
	/* Account number for person Abhishek  */
	Account abhishek;
	/* Account number for person Prateek  */
	Account prateek;
	/* Account number for person Amit  */
	Account amit;
	/* Account number for person Sandy  */
	Account sandy;
	
	@Before
	public void setUp() throws CustomerAlreadyExistExeception {
		bank = new BankService();
		Person personAbhishek = new Person("2233", "Abhishek", "Yadav", 0.0);
		Person personPrateek = new Person("1122", "Prateek", "Pathak", 44.44);
		Person personAmit = new Person("3344", "Amit", "Gupta", 55.55);
		Person personSandy = new Person("4455", "Sandy", "Rathor", 22.22);
		abhishek = bank.openConsumerAccount(personAbhishek, 1111);
		prateek = bank.openConsumerAccount(personPrateek, 2222);
		amit = bank.openConsumerAccount(personAmit, 4444);
		sandy = bank.openConsumerAccount(personSandy, 5555);
	}
	
	@After
	public void tearDown() {
		bank = null;
		abhishek = null;
		prateek =  null;
		amit = null;
		sandy = null;
	}
	
	@Test
	public void checkAccountBalance() {
		assertTrue("", bank.getBalance(abhishek.getAccountNumber()) == 0.0);
		assertTrue("",bank.getBalance(prateek.getAccountNumber()) == 44.44);
		assertTrue("",bank.getBalance(amit.getAccountNumber())== 55.55);
		assertTrue("",bank.getBalance(sandy.getAccountNumber())== 22.22);
	}
	
	@Test
	public void debitTest() {
		double amount = 23.0;
		assertFalse( "Account " + abhishek + "should have insufficient fund.", bank.debit(abhishek.getAccountNumber(), amount));
		assertTrue( "Account " + prateek + "should have sufficient fund.", bank.debit(prateek.getAccountNumber(), amount));
		assertTrue("Account " + amit + "should have sufficient fund.", bank.debit(amit.getAccountNumber(), amount));
		assertFalse("Account " + sandy + "should have insufficient fund.", bank.debit(sandy.getAccountNumber(), amount));
		
	}
	
	@Test
	public void bankCreditTest() throws InvalidCustomerException {
		double amount = 24.45;
		double beforeDepostAbhishek =  bank.getBalance(abhishek.getAccountNumber());
		double beforeDepositPrateek = bank.getBalance(prateek.getAccountNumber());
		double beforeDepositAmit = bank.getBalance(amit.getAccountNumber());
		double beforeDepositSandy = bank.getBalance(sandy.getAccountNumber());
		bank.credit(abhishek.getAccountNumber(), amount);
		bank.credit(prateek.getAccountNumber(), amount);
		bank.credit(amit.getAccountNumber(), amount);
		bank.credit(sandy.getAccountNumber(), amount);
		
		assertTrue(beforeDepostAbhishek + amount == bank.getBalance(abhishek.getAccountNumber()));
		assertTrue(beforeDepositPrateek + amount == bank.getBalance(prateek.getAccountNumber()));
		assertTrue(beforeDepositAmit + amount == bank.getBalance(amit.getAccountNumber()));
		assertTrue(beforeDepositSandy + amount == bank.getBalance(sandy.getAccountNumber()));
	}
	
	@Test
	public void validatePinTransaction() throws Exception {
		new TransactionImpl(abhishek.getAccountNumber(), bank, 1111);
	}
	
	@Test
	public void transationTest() throws Exception {
		ITransaction transaction = new TransactionImpl(abhishek.getAccountNumber(), bank, 1111);
		double beforeDepositAbhishek  = transaction.getBalance();
		double amount = 10000;
		transaction.credit(amount);
		assertTrue("", beforeDepositAbhishek + amount == transaction.getBalance());
		assertTrue("Dedit was successful", transaction.debit(amount));
		assertFalse("Debit was unsuccessful", transaction.debit(amount));
		assertTrue("", transaction.getBalance() == bank.getBalance(abhishek.getAccountNumber()));
	}
}
