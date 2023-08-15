package com.profiteer.core.api.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.profiteer.core.api.dto.ProfiteerResponse;
import com.profiteer.core.api.dto.ProfiteerResult;
import com.profiteer.core.api.dto.MPINRequest;
import com.profiteer.core.api.dto.OtpRequest;
import com.profiteer.core.api.dto.UserLoginRequest;
import com.profiteer.core.api.dto.UserRequest;
import com.profiteer.core.api.dto.UserResponse;
import com.profiteer.core.api.service.IUserApiService;

@RestController
public class UserController implements IUserApis {

	@Autowired
	private IUserApiService userApiService;

	@Override
	public ResponseEntity<ProfiteerResponse<UserResponse>> loginUser(UserLoginRequest userRequest,
			HttpServletRequest httpServletRequest) throws Exception {

		ProfiteerResponse<UserResponse> profiteerResponse = new ProfiteerResponse<>();
		ProfiteerResult<UserResponse> profiteerResult = new ProfiteerResult<>();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		try {

			UserResponse userResponse = userApiService.loginUser(userRequest, httpServletRequest);
			profiteerResult.setResult(userResponse);
			profiteerResponse.setAlphaResult(profiteerResult);
			profiteerResponse.setStatusCode(HttpStatus.OK.value());
			profiteerResponse.setStatusMessage("SUCCESS");
			httpHeaders.set("Authorization", userResponse.getToken());
			userResponse.setToken(null);
			return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(profiteerResponse);

		} catch (Exception e) {
			profiteerResult.setResult(null);
			profiteerResponse.setAlphaResult(profiteerResult);
			profiteerResponse.setStatusMessage(e.getMessage());
			profiteerResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(httpHeaders).body(profiteerResponse);
		}
	}

	@Override
	public ResponseEntity<ProfiteerResponse<UserResponse>> registerUser(UserRequest userRequest,
			HttpServletRequest httpServletRequest) throws Exception {

		ProfiteerResponse<UserResponse> profiteerResponse = new ProfiteerResponse<>();
		ProfiteerResult<UserResponse> profiteerResult = new ProfiteerResult<>();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		try {

			UserResponse userResponse = userApiService.registerUser(userRequest, httpServletRequest);
			profiteerResult.setResult(userResponse);
			profiteerResponse.setAlphaResult(profiteerResult);
			profiteerResponse.setStatusCode(HttpStatus.OK.value());
			profiteerResponse.setStatusMessage("SUCCESS");

			httpHeaders.set("Authorization", userResponse.getToken());
			userResponse.setToken(null);
			return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(profiteerResponse);

		} catch (Exception e) {
			profiteerResult.setResult(null);
			profiteerResponse.setAlphaResult(profiteerResult);
			profiteerResponse.setStatusMessage(e.getMessage());
			profiteerResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(httpHeaders).body(profiteerResponse);
		}
	}

	@Override
	public ResponseEntity<ProfiteerResponse<Boolean>> validateOtp(OtpRequest validateOtpRequest) throws Exception {

		ProfiteerResponse<Boolean> profiteerResponse = new ProfiteerResponse<>();
		ProfiteerResult<Boolean> profiteerResult = new ProfiteerResult<>();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		try {

			boolean isSendOtp = userApiService.isValidateOtp(validateOtpRequest);
			profiteerResult.setResult(isSendOtp);
			profiteerResponse.setStatusCode(HttpStatus.OK.value());
			profiteerResponse.setStatusMessage("SUCCESS");
			profiteerResponse.setAlphaResult(profiteerResult);
			return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(profiteerResponse);

		} catch (Exception e) {
			profiteerResult.setResult(false);
			profiteerResponse.setAlphaResult(profiteerResult);
			profiteerResponse.setStatusMessage(e.getMessage());
			profiteerResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(httpHeaders).body(profiteerResponse);
		}
	}

	@Override
	public ResponseEntity<ProfiteerResponse<Boolean>> sendOtp(OtpRequest otpRequest) throws Exception {

		ProfiteerResponse<Boolean> profiteerResponse = new ProfiteerResponse<>();
		ProfiteerResult<Boolean> profiteerResult = new ProfiteerResult<>();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		try {

			boolean isSendOtp = userApiService.isSendotp(otpRequest);
			profiteerResult.setResult(isSendOtp);
			profiteerResponse.setStatusCode(HttpStatus.OK.value());
			profiteerResponse.setStatusMessage("SUCCESS");
			profiteerResponse.setAlphaResult(profiteerResult);
			return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(profiteerResponse);

		} catch (Exception e) {

			profiteerResult.setResult(false);
			profiteerResponse.setAlphaResult(profiteerResult);
			profiteerResponse.setStatusMessage(e.getMessage());
			profiteerResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(httpHeaders).body(profiteerResponse);
		}
	}

	@Override
	public ResponseEntity<ProfiteerResponse<UserResponse>> getUserByUserId(@NotEmpty String authorization,
			Long userId) {

		ProfiteerResponse<UserResponse> profiteerResponse = new ProfiteerResponse<>();
		ProfiteerResult<UserResponse> profiteerResult = new ProfiteerResult<>();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		try {

			UserResponse userResponse = userApiService.getUserById(userId);
			profiteerResult.setResult(userResponse);
			profiteerResponse.setAlphaResult(profiteerResult);
			profiteerResponse.setStatusCode(HttpStatus.OK.value());
			profiteerResponse.setStatusMessage("SUCCESS");
			return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(profiteerResponse);

		} catch (Exception e) {
			profiteerResult.setResult(null);
			profiteerResponse.setAlphaResult(profiteerResult);
			profiteerResponse.setStatusMessage(e.getMessage());
			profiteerResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(httpHeaders).body(profiteerResponse);
		}
	}

	@Override
	public ResponseEntity<ProfiteerResponse<Boolean>> setMPINByUser(MPINRequest mpinRequest) throws Exception {

		ProfiteerResponse<Boolean> profiteerResponse = new ProfiteerResponse<>();
		ProfiteerResult<Boolean> profiteerResult = new ProfiteerResult<>();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		try {

			boolean isMpinSet = userApiService.isMPINSet(mpinRequest);
			profiteerResult.setResult(isMpinSet);
			profiteerResponse.setStatusCode(HttpStatus.OK.value());
			profiteerResponse.setStatusMessage("SUCCESS");
			profiteerResponse.setAlphaResult(profiteerResult);
			return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(profiteerResponse);

		} catch (Exception e) {

			profiteerResult.setResult(false);
			profiteerResponse.setAlphaResult(profiteerResult);
			profiteerResponse.setStatusMessage(e.getMessage());
			profiteerResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(httpHeaders).body(profiteerResponse);
		}
	}

	@Override
	public ResponseEntity<ProfiteerResponse<Boolean>> isReferralCodeValid(String referralCode) {

		ProfiteerResponse<Boolean> profiteerResponse = new ProfiteerResponse<>();
		ProfiteerResult<Boolean> profiteerResult = new ProfiteerResult<>();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		try {

			boolean isSendOtp = userApiService.isValidReferralCode(referralCode);
			profiteerResult.setResult(isSendOtp);
			profiteerResponse.setStatusCode(HttpStatus.OK.value());
			profiteerResponse.setStatusMessage("SUCCESS");
			profiteerResponse.setAlphaResult(profiteerResult);
			return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(profiteerResponse);

		} catch (Exception e) {
			profiteerResult.setResult(false);
			profiteerResponse.setAlphaResult(profiteerResult);
			profiteerResponse.setStatusMessage(e.getMessage());
			profiteerResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(httpHeaders).body(profiteerResponse);
		}
	}
}
