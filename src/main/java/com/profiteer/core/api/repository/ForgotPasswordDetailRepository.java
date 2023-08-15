package com.profiteer.core.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.profiteer.core.api.entity.ForgotPasswordDetail;

@Repository
public interface ForgotPasswordDetailRepository extends JpaRepository<ForgotPasswordDetail, Long>{

}
