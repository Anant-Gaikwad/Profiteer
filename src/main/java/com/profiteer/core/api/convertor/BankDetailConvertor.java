package com.profiteer.core.api.convertor;

import com.profiteer.core.api.dto.BankDetailResponse;
import com.profiteer.core.api.entity.UserBankDetail;

public class BankDetailConvertor {

	public static BankDetailResponse convertEntityToBankResponse(UserBankDetail userBankDetail) {

		BankDetailResponse bankDetailResponse = new BankDetailResponse();

		bankDetailResponse.setId(userBankDetail.getId());
		bankDetailResponse.setAccountNumber(userBankDetail.getAccountNumber());
		bankDetailResponse.setBankName(userBankDetail.getBankName());
		bankDetailResponse.setBankRegMobileNumber(userBankDetail.getBankRegMobileNumber());
		bankDetailResponse.setAccountHolderName(userBankDetail.getAccountHolderName());
		bankDetailResponse.setBranchName(userBankDetail.getBranch());
		bankDetailResponse.setIfscCode(userBankDetail.getIfscCode());
		bankDetailResponse.setMicrCode(userBankDetail.getMicrCode());
		bankDetailResponse.setUserId(userBankDetail.getUserId());
		bankDetailResponse.setIsDefault(userBankDetail.getIsDefault());
		bankDetailResponse.setBankAddress(userBankDetail.getBankAddress());

		return bankDetailResponse;
	}

}
