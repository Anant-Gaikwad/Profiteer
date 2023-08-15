package com.profiteer.core.api.entity;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity(name = "forgot_password_detail")
public class ForgotPasswordDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id", unique = true, nullable = false, precision = 10)
	private long id;

	@Column(name = "reset_password_link", nullable = false, length = 300)
	private String resetPasswordLink;

	@Column(name = "valid_up_to", nullable = false)
	private Date validUpto;

	@Column(name = "token", nullable = false, length = 100)
	private String token;

	@Column(name = "created_on", nullable = false)
	private Date createdOn;

	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@PrePersist
	public void onPrePersist() {
		this.setCreatedOn(new Date());
	}
}
