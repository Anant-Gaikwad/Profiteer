package com.profiteer.core.api.util;

public class OtpTemplate {

	public static String generateSendOTPMessage(String key, String emailTemplate, String otp) {

		if (key != null && (key.equalsIgnoreCase("LOGIN_SMS_OTP") || key.equalsIgnoreCase("MOBILE_REGISTER"))) {
			emailTemplate = emailTemplate.replace("{otp}", otp);
			System.out.println("Email Template :: " + emailTemplate);
		}

		return emailTemplate;
	}
}
