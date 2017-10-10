package br.com.ajafit.platform.core.service;

import javax.ejb.EJB;
import javax.validation.ValidationException;

import br.com.ajafit.platform.core.persistence.RatePersistence;

public abstract class ServiceValidation {

	@EJB
	protected RatePersistence persistence;

	protected void validatePermissions(Object... params) throws ValidationException {

		/*jogar excessao caso nao tenha permissao*/
		
	}
	protected void required(Object... params) throws ValidationException {

		for (Object obj : params) {
			if (obj == null) {
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
