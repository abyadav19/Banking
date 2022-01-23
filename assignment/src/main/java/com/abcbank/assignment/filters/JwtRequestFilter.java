package com.abcbank.assignment.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.abcbank.assignment.exceptions.ResponseError;
import com.abcbank.assignment.service.ConsumerDetailsService;
import com.abcbank.assignment.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	ConsumerDetailsService consumerDetailsService;
	@Autowired
	JwtUtil jwtRequestUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		String authorizationHeader = request.getHeader("Authorization");
		String userName = null;
		String jwt  = null;
		if(!StringUtils.isEmpty(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
			jwt = authorizationHeader.substring(7);
			try {
				userName = jwtRequestUtil.extractUserName(jwt);
			} catch (Exception e) {
				List<String> details = new ArrayList<String>();
				details.add(e.getMessage());
				ResponseError error = new ResponseError("Supplied token has been expired", details);
	            response.setStatus(HttpStatus.FORBIDDEN.value());
	            response.getWriter().write(convertObjectToJson(error));
			}
		}
		if(!StringUtils.isEmpty(userName) && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails details = consumerDetailsService.loadUserByUsername(userName);
			if(jwtRequestUtil.validateToken(jwt, details)) {
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						details, null, details.getAuthorities());
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		}
		chain.doFilter(request, response);
	}

	public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
