package com.profiteer.core.api.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.profiteer.core.api.dto.OtpRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SendTextOtp {

	public static final String otpMessageFirst = "OTP for your current transaction is ";
	public static final String otpMessageLast = " .This OTP is valid for the next 5 minutes, please do not share this with anyone.";

	@Value("${userId}")
	private String userId;

	@Value("${password}")
	private String password;

	@Value("${senderId}")
	private String senderId;

	@Value("${SMSUrl}")
	private String SMSUrl;

	@Value("${sendMethod}")
	private String sendMethod;

	@SuppressWarnings("deprecation")
	public boolean sendOtpOnMobile(OtpRequest otpRequest, String messageTemplte) {

		try {
			String mainUrl = null;

			URLConnection myURLConnection = null;
			URL myURL = null;
			BufferedReader reader = null;

			String urlencodedmsg = URLEncoder.encode(messageTemplte);
			StringBuilder sendSmsData = new StringBuilder(SMSUrl);

			if (otpRequest != null) {

				mainUrl = SMSUrl + "&userId=" + userId + "&password=" + password + "&sendMethod=" + sendMethod
						+ "&dltEntityId=1201159255823147944" + "&msgType=" + "TEXT" + "&mobile="
						+ otpRequest.getMobileNumber() + "&senderId=" + senderId + "&msg=" + urlencodedmsg
						+ "&dltTemplateId=1207160879871666126" + "&format=" + "json";
			}

			log.info("Url for sending OTP on Mobile : {}", mainUrl);

			myURL = new URL(mainUrl);
			myURLConnection = myURL.openConnection();
			myURLConnection.connect();
			reader = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
			// reading response
			String response;
			while ((response = reader.readLine()) != null)
				log.info("Send OTP Response : {}", response);

			sendSmsData.setLength(0);
			reader.close();
			return true;

		} catch (Exception e) {
			return false;
		}
	}
}
