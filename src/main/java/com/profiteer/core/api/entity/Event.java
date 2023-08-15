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
@Table(name = "color_number_event",
	   indexes = {
			    	@Index(columnList = "event_id")
			   	 })
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, precision = 10)
	private Long id;
	
	@Column(name = "event_id", unique = true, nullable = false)
	private String eventId;

	@Column(name = "winning_color")
	private String winningColor;
	
	@Column(name = "green_amount")
	private Double greenAmount;
	
	@Column(name = "black_amount")
	private Double blacktAmount;
	
	@Column(name = "total_color_event_amount")
	private Double totalColorEventAmount;
	
	@Column(name = "color_ditributed_amount")
	private Double colorDistributedAmount;
	
	@Column(name = "color_profitable_amount")
	private Double colorProfitAmount;
	
	@Column(name = "winning_number")
	private Integer winningNumber;
	
	@Column(name = "total_number_event_amount")
	private Double totalNumberEventAmount;
	
	@Column(name = "number_distributed_amount")
	private Double numberDistributedAmount;
	
	@Column(name = "number_profitable_amount")
	private Double numberProfitAmount;
	
	@Column(name = "total_event_amount")
	private Double totalEventAmount;
	
	@Column(name = "total_profitable_amount")
	private Double totalEventProfitableAmount;
	
	@Column(name = "total_distributed_amount")
	private Double totalEventDistributedAmount;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "event_status")
	private Status eventStatus;
	
	@Column(name = "color_reason")
	private String colorReason;
	
	@Column(name = "number_reason")
	private String numberReason;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "event_start_time", length = 22)
	private Date eventStartTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "event_end_time", length = 22)
	private Date eventEndTime;
	
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
