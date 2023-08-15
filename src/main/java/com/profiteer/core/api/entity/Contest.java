package com.profiteer.core.api.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
@Table(name = "contest")
public class Contest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, precision = 10)
	private Long id;
	
	@Column(name = "contest_id", unique = true, nullable = false)
	private String contestId;

	@Column(name = "first_winner", nullable = false)
	private String firstWinner;

	@Column(name = "second_winner", nullable = false)
	private String secondWinner;

	@Column(name = "third_winner", nullable = false)
	private String thirdWinner;

	@Column(name = "fourth_winner")
	private String fourthWinner;

	@Column(name = "pool_prize", nullable = false)
	private Double poolPrize;

	@Column(name = "pool_size")
	private int poolSize;

	@Column(name = "winning_probability")
	private Double probability;
	
	@Column(name = "entry_fee")
	private Double entryFee;
	
	@Column(name = "processing_fee")
	private Double processingFee;
	
	@Column(name = "refresh_time")
	private Integer refreshTime;

	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on", length = 22)
	private Date createdOn;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_on", length = 22)
	private Date updatedOn;

	@PrePersist
	public void onPrePersist() {
		this.setCreatedOn(new Date());
	}

	@PreUpdate
	public void onPreUpdate() {
		this.setUpdatedOn(new Date());
	}
}
