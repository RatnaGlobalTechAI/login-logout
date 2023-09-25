package com.rgt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rgt.entity.UserInfoEntity;
import com.rgt.repository.UserInfoRepository;

@Configuration
@Service
public class CustomUserDetailServiceImpl  implements UserDetailsService {
	
	@Autowired
	private UserInfoRepository userInfoRepository;

//	@Override
	public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
		return convert(userInfoRepository.findByEmailId(emailId));
	}
	
	//convert 
		public User convert(UserInfoEntity userInfoEntity) {
	        return new User(userInfoEntity.getEmailId(), userInfoEntity.getPassword(), List.of() );
	    }
		
		
		 
}
