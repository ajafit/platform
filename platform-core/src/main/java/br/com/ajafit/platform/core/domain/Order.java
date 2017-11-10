package br.com.ajafit.platform.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "[order]")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private long id;

	@ManyToOne
	@JoinColumns(value = { @JoinColumn(name = "couchee_id"), @JoinColumn(name = "coupon_id") })
	private CouponUsage couponUsage;

	@ManyToOne
	@JoinColumn(name = "cart_id")
	private Cart cart;

	private int amount;

}
