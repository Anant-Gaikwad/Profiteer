package com.profiteer.core.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.profiteer.core.api.entity.ContestDetail;
import com.profiteer.core.api.entity.Status;

@Repository
public interface ContestDetailRepository extends JpaRepository<ContestDetail, Long>{

	Optional<ContestDetail> findByContestIdAndStatus(String contestId, Status status);
}
