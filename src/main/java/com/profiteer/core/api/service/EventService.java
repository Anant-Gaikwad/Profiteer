package com.profiteer.core.api.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.profiteer.core.api.convertor.EventContestConvertor;
import com.profiteer.core.api.dto.EventOrderRequest;
import com.profiteer.core.api.dto.EventPageResponse;
import com.profiteer.core.api.dto.EventResponse;
import com.profiteer.core.api.dto.EventUserOrderResponse;
import com.profiteer.core.api.entity.Event;
import com.profiteer.core.api.entity.EventColor;
import com.profiteer.core.api.entity.EventUserOrderDetail;
import com.profiteer.core.api.entity.GameType;
import com.profiteer.core.api.entity.Status;
import com.profiteer.core.api.entity.TransactionDetail;
import com.profiteer.core.api.entity.TransactionType;
import com.profiteer.core.api.repository.ColorNumberEventRepository;
import com.profiteer.core.api.repository.EventUserOrderDetailRepository;
import com.profiteer.core.api.repository.SearchCriteriaDao;
import com.profiteer.core.api.repository.TransactionDetailRepository;
import com.profiteer.core.api.repository.UserRepository;
import com.profiteer.core.api.scheduler.ColorNumberEventScheduler;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EventService implements IEventApiService {

	@Autowired
	private ColorNumberEventRepository colorNumberEventRepository;

	@Autowired
	private EventUserOrderDetailRepository eventUserOrderDetailRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TransactionDetailRepository transactionDetailRepository;

	@Autowired
	private SearchCriteriaDao searchCriteriaDao;

	@Autowired
	private ColorNumberEventScheduler colorNumberEventScheduler;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public EventResponse createOrUpdateColorOrNumberEvent(EventUserOrderResponse colorNumberEventRequestResponse)
			throws Exception {

		try {
			log.info("createOrUpdateColorOrNumberEvent Request : {}", colorNumberEventRequestResponse);
			colorNumberEventScheduler.createEvent();
			Optional<Event> colorNumberEvent = colorNumberEventRepository.findByEventStatus(Status.INPROCESS);
			return EventContestConvertor.convertEventEntityToResponse(colorNumberEvent.get());

		} catch (Exception e) {
			log.info("Exception occured when create the event : {}", e);
			throw e;
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<EventUserOrderResponse> isPlaceColorOrNumberEventByUserId(EventOrderRequest eventOrderRequest)
			throws Exception {
		try {

			log.info("isPlaceColorOrNumberEventByUserId Request: {}", eventOrderRequest);

			if (userRepository.findByUserId(eventOrderRequest.getUserId()).isEmpty())
				throw new Exception("User details not found");

			EventUserOrderDetail eventUserDetail = new EventUserOrderDetail();
			eventUserDetail.setEventStatus(Status.INPROCESS);

			if (eventOrderRequest.getIsColorEvent() != null && eventOrderRequest.getIsColorEvent()) {
				eventUserDetail.setEventType(GameType.COLOR_EVENT);
				eventUserDetail.setIsColorEvent(true);
				eventUserDetail.setIsNumberEvent(false);
				eventUserDetail.setChooseColor(EventColor.valueOf(eventOrderRequest.getChooseColor()));
			}

			if (eventOrderRequest.getIsNumberEvent() != null && eventOrderRequest.getIsNumberEvent()) {
				eventUserDetail.setEventType(GameType.NUMBER_EVENT);
				eventUserDetail.setIsColorEvent(false);
				eventUserDetail.setIsNumberEvent(true);
				eventUserDetail.setChooseNumber(eventOrderRequest.getChooseNumber());
			}

			eventUserDetail.setAmount(eventOrderRequest.getAmount());
			eventUserDetail.setEventId(eventOrderRequest.getEventId());
			eventUserDetail.setIsUserWin(false);
			eventUserDetail.setWinningAmount(0.0);
			eventUserDetail.setUserId(eventOrderRequest.getUserId());
			eventUserDetail.setPlaceOrderTime(new Date());

			eventUserDetail = eventUserOrderDetailRepository.save(eventUserDetail);

			if (eventUserDetail != null) {
				transactionDetailRepository.save(
						this.makeEntityToPlaceTransactionOrderForEvent(eventUserDetail, TransactionType.EVENT_PLAYED));

				List<EventUserOrderDetail> eventUserDetails = searchCriteriaDao
						.getUserPlacedOrderEventDetails(eventUserDetail.getEventId(), eventUserDetail.getUserId());

				List<EventUserOrderResponse> convertEventUserEntityToResponse = new ArrayList<>();
				for (EventUserOrderDetail eventUserOrderDetail : eventUserDetails) {
					convertEventUserEntityToResponse
							.add(EventContestConvertor.convertEventUserEntityToResponse(eventUserOrderDetail));
				}
				return convertEventUserEntityToResponse;
			}
			return null;

		} catch (Exception e) {
			log.info("Exception occured when place the events : {}", e);
			throw e;
		}
	}

	public TransactionDetail makeEntityToPlaceTransactionOrderForEvent(EventUserOrderDetail eventUserDetail,
			TransactionType transactionRequest) {

		TransactionDetail transactionDetail = new TransactionDetail();

		String transactionRefNumber = UUID.randomUUID().toString();
		transactionRefNumber = transactionRefNumber.replace("-", "");

		transactionDetail.setTransactionReferenceNumber(transactionRefNumber);
		transactionDetail.setTransactionAmount(eventUserDetail.getAmount());
		transactionDetail.setUserId(eventUserDetail.getUserId());
		transactionDetail.setTransactionDate(new Date());
		transactionDetail.setSettleAmount(0 - transactionDetail.getTransactionAmount());
		transactionDetail.setEventContestOrderId(eventUserDetail.getId());
		transactionDetail.setTransactionType(transactionRequest);
		transactionDetail.setTransactionStatus(Status.SUCCESS);
		transactionDetail.setEventContestId(eventUserDetail.getEventId());

		return transactionDetail;
	}

	@Override
	public List<EventUserOrderResponse> getUserEvent(Long userId, Date date) throws Exception {
		try {

			if (userId == null || userId <= 0)
				throw new Exception("User id required");

			List<EventUserOrderResponse> numberEventRequestResponseList = new ArrayList<>();

			if (date == null) {

				Optional<List<EventUserOrderDetail>> eventUserOrderDetails = eventUserOrderDetailRepository
						.findByUserId(userId);

				if (eventUserOrderDetails.isEmpty())
					throw new Exception("Event details not found");

				for (EventUserOrderDetail eventUserOrderDetail : eventUserOrderDetails.get()) {
					numberEventRequestResponseList
							.add(EventContestConvertor.convertEventUserEntityToResponse(eventUserOrderDetail));
				}
				return numberEventRequestResponseList;
			}

			Optional<List<EventUserOrderDetail>> eventUserOrderDetails = eventUserOrderDetailRepository
					.findByUserIdAndPlaceOrderTime(userId, date);

			if (eventUserOrderDetails.isEmpty())
				throw new Exception("Event details not found");

			for (EventUserOrderDetail eventUserOrderDetail : eventUserOrderDetails.get()) {
				numberEventRequestResponseList
						.add(EventContestConvertor.convertEventUserEntityToResponse(eventUserOrderDetail));
			}

			return numberEventRequestResponseList;

		} catch (Exception e) {
			log.info("Error while getting user event details: {}", e);
			throw e;
		}
	}

	public long getCurrentTimestamp() {
		return Calendar.getInstance().getTimeInMillis();
	}

	public Map<Integer, Double> getNumberEventMap() {

		Map<Integer, Double> map = new HashMap<>();
		map.put(0, 0.0);
		map.put(1, 0.0);
		map.put(2, 0.0);
		map.put(3, 0.0);
		map.put(4, 0.0);
		map.put(5, 0.0);
		map.put(6, 0.0);
		map.put(7, 0.0);
		map.put(8, 0.0);
		map.put(9, 0.0);

		return map;
	}

	@Override
	public EventPageResponse getEventDetails() throws Exception {
		try {
			EventPageResponse eventPageResponse = new EventPageResponse();

			List<Event> colorNumberEvents = searchCriteriaDao.getEventDetails(Status.INPROCESS, true);

			if (CollectionUtils.isEmpty(colorNumberEvents))
				throw new Exception("No current event is running");

			eventPageResponse
					.setCurrentEvent(EventContestConvertor.convertEventEntityToResponse(colorNumberEvents.get(0)));

			List<Event> colorNumberEventCompleteList = searchCriteriaDao.getEventDetails(Status.COMPLETE, false);

			if (CollectionUtils.isEmpty(colorNumberEventCompleteList))
				return eventPageResponse;

			List<EventResponse> previousEvents = new ArrayList<>();
			for (Event colorNumberEvent : colorNumberEventCompleteList) {
				previousEvents.add(EventContestConvertor.convertEventEntityToResponse(colorNumberEvent));
			}

			eventPageResponse.setPreviousEvents(previousEvents);
			return eventPageResponse;

		} catch (Exception e) {
			throw e;
		}
	}
}
