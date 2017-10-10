package br.com.ajafit.platform.core.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
@DiscriminatorValue(value = "TRANSFER")
public class Transfer extends Entry {

	@Lob
	private byte[] receipt;
}
