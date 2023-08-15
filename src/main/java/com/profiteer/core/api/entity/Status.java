package com.profiteer.core.api.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum Status {

	SUCCESS,
	COMPLETE,
	PENDING,
	FAILED,
	INPROCESS,
	UPCOMING,
	REQUEST_RECEIVED;
	
	private String value;
}
