package com.profiteer.core.api.dto;

import lombok.Data;

@Data
public class SearchTransactionCriteria {

	private Long userId;
	private Long fromDate;
	private Long toDate;
	private Boolean isGameTransaction;
	private Boolean isBankTransaction;
}
