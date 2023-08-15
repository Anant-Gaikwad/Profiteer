package com.profiteer.core.api.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_NULL)
public class EventUserOrderResponse {

	private Long id;
	private String eventId;
	private Boolean isColorEvent;
	private Boolean isNumberEvent;
	private Long userId;
	private Double amount;
	private String chooseColor;
	private Integer chooseNumber;
	private String status;
	private Date orderDate;
	private String eventType;
	private String winningNumber;
	private String winningColor;

}
