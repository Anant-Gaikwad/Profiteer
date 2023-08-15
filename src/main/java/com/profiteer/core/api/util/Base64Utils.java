package com.profiteer.core.api.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Base64Utils {

	private Base64Utils() {
	}

	public static final String decodeBase64(String inputString) {
		try {
			byte[] base64decodedBytes = Base64.getDecoder().decode(inputString);
			log.info("Input String   :: " + inputString);

			return new String(base64decodedBytes, StandardCharsets.UTF_8);

		} catch (Exception e) {
			log.error("Exception in decode base 64 : {}", e);
		}
		return null;

	}

	public static final String encodeBase64(String inputString) {
		try {
			return Base64.getEncoder().encodeToString(inputString.getBytes(StandardCharsets.UTF_8));

		} catch (Exception e) {
			log.error("Exception in encode base 64 : {}", e);
		}
		return null;

	}

}
