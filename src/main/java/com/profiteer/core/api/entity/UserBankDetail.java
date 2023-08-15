package com.profiteer.core.api.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.profiteer.core.api.repository.BankStatus;

import lombok.Data;

@Entity
@Data
@Table(name = "user_bank_detail",
		indexes = {
				@Index(columnList = "user_id"),
				@Index(columnList = "account_number")
				})
public class UserBankDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, precision = 10)
	private Long id;
	
	@Column(name = "user_id", nullable = false, precision = 10)
	private Long userId;

	@Column(name = "account_holder_name", length = 200, nullable = false)
	private String accountHolderName;

	@Column(name = "bank_name", length = 260, nullable = false)
	private String bankName;

	@Column(name = "account_number", length = 60, nullable = false)
	private String accountNumber;

	@Column(name = "ifsc_code", length = 60, nullable = false)
	private String ifscCode;
	
	@Column(name = "micr_code", length = 60, nullable = false)
	private String micrCode;

	@Column(name = "branch", length = 160)
	private String branch;

	@Column(name = "bank_address", length = 500)
	private String bankAddress;

	@Column(name = "bank_register_mobile_number", precision = 19)
	private Long bankRegMobileNumber;
	
	@Column(name = "is_default",nullable = false)
	private Boolean isDefault;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private BankStatus status;

	@Column(name = "created_on")
	private Date createdOn;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_on")
	private Date updatedOn;

	@Column(name = "updated_by")
	private Long updatedBy;

	@PrePersist
	public void onPrePersist() {
		this.setCreatedOn(new Date());
	}
	
	@PreUpdate
	public void onPreUpdate() {
		this.setUpdatedOn(new Date());
	}
}
