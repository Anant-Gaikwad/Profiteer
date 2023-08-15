package com.profiteer.core.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.profiteer.core.api.dto.ContestOrderResponse;
import com.profiteer.core.api.dto.ContestRequest;
import com.profiteer.core.api.dto.ProfiteerResponse;
import com.profiteer.core.api.dto.ProfiteerResult;
import com.profiteer.core.api.service.IContestService;

@RestController
public class ContestController implements IContestApis {

	@Autowired
	private IContestService contestService;

	@Override
	public ResponseEntity<ProfiteerResponse<ContestOrderResponse>> isPlaceContestOrder(String authorization,
			ContestRequest groupNumberContestReqRes) throws Exception {

		ProfiteerResponse<ContestOrderResponse> famousResponse = new ProfiteerResponse<>();
		ProfiteerResult<ContestOrderResponse> famousResult = new ProfiteerResult<>();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		try {

			ContestOrderResponse contestOrderResponse = contestService.isPlaceOrderForContest(groupNumberContestReqRes);
			famousResult.setResult(contestOrderResponse);
			famousResponse.setAlphaResult(famousResult);

			famousResponse.setStatusCode(HttpStatus.OK.value());
			famousResponse.setStatusMessage("SUCCESS");
			return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(famousResponse);

		} catch (Exception e) {
			famousResult.setResult(null);
			famousResponse.setAlphaResult(famousResult);
			famousResponse.setStatusMessage(e.getMessage());
			famousResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(httpHeaders).body(famousResponse);
		}
	}

}
