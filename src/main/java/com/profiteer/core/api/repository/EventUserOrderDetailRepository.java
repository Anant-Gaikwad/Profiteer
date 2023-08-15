package com.profiteer.core.api.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.profiteer.core.api.entity.EventUserOrderDetail;

@Repository
public interface EventUserOrderDetailRepository extends JpaRepository<EventUserOrderDetail, Long> {

	Optional<List<EventUserOrderDetail>> findByUserId(Long userId);

	Optional<List<EventUserOrderDetail>> findByEventIdAndIsColorEvent(String eventId, boolean isColorEvent);

	Optional<List<EventUserOrderDetail>> findByEventIdAndIsNumberEvent(String eventId, boolean isNumberEvent);

	Optional<List<EventUserOrderDetail>> findByEventIdAndUserId(String eventId, Long userId);

	Optional<List<EventUserOrderDetail>> findByUserIdAndPlaceOrderTime(Long userId, Date placeOrderTime);

	@Query(value = "SELECT SUM(eu.amount) FROM event_user_order_detail eu where eu.event_id= :eventId;", nativeQuery = true)
	double getSumOfUserAmountSelectedByChoosenEvent(@Param("eventId") String eventId);

	@Modifying(clearAutomatically = true)
	@Query(value = "update event_user_order_detail euod set euod.winning_amount=(euod.amount * 9.5), euod.event_status= 'SUCCESS', euod.is_user_win=true where euod.event_id= :eventId AND euod.choose_number= :chooseNumber AND euod.is_number_event = true", nativeQuery = true)
	void updateNumberEventUserStatusWinner(@Param("eventId") String eventId,
			@Param("chooseNumber") Integer chooseNumber);

	@Modifying(clearAutomatically = true)
	@Query(value = "update event_user_order_detail euod set euod.winning_amount=euod.amount - (euod.amount * 2), euod.event_status= 'SUCCESS', euod.is_user_win=false where euod.event_id= :eventId AND euod.is_number_event = true", nativeQuery = true)
	void updateNumberEventUserStatusForLossUser(@Param("eventId") String eventId);

	Optional<List<EventUserOrderDetail>> findByEventIdAndIsUserWinAndChooseNumber(String eventId, Boolean isUserWin,
			int chooseNumber);
}
