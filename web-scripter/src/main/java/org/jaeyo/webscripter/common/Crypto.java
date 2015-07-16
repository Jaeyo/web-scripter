package org.jaeyo.webscripter.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

public class Crypto {
	private static BASE64Encoder base64 = new BASE64Encoder();

	public static String getCryptoSHA512(String source) {
		if (source == null)
			return "";

		byte[] arrDigest = null;
		MessageDigest msgDigest = null;

		try {
			msgDigest = MessageDigest.getInstance("SHA-512");
			msgDigest.reset();
			arrDigest = msgDigest.digest(source.getBytes());
		} catch (NoSuchAlgorithmException e) {
			arrDigest = "".getBytes();
			System.out.println(e.getMessage());
		}

		return base64.encode(arrDigest);
	} // getCryptoSHA512
} // class