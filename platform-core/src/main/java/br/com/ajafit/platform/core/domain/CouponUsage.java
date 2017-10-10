package br.com.ajafit.platform.core.domain;

import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "coupon_id", "coachee_id" }) })
public class CouponUsage {

	@EmbeddedId
	private CouponUsagePK id;
	private Date date;
}
