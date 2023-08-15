package com.profiteer.core.api.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.profiteer.core.api.dto.ProfiteerResponse;
import com.profiteer.core.api.dto.MPINRequest;
import com.profiteer.core.api.dto.OtpRequest;
import com.profiteer.core.api.dto.UserLoginRequest;
import com.profiteer.core.api.dto.UserRequest;
import com.profiteer.core.api.dto.UserResponse;

@Validated
public interface IUserApis {

	@PostMapping(value = "/user/register")
	public ResponseEntity<ProfiteerResponse<UserResponse>> registerUser(

			@Valid @RequestBody UserRequest userRequest, HttpServletRequest httpServletRequest) throws Exception;

	@PostMapping(value = "/user/login")
	public ResponseEntity<ProfiteerResponse<UserResponse>> loginUser(@Valid @RequestBody UserLoginRequest userRequest,
			HttpServletRequest httpServletRequest) throws Exception;

	@PostMapping(value = "/user/validateOtp")
	public ResponseEntity<ProfiteerResponse<Boolean>> validateOtp(@RequestBody OtpRequest validateOtpRequest)
			throws Exception;

	@PostMapping(value = "/user/sendOtp")
	public ResponseEntity<ProfiteerResponse<Boolean>> sendOtp(@RequestBody OtpRequest otpRequest) throws Exception;

	@GetMapping(value = "/user/getUser/{id}")
	public ResponseEntity<ProfiteerResponse<UserResponse>> getUserByUserId(
			@NotEmpty @RequestHeader(value = "Authorization", required = true) String authorization,
			@PathVariable("id") Long userId);

	@PostMapping(value = "/user/setMPIN")
	public ResponseEntity<ProfiteerResponse<Boolean>> setMPINByUser(@RequestBody MPINRequest mpinRequest)
			throws Exception;

	@GetMapping(value = "/user/checkValidReferralCode")
	public ResponseEntity<ProfiteerResponse<Boolean>> isReferralCodeValid(
			@RequestParam("referralCode") String referralCode);
}
