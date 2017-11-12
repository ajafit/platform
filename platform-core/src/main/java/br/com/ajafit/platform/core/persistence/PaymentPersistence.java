package br.com.ajafit.platform.core.persistence;

import java.util.Collection;
import java.util.List;

import javax.persistence.Query;

import br.com.ajafit.platform.core.domain.Cart;
import br.com.ajafit.platform.core.domain.Coachee;
import br.com.ajafit.platform.core.domain.CouponUsage;
import br.com.ajafit.platform.core.domain.CouponUsagePK;
import br.com.ajafit.platform.core.domain.Delivery;
import br.com.ajafit.platform.core.domain.Order;
import br.com.ajafit.platform.core.domain.Region;
import br.com.ajafit.platform.core.domain.ShipAddress;

public abstract class PaymentPersistence extends CoachPersistence {

	public Region createRegion(Region region) {
		em.persist(region);
		return region;
	}

	public Cart createCart(Cart cart) {
		em.persist(cart);
		return cart;
	}

	public ShipAddress createShipAddress(ShipAddress shipAddress) {
		em.persist(shipAddress);
		return shipAddress;
	}

	public void removeShipAddress(ShipAddress shipAddress) {
		em.remove(shipAddress);
	}

	public Delivery createDelivery(Delivery delivery) {
		em.persist(delivery);
		return delivery;
	}

	public void removeDelivery(Delivery delivery) {
		em.remove(delivery);
	}

	public Delivery findDeliveryByShipAddress(ShipAddress address) {

		List<Delivery> list = em.createQuery("from Delivery d where d.shipAddress = :ADDRESS")
				.setParameter("ADDRESS", address).getResultList();
		if (list.isEmpty()) {
			return null;
		} else {
			return list.iterator().next();
		}
	}

	public Delivery updateDelivery(Delivery delivery) {
		return em.merge(delivery);

	}

	public void removeCart(Cart cart) {
		em.remove(cart);

	}

	public Cart getCartByCoachee(Coachee coachee) {

		Collection<Cart> carts = em
				.createQuery(
						"select o.cart from Order o where o.couponUsage.id.coachee = :COACHEE and o.cart.done is false")
				.setParameter("COACHEE", coachee).getResultList();
		if (carts.isEmpty()) {
			return null;
		} else {
			return carts.iterator().next();
		}

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

	public void removeOrder(Order order) {
		em.remove(order);

	}

	public Order getOrderById(long id) {
		return em.find(Order.class, id);
	}

	public Collection<Order> getOrdersByCart(Cart cart) {

		return em.createQuery("from Order o where o.cart = :CART").setParameter("CART", cart).getResultList();
	}

	public Collection<Order> getOrdersByCouponUsage(CouponUsage couponUsage) {

		return em.createQuery("from Order o where o.couponUsage = :USAGE").setParameter("USAGE", couponUsage)
				.getResultList();
	}

	public CouponUsage createOrGetCouponUsage(CouponUsage couponUsage) {

		Query q = em.createQuery("from CouponUsage c where c.id.coupon = :COUPON and c.id.coachee = :COACHEE")
				.setParameter("COUPON", couponUsage.getId().getCoupon())
				.setParameter("COACHEE", couponUsage.getId().getCoachee());
		Collection<CouponUsage> col = q.getResultList();
		if (col.isEmpty()) {
			em.persist(couponUsage);
			return couponUsage;
		} else {
			return col.iterator().next();
		}
	}

	public CouponUsage updateCouponUsage(CouponUsage couponUsage) {

		return em.merge(couponUsage);

	}

	public void removeCouponUsage(CouponUsage couponUsage) {

		em.remove(couponUsage);

	}

	public CouponUsage getCouponUsageById(CouponUsagePK id) {
		return em.find(CouponUsage.class, id);
	}

}
