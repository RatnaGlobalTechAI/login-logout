package com.rgt.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.google.common.base.Splitter;
import com.rgt.entity.UserInfoEntity;
import com.rgt.service.AdminService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {

	private final static Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

	@Autowired
	private AdminService adminService;

	/**
	 * 
	 * @param userDetails
	 * @param roles
	 * @param id
	 * @param issuer
	 * @param ttlMilliSecond
	 * @return created token
	 */
	public String createTokenForUser(UserInfoEntity userDetails, List<String> roles, String id, String issuer,
			long ttlMilliSecond) {
		logger.debug("createTokenForUser: user={},roles={}", userDetails, roles);
		Claims claims = Jwts.claims().setSubject(userDetails.getEmailId());
		claims.put("roles", roles);
		//claims.put("domain", userDetails.get);
		long nowMilliSeccond = System.currentTimeMillis();
		Date now = new Date(nowMilliSeccond);
		// add expiration to created token expiration
		long expMilliSecond = nowMilliSeccond;
		if (ttlMilliSecond >= 0) {
			expMilliSecond = nowMilliSeccond + ttlMilliSecond;
		}
		Date tokenExpireTime = new Date(expMilliSecond);

		return Jwts.builder().setId(id).setClaims(claims).setIssuer(issuer).setIssuedAt(now)
				.setExpiration(tokenExpireTime).signWith(SignatureAlgorithm.HS512, userDetails.getPassword()).compact();
	}
	
	public String createTokenForUserDetailsEntity(UserInfoEntity userDetails, List<String> roles, String id, String issuer,
			long ttlMilliSecond) {
		logger.debug("createTokenForUser: user={},roles={}", userDetails, roles);
		Claims claims = Jwts.claims().setSubject(userDetails.getEmailId());
		claims.put("roles", roles);
		//claims.put("appName", userDetails.getAppName());
		
		long nowMilliSeccond = System.currentTimeMillis();
		Date now = new Date(nowMilliSeccond);
		// add expiration to created token expiration
		long expMilliSecond = nowMilliSeccond;
		if (ttlMilliSecond >= 0) {
			expMilliSecond = nowMilliSeccond + ttlMilliSecond;
		}
		Date tokenExpireTime = new Date(expMilliSecond);

		return Jwts.builder().setId(id).setClaims(claims).setIssuer(issuer).setIssuedAt(now)
				.setExpiration(tokenExpireTime).signWith(SignatureAlgorithm.HS512, userDetails.getPassword()).compact();
	}

	/**
	 * 
	 * @param userName
	 * @param token
	 * @return parseUserFromToken
	 * 
	 */
	public boolean parseUserFromToken(String rgtToken, long tokenExpireinMillis) {
		logger.debug("parseUserFromToken :token ={}", rgtToken);
		Map<String, String> tokens = splitToken(rgtToken);
		String emailId = tokens.get("id");
		String token = tokens.get("rgtToken");
		boolean isValidtoken = false;
		try {
			UserInfoEntity userInfoEntity = new UserInfoEntity();
			userInfoEntity.setEmailId(emailId);
			userInfoEntity = adminService.getUserDetail(userInfoEntity);

			if (userInfoEntity == null) {
				throw new IllegalArgumentException("Seems token is expired.Please login again.");
			}

			String password = userInfoEntity.getPassword();

			if (password == null) {
				throw new IllegalArgumentException("Could not find password in DB");
			}

			Jws<Claims> claims = Jwts.parser().setSigningKey(password).parseClaimsJws(token);

			if (logger.isDebugEnabled()) {
				logger.debug("Token ID: " + claims.getBody().getId());
				logger.debug("Token  Subject: " + claims.getBody().getSubject());
				logger.debug("Token  Issuer: " + claims.getBody().getIssuer());
				logger.debug("Token  Expiration: " + claims.getBody().getExpiration());
			}
			Date logoutTime = userInfoEntity.getLogoutTime();
			if (logoutTime != null && claims.getBody().getExpiration().before(logoutTime)) {
				logger.debug("LogoutTime ={}, IssuedAt ={},ExpirationTime ={}", logoutTime, claims.getBody().getIssuedAt(),claims.getBody().getExpiration());
				isValidtoken = false;
			} else {
				logger.debug("Seems token is not expired.");
				isValidtoken = true;
			}
		} catch (ExpiredJwtException e) {
			logger.error("Exception  : errormsg={}", e.getMessage());
			throw new JwtException("Invalid token passed");
		} catch (JwtException | IllegalArgumentException e) {
			logger.error("Exception : errormsg={}", e.getMessage());
			throw new JwtException("Invalid token passed");
		} catch (Exception e) {
			logger.error("Exception  : errormsg={}", e.getMessage());
			throw new JwtException("Invalid token passed");
		}
		return isValidtoken;
	}

	/**
	 * 
	 * @param userName
	 * @param token
	 * @return getAuthentication
	 * 
	 */

	@SuppressWarnings("unchecked")
	public Authentication getAuthentication(String rgtToken) {
		logger.info("getAuthentication :token ={}", rgtToken);
		Map<String, String> tokens = splitToken(rgtToken);
		String emailId = tokens.get("id");
		String token = tokens.get("rgtToken");
		UserInfoEntity userInfoEntity = new UserInfoEntity();
		userInfoEntity.setEmailId(emailId);
		userInfoEntity = adminService.getUserDetail(userInfoEntity);

		if (userInfoEntity == null) {
			throw new IllegalArgumentException("Seems token is expired.Please login again.");
		}

		String password = userInfoEntity.getPassword();

		Jws<Claims> claims = Jwts.parser().setSigningKey(password).parseClaimsJws(token);
		List<String> roles = claims.getBody().get("roles", List.class);

		List<GrantedAuthority> authRoles = new ArrayList<GrantedAuthority>();
		for (String role : roles) {
			GrantedAuthority authRole = new SimpleGrantedAuthority(role);
			authRoles.add(authRole);
		}
		return new UsernamePasswordAuthenticationToken(emailId, password, authRoles);
	}

	public UserInfoEntity getUserDetails(String token) {
		logger.info("getUserDetails :token ={}", token);
		UserInfoEntity UserInfoEntity = new UserInfoEntity();
		UserInfoEntity.setEmailId(splitToken(token).get("id"));
		return adminService.getUserDetail(UserInfoEntity);
	}

	@SuppressWarnings("unchecked")
	public static String getUserRole(String token) {
		logger.info("getUserRole :token ={}", token);
		List<String> roles = getJwtsClaims(token, null).getBody().get("roles", List.class);
		return roles.get(0);
	}

	public static String getDomain(String token) {
		logger.info("getDomain :token ={}", token);
		return getJwtsClaims(token, null).getBody().get("domain", String.class);
	}

	/*
	 * public static String getPlatform(String token) {
	 * logger.info("getPlatform :token ={}", token); return getJwtsClaims(token,
	 * null).getBody().get("platform", String.class); }
	 */

	private static Jws<Claims> getJwtsClaims(String token, String password) {
		Map<String, String> tokens = splitToken(token);
		String secureKey = null;
		if (password != null && password.trim().length() > 0) {
			secureKey = password;
		} else {
			secureKey = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
		}
		return Jwts.parser().setSigningKey(secureKey).parseClaimsJws(tokens.get("rgtToken"));
	}

	private static Map<String, String> splitToken(String token) {
		Map<String, String> tokens = Splitter.on(",").withKeyValueSeparator("=").split(token);
		if (tokens != null && (tokens.get("id") == null || tokens.get("rgtToken") == null)) {
			throw new MalformedJwtException("Invalid Token format");
		}
		return tokens;
	}

	public static Date getIssuedAt(String token) {
		logger.info("getIssuedAt :token ={}", token);
		return getJwtsClaims(token, null).getBody().getIssuedAt();
	}

}
