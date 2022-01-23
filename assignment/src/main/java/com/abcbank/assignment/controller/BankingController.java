package com.abcbank.assignment.controller;

import java.util.List;
import java.util.Random;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.abcbank.assignment.account.Account;
import com.abcbank.assignment.exceptions.CustomerAlreadyExistExeception;
import com.abcbank.assignment.exceptions.InsufficientAccountBalanceException;
import com.abcbank.assignment.exceptions.InvalidCustomerException;
import com.abcbank.assignment.service.ConsumerDetailsService;
import com.abcbank.assignment.service.IBankService;
import com.abcbank.assignment.util.JwtUtil;
import com.abcbank.assignment.vo.AuthenticationRequest;
import com.abcbank.assignment.vo.CustomerBalanceEnquiry;
import com.abcbank.assignment.vo.CustomerTransactionVo;
import com.abcbank.assignment.vo.CustomerVo;
import com.abcbank.assignment.vo.JwtResponse;
import com.abcbank.assignment.vo.Person;
import com.abcbank.assignment.vo.Transaction;

import net.bytebuddy.utility.RandomString;

@RestController
@RequestMapping("/abcbank")
public class BankingController {

	Logger logger = LogManager.getLogger(BankingController.class);
	Random random = new Random();
	@Autowired
	IBankService bankService;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	ConsumerDetailsService consumerDetailsService;
	@Autowired
	JwtUtil jwtTokenUtil;
	
