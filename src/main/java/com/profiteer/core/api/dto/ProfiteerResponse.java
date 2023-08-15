package com.profiteer.core.api.dto;

import lombok.Data;

@Data
public class ProfiteerResponse<T> {

	private String statusMessage;
	private int statusCode;
	private Error error;
	private ProfiteerResult<T> alphaResult;
}
