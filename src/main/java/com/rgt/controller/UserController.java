package com.rgt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.rgt.request.LoginRequest;
import com.rgt.request.OtpVerificationRequest;
import com.rgt.request.UserRequest;
import com.rgt.response.ResponseObject;
import com.rgt.service.UserService;

@RestController
public class UserController {

	private static Logger logger = LoggerFactory.getLogger(UserController.class);
	

	@Autowired
	private UserService userService;

	@PostMapping(value = "/sign-up")
	public ResponseObject createAccount(@RequestBody UserRequest userRequest) {
		ResponseObject response = new ResponseObject();
		response = userService.createAccount(userRequest);
		return response;

	}

	@PostMapping(value = "/login")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseObject userLogin(@RequestBody LoginRequest loginRequest) {
		ResponseObject response = new ResponseObject();
		response = userService.userLogin(loginRequest);
		return response;
	}

	@PostMapping(value = "/forget-password")
	public ResponseObject forgetPassword(@RequestBody LoginRequest loginRequest) {
		ResponseObject response = new ResponseObject();
		response = userService.forgetPassword(loginRequest);
		return response;

	}

	@PostMapping(value = "/verify-otp")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseObject verifyOtp(@RequestBody OtpVerificationRequest otpVerificationRequest) {
		ResponseObject response = new ResponseObject();
		response = userService.verifyOtp(otpVerificationRequest);
		return response;
	}

	@PostMapping(value = "/logout")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseObject logoutUser(@RequestHeader("Authorization") String token) {
		ResponseObject response = new ResponseObject();
		logger.debug("logoutUser");
		response = userService.logoutUser(token);
		return response;
	}

}
