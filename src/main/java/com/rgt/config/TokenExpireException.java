package com.rgt.config;

public class TokenExpireException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TokenExpireException(String message) {
		super(message);
	}
}
