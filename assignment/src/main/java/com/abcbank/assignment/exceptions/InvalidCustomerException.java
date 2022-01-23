package com.abcbank.assignment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidCustomerException extends Exception {

	public InvalidCustomerException(String message) {
		super(message);
	}

}
