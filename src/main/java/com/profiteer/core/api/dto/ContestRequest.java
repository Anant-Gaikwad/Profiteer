package com.profiteer.core.api.dto;

import lombok.Data;

@Data
public class ContestRequest {

	private String contestId;
	private Long contestDetailPkId;
	private Long selectedNumber;
	private Double amount;
	private Long userId;
	
}
