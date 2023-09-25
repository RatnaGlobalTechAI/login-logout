package com.rgt.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface SecurityService {

	UserDetails loadUserByUsername(String emailId);

}
