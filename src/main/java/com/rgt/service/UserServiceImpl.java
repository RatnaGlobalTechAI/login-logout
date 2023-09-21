package com.rgt.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.google.common.cache.LoadingCache;
import com.rgt.entity.DomainEntity;
import com.rgt.entity.UserActivityLogsEntity;
import com.rgt.entity.UserDomainMappingEntity;
import com.rgt.entity.UserInfoEntity;
import com.rgt.enums.OtpContext;
import com.rgt.repository.DomainRepository;
import com.rgt.repository.UserActivityLogsRepository;
import com.rgt.repository.UserDomainMappingRepository;
import com.rgt.repository.UserInfoRepository;
import com.rgt.request.LoginRequest;
import com.rgt.request.OtpVerificationRequest;
import com.rgt.request.UserRequest;
import com.rgt.response.LoginResponse;
import com.rgt.response.ResponseObject;
import com.rgt.utils.CommonUtility;

@Service
public class UserServiceImpl implements UserService {

	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserInfoRepository userInfoRepository;

	
	 @Autowired
	 private PasswordEncoder passwordEncoder;
	 
	@Autowired
	private LoadingCache<String, Integer> oneTimePasswordCache;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private DomainRepository domainRepository;
	
	@Autowired
	private UserDomainMappingRepository userDomainMappingRepository;
	
	@Autowired 
	private UserActivityLogsRepository userActivityLogsRepository;

	@Autowired
	private ActivityLogsService activityLogsService;
	 

	@Override
	public ResponseObject createAccount(UserRequest userRequest) {
		ResponseObject response = new ResponseObject();

		try {
			UserInfoEntity userInfoEntity = userInfoRepository.findByEmailId(userRequest.getEmailId());
			DomainEntity domainEntity = domainRepository.findByName(userRequest.getDomain().toUpperCase());
			
			if(domainEntity !=null) {
				if (userInfoEntity == null) {
					UserInfoEntity entity = new UserInfoEntity();
					UserDomainMappingEntity userDomainMappingEntity = new UserDomainMappingEntity();
					entity.setUsername(userRequest.getUsername());
					entity.setAddress(userRequest.getAddress());
					entity.setAdharcard(userRequest.getAdharcard());
					entity.setAge(userRequest.getAge());
					entity.setCity(userRequest.getCity());
					entity.setCreatedOn(new Date());
					entity.setDob(userRequest.getDob());
					entity.setEmailId(userRequest.getEmailId());
					entity.setGender(userRequest.getGender().toUpperCase());
					entity.setJobTitle(userRequest.getJobTitle());
					entity.setMaritalStatus(userRequest.getMaritalStatus().toUpperCase());
					entity.setMobile(userRequest.getMobile());
					entity.setPancard(userRequest.getPancard());
					entity.setPassword(passwordEncoder.encode(userRequest.getPassword()));
					entity.setPincode(userRequest.getPincode());
					entity.setQualification(userRequest.getQualification());
					entity.setState(userRequest.getState());
					
					
					userInfoRepository.save(entity);
					
					
				
						userDomainMappingEntity.setEmailId(userRequest.getEmailId());
						userDomainMappingEntity.setDomainId(domainEntity.getId());
						
						userDomainMappingRepository.save(userDomainMappingEntity);
						
						response.setStatus(true);
						response.setSuccessMessage("User Account Create Sucessfully");
						logger.info("User Account Create Successfully");
					
					

				} else {
					response.setStatus(false);
					response.setErrorMessage("User Already Exist.Please login... ");
					logger.info("User Already Exist.Please login... ");
				}
			}else {
				response.setStatus(false);
				response.setErrorMessage("Provided domain name does not exist");
				logger.info("Provided domain name does not exist");
			}
			
			

		} catch (Exception e) {

			response.setErrorMessage(e.getMessage());
			response.setStatus(false);
			logger.error(e.getMessage());

		}

		return response;
	}

	@Override
	public ResponseObject userLogin(LoginRequest loginRequest) {
		ResponseObject response = new ResponseObject();
		LoginResponse loginResponse = new LoginResponse();
		try {
			UserInfoEntity userInfoEntity = userInfoRepository.findByEmailId(loginRequest.getEmailId());
			
			if(userInfoEntity !=null) {
				if(!passwordEncoder.matches(loginRequest.getPassword(), userInfoEntity.getPassword()))
					throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Login Creadentials");
				
				Date lastLogin = null;
				List<UserActivityLogsEntity> activityLogsModels = 
						userActivityLogsRepository.findByNameOrderByLastLoginDesc(userInfoEntity.getEmailId());
						
				if (activityLogsModels != null && !activityLogsModels.isEmpty()) {
					lastLogin = activityLogsModels.get(0).getLastLogin();
					Date date=CommonUtility.convertDateStringToDate(lastLogin.toString(),CommonUtility.Date_Formate);
					logger.debug("Last Login:" + date);
				}
				
				logger.debug("Start mark user Login logs into database");
				activityLogsService.markUserLogs(null, loginRequest.getDomain(), "User logged in sucessfully.",
						CommonUtility.getCurrentTimeStamp(), userInfoEntity.getEmailId(), userInfoEntity.getEmailId(), CommonUtility.getCurrentTimeStamp());
				logger.debug("End mark user Login logs into database");
				
				
				loginResponse.setAddress(userInfoEntity.getAddress());
				loginResponse.setUsername(userInfoEntity.getUsername());
				
				response.setResponse(loginResponse);
				response.setStatus(true);
				response.setSuccessMessage("User Logged In Successfully");
				
				
			}else {
				response.setStatus(false);
				response.setSuccessMessage("User Does not Exist");
			}
			
		}catch(Exception e) {
			response.setErrorMessage(e.getMessage());
			response.setStatus(false);
		}
		
		
		return response;
	}

