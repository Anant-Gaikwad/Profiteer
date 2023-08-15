package com.profiteer.core.api.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.profiteer.core.api.entity.LoginTrackingDetail;
import com.profiteer.core.api.repository.TrackingRepository;
import com.profiteer.core.api.util.BlowfishUtils;
import com.profiteer.core.api.util.FindIPAddress;

@Service
public class TrackingService {

	@Autowired
	private TrackingRepository trackingRepository;

	public void saveUserSession(HttpServletRequest httpServletRequest, Long userId, boolean isTokenFailed) {

		LoginTrackingDetail loginTrackingDetail = new LoginTrackingDetail();
		loginTrackingDetail.setIpAddress(FindIPAddress.getClientIpAddress(httpServletRequest));
		loginTrackingDetail.setUserId(userId);
		try {
			loginTrackingDetail.setRequestedApi(BlowfishUtils.encrypt(FindIPAddress.getFullURL(httpServletRequest)));
		} catch (Exception e) {
		}
		loginTrackingDetail.setTokenFailed(isTokenFailed);

		trackingRepository.save(loginTrackingDetail);
	}
}
