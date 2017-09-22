package br.com.ajafit.platform.core.domain;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class CouponMatch {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coupon_match_id")
	private long id;

	@ManyToMany
	@JoinTable(name = "CouponMatchKit", joinColumns = @JoinColumn(name = "coupon_match_id"), inverseJoinColumns = @JoinColumn(name = "kit_id"))
	private Collection<Kit> kits;

	private String value;
}
