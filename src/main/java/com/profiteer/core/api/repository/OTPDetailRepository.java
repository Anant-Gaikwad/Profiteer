package com.profiteer.core.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.profiteer.core.api.entity.OTPDetail;

@Repository
public interface OTPDetailRepository extends JpaRepository<OTPDetail, Long>{

  Optional<OTPDetail> findByUserIdAndMobileNumberAndOtp(Long userId, Long mobileNumber, String otp);
  
  Optional<OTPDetail> findByMobileNumber(Long mobileNumber);
  
  Optional<OTPDetail> findByMobileNumberAndOtp(Long mobileNumber, Long otp);
}
