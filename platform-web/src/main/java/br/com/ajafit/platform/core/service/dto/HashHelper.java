package br.com.ajafit.platform.core.service.dto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class HashHelper {

	public static String SEED = "AJAFIT";
	static {
		SEED = System.getProperties().getProperty("AJAFIT_TOKEN_SEED") == null ? "AJAFIT"
				: System.getProperties().getProperty("AJAFIT_TOKEN_SEED");
	}

	public static String generateToken(String userContext) {
		String text = SEED + "_" + userContext + System.currentTimeMillis();
		return hash(text);

	}

	public static String hash(String v) {

		try {
			StringBuffer hexString = new StringBuffer();

			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(v.getBytes());
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

	public static Collection<Icon> shuffle(int size) {

		LinkedList<Icon> list = new LinkedList<>();
		list.add(new Icon("calculator", "Calculadora"));
		list.add(new Icon("bicycle", "Bicicleta"));
		list.add(new Icon("bath", "Banheira"));
		list.add(new Icon("bell", "Sino"));
		list.add(new Icon("envelope", "Envelope"));
		list.add(new Icon("heart", "Coração"));
		list.add(new Icon("home", "Casa"));
		list.add(new Icon("truck", "Caminhão"));
		list.add(new Icon("tree", "Arvore"));

		Collections.shuffle(list);
		return list.subList(0, size);
	}

	public static void main(String[] args) throws NoSuchAlgorithmException, CloneNotSupportedException {

		for (Icon i : shuffle(5)) {
			System.err.println(i);
		}
	}
}
