package br.com.ajafit.platform.core.service;

import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import br.com.ajafit.platform.core.domain.Cart;
import br.com.ajafit.platform.core.domain.Coachee;
import br.com.ajafit.platform.core.domain.Coupon;
import br.com.ajafit.platform.core.domain.CouponUsage;
import br.com.ajafit.platform.core.domain.CouponUsagePK;
import br.com.ajafit.platform.core.domain.Deliver;
import br.com.ajafit.platform.core.domain.Delivery;
import br.com.ajafit.platform.core.domain.Order;
import br.com.ajafit.platform.core.domain.Person;
import br.com.ajafit.platform.core.domain.Profile;
import br.com.ajafit.platform.core.domain.Region;
import br.com.ajafit.platform.core.domain.ScreenConfig;
import br.com.ajafit.platform.core.domain.ShipAddress;
import br.com.ajafit.platform.core.service.dto.CartDTO;
import br.com.ajafit.platform.core.service.dto.EntityDTOConverter;
import br.com.ajafit.platform.core.service.dto.HashHelper;
import br.com.ajafit.platform.core.service.dto.MoneyHelper;
import br.com.ajafit.platform.core.service.dto.RegionDTO;
import br.com.ajafit.platform.core.service.dto.ScreenItemDTO;

@Path("/service/cart")
@Stateless
public class CartService extends ServiceValidation {

	private Logger logger = Logger.getLogger(CartService.class);

