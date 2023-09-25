package com.rgt.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.rgt.entity.RoleEntity;
import com.rgt.entity.UserInfoEntity;
import com.rgt.entity.UserRoleMappingEntity;
import com.rgt.repository.RoleRepository;
import com.rgt.repository.UserInfoRepository;
import com.rgt.repository.UserRoleMappingRepository;

@Service
public class SecurityServiceImpl implements SecurityService{
	
	@Autowired
	private UserInfoRepository userInfoRepository;
	
	@Autowired
	private UserRoleMappingRepository userRoleMappingRepository;
	
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public UserDetails loadUserByUsername(String emailId) {
		UserInfoEntity user = userInfoRepository.findByEmailId(emailId);
		UserRoleMappingEntity userRoleMappingEntity = userRoleMappingRepository.findByEmailId(user.getEmailId());
		RoleEntity roleEntity = roleRepository.findByRoleId(userRoleMappingEntity.getRoleId());
		if (user != null){
			Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
//			for (RoleEntity role : user.getUserRoleList()) {
//				grantedAuthorities.add(new SimpleGrantedAuthority(roleEntity.getRole()));
//			}
			grantedAuthorities.add(new SimpleGrantedAuthority(roleEntity.getRole()));
			return new User(user.getEmailId(), user.getPassword(), grantedAuthorities);
		}else{
			return null;
		}
	}

}
