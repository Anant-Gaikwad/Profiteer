package com.profiteer.core.api.dto;

import lombok.Data;

@Data
public class UserBankDetailRequest {

	private Long id;
	private Long userId;
	private String bankUserName;
	private String bankName;
	private String bankUserAccountNumber;
	private String ifscCode;
	private String micrCode;
	private String branchName;
	private Long bankRegMobileNumber;
	private String bankAddress;
	private Boolean isDefault;
}
