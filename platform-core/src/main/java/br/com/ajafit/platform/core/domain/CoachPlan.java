package br.com.ajafit.platform.core.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class CoachPlan {

	@EmbeddedId
	private CoachPlanPK id;

	private boolean active;

}
