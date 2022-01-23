package com.abcbank.assignment.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtil {

	Logger log = LogManager.getLogger(JwtUtil.class);
	@Value("${secretkey}")
	private String secretKey;
	
	@Value("${tokenTimeOut}")
	private int timeOutInSeconds;
	
	public String extractUserName(String token) {
		return extractClaims(token, Claims::getSubject);
	}
	
	public Date extractExpiration(String token) {
		return extractClaims(token, Claims::getExpiration);
	}
	
	public <T> T extractClaims(String token, Function<Claims, T> claimResolver) {
		final Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}
	
	public Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}
	
	public boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date(System.currentTimeMillis()));
	}
	
	public boolean validateToken(String token, UserDetails details) {
		String userName = extractUserName(token);
		return (userName.equalsIgnoreCase(details.getUsername()) && !isTokenExpired(token));
	}
	
	public String generateTokken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<String, Object>();
		return createToken(claims, userDetails.getUsername());
	}
	
	public String createToken(Map<String, Object> claims, String subject) {
		int tokentimeout = timeOutInSeconds * 1000;
		Date toknCreationTime = new Date(System.currentTimeMillis());
		Date tokenExpiray = new Date(System.currentTimeMillis() + tokentimeout);
		log.info("toknCreationTime: "+ toknCreationTime);
		log.info("tokenExpiray: "+ tokenExpiray);
		String token =  Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(toknCreationTime)
		.setExpiration(tokenExpiray).signWith(SignatureAlgorithm.HS256, secretKey).compact();
		return token;
	}
	
	
	
	
	
	
}
