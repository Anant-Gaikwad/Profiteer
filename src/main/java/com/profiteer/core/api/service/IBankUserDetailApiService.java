package com.profiteer.core.api.service;

import java.util.List;

import com.profiteer.core.api.dto.BankDetailResponse;
import com.profiteer.core.api.dto.BankDetailsResponseTemp;
import com.profiteer.core.api.dto.UserBankDetailRequest;

public interface IBankUserDetailApiService {

	public boolean addUserBankDetails(UserBankDetailRequest userBankDetailRequest) throws Exception;

	public BankDetailsResponseTemp getBankdetailByIfscCode(String ifscCode) throws Exception;

	public List<BankDetailResponse> getUserBankdetails(Long userId) throws Exception;
	
	public boolean isDeleteBankUserdetail(Long id, Long userId) throws Exception;

}
