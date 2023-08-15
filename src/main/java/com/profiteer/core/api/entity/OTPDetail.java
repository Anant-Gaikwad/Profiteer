package com.profiteer.core.api.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name = "otp_detail")
@Data
public class OTPDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long otpId;

	@Column(name = "otp", length = 4, nullable = false)
	private Long otp;

	@Column(name = "mobile_number", unique = true)
	private Long mobileNumber;

	@Column(name = "email", length = 150)
	private String emailId;

	@Column(name = "user_id", length = 120)
	private Long userId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "valid_upto", length = 22)
	private Date validUpto;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on", length = 22)
	private Date createdOn;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_on", length = 22)
	private Date updatedOn;
}
