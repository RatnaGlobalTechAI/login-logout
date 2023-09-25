package com.rgt.config;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.rgt.entity.UserInfoEntity;

@Component
public class RestTokenProvider {

	@Autowired
	private JwtTokenProvider tokenHandler;

	@Value("${token.expiry.time.millis}")
	private int tokenExpireinMillis;

	@Value("${token.expiry.days}")
	private int tokenExpireinDays;

	@Value("${user.token.issuer}")
	private String issuer;

	@Value("${token.jwt.key}")
	private String secureKey;

	public RestTokenProvider() {
	}

	public String createTokenForUser(UserInfoEntity userDetails, List<String> roles) {
		return tokenHandler.createTokenForUser(userDetails, roles, UUID.randomUUID().toString(), issuer,
				tokenExpireinDays * tokenExpireinMillis);
	}
	
	public String createTokenForUserDetailsEntity(UserInfoEntity userDetails, List<String> roles) {
		return tokenHandler.createTokenForUserDetailsEntity(userDetails, roles, UUID.randomUUID().toString(), issuer,
				tokenExpireinDays * tokenExpireinMillis);
	}

	public boolean authenticateUserByToken(String token) {
		return tokenHandler.parseUserFromToken(token, tokenExpireinMillis);
	}

	public Authentication getAuthentication(String token) {
		return tokenHandler.getAuthentication(token);
	}

	public static Date getTokenCreationDate(String token) {
		return JwtTokenProvider.getIssuedAt(token);
	}
}
