package br.com.ajafit.platform.core.domain;

import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class CouponUsage {

	@EmbeddedId
	private CouponUsagePK id;
	private Date date;
}
