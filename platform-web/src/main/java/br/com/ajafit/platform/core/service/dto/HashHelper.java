package br.com.ajafit.platform.core.service.dto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.bouncycastle.crypto.RuntimeCryptoException;

public class HashHelper {

	public static String SEED = "AJAFIT";
	static {
		SEED = System.getProperties().getProperty("AJAFIT_TOKEN_SEED") == null ? "AJAFIT"
				: System.getProperties().getProperty("AJAFIT_TOKEN_SEED");
	}

	public static String generateToken(String userContext) {

		try {
			String text = SEED + "_" + userContext + System.currentTimeMillis();
			StringBuffer hexString = new StringBuffer();

			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(text.getBytes());
			byte[] hash = md.digest();

			for (int i = 0; i < hash.length; i++) {
				if ((0xff & hash[i]) < 0x10) {
					hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
				} else {
					hexString.append(Integer.toHexString(0xFF & hash[i]));
				}

			}
			return hexString.toString();
		} catch (Exception e) {
			throw new RuntimeException("problema interno", e);
		}

	}

	public static void main(String[] args) throws NoSuchAlgorithmException, CloneNotSupportedException {
		for (int i = 0; i < 10; i++)
			System.err.println("resp:" + generateToken("alexandre"));
	}
}
