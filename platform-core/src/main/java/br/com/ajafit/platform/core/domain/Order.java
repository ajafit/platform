package br.com.ajafit.platform.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "[ordering]", uniqueConstraints= {@UniqueConstraint(columnNames= {"coupon_id","couchee_id","kit_id"})})
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ordering_id")
	private long id;

	@OneToOne


	@JoinColumns(value = { @JoinColumn(name = "coupon_id"), @JoinColumn(name = "couchee_id"),
			@JoinColumn(name = "kit_id") })
	private CouponUsage couponUsage;

	@ManyToOne
	@JoinColumn(name = "diet_id", nullable = true)
	private Diet diet;

	@ManyToOne
	@JoinColumn(name = "cart_id")
	private Cart cart;

}
