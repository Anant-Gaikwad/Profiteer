package com.profiteer.core.api.service;

import com.profiteer.core.api.dto.ContestOrderResponse;
import com.profiteer.core.api.dto.ContestRequest;

public interface IContestService {

	public ContestOrderResponse isPlaceOrderForContest(ContestRequest groupNumberContestReqRes)
			throws Exception;
}
