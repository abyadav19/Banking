package com.abcbank.assignment.service;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.abcbank.assignment.account.Account;

@Service
public class ConsumerDetailsService implements UserDetailsService {

	@Autowired
	IBankService bankService;
	
	public UserDetails loadUserByUsername(String accountNumber) throws UsernameNotFoundException {
		Long acNumber  = Long.parseLong(accountNumber);
		Account account = bankService.getAccount(acNumber);
		return new User(account.getUserName(), account.getPassword(), new ArrayList<GrantedAuthority>());
	}

}
