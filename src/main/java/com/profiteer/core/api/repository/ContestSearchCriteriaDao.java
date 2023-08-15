package com.profiteer.core.api.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.profiteer.core.api.entity.ContestDetail;
import com.profiteer.core.api.entity.ContestOrderDetail;
import com.profiteer.core.api.entity.Status;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class ContestSearchCriteriaDao {

	@PersistenceContext
	private EntityManager entityManager;

	public List<ContestDetail> getContestDetail(String contestId, Status status) throws Exception {

		try {

			List<Predicate> predicateList = new ArrayList<>();

			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			CriteriaQuery<ContestDetail> criteria = builder.createQuery(ContestDetail.class);
			Root<ContestDetail> root = criteria.from(ContestDetail.class);

			if (StringUtils.hasText(contestId)) {
				Predicate contestIdRestriction = builder.equal(root.get("contestId"), contestId);
				predicateList.add(contestIdRestriction);
			}

			Predicate statusRestriction = builder.equal(root.get("status"), status);
			predicateList.add(statusRestriction);

            criteria.where(builder.and(predicateList.toArray(new Predicate[]{})));

            TypedQuery<ContestDetail> query = entityManager.createQuery(criteria);
			return query.getResultList();

		} catch (Exception e) {
			log.info("Error  : {}", e);
			throw e;
		}
	}

	public List<ContestOrderDetail> getContestOrderDetail(String contestId, Long contestDetailPkId, Status status,
			Long userId) throws Exception {

		try {

			List<Predicate> predicateList = new ArrayList<>();

			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			CriteriaQuery<ContestOrderDetail> criteria = builder.createQuery(ContestOrderDetail.class);
			Root<ContestOrderDetail> root = criteria.from(ContestOrderDetail.class);

			if (StringUtils.hasText(contestId)) {
				Predicate contestIdRestriction = builder.equal(root.get("contestId"), contestId);
				predicateList.add(contestIdRestriction);
			}

			if (contestDetailPkId != null && contestDetailPkId > 0) {
				Predicate contestDetailIdRestriction = builder.equal(root.get("contestDetailId"), contestDetailPkId);
				predicateList.add(contestDetailIdRestriction);
			}

			if (userId != null && userId > 0) {
				Predicate userIdRestriction = builder.equal(root.get("userId"), userId);
				predicateList.add(userIdRestriction);
			}

			Predicate statusRestriction = builder.equal(root.get("status"), status);
			predicateList.add(statusRestriction);

            criteria.where(builder.and(predicateList.toArray(new Predicate[]{})));

            TypedQuery<ContestOrderDetail> query = entityManager.createQuery(criteria);
			return query.getResultList();

		} catch (Exception e) {
			log.info("Error  : ", e);
			throw e;
		}
	}

}
