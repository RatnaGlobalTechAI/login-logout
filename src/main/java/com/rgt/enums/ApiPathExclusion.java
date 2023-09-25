package com.rgt.enums;

public enum ApiPathExclusion {
	LOGIN("/login"), 
	SIGN_UP("/sign-up"),
    OTP_VERIFICATION("/verify-otp"),
    FORGET_PASSWORD("/forget-password"),
    CREATE_ROLE("/createRole"),
	ASSIGN_ROLE("/assignRole");
    
	
	
	
	private ApiPathExclusion(String path) {
		this.path = path;
	}

	private String path;


	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	

}
