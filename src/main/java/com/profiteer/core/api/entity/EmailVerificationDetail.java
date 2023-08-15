package com.profiteer.core.api.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "email_verification_detail")
public class EmailVerificationDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, precision = 10)
	private long id;

	@Column(name = "email", nullable = false, length = 100)
	private String email;

	@Column(name = "verification_link", nullable = false, length = 250)
	private String verificationLink;

	@Column(name = "valid_upto", nullable = false)
	private Date validUpto;

	@Column(name = "token", nullable = false, length = 100)
	private String token;

	@Column(name = "user_id", nullable = false, precision = 19)
	private long userId;

	@Column(name = "created_on", nullable = false)
	private Date createdOn;

}
