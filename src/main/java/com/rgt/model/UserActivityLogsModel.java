package com.rgt.model;

import java.util.Date;

public class UserActivityLogsModel extends ActivityLogsModel {
	
	private Date lastLogin;

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	
	

}
