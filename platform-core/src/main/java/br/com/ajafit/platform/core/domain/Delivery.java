package br.com.ajafit.platform.core.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Delivery extends Cart {

	@OneToOne
	@JoinColumn(name = "shipaddress_id")
	private ShipAddress shipAddress;

	@ManyToOne
	@JoinColumn(name = "deliver_id")
	private Deliver deliverer;
	
	private double fare;
	
	private Date estimate;
}
