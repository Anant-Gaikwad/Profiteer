package com.profiteer.core.api.service;

import java.util.Date;
import java.util.List;

import com.profiteer.core.api.dto.EventOrderRequest;
import com.profiteer.core.api.dto.EventPageResponse;
import com.profiteer.core.api.dto.EventResponse;
import com.profiteer.core.api.dto.EventUserOrderResponse;

public interface IEventApiService {

	public EventResponse createOrUpdateColorOrNumberEvent(EventUserOrderResponse colorNumberEventRequestResponse)
			throws Exception;

	public List<EventUserOrderResponse> isPlaceColorOrNumberEventByUserId(EventOrderRequest eventOrderRequest)
			throws Exception;

	public List<EventUserOrderResponse> getUserEvent(Long userId, Date date) throws Exception;

	public EventPageResponse getEventDetails() throws Exception;
}
