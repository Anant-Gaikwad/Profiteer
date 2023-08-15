package com.profiteer.core.api.dto;

import lombok.Data;

@Data
public class UserLoginRequest {
	private Long mobileNumber;
	private String password;
	private String mpin;
	private String otp;
	private Boolean isLoginWithPassword;
	private Boolean isLoginWithMpin;
	private Boolean isLoginWithOtp;
}
