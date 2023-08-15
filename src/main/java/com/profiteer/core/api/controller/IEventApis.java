package com.profiteer.core.api.controller;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.profiteer.core.api.dto.EventOrderRequest;
import com.profiteer.core.api.dto.EventPageResponse;
import com.profiteer.core.api.dto.EventResponse;
import com.profiteer.core.api.dto.EventUserOrderResponse;
import com.profiteer.core.api.dto.ProfiteerResponse;

@Validated
public interface IEventApis {

	@PostMapping(value = "/game/event/create_color_number_event")
	ResponseEntity<ProfiteerResponse<EventResponse>> createColorOrNumberEvent(
			@NotEmpty @RequestHeader(value = "Authorization", required = true) String authorization,
			@Valid @RequestBody EventUserOrderResponse colorNumberEventRequestResponse) throws Exception;

	@PostMapping(value = "/game/contest/place_event_order")
	ResponseEntity<ProfiteerResponse<List<EventUserOrderResponse>>> isPlaceColorOrNumberEventByUserId(
			@Valid @RequestBody EventOrderRequest eventOrderRequest) throws Exception;

	@GetMapping(value = "/game/event/getUserEventDetails")
	ResponseEntity<ProfiteerResponse<List<EventUserOrderResponse>>> getUserEvents(
			@NotEmpty @RequestHeader(value = "Authorization", required = true) String authorization,
			@RequestParam("userId") Long userId, @RequestParam("date") Date date) throws Exception;

	@GetMapping(value = "/game/event/getEventDetails")
	ResponseEntity<ProfiteerResponse<EventPageResponse>> getEventDetails(
			@NotEmpty @RequestHeader(value = "Authorization", required = true) String authorization) throws Exception;

	@GetMapping(value = "/game/contest/winning_event")
	ResponseEntity<ProfiteerResponse<Boolean>> isWinningEvent(
			@NotEmpty @RequestHeader(value = "Authorization", required = true) String authorization) throws Exception;

}
