package br.com.ajafit.platform.core.domain;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Evolution {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "evolution_id")
	private long id;

	@ManyToOne
	@JoinColumn(name = "plan_id")
	private Plan plan;

	private Date date;

	private boolean target;
}
