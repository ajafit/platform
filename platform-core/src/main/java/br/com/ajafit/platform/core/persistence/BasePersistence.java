package br.com.ajafit.platform.core.persistence;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.com.ajafit.platform.core.domain.Region;

public abstract class BasePersistence {

	@PersistenceContext
	protected EntityManager em;

	public Region createRegion(Region region) {
		em.persist(region);
		return region;
	}

	public Region updateRegion(Region region) {
		return em.merge(region);
	}

	public void deleteRegion(Region region) {
		em.remove(region);
	}

	public Region findRegionById(long regionId) {
		return em.find(Region.class, regionId);
	}

	public Collection<Region> filterRegion(String filter) {
		Query query = em.createQuery("select r from Region r where r.descriptions like :FILTER");
		query.setParameter("FILTER", "%"+filter+"%");
		return query.getResultList();
	}

}
