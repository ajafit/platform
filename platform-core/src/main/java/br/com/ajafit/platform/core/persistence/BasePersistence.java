package br.com.ajafit.platform.core.persistence;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.com.ajafit.platform.core.domain.Coupon;
import br.com.ajafit.platform.core.domain.Region;
import br.com.ajafit.platform.core.domain.Screen;
import br.com.ajafit.platform.core.domain.ScreenConfig;
import br.com.ajafit.platform.core.domain.ScreenConfigPK;

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
		query.setParameter("FILTER", "%" + filter + "%");
		return query.getResultList();
	}

	public Collection<ScreenConfig> findCouponsByScreenCode(String code) {

		Query query = em.createQuery("select c from ScreenConfig c where c.id.screen.code = :CODE");
		query.setParameter("CODE", code);
		return query.getResultList();
	}

	public ScreenConfig getScreenConfigById(Long screenId, Long couponId) {
		Screen screen = em.find(Screen.class, screenId);
		Coupon coupon = em.find(Coupon.class, couponId);
		return em.find(ScreenConfig.class, new ScreenConfigPK(screen, coupon));
	}

}
