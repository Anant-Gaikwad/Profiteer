package com.profiteer.core.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.profiteer.core.api.dto.BankDetailResponse;
import com.profiteer.core.api.dto.BankDetailsResponseTemp;
import com.profiteer.core.api.dto.ProfiteerResponse;
import com.profiteer.core.api.dto.ProfiteerResult;
import com.profiteer.core.api.dto.UserBankDetailRequest;
import com.profiteer.core.api.service.IBankUserDetailApiService;

@RestController
public class UserBankdetailController implements IUserbankDetailApis {

	@Autowired
	private IBankUserDetailApiService bankUserDetailApiService;

	@Override
	public ResponseEntity<ProfiteerResponse<Boolean>> addUserBankDetails(String authorization,
			UserBankDetailRequest bankDetailRequest) throws Exception {

		ProfiteerResponse<Boolean> famousResponse = new ProfiteerResponse<>();
		ProfiteerResult<Boolean> famousResult = new ProfiteerResult<>();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		try {

			boolean isAddMoney = bankUserDetailApiService.addUserBankDetails(bankDetailRequest);
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
	public ResponseEntity<ProfiteerResponse<BankDetailsResponseTemp>> fetchBankDetailsByIfscCode(String authorization,
			String ifscCode) throws Exception {

		ProfiteerResponse<BankDetailsResponseTemp> famousResponse = new ProfiteerResponse<>();
		ProfiteerResult<BankDetailsResponseTemp> famousResult = new ProfiteerResult<>();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		try {

			BankDetailsResponseTemp bankDetailsResponse = bankUserDetailApiService.getBankdetailByIfscCode(ifscCode);
			famousResult.setResult(bankDetailsResponse);
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
	public ResponseEntity<ProfiteerResponse<List<BankDetailResponse>>> getBankUserDetailByUserId(String authorization,
			Long userId) throws Exception {

		ProfiteerResponse<List<BankDetailResponse>> famousResponse = new ProfiteerResponse<>();
		ProfiteerResult<List<BankDetailResponse>> famousResult = new ProfiteerResult<>();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		try {

			List<BankDetailResponse> bankDetailResponses = bankUserDetailApiService.getUserBankdetails(userId);
			famousResult.setResult(bankDetailResponses);
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
	public ResponseEntity<ProfiteerResponse<Boolean>> deleteBankUserDetails(String authorization, Long userId, Long id)
			throws Exception {

		ProfiteerResponse<Boolean> famousResponse = new ProfiteerResponse<>();
		ProfiteerResult<Boolean> famousResult = new ProfiteerResult<>();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		try {

			boolean isDeleteUserBankdetail = bankUserDetailApiService.isDeleteBankUserdetail(id, userId);
			famousResult.setResult(isDeleteUserBankdetail);
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
}
