package br.com.ajafit.platform.core.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class PlanCoach extends Plan {

	private int trialDays;
	private int signDays;

	@OneToOne
	@JoinColumn(name = "saleable_id", nullable = false)
	private Saleable saleable;

}
