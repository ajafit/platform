package br.com.ajafit.platform.core.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@DiscriminatorValue(value = "COACHEE")
public class Coachee extends Balance {

	@OneToOne
	@JoinColumn
	private Indication indication;
}
