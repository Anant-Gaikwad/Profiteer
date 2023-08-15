package com.profiteer.core.api.dto;

import lombok.Data;

@Data
public class ContestOrderResponse {

	private Long userId;
	private Double amount;
	private Long selectedNumber;
	private boolean isOrderStatus;
}
