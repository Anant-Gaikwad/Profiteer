package com.profiteer.core.api.service;

import javax.servlet.http.HttpServletRequest;

import com.profiteer.core.api.dto.MPINRequest;
import com.profiteer.core.api.dto.OtpRequest;
import com.profiteer.core.api.dto.UserLoginRequest;
import com.profiteer.core.api.dto.UserRequest;
import com.profiteer.core.api.dto.UserResponse;

public interface IUserApiService {

	public UserResponse registerUser(UserRequest userRequest, HttpServletRequest httpServletRequest)throws Exception;

	public UserResponse loginUser(UserLoginRequest userRequest, HttpServletRequest httpServletRequest) throws Exception;
	
	public boolean isSendotp(OtpRequest otpRequest) throws Exception;
	
	public boolean isValidateOtp(OtpRequest otpRequest) throws Exception;
	
	public UserResponse getUserById(Long id) throws Exception;
	
	public boolean isMPINSet(MPINRequest mpinRequest) throws Exception;
	
	public boolean isValidReferralCode(String referralCode) throws Exception;
}
