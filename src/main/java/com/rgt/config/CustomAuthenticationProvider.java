package com.rgt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.rgt.service.SecurityService;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider{
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private SecurityService securityService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String emailId = authentication.getName();
		UserDetails user = securityService.loadUserByUsername(emailId);
		if(user !=null && user.getUsername().equals(authentication.getName()) && passwordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())){
			return new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword(),user.getAuthorities());
		}else{
			throw new BadCredentialsException("Provided username and password are not matching.");
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
