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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
@Table(name = "transaction_detail", indexes = { @Index(columnList = "transaction_ref_number"),
		@Index(columnList = "transaction_date") })
public class TransactionDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "transaction_ref_number", length = 200, nullable = false, unique = true)
	private String transactionReferenceNumber;

	@Column(name = "bank_transaction_ref_number", length = 200)
	private String bankTransactionReferenceNumber;

	@Column(name = "event_contest_order_id", length = 200, unique = true)
	private Long eventContestOrderId;

	@Column(name = "event_contest_id", length = 200)
	private String eventContestId;

	@Column(name = "transaction_amount", length = 20, nullable = false)
	private Double transactionAmount;

	@Column(name = "settle_amount", length = 20, nullable = false)
	private Double settleAmount;

	@Enumerated(EnumType.STRING)
	@Column(name = "transaction_type", nullable = false)
	private TransactionType transactionType;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "transaction_date", length = 22)
	private Date transactionDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "transaction_status", nullable = false)
	private Status transactionStatus;

	@Column(name = "user_id", length = 20, nullable = false)
	private Long userId;

	@Column(name = "bank_detail_id", length = 20)
	private Long bankDetailId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on", length = 22)
	private Date createdOn;

	@PrePersist
	public void onPrePersist() {
		this.setCreatedOn(new Date());
	}
}
