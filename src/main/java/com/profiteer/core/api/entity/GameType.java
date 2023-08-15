package com.profiteer.core.api.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum GameType {

	COLOR_EVENT, 
	NUMBER_EVENT, 
	GROUP_NUMBER_CONTENT;

	private String value;
}
