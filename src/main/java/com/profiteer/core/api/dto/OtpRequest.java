package com.profiteer.core.api.dto;

import lombok.Data;

@Data
public class OtpRequest {
	
	private Long otp;
	private Long userId;
	private Long mobileNumber;
	private String email;
	private Boolean isLogin;
	private Boolean isRegister;
}
