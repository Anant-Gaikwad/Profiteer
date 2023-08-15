package com.profiteer.core.api.dto;

import lombok.Data;

@Data
public class EventOrderRequest {

	private String eventId;
	private Boolean isColorEvent;
	private Boolean isNumberEvent;
	private Long userId;
	private Double amount;
	private String chooseColor;
	private Integer chooseNumber;
}
