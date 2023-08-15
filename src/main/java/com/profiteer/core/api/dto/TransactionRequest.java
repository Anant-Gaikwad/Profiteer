package com.profiteer.core.api.dto;

import lombok.Data;

@Data
public class TransactionRequest {

	private Long userId;
	private Boolean isAddAmount;
	private Boolean isWithdraw;
	private Double amount;
	private Long bankUserDetailId;
	private String referenceNumber;
}
