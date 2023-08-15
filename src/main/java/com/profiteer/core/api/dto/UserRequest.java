package com.profiteer.core.api.dto;

import lombok.Data;

@Data
public class UserRequest {

	private Long userId;
	private Long mobileNumber;
	private String email;
	private String firstName;
	private String lastName;
	private String state;
	private String referralCode;
	private String otp;
	private String password;
	
	private Boolean isLoginWithPassword;
}
