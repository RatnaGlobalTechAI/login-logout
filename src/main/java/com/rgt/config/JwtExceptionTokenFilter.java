package com.rgt.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.filter.GenericFilterBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rgt.response.ResponseObject;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;

public class JwtExceptionTokenFilter extends GenericFilterBean {

	private static ObjectMapper objectMapper = new ObjectMapper();

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		try {
			filterChain.doFilter(request, response);
		} catch (RuntimeException ex) {
			String message = "";
			if (ex instanceof TokenExpireException) {
				message = "Invalid token or token is expire.";
			} else if (ex instanceof TokenNotFoundException) {
				message = "Invalid header or token not passed.";
			} else if (ex instanceof BadCredentialsException) {
				message = ex.getMessage();
			} else if (ex instanceof JwtException || ex instanceof IllegalArgumentException
					|| ex instanceof MalformedJwtException) {
				message = "Invalid token passed";
			}
			//Response result = new Response(403, null, "AUTH_ERROR", message, null);
			
			ResponseObject responseObject = new ResponseObject();
			responseObject.setStatus(false);
			responseObject.setErrorMessage("ÄUTH_ERROR");
			responseObject.setResponse(message);
			
			
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.getWriter().write(objectMapper.writeValueAsString(responseObject));
		}
	}

}
