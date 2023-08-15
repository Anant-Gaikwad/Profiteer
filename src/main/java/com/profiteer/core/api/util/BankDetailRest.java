package com.profiteer.core.api.util;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.profiteer.core.api.dto.BankDetailsResponseTemp;
import com.profiteer.core.api.dto.BankDetailsResponseWrapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BankDetailRest {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${ifsc_code_bank}")
	private String fetchIfscCodeBankDetail;

	public BankDetailsResponseTemp getBankDetails(String ifscCode) throws NoResultException, Exception {

		BankDetailsResponseTemp bankDetailsResponse = new BankDetailsResponseTemp();

		try {
			log.info("fetchIfscCodeBankDetail : {}", fetchIfscCodeBankDetail);
			String ifscResponse = restTemplate.getForObject("https://ifsc.razorpay.com/" + ifscCode, String.class);
			ObjectMapper mapper = new ObjectMapper();
			bankDetailsResponse = mapper.readValue(ifscResponse, BankDetailsResponseTemp.class);
			return bankDetailsResponse;

		} catch (Exception e) {
			log.info("fetchIfscCodeBankDetail : {}", fetchIfscCodeBankDetail);
			String ifscResponse = restTemplate.getForObject("http://api.techm.co.in/api/v1/ifsc/" + ifscCode,
					String.class);

			ObjectMapper mapper = new ObjectMapper();
			BankDetailsResponseWrapper bankDetailsResponseWrapper = mapper.readValue(ifscResponse,
					BankDetailsResponseWrapper.class);
			return bankDetailsResponseWrapper.getData();
		}
	}

}
