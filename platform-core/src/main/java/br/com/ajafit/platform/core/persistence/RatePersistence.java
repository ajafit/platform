package br.com.ajafit.platform.core.persistence;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.ajafit.platform.core.domain.Review;

@Stateless
public class RatePersistence extends DeliveryPersistence {

	public Collection<Review> getReviews(long saleableId) {

		Query query = em.createQuery("select c from Review c where c.id.saleable.id = :SALEABLE");
		query.setParameter("SALEABLE", saleableId);
		return query.getResultList();
	}
}
