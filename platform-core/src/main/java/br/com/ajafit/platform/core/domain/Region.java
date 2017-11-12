package br.com.ajafit.platform.core.domain;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Region {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "region_id")
	private long id;

	@ManyToOne
	@JoinColumn(name = "outer_id", nullable = true)
	private Region outer;

	@OneToMany(mappedBy = "outer")
	private Collection<Region> inner;

	private String descriptions;

}
