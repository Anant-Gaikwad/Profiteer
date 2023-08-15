package com.profiteer.core.api.convertor;

import com.profiteer.core.api.dto.EventResponse;
import com.profiteer.core.api.dto.EventUserOrderResponse;
import com.profiteer.core.api.entity.Event;
import com.profiteer.core.api.entity.EventUserOrderDetail;

public class EventContestConvertor {

	public static EventResponse convertEventEntityToResponse(Event colorNumberEvent) {

		EventResponse eventResponse = new EventResponse();
		eventResponse.setEventId(colorNumberEvent.getEventId());
		eventResponse.setStatus(colorNumberEvent.getEventStatus().toString());
		eventResponse.setEventStartTime(colorNumberEvent.getEventStartTime());
		eventResponse.setEventEndTime(colorNumberEvent.getEventEndTime());
		eventResponse.setWinningColor(colorNumberEvent.getWinningColor());
		eventResponse.setWinningNumber(colorNumberEvent.getWinningNumber());
		
		return eventResponse;
	}

	public static EventUserOrderResponse convertEventUserEntityToResponse(EventUserOrderDetail eventUserOrderDetail) {

		EventUserOrderResponse eventUserDetailResponse = new EventUserOrderResponse();
		
		eventUserDetailResponse.setUserId(eventUserOrderDetail.getUserId());
		eventUserDetailResponse.setAmount(eventUserOrderDetail.getAmount());
		eventUserDetailResponse.setEventId(eventUserOrderDetail.getEventId());
		eventUserDetailResponse.setStatus(eventUserOrderDetail.getEventStatus().toString());

		eventUserDetailResponse.setIsColorEvent(eventUserOrderDetail.getIsColorEvent());
		eventUserDetailResponse.setIsNumberEvent(eventUserOrderDetail.getIsNumberEvent());
		if (eventUserOrderDetail.getChooseColor() != null)
			eventUserDetailResponse.setChooseColor(eventUserOrderDetail.getChooseColor().toString());
		eventUserDetailResponse.setChooseNumber(eventUserOrderDetail.getChooseNumber());

		eventUserDetailResponse.setOrderDate(eventUserOrderDetail.getPlaceOrderTime());
		eventUserDetailResponse.setEventType(eventUserOrderDetail.getEventType().toString());

		return eventUserDetailResponse;
	}
}
