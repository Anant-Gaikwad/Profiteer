package com.profiteer.core.api.convertor;

import java.util.Calendar;
import java.util.Date;

import com.profiteer.core.api.dto.OtpRequest;
import com.profiteer.core.api.entity.OTPDetail;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OtpDetailConvertor {

	private OtpDetailConvertor() {
	}

	public static OTPDetail optDetailDtoToOrm(OtpRequest otpRequest, OTPDetail optDetailOrm) throws Exception {

		try {
			log.info("Login otp request details : {}", otpRequest);
			optDetailOrm
					.setOtpId(optDetailOrm != null && optDetailOrm.getOtpId() != null ? optDetailOrm.getOtpId() : null);
			optDetailOrm.setOtp(otpRequest.getOtp());
			optDetailOrm.setEmailId(otpRequest.getEmail());
			optDetailOrm.setMobileNumber(otpRequest.getMobileNumber());
			optDetailOrm.setUserId(otpRequest.getUserId());
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MINUTE, 5);

			optDetailOrm.setCreatedOn(
					optDetailOrm != null && optDetailOrm.getOtpId() != null ? optDetailOrm.getCreatedOn() : new Date());
			optDetailOrm.setUpdatedOn(new Date());
			optDetailOrm.setValidUpto(calendar.getTime());
			return optDetailOrm;

		} catch (Exception e) {
			log.error("Exception occured when converting the otp detail : {}", e);
			throw e;
		}
	}

}
