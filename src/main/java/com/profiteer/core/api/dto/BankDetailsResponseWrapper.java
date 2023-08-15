package com.profiteer.core.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankDetailsResponseWrapper {

	private String status;
	private BankDetailsResponseTemp data;
	private String message;
}
