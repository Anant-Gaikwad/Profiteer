package com.profiteer.core.api.dto;

import java.util.Date;

import lombok.Data;

@Data
public class EventResponse {

	private String eventId;
	private String status;
	private Date eventStartTime;
	private Date eventEndTime;
	private Integer winningNumber;
	private String winningColor;
}
