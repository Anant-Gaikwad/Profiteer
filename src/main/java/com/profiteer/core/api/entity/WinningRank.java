package com.profiteer.core.api.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum WinningRank {

	FIRST,
	SECOND,
	THIRD,
	FOURTH;
	private String value;
}
