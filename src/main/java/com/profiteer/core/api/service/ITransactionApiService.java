package com.profiteer.core.api.service;

import java.util.List;

import com.profiteer.core.api.dto.SearchTransactionCriteria;
import com.profiteer.core.api.dto.TransactionRequest;
import com.profiteer.core.api.dto.TransactionResponse;
import com.profiteer.core.api.entity.UserBankDetail;

public interface ITransactionApiService {

	public Boolean addorWithdrawMoney(TransactionRequest transactionRequest) throws Exception;

	public List<TransactionResponse> getTransactionDetail(SearchTransactionCriteria searchTransactionCriteria) throws Exception;

	public Boolean isBankDetailVerified(UserBankDetail bankDetail) throws Exception;
	
	public Double getWalletBalance(Long userId);
}
