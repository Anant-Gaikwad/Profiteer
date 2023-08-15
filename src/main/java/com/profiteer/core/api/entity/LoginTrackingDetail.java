package com.profiteer.core.api.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "login_tracking_detail",
	   indexes = {
			    	@Index(columnList = "user_id")
			   	 })
public class LoginTrackingDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, precision = 10)
	private Long id;
	
	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "ip_address")
	private String ipAddress;
	
	@Column(name = "request")
	private String request;
	
	@Column(name = "requested_api")
	private String requestedApi;
	
	@Column(name = "token_failed")
	private boolean tokenFailed;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on", length = 22)
	private Date createdOn;

	@PrePersist
	public void onPrePersist() {
		this.setCreatedOn(new Date());
	}
}
