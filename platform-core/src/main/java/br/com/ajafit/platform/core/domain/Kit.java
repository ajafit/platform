package br.com.ajafit.platform.core.domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Kit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "kit_id")
	private long id;

	private String image;

	private Date date;

	@Column(nullable = true)
	private boolean client;
	
	private String descriptions;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "kit_id")
	private Collection<Item> items;

}
