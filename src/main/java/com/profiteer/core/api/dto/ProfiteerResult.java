package com.profiteer.core.api.dto;

import lombok.Data;

@Data
public class ProfiteerResult<T> {

	private T result;
}