	@Override
	public ResponseObject forgetPassword(LoginRequest loginRequest) {
		ResponseObject response = new ResponseObject();
		
		try {
			UserInfoEntity userInfoEntity = userInfoRepository.findByEmailId(loginRequest.getEmailId());
			
			if(userInfoEntity !=null) {
//				if(!passwordEncoder.matches(loginRequest.getPassword(), userInfoEntity.getPassword()))
//					throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Login Creadentials");
//				
				sendOtp(userInfoEntity, "Request to forget password your account");
				
			 response.setResponse(getOtpSendMessage());
			 response.setStatus(true);
			 response.setSuccessMessage("OTP Send to your registered email id");
				
			}
			
		}catch(Exception e) {
			
			response.setErrorMessage(e.getMessage());
			response.setStatus(false);
		}
		
		
		
		return response;
	}
	
	
private void sendOtp(UserInfoEntity entity, String subject) {
		
		try {
			if(oneTimePasswordCache.get(entity.getEmailId()) !=null) 
				oneTimePasswordCache.invalidate(entity.getEmailId());
			
		}catch(ExecutionException e) {
			logger.error("Failed to fetch OTP cache: {}" ,e);
			throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
			
		}
		
		int otp = new Random().ints(1, 100000 , 999999).sum();
		oneTimePasswordCache.put(entity.getEmailId(), otp);
		
		
		CompletableFuture.supplyAsync(() -> {
			mailService.sendEmail(entity.getEmailId(), subject, "OTP: " + otp);
            return HttpStatus.OK;
        });
		
	}

private Map<String, String> getOtpSendMessage() {
	 HashMap<String , String>  response= new HashMap<String , String>();
	 
	 response.put("message", "OTP sent successfully sent to your registered email-address. verify using /rgt/verify-otp endpoint");
	
	return response;
}

@Override
public ResponseObject verifyOtp(OtpVerificationRequest otpVerificationRequest) {
	ResponseObject response= new ResponseObject();
	
	UserInfoEntity userInfoEntity = userInfoRepository.findByEmailId(otpVerificationRequest.getEmailId());
	if(userInfoEntity !=null) {
		
		Integer storedOneTimePassword = null;
        try {
            storedOneTimePassword = oneTimePasswordCache.get(userInfoEntity.getEmailId());
        } catch (ExecutionException e) {
            logger.error("FAILED TO FETCH PAIR FROM OTP CACHE: {}", e);
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
        }

        if (storedOneTimePassword != null) {
            if (storedOneTimePassword.equals(otpVerificationRequest.getOtp())) {
                if (otpVerificationRequest.getOtpContext().equals(OtpContext.FORGET_PASSWORD)) {
                	userInfoEntity.setPassword(passwordEncoder.encode(otpVerificationRequest.getForgetPassword()));
                	userInfoEntity = userInfoRepository.save(userInfoEntity);
                    
                	response.setSuccessMessage("Your password changed successfully. Please go to login page");
                	response.setStatus(true);
                	return response;
                }
                
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }else {
		
    	throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Email Id");
	}

	
	
	
}

@Override
public ResponseObject logoutUser(LoginRequest loginRequest) {
	ResponseObject response = new ResponseObject();
	
	UserInfoEntity userInfoEntity = userInfoRepository.findByEmailId(loginRequest.getEmailId());
	
	
	try {
	if(userInfoEntity ==null) 
		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Email Id");
	
	
		logger.debug("Start mark user Logout logs into database");
		activityLogsService.markUserLogs(null, loginRequest.getDomain(), "User Logout in sucessfully.",
				CommonUtility.getCurrentTimeStamp(), loginRequest.getEmailId(), loginRequest.getEmailId(), CommonUtility.getCurrentTimeStamp());
		logger.debug("End mark user Logout logs into database");

		response.setStatus(true);
		response.setSuccessMessage("User logout in successfully");
	
	}catch(Exception e) {
		response.setStatus(false);
		response.setErrorMessage(e.getMessage());
	}
	
	


	return response;
}

}
