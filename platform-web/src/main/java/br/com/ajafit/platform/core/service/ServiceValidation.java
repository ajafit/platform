package br.com.ajafit.platform.core.service;

import javax.validation.ValidationException;

public abstract class ServiceValidation {

	protected void required(String... params) throws ValidationException {

		for (String string : params) {
			if (string == null) {
				throw new ValidationException("required");
			}
		}
	}

	// TODO buscar referencia de verdade
	protected void captcha(String code, String ref) throws ValidationException {

		if (!code.equals(ref)) {
			throw new ValidationException("captcha");
		}
	}
}
