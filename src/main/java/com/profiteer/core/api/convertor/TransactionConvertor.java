package com.profiteer.core.api.convertor;

import java.util.Date;
import java.util.UUID;

import com.profiteer.core.api.dto.TransactionRequest;
import com.profiteer.core.api.dto.TransactionResponse;
import com.profiteer.core.api.entity.Status;
import com.profiteer.core.api.entity.TransactionDetail;
import com.profiteer.core.api.entity.TransactionType;

public class TransactionConvertor {

	public static TransactionDetail convertTransactionRequestToEntity(TransactionRequest transactionRequest) {

		TransactionDetail transactionDetail = new TransactionDetail();

		String transactionRefNumber = UUID.randomUUID().toString();
		transactionRefNumber = transactionRefNumber.replace("-", "");

		transactionDetail.setTransactionReferenceNumber(transactionRefNumber);
		transactionDetail.setTransactionAmount(transactionRequest.getAmount());

		transactionDetail.setUserId(transactionRequest.getUserId());
		transactionDetail.setTransactionDate(new Date());

		if (transactionRequest.getIsAddAmount() != null && transactionRequest.getIsAddAmount()) {
			transactionDetail.setTransactionType(TransactionType.ADD_MONEY);
			transactionDetail.setSettleAmount(transactionRequest.getAmount());
			transactionDetail.setTransactionStatus(Status.SUCCESS);
			transactionDetail.setBankTransactionReferenceNumber(transactionRequest.getReferenceNumber());
		}

		if (transactionRequest.getIsWithdraw() != null && transactionRequest.getIsWithdraw()) {
			transactionDetail.setTransactionType(TransactionType.WITHDRAW);
			transactionDetail.setBankDetailId(transactionRequest.getBankUserDetailId());
			transactionDetail.setTransactionStatus(Status.REQUEST_RECEIVED);
			transactionDetail.setSettleAmount(0 - transactionRequest.getAmount());
		}

		return transactionDetail;
	}

	public static TransactionResponse convertTransactionEntityToResponse(TransactionDetail transactionDetail) {

		TransactionResponse transactionResponse = new TransactionResponse();

		//transactionResponse.setId(transactionDetail.getId());
		transactionResponse.setUserId(transactionDetail.getUserId());
		transactionResponse.setTransactionId(transactionDetail.getTransactionReferenceNumber());
		transactionResponse.setTransactionAmount(transactionDetail.getTransactionAmount());
		transactionResponse.setTransactionStatus(transactionDetail.getTransactionStatus().toString());
		transactionResponse.setTransactionType(transactionDetail.getTransactionType().toString());
		transactionResponse.setTransactionDate(transactionDetail.getTransactionDate());
		transactionResponse.setBankReferenceNumber(transactionDetail.getBankTransactionReferenceNumber());

		return transactionResponse;
	}
}
