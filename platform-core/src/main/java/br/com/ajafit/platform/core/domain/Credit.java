package br.com.ajafit.platform.core.domain;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "CREDIT")
public class Credit extends Entry {

	private Date prediction;
}
