package com.rgt.request;

import java.io.Serializable;

import org.springframework.security.core.GrantedAuthority;

public class UserRole implements Serializable,GrantedAuthority {
	
	 private static final long serialVersionUID = 1L;
	    private Long roleId;
	    private String role;
	    

	    public Long getRoleId() {
			return roleId;
		}


		public void setRoleId(Long roleId) {
			this.roleId = roleId;
		}


		public String getRole() {
			return role;
		}


		public void setRole(String role) {
			this.role = role;
		}


		public String getAuthority() {
	        return this.getRole();
	    }


}
