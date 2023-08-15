package com.profiteer.core.api.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum EventColor {

	BLACK,
	GREEN,
	AQUA,
	BLACK_AQUA,
	GREEN_AQUA,
	NO_COLOR_WINNER;
	private String value;
}
