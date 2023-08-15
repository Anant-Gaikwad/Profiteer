package com.profiteer.core.api.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "contest_detail", indexes = { @Index(columnList = "contest_id") })
public class ContestDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, precision = 10)
	private Long id;

	@Column(name = "contest_id",nullable = false)
	private String contestId;

	@Column(name = "total_contest_amount")
	private Double totalContestAmount;
	
	@Column(name = "entry_fee")
	private Double entryFee;

	@Column(name = "contest_ditributed_amount")
	private Double contestDistributedAmount;

	@Column(name = "contest_profitable_amount")
	private Double contestProfitAmount;

/*	@Column(name = "first_winner_user_id")
	private Long firstWinnerUserId;

	@Column(name = "first_winner_amount")
	private Double firstWinnerAmount;

	@Column(name = "second_winner_user_id")
	private Long secondWinnerUserId;

	@Column(name = "second_winner_amount")
	private Double secondWinnerAmount;

	@Column(name = "third_winner_amount")
	private Double thirdWinnerAmount;

	@Column(name = "third_winner_user_id")
	private Long thirdWinnerUserId;

	@Column(name = "fourth_winner_user_id")
	private Long fourthWinnerUserId;

	@Column(name = "fourth_winner_amount")
	private Double fourthWinnerAmount;*/

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private Status status;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "event_start_time", length = 22)
	private Date eventStartTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "event_end_time", length = 22)
	private Date eventEndTime;

	@Column(name = "reason")
	private String reason;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on", length = 22)
	private Date createdOn;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_on", length = 22)
	private Date updatedOn;

	@OneToMany(mappedBy="contestDetail", cascade = CascadeType.ALL)
	private List<ContestOrderWinner> orderWinnerList = new ArrayList<>();

	@OneToMany(mappedBy="contestDetail")
	private List<ContestOrderDetail> orderDetails = new ArrayList<>();

	@PrePersist
	public void onPrePersist() {
		this.setCreatedOn(new Date());
	}

	@PreUpdate
	public void onPreUpdate() {
		this.setUpdatedOn(new Date());
	}
}