	@PUT
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/remove")
	public void remove(ScreenItemDTO dto) {
		required(dto.getItemId());
		Order order = persistence.getOrderById(dto.getItemId());
		CouponUsage couponUsage = null;
		Coachee coachee = null;
		Cart cart = null;
		if (order != null) {
			cart = order.getCart();
			couponUsage = order.getCouponUsage();
			coachee = order.getCouponUsage().getId().getCoachee();

			persistence.removeOrder(order);
			logger.info("order removed");
		}
		Collection<Order> col = persistence.getOrdersByCouponUsage(couponUsage);
		if (col.isEmpty()) {
			/* copou usage aponta pra nada.. limpar */
			persistence.removeCouponUsage(couponUsage);
		}
		col = persistence.getOrdersByCart(cart);
		if (col.isEmpty()) {
			/* zerou as orders,, limpar cart tmpcoache e tmpperson */
			Delivery delivery = cart.getDelivery();
			if (delivery != null) {
				ShipAddress reference = delivery.getShipAddress();
				persistence.removeDelivery(delivery);

				delivery = persistence.findDeliveryByShipAddress(reference);
				if (delivery == null) {
					/* limpa shipaddress */
					persistence.removeShipAddress(reference);
				}
			}

			persistence.removeCart(cart);

			/*
			 * if (coachee.getPerson().getTemp() != null && coachee.getPerson().getTemp()) {
			 * persistence.removeCoachee(coachee);
			 * persistence.removePerson(coachee.getPerson()); }
			 */

		}

	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/amount")
	public void changeAmount(ScreenItemDTO dto) {
		required(dto.getItemId(), dto.getAmount());
		Order order = persistence.getOrderById(dto.getItemId());
		if (order != null) {
			order.setAmount(dto.getAmount());
			persistence.updateOrder(order);
			logger.info("order  atualizado");
		}

	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/region")
	public void region(RegionDTO dto) {

		required(dto.getRegionId());
		Region region = persistence.getRegionByID(dto.getRegionId());
		if (region == null) {
			throw new WebApplicationException("parametro invalido", 400);
		}

		Coachee coachee = getCoachee();
		Cart cart = persistence.getCartByCoachee(coachee);
		if (cart.getDelivery() == null) {
			ShipAddress address = new ShipAddress();
			address.setRegion(region);
			address = persistence.createShipAddress(address);

			Delivery d = new Delivery();
			d.setCart(cart);
			d.setShipAddress(address);

			d = persistence.createDelivery(d);
			logger.info("delivery created: " + d);
		} else {
			if (!cart.getDelivery().getShipAddress().getRegion().equals(region)) {

				ShipAddress address = new ShipAddress();
				address.setRegion(region);
				address = persistence.createShipAddress(address);

				Delivery d = cart.getDelivery();
				ShipAddress old = d.getShipAddress();
				d.setShipAddress(address);
				persistence.updateDelivery(d);

				Delivery delivery = persistence.findDeliveryByShipAddress(old);
				if (delivery == null) {
					/* shipaddress orfao,, remover */
					persistence.removeShipAddress(old);
				}

				logger.info("delivery  updated: " + d);
			}
		}

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/region/tree/{filter}")
	public RegionDTO regions(@PathParam("filter") String filter) {
		Region region = persistence.findRegionById(Integer.parseInt(filter));
		RegionDTO dto = EntityDTOConverter.parse(region, true, 1);
		return dto;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/add")
	public ScreenItemDTO add(ScreenItemDTO dto) {

		logger.info("começando a adicionar item ao carrinho");
		required(dto.getCouponId(), dto.getScreenId());
		ScreenConfig screenItem = this.persistence.getScreenConfigById(dto.getScreenId(), dto.getCouponId());
		if (screenItem == null) {
			throw new WebApplicationException(
					" parametros invalidos para item de tela" + dto.getScreenId() + "," + dto.getCouponId(), 400);
		}

		Coachee coachee = getCoachee();

		/* pega um carinho not dode */
		Cart cart = this.persistence.getCartByCoachee(coachee);
		if (cart == null) {
			/* se nao tem, cria */
			cart = new Cart();
			cart = this.persistence.createCart(cart);
		}

		/* create Coupon usage */
		Coupon coupon = screenItem.getId().getCoupon();
		CouponUsage couponUsage = this.persistence.getCouponUsageById(new CouponUsagePK(coupon, coachee));
		if (couponUsage == null) {
			/* se n tem cria */
			couponUsage = new CouponUsage();
			couponUsage.setId(new CouponUsagePK(coupon, coachee));
			couponUsage = this.persistence.createOrGetCouponUsage(couponUsage);
		}

		/* create or get cart */

		Order order = null;
		if (couponUsage.getOrders().isEmpty()) {
			/* se nao ter orde pra esse cuoponusage cria.. */
			order = new Order();
			order.setAmount(1);
			order.setCart(cart);
			order.setCouponUsage(couponUsage);
			order = this.persistence.createOrder(order);

		} else {

			/* tem order, pega e somar */
			if (couponUsage.getOrders().stream().filter((Order o) -> !o.getCart().isDone()).findFirst().isPresent()) {
				order = couponUsage.getOrders().stream().filter((Order o) -> !o.getCart().isDone()).findFirst().get();
				order.setAmount(order.getAmount() + 1);

			} else {
				/* nao tem order para carrinho not done. */
				order = new Order();
				order.setAmount(1);
				order.setCart(cart);
				order.setCouponUsage(couponUsage);

			}

			order = this.persistence.createOrder(order);

		}

		/* responder quantidade */
		ScreenItemDTO resp = new ScreenItemDTO();
		Collection<Order> col = this.persistence.getOrdersByCart(cart);
		int count = col.stream().map((Order o) -> o.getAmount()).reduce(Integer::sum).get();
		resp.setCount(count);

		return resp;

	}

	private Coachee getCoachee() {
		try {
			String token = AccessFilter.getToken(ResteasyProviderFactory.getContextData(HttpServletRequest.class));
			/* user logado.. getCoache */
			Person person = persistence.getPersonByToken(token);
			if (person == null) {
				throw new WebApplicationException("token inexistente", 401);
			}
			Collection<Profile> col = persistence.getProfilesFromPerson(person);
			try {
				Coachee coachee = (Coachee) col.stream()
						.filter((Profile p) -> p.getClass().getSimpleName().equals("Coachee")).findFirst().get();
				return coachee;
			} catch (Exception e) {
				throw new WebApplicationException("Perfil de usuario nao definido para " + person.getEmail(), 401);
			}
		} catch (Exception e) {
			/* user nao logado// create temp person and associate a coachee */
			Person person = new Person();
			person.setTemp(true);
			String tmpToken = HashHelper.generateToken("tmp");
			person.setToken(tmpToken);
			person.setTokenDate(new Date());
			person = this.persistence.createPerson(person);

			/* create tmp coachee */
			Coachee tmpCoachee = new Coachee();
			tmpCoachee.setPerson(person);
			tmpCoachee = this.persistence.createCouchee(tmpCoachee);

			/* responder cookie para temp person */
			Cookie cookie = new Cookie("authorization", person.getToken());
			cookie.setDomain("localhost");
			cookie.setPath("/");
			cookie.setMaxAge(-1);
			logger.info("create cookie");
			ResteasyProviderFactory.getContextData(HttpServletResponse.class).addCookie(cookie);

			return tmpCoachee;
		}

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/number")
	public ScreenItemDTO currentCartNumber() {

		ScreenItemDTO resp = new ScreenItemDTO();
		try {
			Coachee coachee = getCoachee();
			Cart cart = persistence.getCartByCoachee(coachee);
			if (cart == null) {
				resp.setCount(0);
				return resp;
			}
			Collection<Order> col = this.persistence.getOrdersByCart(cart);
			int count = col.stream().map((Order o) -> o.getAmount()).reduce(Integer::sum).get();
			resp.setCount(count);
		} catch (Exception e) {
			logger.error("nao pode pegar o current count", e);
		}

		return resp;

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/cart")
	public CartDTO cart() {

		Coachee coachee = getCoachee();
		Cart cart = persistence.getCartByCoachee(coachee);
		Collection<Order> col = this.persistence.getOrdersByCart(cart);
		if (col.isEmpty()) {
			return new CartDTO();
		}
		List<ScreenItemDTO> list = col.stream().map((Order o) -> EntityDTOConverter.parse(o))
				.collect(Collectors.toList());

		list.sort(new Comparator<ScreenItemDTO>() {
			public int compare(ScreenItemDTO o1, ScreenItemDTO o2) {
				return o1.getItemId().compareTo(o2.getItemId());
			}
		});

		int finalV = col.stream()
				.map((Order o) -> MoneyHelper.calculate(o.getCouponUsage().getId().getCoupon()).length == 1
						? MoneyHelper.calculate(o.getCouponUsage().getId().getCoupon())[0] * o.getAmount()
						: MoneyHelper.calculate(o.getCouponUsage().getId().getCoupon())[1] * o.getAmount())
				.reduce(Integer::sum).get();

		CartDTO dto = new CartDTO();
		dto.setCartId(cart.getId());
		dto.setItems(list.toArray(new ScreenItemDTO[0]));
		dto.setSubTotalValue(MoneyHelper.toString(finalV));

		if (cart.getDelivery() != null) {
			dto.setRegion(cart.getDelivery().getShipAddress().getRegion().getDescriptions());
			if (finalV > 10000) {
				dto.setEstimatedShipping("0,00");
			} else {
				dto.setEstimatedShipping(MoneyHelper.toString(1000));
			}

		}
		return dto;

	}

}
