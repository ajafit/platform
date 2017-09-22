package br.com.ajafit.platform.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Distribution {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "distribution_id")
	private long id;

	@ManyToOne
	@JoinColumn(name = "factory_id")
	private Factory factory;

	@ManyToOne
	@JoinColumn(name = "region_id")
	private Region region;

}
