package com.profiteer.core.api.dto;

import lombok.Data;

@Data
public class BankDetailResponse {

	private Long id;
	private Long userId;
	private String accountHolderName;
	private String bankName;
	private String accountNumber;
	private String ifscCode;
	private String micrCode;
	private String branchName;
	private Long bankRegMobileNumber;
	private String bankAddress;
	private Boolean isDefault;
}
