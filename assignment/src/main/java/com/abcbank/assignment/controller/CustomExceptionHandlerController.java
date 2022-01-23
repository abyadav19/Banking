package com.abcbank.assignment.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.abcbank.assignment.exceptions.CustomerAlreadyExistExeception;
import com.abcbank.assignment.exceptions.InsufficientAccountBalanceException;
import com.abcbank.assignment.exceptions.InvalidCustomerException;
import com.abcbank.assignment.exceptions.ResponseError;


@ControllerAdvice
public class CustomExceptionHandlerController extends ResponseEntityExceptionHandler {

	Logger log = LogManager.getLogger(CustomExceptionHandlerController.class);

	@ExceptionHandler(CustomerAlreadyExistExeception.class)
	public final ResponseEntity<Object> handleCustomerAlreadyExistException(
			CustomerAlreadyExistExeception alreadyExistExeception, WebRequest webRequest) {
		log.info("handleCustomerAlreadyExistException() -> " + alreadyExistExeception.getMessage());
		List<String> details = new ArrayList<String>();
		details.add(alreadyExistExeception.getMessage());
		ResponseError error = new ResponseError("Customer already exists", details);
		return new ResponseEntity<Object>(error, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(InvalidCustomerException.class)
	public final ResponseEntity<Object> handleInvalidCustomerException(
			InvalidCustomerException invalidCustomerException, WebRequest webRequest) {
		log.info("handleInvalidCustomerException() -> " + invalidCustomerException.getMessage());
		List<String> details = new ArrayList<String>();
		details.add(invalidCustomerException.getMessage());
		ResponseError error = new ResponseError("Invalid account number", details);
		return new ResponseEntity<Object>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(InsufficientAccountBalanceException.class)
	public final ResponseEntity<Object> handleInsufficientAccountBalance(
			InsufficientAccountBalanceException insufficientAccountBalanceException, WebRequest webRequest) {
		log.info("handleInsufficientAccountBalance() -> " + insufficientAccountBalanceException.getMessage());
		List<String> details = new ArrayList<String>();
		details.add(insufficientAccountBalanceException.getMessage());
		ResponseError error = new ResponseError("Insufficient Account balance", details);
		return new ResponseEntity<Object>(error, HttpStatus.INSUFFICIENT_STORAGE);
	}
	
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleDefaultExceptions(
			Exception exception, WebRequest webRequest) {
		log.info("handleDefaultExceptions() -> " + exception.getMessage());
		List<String> details = new ArrayList<String>();
		details.add(exception.getMessage());
		ResponseError error = new ResponseError("Exception received", details);
		return new ResponseEntity<Object>(error, HttpStatus.EXPECTATION_FAILED);
	}
	
	
}
