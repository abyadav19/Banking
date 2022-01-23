package com.abcbank.assignment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INSUFFICIENT_STORAGE)
public class InsufficientAccountBalanceException extends Exception {

	public InsufficientAccountBalanceException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
