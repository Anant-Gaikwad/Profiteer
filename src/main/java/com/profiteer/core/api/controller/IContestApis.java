package com.profiteer.core.api.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.profiteer.core.api.dto.ContestOrderResponse;
import com.profiteer.core.api.dto.ContestRequest;
import com.profiteer.core.api.dto.ProfiteerResponse;

@Validated
public interface IContestApis {

	@PostMapping(value = "/game/contest/placeContestOrder")
	ResponseEntity<ProfiteerResponse<ContestOrderResponse>> isPlaceContestOrder(
			@NotEmpty @RequestHeader(value = "Authorization", required = true) String authorization,
			@Valid @RequestBody ContestRequest contestRequest) throws Exception;

}
