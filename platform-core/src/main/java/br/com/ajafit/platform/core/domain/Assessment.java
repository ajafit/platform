package br.com.ajafit.platform.core.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class Assessment {

	@EmbeddedId
	private AssessmentPK id;
}
