package com.profiteer.core.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.profiteer.core.api.dto.ProfiteerResponse;
import com.profiteer.core.api.dto.ProfiteerResult;
import com.profiteer.core.api.dto.SearchTransactionCriteria;
import com.profiteer.core.api.dto.TransactionRequest;
import com.profiteer.core.api.dto.TransactionResponse;
import com.profiteer.core.api.service.ITransactionApiService;

@RestController
public class TransactionController implements ITransactionApis {

	@Autowired
	private ITransactionApiService transactionApiService;

	@Override
	public ResponseEntity<ProfiteerResponse<Boolean>> addAmount(String authorization,
			TransactionRequest transactionRequest) throws Exception {

		ProfiteerResponse<Boolean> famousResponse = new ProfiteerResponse<>();
		ProfiteerResult<Boolean> famousResult = new ProfiteerResult<>();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		try {

			boolean isAddMoney = transactionApiService.addorWithdrawMoney(transactionRequest);
			famousResult.setResult(isAddMoney);
			famousResponse.setStatusCode(HttpStatus.OK.value());
			famousResponse.setStatusMessage("SUCCESS");
			famousResponse.setAlphaResult(famousResult);
			return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(famousResponse);

		} catch (Exception e) {
			famousResult.setResult(false);
			famousResponse.setAlphaResult(famousResult);
			famousResponse.setStatusMessage(e.getMessage());
			famousResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(httpHeaders).body(famousResponse);
		}
	}

	@Override
	public ResponseEntity<ProfiteerResponse<List<TransactionResponse>>> getTransactionDetails(String authorization,
			SearchTransactionCriteria searchTransactionCriteria) throws Exception {
		ProfiteerResponse<List<TransactionResponse>> famousResponse = new ProfiteerResponse<>();
		ProfiteerResult<List<TransactionResponse>> famousResult = new ProfiteerResult<>();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		try {

			List<TransactionResponse> transactionResponses = transactionApiService
					.getTransactionDetail(searchTransactionCriteria);
			famousResult.setResult(transactionResponses);
			famousResponse.setStatusCode(HttpStatus.OK.value());
			famousResponse.setStatusMessage("SUCCESS");
			famousResponse.setAlphaResult(famousResult);
			return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(famousResponse);

		} catch (Exception e) {
			famousResult.setResult(null);
			famousResponse.setAlphaResult(famousResult);
			famousResponse.setStatusMessage(e.getMessage());
			famousResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(httpHeaders).body(famousResponse);
		}
	}

	@Override
	public ResponseEntity<ProfiteerResponse<Double>> getWalletBalance(String authorization, Long userId) throws Exception {
		ProfiteerResponse<Double> famousResponse = new ProfiteerResponse<>();
		ProfiteerResult<Double> famousResult = new ProfiteerResult<>();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		try {

			Double walletBalance = transactionApiService.getWalletBalance(userId);
			famousResult.setResult(walletBalance != null ? walletBalance : 0.0);
			famousResponse.setStatusCode(HttpStatus.OK.value());
			famousResponse.setStatusMessage("SUCCESS");
			famousResponse.setAlphaResult(famousResult);
			return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(famousResponse);

		} catch (Exception e) {
			famousResult.setResult(0.0);
			famousResponse.setAlphaResult(famousResult);
			famousResponse.setStatusMessage(e.getMessage());
			famousResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(httpHeaders).body(famousResponse);
		}
	}

}
