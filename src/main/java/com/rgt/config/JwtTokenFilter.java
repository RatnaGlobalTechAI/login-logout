package com.rgt.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class JwtTokenFilter extends GenericFilterBean {

	private RestTokenProvider restTokenService;

	public JwtTokenFilter(RestTokenProvider restTokenService) {
		this.restTokenService = restTokenService;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
        String token = httpRequest.getHeader("Authorization"); 
		if(token !=null){
			Boolean isValidtoken=restTokenService.authenticateUserByToken(token);
			if(isValidtoken) {
				Authentication authentication = restTokenService.getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}else {
				throw new TokenExpireException("Invalid token or token is expired,Please login again.");
			}
		}else{
			String requestUri= httpRequest.getRequestURI();
			if(!(requestUri.equalsIgnoreCase("/") || requestUri.endsWith(".js") || requestUri.endsWith("login") || requestUri.endsWith("logout") || requestUri.endsWith("error")|| requestUri.endsWith("swagger-ui.html") || requestUri.endsWith("api-docs"))){
				throw new TokenNotFoundException("Invalid header or token not passed");
			}
		}
		filterChain.doFilter(request, response);
	}
}
