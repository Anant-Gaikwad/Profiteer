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
import org.springframework.web.bind.annotation.RequestParam;

import com.profiteer.core.api.dto.BankDetailResponse;
import com.profiteer.core.api.dto.BankDetailsResponseTemp;
import com.profiteer.core.api.dto.ProfiteerResponse;
import com.profiteer.core.api.dto.UserBankDetailRequest;

@Validated
public interface IUserbankDetailApis {

	@PostMapping(value = "/bank/addbankdetail")
	ResponseEntity<ProfiteerResponse<Boolean>> addUserBankDetails(

			@NotEmpty @RequestHeader(value = "Authorization", required = true) String authorization,

			@Valid @RequestBody UserBankDetailRequest bankDetailRequest) throws Exception;

	@GetMapping(value = "/bank/{ifscCode}")
	ResponseEntity<ProfiteerResponse<BankDetailsResponseTemp>> fetchBankDetailsByIfscCode(

			@NotEmpty @RequestHeader(value = "Authorization", required = true) String authorization,

			@PathVariable String ifscCode) throws Exception;

	@GetMapping(value = "/bank/userbankdetail/{userId}")
	ResponseEntity<ProfiteerResponse<List<BankDetailResponse>>> getBankUserDetailByUserId(

			@NotEmpty @RequestHeader(value = "Authorization", required = true) String authorization,

			@Valid @PathVariable("userId") Long userId) throws Exception;

	@PostMapping(value = "/bank/delete")
	ResponseEntity<ProfiteerResponse<Boolean>> deleteBankUserDetails(

			@NotEmpty @RequestHeader(value = "Authorization", required = true) String authorization,
			@RequestParam("userId") Long userId, @RequestParam("id") Long id) throws Exception;
}
