package com.profiteer.core.api.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.profiteer.core.api.dto.SearchTransactionCriteria;
import com.profiteer.core.api.entity.Event;
import com.profiteer.core.api.entity.EventUserOrderDetail;
import com.profiteer.core.api.entity.Status;
import com.profiteer.core.api.entity.TransactionDetail;
import com.profiteer.core.api.entity.TransactionType;
import com.profiteer.core.api.entity.UserBankDetail;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class SearchCriteriaDao {

	@PersistenceContext
	private EntityManager entityManager;

	public List<TransactionDetail> getTransactionDetail(SearchTransactionCriteria searchTransactionCriteria)
			throws Exception {

		try {
			List<Predicate> predicateList = new ArrayList<>();

			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			CriteriaQuery<TransactionDetail> criteria = builder.createQuery(TransactionDetail.class);
			Root<TransactionDetail> root = criteria.from(TransactionDetail.class);

			if (searchTransactionCriteria.getUserId() != null) {
				Predicate isinRestriction = builder.equal(root.get("userId"), searchTransactionCriteria.getUserId());
				predicateList.add(isinRestriction);
			}

			if (searchTransactionCriteria.getFromDate() != null && searchTransactionCriteria.getFromDate() > 0
					&& searchTransactionCriteria.getToDate() != null && searchTransactionCriteria.getToDate() > 0) {

				Predicate dateRestriction = builder.between(root.get("transactionDate"),
						new Date(searchTransactionCriteria.getFromDate()),
						new Date(searchTransactionCriteria.getToDate()));
				predicateList.add(dateRestriction);
			}

			if (searchTransactionCriteria.getIsBankTransaction() != null
					&& searchTransactionCriteria.getIsBankTransaction()) {

				List<TransactionType> bankTransactions = new ArrayList<>();
				bankTransactions.add(TransactionType.ADD_MONEY);
				bankTransactions.add(TransactionType.WITHDRAW);
				Predicate inRestriction = builder.in(root.get("transactionType")).value(bankTransactions);
				predicateList.add(inRestriction);
			}

			if (searchTransactionCriteria.getIsGameTransaction() != null
					&& searchTransactionCriteria.getIsGameTransaction()) {

				List<TransactionType> gameTransactions = new ArrayList<>();
				gameTransactions.add(TransactionType.CONTEST_PLAYED);
				gameTransactions.add(TransactionType.CONTEST_WIN);
				gameTransactions.add(TransactionType.EVENT_PLAYED);
				gameTransactions.add(TransactionType.EVENT_WIN);
				Predicate inRestriction = builder.in(root.get("transactionType")).value(gameTransactions);
				predicateList.add(inRestriction);
			}

			if (!predicateList.isEmpty()) {
				criteria.where(builder.and(predicateList.toArray(new Predicate[] {})));
			}

			criteria.orderBy(builder.desc(root.get("transactionDate")));
			TypedQuery<TransactionDetail> query = entityManager.createQuery(criteria).setMaxResults(50);
			return query.getResultList();

		} catch (Exception e) {
			log.info("Error  : {}", e);
			throw e;
		}
	}

	public List<Event> getEventDetails(Status status, boolean isCurrentEvent) throws Exception {
		try {
			List<Predicate> predicateList = new ArrayList<>();

			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			CriteriaQuery<Event> criteria = builder.createQuery(Event.class);
			Root<Event> root = criteria.from(Event.class);

			Predicate statusRestriction = builder.equal(root.get("eventStatus"), status);
			predicateList.add(statusRestriction);

			if (!predicateList.isEmpty()) {
				criteria.where(builder.and(predicateList.toArray(new Predicate[] {})));
			}

			criteria.orderBy(builder.desc(root.get("eventStartTime")));

			if (isCurrentEvent) {
				TypedQuery<Event> query = entityManager.createQuery(criteria);
				return query.getResultList();
			}

			TypedQuery<Event> query = entityManager.createQuery(criteria).setMaxResults(50);
			return query.getResultList();

		} catch (Exception e) {
			throw e;
		}
	}

	public List<EventUserOrderDetail> getUserPlacedOrderEventDetails(String eventId, Long userId) throws Exception {
		try {
			List<Predicate> predicateList = new ArrayList<>();

			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			CriteriaQuery<EventUserOrderDetail> criteria = builder.createQuery(EventUserOrderDetail.class);
			Root<EventUserOrderDetail> root = criteria.from(EventUserOrderDetail.class);

			Predicate statusRestriction = builder.equal(root.get("userId"), userId);
			predicateList.add(statusRestriction);

			Predicate typeRestriction = builder.equal(root.get("eventId"), eventId);
			predicateList.add(typeRestriction);

			if (!predicateList.isEmpty()) {
				criteria.where(builder.and(predicateList.toArray(new Predicate[] {})));
			}

			criteria.orderBy(builder.desc(root.get("placeOrderTime")));
			TypedQuery<EventUserOrderDetail> query = entityManager.createQuery(criteria).setMaxResults(10);
			return query.getResultList();

		} catch (Exception e) {
			throw e;
		}
	}

	public List<UserBankDetail> getUserBankDetails(Long userId) throws Exception {
		try {
			List<Predicate> predicateList = new ArrayList<>();

			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			CriteriaQuery<UserBankDetail> criteria = builder.createQuery(UserBankDetail.class);
			Root<UserBankDetail> root = criteria.from(UserBankDetail.class);

			Predicate userIDRestriction = builder.equal(root.get("userId"), userId);
			predicateList.add(userIDRestriction);

			List<BankStatus> bankStatus = new ArrayList<>();
			bankStatus.add(BankStatus.PENDING);
			bankStatus.add(BankStatus.VERIFIED);
			
			Predicate inRestriction = builder.in(root.get("status")).value(bankStatus);
			predicateList.add(inRestriction);

			if (!predicateList.isEmpty()) {
				criteria.where(builder.and(predicateList.toArray(new Predicate[] {})));
			}

			criteria.orderBy(builder.desc(root.get("createdOn")));
			TypedQuery<UserBankDetail> query = entityManager.createQuery(criteria).setMaxResults(10);
			return query.getResultList();

		} catch (Exception e) {
			throw e;
		}
	}
}
