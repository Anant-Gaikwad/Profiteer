package com.profiteer.core.api.entity;

import java.util.Date;

import javax.persistence.*;

import lombok.Data;

@Entity
@Table(name = "contest_order_detail", indexes = { @Index(columnList = "id"), @Index(columnList = "contest_id") })
@Data
public class ContestOrderDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, precision = 10)
	private Long id;

	@Column(name = "contest_id", length = 100, nullable = false)
	private String contestId;

	@Column(name = "contest_detail_id", nullable = false)
	private Long contestDetailId;

	@Column(name = "user_type", nullable = false)
	private String userType;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "contest_choose_number", length = 20, nullable = false)
	private Long contestChooseNumber;

	@Column(name = "contest_amount", length = 20, nullable = false)
	private Double contestPoolAmount;

	@Column(name = "contest_winner_amount", length = 20, nullable = false)
	private Double contestWinningAmount;

	@Column(name = "contest_winner_type", length = 20)
	private Long contestWinningType;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "order_time", nullable = false)
	private Date orderTime;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 20, nullable = false)
	private Status status;

	@Column(name = "is_user_win", nullable = false)
	private boolean isUserWin;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on", length = 22)
	private Date createdOn;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_on", length = 22)
	private Date updatedOn;

	@ManyToOne
	@JoinColumn(name="contestDetail_id", nullable=false)
	private ContestDetail contestDetail;

	@PrePersist
	public void onPrePersist() {
		this.setCreatedOn(new Date());
	}

	@PreUpdate
	public void onPreUpdate() {
		this.setUpdatedOn(new Date());
	}
}
