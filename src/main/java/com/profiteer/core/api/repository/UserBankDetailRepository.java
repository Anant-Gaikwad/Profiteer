package com.profiteer.core.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.profiteer.core.api.entity.UserBankDetail;

@Repository
public interface UserBankDetailRepository extends JpaRepository<UserBankDetail, Long> {

	Optional<List<UserBankDetail>> findByUserIdAndStatus(Long userId, BankStatus bankStatus);

	Optional<UserBankDetail> findByUserIdAndIfscCodeAndStatusAndAccountNumber(Long userId, String ifscCode,
			BankStatus bankStatus, String accountNumber);

}
