package com.profiteer.core.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class UserResponse {

	private Long userId;
	private Long mobileNumber;
	private String email;
	private String firstName;
	private String lastName;
	private Boolean isActive;
	private String token = null;
	private String referralCode;
	private Double walletBalance;
}
