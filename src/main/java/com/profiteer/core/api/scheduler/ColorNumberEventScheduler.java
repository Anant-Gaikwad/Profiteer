package com.profiteer.core.api.scheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.profiteer.core.api.entity.Event;
import com.profiteer.core.api.entity.EventColor;
import com.profiteer.core.api.entity.EventUserOrderDetail;
import com.profiteer.core.api.entity.Status;
import com.profiteer.core.api.entity.TransactionDetail;
import com.profiteer.core.api.repository.ColorNumberEventRepository;
import com.profiteer.core.api.repository.EventUserOrderDetailRepository;
import com.profiteer.core.api.repository.TransactionDetailRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ColorNumberEventScheduler {

	@Value("${event_reboot_time}")
	private int eventRebootTime;

	@Autowired
	private ColorNumberEventRepository colorNumberEventRepository;

	@Autowired
	private EventUserOrderDetailRepository eventUserOrderDetailRepository;

	@Autowired
	private TransactionDetailRepository transactionDetailRepository;

	public static boolean isGreen = true;

	@Autowired
	@Qualifier("taskScheduler")
	private TaskScheduler taskScheduler;

//	@Scheduled(cron = "0 0/3 08-23 * * *", zone = "Asia/Kolkata")
	public void EventScheduler() {

		log.info("SCHEDULER STARTED...........................................");
		this.createEvent();

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, 150);

		taskScheduler.schedule((Runnable) () -> {
			log.info("isWinningEvent Method STARTED BY {}::", calendar.getTime());
			this.isWinningEvent();
		}, new Date(calendar.getTimeInMillis()));
	}

	public void createEvent() {

		Event colorNumberEvent = new Event();
		colorNumberEvent.setEventStatus(Status.INPROCESS);
		colorNumberEvent.setTotalEventAmount(0.0);
		colorNumberEvent.setTotalColorEventAmount(0.0);
		colorNumberEvent.setTotalNumberEventAmount(0.0);

		colorNumberEvent.setTotalColorEventAmount(0.0);
		colorNumberEvent.setColorDistributedAmount(0.0);
		colorNumberEvent.setColorProfitAmount(0.0);

		colorNumberEvent.setTotalNumberEventAmount(0.0);
		colorNumberEvent.setNumberDistributedAmount(0.0);
		colorNumberEvent.setNumberProfitAmount(0.0);
		colorNumberEvent.setEventStartTime(new Date());
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, 3);
		colorNumberEvent.setEventEndTime(new Date(calendar.getTimeInMillis()));
		String eventId = "CNE-" + this.getCurrentTimestamp();
		colorNumberEvent.setEventId(eventId);
		colorNumberEventRepository.save(colorNumberEvent);
	}

	public long getCurrentTimestamp() {
		return Calendar.getInstance().getTimeInMillis();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void isWinningEvent() {
		try {

			List<Integer> numberEventArrays = new LinkedList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
			List<Integer> removedNumberEvent = new LinkedList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));

			boolean isPairColor = false;

			Optional<Event> colorNumberEvent = colorNumberEventRepository.findByEventStatus(Status.INPROCESS);

			if (colorNumberEvent.isEmpty())
				throw new Exception("Event is not found");

			if (colorNumberEvent.get().getEventStatus() == Status.INPROCESS) {

				int blackColorUser = 0;
				int greenColorUser = 0;

				Map<String, Double> eventColorMap = new HashMap<>();
				Double totalColorEventAmount = 0.0;
				Double greenColorAmount = 0.0;
				Double blackColorAmount = 0.0;

				Optional<List<EventUserOrderDetail>> eventUserOrderDetails = eventUserOrderDetailRepository
						.findByEventIdAndIsColorEvent(colorNumberEvent.get().getEventId(), true);

				if (eventUserOrderDetails.get() == null || eventUserOrderDetails.get().isEmpty()) {
					ArrayList<Integer> araylist = new ArrayList<Integer>();
					araylist.add(0);
					araylist.add(1);
					araylist.add(2);
					araylist.add(3);

					colorNumberEvent.get().setTotalColorEventAmount(0.0);
					colorNumberEvent.get().setColorDistributedAmount(0.0);
					colorNumberEvent.get().setColorReason("No user found for this event");
					Random rndm = new Random();
					int rndmElem = araylist.get(rndm.nextInt(araylist.size()));
					log.info("Random Number : {}", rndmElem);
					if (rndmElem % 2 == 0) {
						colorNumberEvent.get().setWinningColor(EventColor.GREEN.toString());
						isGreen = false;
					} else {
						colorNumberEvent.get().setWinningColor(EventColor.BLACK.toString());
						isGreen = true;
					}
					colorNumberEvent.get().setEventStatus(Status.COMPLETE);
					colorNumberEventRepository.save(colorNumberEvent.get());
				}

				if (eventUserOrderDetails.get() != null && !eventUserOrderDetails.get().isEmpty()) {

					for (EventUserOrderDetail eventUserOrderDetail : eventUserOrderDetails.get()) {

						if (eventUserOrderDetail.getChooseColor() == EventColor.BLACK) {

							blackColorUser = blackColorUser + 1;
							if (eventColorMap.get(EventColor.BLACK.toString()) == null) {

								eventColorMap.put(EventColor.BLACK.toString(),
										eventUserOrderDetail.getAmount() != null ? eventUserOrderDetail.getAmount()
												: 0.0);
							} else {
								eventColorMap.put(EventColor.BLACK.toString(), eventColorMap
										.get(EventColor.BLACK.toString())
										+ (eventUserOrderDetail.getAmount() != null ? eventUserOrderDetail.getAmount()
												: 0.0));
							}
						}

						if (eventUserOrderDetail.getChooseColor() == EventColor.GREEN) {

							greenColorUser = greenColorUser + 1;

							if (eventColorMap.get(EventColor.GREEN.toString()) == null) {
								eventColorMap.put(EventColor.GREEN.toString(),
										eventUserOrderDetail.getAmount() != null ? eventUserOrderDetail.getAmount()
												: 0.0);
							} else {
								eventColorMap.put(EventColor.GREEN.toString(), eventColorMap
										.get(EventColor.GREEN.toString())
										+ (eventUserOrderDetail.getAmount() != null ? eventUserOrderDetail.getAmount()
												: 0.0));
							}
						}
					}

					log.info("Color Green Total Event Amount :: {}", eventColorMap.get(EventColor.GREEN.toString()));
					log.info("Color BLACK Total Event Amount :: {}", eventColorMap.get(EventColor.BLACK.toString()));
					log.info("BLACK color user count : {}", blackColorUser);
					log.info("GREEN color user count : {}", greenColorUser);

					totalColorEventAmount = eventColorMap.get(EventColor.GREEN.toString())
							+ eventColorMap.get(EventColor.BLACK.toString());
					greenColorAmount = eventColorMap.get(EventColor.GREEN.toString());
					blackColorAmount = eventColorMap.get(EventColor.BLACK.toString());

					Double greenPercentage = (100 * greenColorAmount) / totalColorEventAmount;
					Double blackPercentage = (100 * blackColorAmount) / totalColorEventAmount;

					Double diffColorPerc = greenPercentage - blackPercentage;
					if (diffColorPerc <= 0)
						diffColorPerc = blackPercentage - greenPercentage;

					if (diffColorPerc <= 5)
						isPairColor = true;

					if (isPairColor) {
						boolean isGreenUserPair = false;
						boolean isBlackUserPair = false;

						if (blackColorUser > greenColorUser)
							isBlackUserPair = true;
						else
							isGreenUserPair = true;

						colorNumberEvent.get().setTotalColorEventAmount(totalColorEventAmount);
						colorNumberEvent.get().setWinningColor(
								isBlackUserPair ? EventColor.BLACK_AQUA.toString() : EventColor.GREEN_AQUA.toString());
						colorNumberEvent.get().setColorReason(
								"Winning the pair color : " + colorNumberEvent.get().getWinningColor().toString());
						colorNumberEvent.get().setColorDistributedAmount(
								isBlackUserPair ? (eventColorMap.get(EventColor.BLACK.toString()) * 1.5)
										: (eventColorMap.get(EventColor.GREEN.toString()) * 1.5));

						colorNumberEvent.get().setGreenAmount(greenColorAmount);
						colorNumberEvent.get().setBlacktAmount(blackColorAmount);
						colorNumberEvent.get().setColorProfitAmount(colorNumberEvent.get().getTotalColorEventAmount()
								- colorNumberEvent.get().getColorDistributedAmount());

						colorNumberEventRepository.save(colorNumberEvent.get());

						if (isBlackUserPair) {

							for (EventUserOrderDetail eventUserOrderDetail : eventUserOrderDetails.get()) {

								eventUserOrderDetail.setEventStatus(Status.SUCCESS);

								if (EventColor.BLACK == eventUserOrderDetail.getChooseColor()) {
									eventUserOrderDetail.setWinningAmount(eventUserOrderDetail.getAmount()
											+ (((eventUserOrderDetail.getAmount() / 2) * 95) / 100));
									eventUserOrderDetail.setIsUserWin(true);

									eventUserOrderDetailRepository.save(eventUserOrderDetail);
									Optional<TransactionDetail> transactionDetail = transactionDetailRepository
											.findByUserIdAndEventContestOrderIdAndEventContestId(
													eventUserOrderDetail.getUserId(), eventUserOrderDetail.getEventId(),
													eventUserOrderDetail.getId());
									if (transactionDetail.get() != null) {
										transactionDetail.get()
												.setSettleAmount(eventUserOrderDetail.getWinningAmount());
										transactionDetailRepository.save(transactionDetail.get());
									}
								} else {
									eventUserOrderDetail.setWinningAmount(
											eventUserOrderDetail.getAmount() - (eventUserOrderDetail.getAmount() * 2));
									eventUserOrderDetail.setIsUserWin(false);
									eventUserOrderDetailRepository.save(eventUserOrderDetail);
								}
							}
						}

						if (isGreenUserPair) {

							for (EventUserOrderDetail eventUserOrderDetail : eventUserOrderDetails.get()) {

								eventUserOrderDetail.setEventStatus(Status.SUCCESS);

								if (EventColor.GREEN == eventUserOrderDetail.getChooseColor()) {
									eventUserOrderDetail.setWinningAmount(eventUserOrderDetail.getAmount()
											+ (((eventUserOrderDetail.getAmount() / 2) * 95) / 100));
									eventUserOrderDetail.setIsUserWin(true);
									eventUserOrderDetailRepository.save(eventUserOrderDetail);

									Optional<TransactionDetail> transactionDetail = transactionDetailRepository
											.findByUserIdAndEventContestOrderIdAndEventContestId(
													eventUserOrderDetail.getUserId(), eventUserOrderDetail.getEventId(),
													eventUserOrderDetail.getId());

									if (transactionDetail.get() != null) {
										transactionDetail.get()
												.setSettleAmount(eventUserOrderDetail.getWinningAmount());
										transactionDetailRepository.save(transactionDetail.get());
									}

								} else {
									eventUserOrderDetail.setWinningAmount(
											eventUserOrderDetail.getAmount() - (eventUserOrderDetail.getAmount() * 2));
									eventUserOrderDetail.setIsUserWin(false);
									eventUserOrderDetailRepository.save(eventUserOrderDetail);
								}
							}
						}
					}

					if (!isPairColor && eventColorMap.get(EventColor.GREEN.toString()) > eventColorMap
							.get(EventColor.BLACK.toString())) {

						colorNumberEvent.get().setTotalColorEventAmount(totalColorEventAmount);
						colorNumberEvent.get().setWinningColor(EventColor.BLACK.toString());
						colorNumberEvent.get().setColorReason("Winning the black color");
						colorNumberEvent.get()
								.setColorDistributedAmount(eventColorMap.get(EventColor.BLACK.toString()) * 2);
						colorNumberEvent.get().setGreenAmount(greenColorAmount);
						colorNumberEvent.get().setBlacktAmount(blackColorAmount);
						colorNumberEvent.get().setColorProfitAmount(colorNumberEvent.get().getTotalColorEventAmount()
								- colorNumberEvent.get().getColorDistributedAmount());
						colorNumberEventRepository.save(colorNumberEvent.get());

						for (EventUserOrderDetail eventUserOrderDetail : eventUserOrderDetails.get()) {

							eventUserOrderDetail.setEventStatus(Status.SUCCESS);

							if (EventColor.BLACK == eventUserOrderDetail.getChooseColor()) {
								eventUserOrderDetail.setWinningAmount(eventUserOrderDetail.getAmount()
										+ ((eventUserOrderDetail.getAmount() * 95) / 100));
								eventUserOrderDetail.setIsUserWin(true);

								eventUserOrderDetailRepository.save(eventUserOrderDetail);
								Optional<TransactionDetail> transactionDetail = transactionDetailRepository
										.findByUserIdAndEventContestOrderIdAndEventContestId(
												eventUserOrderDetail.getUserId(), eventUserOrderDetail.getEventId(),
												eventUserOrderDetail.getId());
								if (transactionDetail.get() != null) {
									transactionDetail.get().setSettleAmount(eventUserOrderDetail.getWinningAmount());
									transactionDetailRepository.save(transactionDetail.get());
								}
							} else {
								eventUserOrderDetail.setWinningAmount(
										eventUserOrderDetail.getAmount() - (eventUserOrderDetail.getAmount() * 2));
								eventUserOrderDetail.setIsUserWin(false);
								eventUserOrderDetailRepository.save(eventUserOrderDetail);
							}
						}
					}

					if (!isPairColor && eventColorMap.get(EventColor.GREEN.toString()) < eventColorMap
							.get(EventColor.BLACK.toString())) {

						colorNumberEvent.get().setTotalColorEventAmount(totalColorEventAmount);
						colorNumberEvent.get().setWinningColor(EventColor.GREEN.toString());
						colorNumberEvent.get().setColorReason("Winning the green color");
						colorNumberEvent.get()
								.setColorDistributedAmount(eventColorMap.get(EventColor.GREEN.toString()) * 2);
						colorNumberEvent.get().setGreenAmount(greenColorAmount);
						colorNumberEvent.get().setBlacktAmount(blackColorAmount);
						colorNumberEvent.get().setColorProfitAmount(colorNumberEvent.get().getTotalColorEventAmount()
								- colorNumberEvent.get().getColorDistributedAmount());

						colorNumberEventRepository.save(colorNumberEvent.get());

						for (EventUserOrderDetail eventUserOrderDetail : eventUserOrderDetails.get()) {

							eventUserOrderDetail.setEventStatus(Status.SUCCESS);

							if (EventColor.GREEN == eventUserOrderDetail.getChooseColor()) {
								eventUserOrderDetail.setWinningAmount(eventUserOrderDetail.getAmount()
										+ ((eventUserOrderDetail.getAmount() * 95) / 100));
								eventUserOrderDetail.setIsUserWin(true);
								eventUserOrderDetailRepository.save(eventUserOrderDetail);

								Optional<TransactionDetail> transactionDetail = transactionDetailRepository
										.findByUserIdAndEventContestOrderIdAndEventContestId(
												eventUserOrderDetail.getUserId(), eventUserOrderDetail.getEventId(),
												eventUserOrderDetail.getId());

								if (transactionDetail.get() != null) {
									transactionDetail.get().setSettleAmount(eventUserOrderDetail.getWinningAmount());
									transactionDetailRepository.save(transactionDetail.get());
								}

							} else {
								eventUserOrderDetail.setWinningAmount(
										eventUserOrderDetail.getAmount() - (eventUserOrderDetail.getAmount() * 2));
								eventUserOrderDetail.setIsUserWin(false);
								eventUserOrderDetailRepository.save(eventUserOrderDetail);
							}
						}
					}
				}

				/*
				 * #############################################################################
				 * ########################
				 * #############################################################################
				 * ########################
				 * #############################################################################
				 * ######################## Number Event is started
				 * #############################################################################
				 * #############################################################################
				 * ########################
				 * #############################################################################
				 * ########################
				 * #############################################################################
				 * ########################
				 */

				Map<Integer, Integer> eventNumberUserCountMap = new HashMap<>();
				SortedMap<Integer, Double> eventNumberMap = new TreeMap<>();

				Optional<List<EventUserOrderDetail>> eventNumberOrderDetails = eventUserOrderDetailRepository
						.findByEventIdAndIsNumberEvent(colorNumberEvent.get().getEventId(), true);

				if (eventNumberOrderDetails.get() == null || eventNumberOrderDetails.get().isEmpty()) {

					Random random = new Random();
					random.nextInt((9 - 0 + 1) + 0);
					colorNumberEvent.get().setTotalNumberEventAmount(0.0);
					colorNumberEvent.get().setNumberDistributedAmount(0.0);
					colorNumberEvent.get().setNumberReason("No user found for this event");
					colorNumberEvent.get().setWinningNumber(random.nextInt((9 - 0 + 1) + 0));
					colorNumberEvent.get().setEventStatus(Status.COMPLETE);

					colorNumberEvent.get().setTotalEventAmount(colorNumberEvent.get().getTotalColorEventAmount()
							+ colorNumberEvent.get().getTotalNumberEventAmount());
					colorNumberEvent.get().setTotalEventProfitableAmount(colorNumberEvent.get().getColorProfitAmount()
							+ colorNumberEvent.get().getNumberProfitAmount());
					colorNumberEvent.get()
							.setTotalEventDistributedAmount(colorNumberEvent.get().getColorDistributedAmount()
									+ colorNumberEvent.get().getNumberDistributedAmount());
					colorNumberEvent.get().setUpdatedOn(new Date());
					colorNumberEventRepository.save(colorNumberEvent.get());
					return;
				}

				for (EventUserOrderDetail eventUserOrderDetail : eventNumberOrderDetails.get()) {

					if (eventNumberMap.get(eventUserOrderDetail.getChooseNumber()) == null) {
						eventNumberMap.put(eventUserOrderDetail.getChooseNumber(), eventUserOrderDetail.getAmount());

						eventNumberUserCountMap.put(eventUserOrderDetail.getChooseNumber(), 1);
						removedNumberEvent.remove(eventUserOrderDetail.getChooseNumber());

					} else {
						eventNumberMap.put(eventUserOrderDetail.getChooseNumber(),
								eventNumberMap.get(eventUserOrderDetail.getChooseNumber())
										+ eventUserOrderDetail.getAmount());

						eventNumberUserCountMap.put(eventUserOrderDetail.getChooseNumber(),
								eventNumberUserCountMap.get(eventUserOrderDetail.getChooseNumber()) + 1);
					}
				}

				log.info("Event User count map with Number :: {}", eventNumberUserCountMap);
				log.info("Total players played on number :: {}", eventNumberMap);

				if (eventNumberMap.size() == 1) {

					Integer key = null;
					Double value = null;
					for (Entry<Integer, Double> entry : eventNumberMap.entrySet()) {
						key = entry.getKey();
						value = entry.getValue();
						log.info(key + " => " + value);
					}

					numberEventArrays.remove(key);
					Random rand = new Random();

					int randomElement = numberEventArrays.get(rand.nextInt(numberEventArrays.size()));

					eventUserOrderDetails.get().get(0).setWinningAmount(eventUserOrderDetails.get().get(0).getAmount()
							- (eventUserOrderDetails.get().get(0).getAmount() * 2));
					eventUserOrderDetails.get().get(0).setIsUserWin(false);
					eventUserOrderDetails.get().get(0).setEventStatus(Status.SUCCESS);
					eventUserOrderDetailRepository.save(eventUserOrderDetails.get().get(0));

					colorNumberEvent.get().setEventStatus(Status.COMPLETE);
					colorNumberEvent.get().setTotalNumberEventAmount(value);
					colorNumberEvent.get().setWinningNumber(randomElement);
					colorNumberEvent.get().setNumberReason("Winning the " + randomElement + " number");
					colorNumberEvent.get().setTotalEventDistributedAmount(0.0);
					colorNumberEvent.get().setNumberProfitAmount(colorNumberEvent.get().getTotalNumberEventAmount()
							- colorNumberEvent.get().getNumberDistributedAmount());

					colorNumberEvent.get().setTotalEventAmount(colorNumberEvent.get().getTotalColorEventAmount()
							+ colorNumberEvent.get().getTotalNumberEventAmount());
					colorNumberEvent.get().setTotalEventProfitableAmount(colorNumberEvent.get().getColorProfitAmount()
							+ colorNumberEvent.get().getNumberProfitAmount());
					colorNumberEvent.get()
							.setTotalEventDistributedAmount(colorNumberEvent.get().getColorDistributedAmount()
									+ colorNumberEvent.get().getNumberDistributedAmount());
					colorNumberEventRepository.save(colorNumberEvent.get());
					return;
				}

				if (eventNumberMap.size() > 1) {

					Double minProfitAmount = 0.0;
					boolean isPairChoose = false;
					boolean isChooseNumberWin = false;
					Double minAmount = 0.0;
					Integer minChooseNumber = null;
					Integer maxChooseNumber = null;
					Double maxAmount = 0.0;
					Double totalNumberAmount = 0.0;

					for (Entry<Integer, Double> numberEntryMap : eventNumberMap.entrySet()) {

						if (numberEntryMap.getValue() > minAmount && maxAmount == 0.0) {
							minAmount = numberEntryMap.getValue();
							maxAmount = numberEntryMap.getValue();
							maxChooseNumber = numberEntryMap.getKey();
						}

						if (numberEntryMap.getValue() <= maxAmount && maxAmount > 0.0) {
							minAmount = numberEntryMap.getValue();
							minChooseNumber = numberEntryMap.getKey();
						}

						totalNumberAmount = totalNumberAmount + numberEntryMap.getValue();
					}

					minProfitAmount = totalNumberAmount % 10;
					log.info("Total Number chooosen Amount : {} ", totalNumberAmount);
					log.info("Max number amount : {}  Max Number : {}", maxAmount, maxChooseNumber);
					log.info("Min number amount : {}  Min Number : {}", minAmount, minChooseNumber);

					if (totalNumberAmount >= (minAmount * 10)
							&& minProfitAmount >= (totalNumberAmount - (minAmount * 10)) || minAmount == maxAmount) {

						colorNumberEvent.get().setEventStatus(Status.COMPLETE);
						colorNumberEvent.get().setTotalNumberEventAmount(totalNumberAmount);
						colorNumberEvent.get().setWinningNumber(minChooseNumber);
						colorNumberEvent.get().setNumberReason("Winning the " + minChooseNumber + " number");
						colorNumberEvent.get().setTotalEventDistributedAmount(minAmount * 9.5);
						colorNumberEvent.get().setNumberProfitAmount(totalNumberAmount - (minAmount * 9.5));

						colorNumberEvent.get().setTotalEventAmount(colorNumberEvent.get().getTotalColorEventAmount()
								+ colorNumberEvent.get().getTotalNumberEventAmount());
						colorNumberEvent.get()
								.setTotalEventProfitableAmount(colorNumberEvent.get().getColorProfitAmount()
										+ colorNumberEvent.get().getNumberProfitAmount());
						colorNumberEvent.get()
								.setTotalEventDistributedAmount(colorNumberEvent.get().getColorDistributedAmount()
										+ colorNumberEvent.get().getNumberDistributedAmount());
						// colorNumberEvent.get().setEventEndTime(new Date());
						colorNumberEventRepository.save(colorNumberEvent.get());

						eventUserOrderDetailRepository
								.updateNumberEventUserStatusForLossUser(colorNumberEvent.get().getEventId());
						eventUserOrderDetailRepository.updateNumberEventUserStatusWinner(
								colorNumberEvent.get().getEventId(), minChooseNumber);

						Optional<List<EventUserOrderDetail>> eventUserOrderDetailList = eventUserOrderDetailRepository
								.findByEventIdAndIsUserWinAndChooseNumber(colorNumberEvent.get().getEventId(), true,
										minChooseNumber);

						if (eventUserOrderDetailList.isPresent())
							for (EventUserOrderDetail eventUserOrderDetail : eventUserOrderDetailList.get()) {

								Optional<TransactionDetail> transactionDetail = transactionDetailRepository
										.findByUserIdAndEventContestOrderIdAndEventContestId(
												eventUserOrderDetail.getUserId(), eventUserOrderDetail.getEventId(),
												eventUserOrderDetail.getId());

								if (transactionDetail.get() != null) {
									transactionDetail.get().setSettleAmount(eventUserOrderDetail.getWinningAmount());
									transactionDetailRepository.save(transactionDetail.get());
								}
							}
						return;

					} else {

						colorNumberEvent.get().setEventStatus(Status.COMPLETE);
						colorNumberEvent.get().setTotalNumberEventAmount(totalNumberAmount);
						removedNumberEvent.get(0);
						colorNumberEvent.get().setWinningNumber(removedNumberEvent.get(0));
						colorNumberEvent.get().setNumberReason("Winning the " + removedNumberEvent.get(0) + " number");
						colorNumberEvent.get().setTotalEventDistributedAmount(0.0);
						colorNumberEvent.get().setNumberProfitAmount(totalNumberAmount);

						colorNumberEvent.get().setTotalEventAmount(colorNumberEvent.get().getTotalColorEventAmount()
								+ colorNumberEvent.get().getTotalNumberEventAmount());
						colorNumberEvent.get()
								.setTotalEventProfitableAmount(colorNumberEvent.get().getColorProfitAmount()
										+ colorNumberEvent.get().getNumberProfitAmount());
						colorNumberEvent.get()
								.setTotalEventDistributedAmount(colorNumberEvent.get().getColorDistributedAmount()
										+ colorNumberEvent.get().getNumberDistributedAmount());
						// colorNumberEvent.get().setEventEndTime(new Date());
						colorNumberEventRepository.save(colorNumberEvent.get());

						eventUserOrderDetailRepository
								.updateNumberEventUserStatusForLossUser(colorNumberEvent.get().getEventId());
						eventUserOrderDetailRepository.updateNumberEventUserStatusWinner(
								colorNumberEvent.get().getEventId(), minChooseNumber);

						Optional<List<EventUserOrderDetail>> eventUserOrderDetailList = eventUserOrderDetailRepository
								.findByEventIdAndIsUserWinAndChooseNumber(colorNumberEvent.get().getEventId(), true,
										minChooseNumber);

						if (eventUserOrderDetailList.isPresent()) {
							for (EventUserOrderDetail eventUserOrderDetail : eventUserOrderDetailList.get()) {

								Optional<TransactionDetail> transactionDetail = transactionDetailRepository
										.findByUserIdAndEventContestOrderIdAndEventContestId(
												eventUserOrderDetail.getUserId(), eventUserOrderDetail.getEventId(),
												eventUserOrderDetail.getId());

								if (transactionDetail.get() != null) {
									transactionDetail.get().setSettleAmount(eventUserOrderDetail.getWinningAmount());
									transactionDetailRepository.save(transactionDetail.get());
								}
							}
						}

						return;
					}
				}
			}

			return;

		} catch (Exception e) {
			log.info("Error while getting winning event details: {}", e);
		}
	}
}
