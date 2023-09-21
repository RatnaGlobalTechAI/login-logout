package com.rgt.service;

import com.rgt.request.LoginRequest;
import com.rgt.request.OtpVerificationRequest;
import com.rgt.request.UserRequest;
import com.rgt.response.ResponseObject;

public interface UserService {

	ResponseObject createAccount(UserRequest userRequest);

	ResponseObject userLogin(LoginRequest loginRequest);

	ResponseObject forgetPassword(LoginRequest loginRequest);

	ResponseObject verifyOtp(OtpVerificationRequest otpVerificationRequest);

	ResponseObject logoutUser(LoginRequest loginRequest);

}
