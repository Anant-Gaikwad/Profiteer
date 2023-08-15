package com.profiteer.core.api.util;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

@Component
public class BlowfishUtils {

	private static final String strkey = "Blowfish";
	private static final Base64 base64 = new Base64(true);

	public static final String encrypt(String data) throws Exception {

		SecretKeySpec key = new SecretKeySpec(strkey.getBytes(StandardCharsets.UTF_8), strkey);
		Cipher cipher = Cipher.getInstance(strkey);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return base64.encodeToString(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));

	}

	public static final String decrypt(String encrypted) throws Exception {
		byte[] encryptedData = Base64.decodeBase64(encrypted);
		SecretKeySpec key = new SecretKeySpec(strkey.getBytes(StandardCharsets.UTF_8), strkey);
		Cipher cipher = Cipher.getInstance(strkey);
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] decrypted = cipher.doFinal(encryptedData);
		return new String(decrypted);
	}
}