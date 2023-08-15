package com.profiteer.core.api.dto;

import java.util.List;

import lombok.Data;

@Data
public class EventPageResponse {

	private EventResponse currentEvent;
	private List<EventResponse> previousEvents;
}
