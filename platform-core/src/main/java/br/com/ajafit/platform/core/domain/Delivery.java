package br.com.ajafit.platform.core.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Delivery {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@OneToOne
	@JoinColumn(name = "cart_id", unique = true)
	private Cart cart;

	@ManyToOne
	@JoinColumn(name = "shipaddress_id")
	private ShipAddress shipAddress;

	@ManyToOne
	@JoinColumn(name = "deliver_id", nullable = true)
	private Deliver deliver;

	private String comments;

	private double fare;

	private Date estimate;
}
