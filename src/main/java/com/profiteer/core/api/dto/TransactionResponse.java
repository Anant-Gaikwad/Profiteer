package com.profiteer.core.api.dto;

import java.util.Date;

import lombok.Data;

@Data
public class TransactionResponse {

	private Long id;
	private Long userId;
	private String transactionId;
	private String transactionType;
	private String transactionStatus;
	private Date transactionDate;
	private String bankReferenceNumber;
	private Double transactionAmount;
}
