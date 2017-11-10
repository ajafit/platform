package br.com.ajafit.platform.core.persistence;

import java.util.Collection;

import br.com.ajafit.platform.core.domain.Cart;
import br.com.ajafit.platform.core.domain.Coachee;
import br.com.ajafit.platform.core.domain.CouponUsage;
import br.com.ajafit.platform.core.domain.CouponUsagePK;
import br.com.ajafit.platform.core.domain.Order;
import br.com.ajafit.platform.core.domain.Region;

public abstract class PaymentPersistence extends CoachPersistence {

	public Region createRegion(Region region) {
		em.persist(region);
		return region;
	}

	public Cart createCart(Cart cart) {
		em.persist(cart);
		return cart;
	}

	public Cart getCartByCoachee(Coachee coachee) {
		try {
			return (Cart) em.createQuery(
					"select o.cart from Order o where o.couponUsage.id.coachee = :COACHEE and o.cart.done is false")
					.setParameter("COACHEE", coachee).getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public Cart getCartByToken(String token) {
		return (Cart) em.createQuery("from Cart c where c.token = :TOKEN").setParameter("TOKEN", token)
				.getSingleResult();
	}

	public Order createOrder(Order order) {
		em.persist(order);
		return order;
	}

	public Order updateOrder(Order order) {
		return em.merge(order);

	}

	public Collection<Order> getOrdersByCart(Cart cart) {

		return em.createQuery("from Order o where o.cart = :CART").setParameter("CART", cart).getResultList();
	}

	public CouponUsage createCouponUsage(CouponUsage couponUsage) {

		em.persist(couponUsage);
		return couponUsage;
	}

	public CouponUsage getCouponUsageById(CouponUsagePK id) {
		return em.find(CouponUsage.class, id);
	}

}
