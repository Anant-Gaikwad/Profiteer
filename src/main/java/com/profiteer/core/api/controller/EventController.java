package com.profiteer.core.api.controller;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.profiteer.core.api.dto.EventOrderRequest;
import com.profiteer.core.api.dto.EventPageResponse;
import com.profiteer.core.api.dto.EventResponse;
import com.profiteer.core.api.dto.EventUserOrderResponse;
import com.profiteer.core.api.dto.ProfiteerResponse;
import com.profiteer.core.api.dto.ProfiteerResult;
import com.profiteer.core.api.scheduler.ColorNumberEventScheduler;
import com.profiteer.core.api.service.IEventApiService;

@RestController
public class EventController implements IEventApis {

	@Autowired
	private IEventApiService contestAndGameEventApiService;

	@Autowired
	private ColorNumberEventScheduler colorNumberEventScheduler;

	@Override
	public ResponseEntity<ProfiteerResponse<EventResponse>> createColorOrNumberEvent(String authorization,
			EventUserOrderResponse colorNumberEventRequestResponse) throws Exception {

		ProfiteerResponse<EventResponse> famousResponse = new ProfiteerResponse<>();
		ProfiteerResult<EventResponse> famousResult = new ProfiteerResult<>();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		try {

			EventResponse eventResponse = contestAndGameEventApiService
					.createOrUpdateColorOrNumberEvent(colorNumberEventRequestResponse);
			famousResult.setResult(eventResponse);
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

	@Override
	public ResponseEntity<ProfiteerResponse<List<EventUserOrderResponse>>> isPlaceColorOrNumberEventByUserId(
			EventOrderRequest eventOrderRequest) throws Exception {

		ProfiteerResponse<List<EventUserOrderResponse>> famousResponse = new ProfiteerResponse<>();
		ProfiteerResult<List<EventUserOrderResponse>> famousResult = new ProfiteerResult<>();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		try {

			List<EventUserOrderResponse> colorNumberEventRequestResponses = contestAndGameEventApiService
					.isPlaceColorOrNumberEventByUserId(eventOrderRequest);
			famousResult.setResult(colorNumberEventRequestResponses);
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

	@Override
	public ResponseEntity<ProfiteerResponse<List<EventUserOrderResponse>>> getUserEvents(String authorization, Long userId,
			Date date) throws Exception {

		ProfiteerResponse<List<EventUserOrderResponse>> famousResponse = new ProfiteerResponse<>();
		ProfiteerResult<List<EventUserOrderResponse>> famousResult = new ProfiteerResult<>();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		try {

			List<EventUserOrderResponse> colorNumberEventRequestResponses = contestAndGameEventApiService
					.getUserEvent(userId, date);
			famousResult.setResult(colorNumberEventRequestResponses);
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

	@Override
	public ResponseEntity<ProfiteerResponse<Boolean>> isWinningEvent(@NotEmpty String authorization) throws Exception {

		ProfiteerResponse<Boolean> famousResponse = new ProfiteerResponse<>();
		ProfiteerResult<Boolean> famousResult = new ProfiteerResult<>();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		try {

			colorNumberEventScheduler.isWinningEvent();
			famousResult.setResult(true);
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

	@Override
	public ResponseEntity<ProfiteerResponse<EventPageResponse>> getEventDetails(String authorization) throws Exception {

		ProfiteerResponse<EventPageResponse> famousResponse = new ProfiteerResponse<>();
		ProfiteerResult<EventPageResponse> famousResult = new ProfiteerResult<>();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		try {

			EventPageResponse eventPageResponse = contestAndGameEventApiService.getEventDetails();
			famousResult.setResult(eventPageResponse);
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
