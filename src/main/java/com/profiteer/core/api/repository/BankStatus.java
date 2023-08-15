package com.profiteer.core.api.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum BankStatus {

	PENDING,
	VERIFIED,
	INACTIVE;
	
	private String value;
}
