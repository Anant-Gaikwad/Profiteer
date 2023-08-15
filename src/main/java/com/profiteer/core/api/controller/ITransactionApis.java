package com.profiteer.core.api.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.profiteer.core.api.dto.ProfiteerResponse;
import com.profiteer.core.api.dto.SearchTransactionCriteria;
import com.profiteer.core.api.dto.TransactionRequest;
import com.profiteer.core.api.dto.TransactionResponse;

@Validated
public interface ITransactionApis {

	@PostMapping(value = "/transaction/money/addorwithdraw")
	ResponseEntity<ProfiteerResponse<Boolean>> addAmount(
			@NotEmpty @RequestHeader(value = "Authorization", required = true) String authorization,
			@Valid @RequestBody TransactionRequest transactionRequest) throws Exception;

	@PostMapping(value = "/transaction/getTransactions")
	ResponseEntity<ProfiteerResponse<List<TransactionResponse>>> getTransactionDetails(
			@NotEmpty @RequestHeader(value = "Authorization", required = true) String authorization,
			@Valid @RequestBody SearchTransactionCriteria searchTransactionCriteria) throws Exception;

	@GetMapping(value = "/transaction/getWalletBalance/{userid}")
	ResponseEntity<ProfiteerResponse<Double>> getWalletBalance(
			@NotEmpty @RequestHeader(value = "Authorization", required = true) String authorization,
			@PathVariable("userid") Long userid) throws Exception;
}
