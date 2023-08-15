package com.profiteer.core.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.profiteer.core.api.entity.TransactionDetail;

@Repository
public interface TransactionDetailRepository extends JpaRepository<TransactionDetail, Long> {

	Optional<List<TransactionDetail>> findByUserId(Long userId);
	
	Optional<TransactionDetail> findByUserIdAndEventContestOrderIdAndEventContestId(Long userId, String eventId, Long id);

	@Query(value = "SELECT td FROM transaction_detail td WHERE td.user_id= :userId and td.transaction_type in :transactionTypes ORDER BY td.transaction_date DESC limit 30;", nativeQuery = true)
	public List<TransactionDetail> getTransactionDetailsByUserId(@Param("userId") Long userId,
			@Param("transactionTypes") List<String> transactionTypes);

	@Query(value = "SELECT SUM(td.settle_amount) FROM transaction_detail td WHERE td.user_id=:userId ", nativeQuery = true)
	public Double getWalletBalanceByUserId(Long userId);

}
