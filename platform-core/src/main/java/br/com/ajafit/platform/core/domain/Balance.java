package br.com.ajafit.platform.core.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "BALANCE")
public abstract class Balance extends Profile {

	private String cpf;
	private String cnpj;
}
