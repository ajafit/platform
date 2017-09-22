package br.com.ajafit.platform.core.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class CouponUsagePK implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "coupon_id")
	private Coupon coupon;

	@ManyToOne
	@JoinColumn(name = "coachee_id")
	private Coachee coachee;
	
	@ManyToOne
	@JoinColumn(name = "kit_id")
	private Kit kit;
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coachee == null) ? 0 : coachee.hashCode());
		result = prime * result + ((coupon == null) ? 0 : coupon.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CouponUsagePK other = (CouponUsagePK) obj;
		if (coachee == null) {
			if (other.coachee != null)
				return false;
		} else if (!coachee.equals(other.coachee))
			return false;
		if (coupon == null) {
			if (other.coupon != null)
				return false;
		} else if (!coupon.equals(other.coupon))
			return false;
		return true;
	}

}
