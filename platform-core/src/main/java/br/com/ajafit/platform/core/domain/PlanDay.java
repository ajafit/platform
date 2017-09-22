package br.com.ajafit.platform.core.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class PlanDay {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "planday_id")
	private long id;

	@ManyToOne
	@JoinColumn(name = "choachee_id")
	private Coachee coachee;

	@ManyToOne
	@JoinColumn(name = "plan_id")
	private Plan plan;
	
	private Date date;

}
