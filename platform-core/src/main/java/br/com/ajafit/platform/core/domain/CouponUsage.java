package br.com.ajafit.platform.core.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = { "id" })
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "coupon_id", "coachee_id" }) })
public class CouponUsage {

	@EmbeddedId
	private CouponUsagePK id;
	private Date date;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "couponUsage")
	// @JoinColumns(value = { @JoinColumn(name = "coupon_id"), @JoinColumn(name =
	// "couchee_id") })
	private Collection<Order> orders = new ArrayList<Order>();

}
