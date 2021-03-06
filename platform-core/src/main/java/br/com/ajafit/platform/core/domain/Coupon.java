package br.com.ajafit.platform.core.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(of = { "id" })
@ToString
@Entity
public @Data class Coupon {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coupon_id")
	private long id;

	@ManyToOne
	@JoinColumn(name = "seller_id")
	private Seller seller;

	@ManyToOne
	@JoinColumn(name = "kit_id")
	private Kit kit;

	@Column(nullable = false, unique = true)
	private String code;

	private int discount;

	private int amountToGetOneFree;

	private Date date;

	private boolean expired;

}
