package com.profiteer.core.api.convertor;

import java.util.Date;

import org.springframework.util.StringUtils;

import com.profiteer.core.api.dto.UserRequest;
import com.profiteer.core.api.dto.UserResponse;
import com.profiteer.core.api.entity.User;

public class UserConvertor {

	public static UserResponse convertUserEntityToUserResponse(User user, String token, Double walletBalance) {

		UserResponse userResponse = new UserResponse();
		userResponse.setFirstName(user.getFirstName());
		userResponse.setMobileNumber(user.getMobile());
		userResponse.setEmail(user.getEmail());
		userResponse.setUserId(user.getUserId());
		userResponse.setLastName(user.getLastName());
		userResponse.setIsActive(user.isActive());
		if (StringUtils.hasText(token))
			userResponse.setToken(token);

		userResponse.setWalletBalance(walletBalance != null ? walletBalance : 0.0);
		return userResponse;
	}

	public static User mapCreateUserRequest(UserRequest userRequest, String userRefferal) {

		User user = new User();

		user.setUserId(userRequest.getUserId() != null ? userRequest.getUserId() : new Date().getTime());
		user.setActive(true);
		user.setAgreementPolicy(false);
		user.setEmail(userRequest.getEmail());
		user.setMobile(userRequest.getMobileNumber());
		user.setFirstName(userRequest.getFirstName());
		user.setLastName(userRequest.getLastName());
		user.setReferredBy(userRequest.getReferralCode());
		user.setState(userRequest.getState());
		user.setEmailVerified(false);
		user.setPassword(userRequest.getPassword());
		user.setCreatedBy(user.getUserId());
		user.setUpdatedBy(user.getUserId());
		user.setUserReferralCode(userRefferal);
		user.setReferredUserCreditCount(0);

		return user;
	}
}
