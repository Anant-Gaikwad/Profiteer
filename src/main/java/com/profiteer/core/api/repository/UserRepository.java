package com.profiteer.core.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.profiteer.core.api.entity.User;

@Repository
public interface UserRepository  extends JpaRepository<User	, Long>{
	
	Optional<User> findByMobile(Long mobileNumber);
	
	Optional<User> findByUserId(Long userId);
	
	Optional<User> findByUserReferralCode(String referalCode);

}
