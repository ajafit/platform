package br.com.ajafit.platform.core.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "TRANSFER")
public class Transfer extends Entry {

	private String receipt;
}
