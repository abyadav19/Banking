package com.abcbank.assignment.vo;

import javax.validation.constraints.NotNull;

import com.abcbank.assignment.account.AccountHolder;

public class Person extends AccountHolder {

	@NotNull
	private String firstName;
	@NotNull
	private String lastName;
	@NotNull
	private String idDetails;
	
	@NotNull
	private double openingAmount;
	public Person() {
		
	}
	public Person(String idDetails, String firstName, String lastName, double openingAmount) {
		super(idDetails);
		this.firstName = firstName;
		this.lastName = lastName;
		this.idDetails = idDetails;
		this.openingAmount = openingAmount;
	}

	public String getIdDetails() {
		return idDetails;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public double getOpeningAmount() {
		return openingAmount;
	}
	@Override
	public String toString() {
		return "Person [firstName=" + firstName + ", lastName=" + lastName + ", idDetails=" + idDetails + "]";
	}

}
