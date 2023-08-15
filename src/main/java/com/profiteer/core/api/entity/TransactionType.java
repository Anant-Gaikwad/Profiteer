package com.profiteer.core.api.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum TransactionType {

	EVENT_PLAYED,
	CONTEST_PLAYED,
	EVENT_WIN,
	CONTEST_WIN,
	ADD_MONEY,
	WITHDRAW,
	REFFRRED_USER;

	private String value;
}
