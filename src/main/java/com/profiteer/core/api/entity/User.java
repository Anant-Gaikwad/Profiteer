package com.profiteer.core.api.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.profiteer.core.api.util.Base64Utils;

import lombok.Data;

@Data
@Entity
@Table(name = "user", indexes = { @Index(columnList = "user_id"), @Index(columnList = "email"),
		@Index(columnList = "mobile") })
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Id", unique = true, nullable = false, precision = 10)
	private long id;

	@Column(name = "user_id", unique = true, nullable = false, precision = 19)
	private long userId;

	@Column(name = "first_name", length = 60)
	private String firstName;

	@Column(name = "last_name", length = 60)
	private String lastName;

	@Column(name = "email", length = 255)
	private String email;

	@Column(name = "is_email_verified")
	private boolean isEmailVerified;

	@Column(name = "mobile", precision = 19, unique = true, nullable = false)
	private Long mobile;

	@Temporal(TemporalType.DATE)
	@Column(name = "date_of_birth")
	private Date dob;

	@Column(name = "pan_number", length = 200)
	private String pan;

	@Column(name = "kyc_status", length = 200)
	private String kycStatus;

	@Column(name = "profile_photo_url", length = 300)
	private String photoUrl;

	@Column(name = "password", length = 255)
	private String password;
	
	@Column(name = "mpin", length = 6)
	private String mpin;

	@Column(name = "is_user_active", nullable = false, length = 3)
	private boolean active;

	@Column(name = "is_agreement_policy", columnDefinition = "tinyint(1) default 0")
	private boolean isAgreementPolicy;

	@Column(name = "agreement_policy_date")
	private Date agreementPolicyDate;

	@Column(name = "state")
	private String state;

	@Column(name = "user_referral_code", unique = true)
	private String userReferralCode;

	@Column(name = "referred_by")
	private String referredBy;
	
	@Column(name = "referred_user_credit_count")
	private int referredUserCreditCount;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on")
	private Date createdOn;

	@Column(name = "created_by")
	private long createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_on")
	private Date updatedOn;

	@Column(name = "updated_by")
	private long updatedBy;

	@PrePersist
	public void onPrePersist() {
		this.setCreatedOn(new Date());
	}

	@PreUpdate
	public void onPreUpdate() {
		this.setUpdatedOn(new Date());
	}

	public void setPassword(String password) {
		try {
			if (password != null)
				this.password = (Base64Utils.encodeBase64(password));
			else
				this.password = password;
		} catch (Exception e) {
		}
	}
}
