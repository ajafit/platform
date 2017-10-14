package br.com.ajafit.platform.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Saleable {

	@Id
	@TableGenerator(name = "SALEABLE_GEN", table = "SALEABLE_ID_Generator", pkColumnName = "name", valueColumnName = "sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "SALEABLE_GEN")
	@Column(name = "saleable_id")
	private long id;

	private int cost;
	private int revenueShare;

	private String image;
	private String name;
	private String descriptions;

	@OneToOne(mappedBy = "saleable")
	private PlanCoach planCoach;

}
