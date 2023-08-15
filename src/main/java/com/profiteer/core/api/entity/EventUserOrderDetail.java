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

import lombok.Data;

@Data
@Entity
@Table(name = "event_user_order_detail", indexes = { @Index(columnList = "event_id"), @Index(columnList = "user_id"),
		@Index(columnList = "event_status"), @Index(columnList = "created_on") })
public class EventUserOrderDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, precision = 10)
	private Long id;

	@Column(name = "event_id", nullable = false)
	private String eventId;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "amount", nullable = false)
	private Double amount;

	@Column(name = "is_color_event")
	private Boolean isColorEvent;

	@Column(name = "is_number_event")
	private Boolean isNumberEvent;

	@Enumerated(EnumType.STRING)
	@Column(name = "choose_color")
	private EventColor chooseColor;

	@Column(name = "choose_number")
	private Integer chooseNumber;

	@Column(name = "is_user_win")
	private Boolean isUserWin;

	@Column(name = "winning_amount")
	private Double winningAmount;

	@Enumerated(EnumType.STRING)
	@Column(name = "event_status")
	private Status eventStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "event_type")
	private GameType eventType;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "placed_order_time", length = 22)
	private Date placeOrderTime;
	
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