	@PostMapping(value = "/openAccount", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Account> openConsumerBankAccount(@Valid @RequestBody Person person) throws CustomerAlreadyExistExeception, InvalidCustomerException {
		logger.info("openConsumerBankAccount() -> opening account request received for " + person);
		Account account = bankService.openConsumerAccount(person, random.nextInt(9999));
		logger.info("openConsumerBankAccount() -> account opened with details " + account);
		String password = RandomString.make(8);
		account.setUserName(String.valueOf(account.getAccountNumber()));
		account.setPassword(password);
		String jwtToekn = getAuthenticated(String.valueOf(account.getAccountNumber()), password);
		account.setJwtResponse(new JwtResponse(jwtToekn));
		return new ResponseEntity<Account>(account, HttpStatus.OK);
	}

	@PostMapping(value = "/balanceEnquiry", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CustomerTransactionVo> balanceEnquiry(@Valid @RequestBody CustomerBalanceEnquiry balanceEnquiry)
			throws InvalidCustomerException {
		logger.info("balanceEnquiry() -> Balance request for " + balanceEnquiry);
		if (!bankService.authenticateUser(balanceEnquiry.getAccountNumber(), balanceEnquiry.getPin())) {
			throw new InvalidCustomerException("Invalid customer account no("+balanceEnquiry.getAccountNumber()+")");
		}
		double balance = bankService.getBalance(balanceEnquiry.getAccountNumber());
		CustomerTransactionVo transactionVo = new CustomerTransactionVo();
		transactionVo.setAccountNumber(balanceEnquiry.getAccountNumber());
		transactionVo.setAmount(balance);
		logger.info("balanceEnquiry() -> Successfully enquired account balance for Ac/No:" + transactionVo.getAccountNumber());
		return new ResponseEntity<CustomerTransactionVo>(transactionVo, HttpStatus.OK);
	}

	@PostMapping(value = "/depositAmount", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CustomerTransactionVo> depositAmount(@Valid @RequestBody CustomerTransactionVo transactionVo)
			throws InvalidCustomerException {
		logger.info("depositAmount() -> Deposit request for " + transactionVo);
		if (!bankService.authenticateUser(transactionVo.getAccountNumber(), transactionVo.getPin())) {
			logger.error("depositAmount() -> Invalid customer account("+transactionVo.getAccountNumber());
			throw new InvalidCustomerException("Invalid customer account no("+transactionVo.getAccountNumber()+")");
		}
		double creditedAmount = bankService.credit(transactionVo.getAccountNumber(), transactionVo.getAmount());
		transactionVo.setPin(0);
		transactionVo.setAmount(creditedAmount);
		logger.info("depositAmount() -> Deposited amount for Ac/No:" + transactionVo.getAccountNumber());
		return new ResponseEntity<CustomerTransactionVo>(transactionVo,HttpStatus.OK);
	}

	@PostMapping(value = "/debitAmount", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CustomerTransactionVo> debitAmount(@Valid @RequestBody CustomerTransactionVo transactionVo)
			throws InvalidCustomerException, InsufficientAccountBalanceException {
		logger.info("debitAmount() -> Debit request for " + transactionVo);
		if (!bankService.authenticateUser(transactionVo.getAccountNumber(), transactionVo.getPin())) {
			logger.error("debitAmount() -> Invalid customer account("+transactionVo.getAccountNumber());
			throw new InvalidCustomerException("Invalid customer account no("+transactionVo.getAccountNumber()+")");
		}
		if (!bankService.debit(transactionVo.getAccountNumber(), transactionVo.getAmount())) {
			logger.error("debitAmount() -> Insufficient account balance in account("+transactionVo.getAccountNumber()+").");
			throw new InsufficientAccountBalanceException("Insufficient account balance in account("+transactionVo.getAccountNumber()+").");
		}
		double balance = bankService.getBalance(transactionVo.getAccountNumber());
		transactionVo.setPin(0);
		transactionVo.setAmount(balance);
		logger.info("debitAmount() -> Debited amount for Ac/No:" + transactionVo.getAccountNumber());
		return new ResponseEntity<CustomerTransactionVo>(transactionVo, HttpStatus.OK);
	}

	@PostMapping(value = "/transferFund", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> transferFund(@Valid @RequestBody CustomerVo customerVo)
			throws InsufficientAccountBalanceException, InvalidCustomerException {
		logger.info("transferFund() -> Fund Transfer request for " + customerVo);
		if (!bankService.authenticateUser(customerVo.getAccountNumber(), customerVo.getPin())) {
			logger.error("transferFund() -> Invalid customer account("+customerVo.getAccountNumber());
			throw new InvalidCustomerException("Invalid customer account no("+customerVo.getAccountNumber()+")");
		}
		
		if (!bankService.isCustomerAccountExists(customerVo.getTargetAccountNumber())) {
			logger.error("transferFund() -> Invalid target customer account("+customerVo.getTargetAccountNumber());
			throw new InvalidCustomerException("Invalid target customer account no("+customerVo.getTargetAccountNumber()+")");
		}
		if (!bankService.debit(customerVo.getAccountNumber(), customerVo.getAmount())) {
			logger.error("transferFund() -> Insufficient account balance in account("+customerVo.getAccountNumber()+").");
			throw new InsufficientAccountBalanceException("Failed to Transfer the amount. Due to insufficient account balance in account("+customerVo.getAccountNumber()+").");
		}
		logger.info("transferFund() -> Sucessfully debited amount:" + customerVo.getAmount() + " from account no " + customerVo.getAccountNumber());
		bankService.credit(customerVo.getTargetAccountNumber(), customerVo.getAmount(),
				"Credit from " + customerVo.getAccountNumber());
		logger.info("transferFund() -> Sucessfully transferred the fund:" + customerVo.getAmount() + " from account no:" + customerVo.getAccountNumber() + " to account"
				+ " no:" + customerVo.getTargetAccountNumber());
		
		return new ResponseEntity<String>("Successfully transferred the amount to Ac/No: " + customerVo.getTargetAccountNumber() , HttpStatus.OK);
	}
	
	@PostMapping(value = "/getBankStatement", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Transaction>> getBankStatement(@Valid @RequestBody CustomerBalanceEnquiry enquiry)
			throws InvalidCustomerException {
		logger.info("getBankStatement() -> bank statement request for " + enquiry);
		if (!bankService.authenticateUser(enquiry.getAccountNumber(), enquiry.getPin())) {
			logger.error("getBankStatement() -> Invalid customer account("+enquiry.getAccountNumber());
			throw new InvalidCustomerException("Invalid customer account no("+enquiry.getAccountNumber()+")");
		}
		List<Transaction> customerTransactions = bankService.getTransationStatement(enquiry.getAccountNumber());
		logger.info("getBankStatement() -> bank statement request served successfully for :" + enquiry);
		return new ResponseEntity<List<Transaction>>(customerTransactions , HttpStatus.OK);
	}
	
	
	@PostMapping(value = "/authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JwtResponse> authenticate(@Valid @RequestBody AuthenticationRequest authenticationRequest) throws InvalidCustomerException {
		logger.info("authenticate() -> authentication request received for user " + authenticationRequest.getUserName());
		String jwtToken = getAuthenticated(authenticationRequest.getUserName(),
				authenticationRequest.getPassword());
		logger.info("authenticate() -> authentication request received for user " + authenticationRequest.getUserName());
		return new ResponseEntity<JwtResponse>(new JwtResponse(jwtToken), HttpStatus.OK);
	}

	private String getAuthenticated(String userName, String password) throws InvalidCustomerException {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName,password));
		} catch(BadCredentialsException e) {
			throw new InvalidCustomerException("incorrect Username or Password");
		}
		UserDetails userDetails = consumerDetailsService.loadUserByUsername(userName);
		String jwtToken = jwtTokenUtil.generateTokken(userDetails);
		return jwtToken;
	}
}
