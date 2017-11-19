package br.com.ajafit.platform.core.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.ServletContext;
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
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import br.com.ajafit.platform.core.domain.Cart;
import br.com.ajafit.platform.core.domain.Coachee;
import br.com.ajafit.platform.core.domain.CouponUsage;
import br.com.ajafit.platform.core.domain.CouponUsagePK;
import br.com.ajafit.platform.core.domain.Delivery;
import br.com.ajafit.platform.core.domain.Manager;
import br.com.ajafit.platform.core.domain.Order;
import br.com.ajafit.platform.core.domain.Person;
import br.com.ajafit.platform.core.domain.PersonAuthType;
import br.com.ajafit.platform.core.domain.PersonGender;
import br.com.ajafit.platform.core.domain.Profile;
import br.com.ajafit.platform.core.domain.Region;
import br.com.ajafit.platform.core.domain.ShipAddress;
import br.com.ajafit.platform.core.integration.MailMessage;
import br.com.ajafit.platform.core.integration.SendMailIntegration;
import br.com.ajafit.platform.core.service.dto.EntityDTO;
import br.com.ajafit.platform.core.service.dto.EntityDTOConverter;
import br.com.ajafit.platform.core.service.dto.HashHelper;
import br.com.ajafit.platform.core.service.dto.Icon;

@Path("/service/profile")
@Stateless
public class ProfileService extends ServiceValidation {

	private Logger logger = Logger.getLogger(ProfileService.class);

	@Inject
	private ServletContext context;

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/activate/{token}")
	public Response activate(@PathParam("token") String token) {

		try {
			required(token);
		} catch (Exception e) {
			EntityDTO resp = new EntityDTO();
			resp.setDescriptions("Parametros invalidos");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}

		Person person = this.persistence.getPersonByToken(token);
		if (person == null) {
			EntityDTO resp = new EntityDTO();
			resp.setDescriptions("Parametros invalidos");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}

		if (person.isEmailValidation() == true) {

			return Response.status(Response.Status.MOVED_PERMANENTLY).header("Location", "http://localhost:4200")
					.build();
		}
		person.setEmailValidation(true);
		token = HashHelper.generateToken(person.getId() + "");
		person.setToken(token);
		person.setTokenDate(new Date());

		persistence.updatePerson(person);
		
		
		Cookie cookie = new Cookie("authorization", person.getToken());
		cookie.setDomain("www.ajafit.com.br");
		cookie.setPath("/");
		cookie.setMaxAge(-1);
		logger.info("create cookie");
		ResteasyProviderFactory.getContextData(HttpServletResponse.class).addCookie(cookie);
		

		return Response.status(Response.Status.MOVED_PERMANENTLY).header("Location", "http://localhost:4200")
				.build();
		

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/person/registration")
	public EntityDTO personRegistration() {

		return new EntityDTO();

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/person/registered")
	public EntityDTO personRegistered() {

		return new EntityDTO();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/person/current")
	public EntityDTO current() {
		String token = AccessFilter.getToken(ResteasyProviderFactory.getContextData(HttpServletRequest.class));
		if (token == null) {
			throw new WebApplicationException("token invalido", 401);
		}
		Person person = persistence.getPersonByToken(token);
		if (person == null || (person.getTemp() != null) && person.getTemp() == true) {
			throw new WebApplicationException("token inexistente", 401);
		}
		Calendar calTenMinutesAgo = Calendar.getInstance();
		calTenMinutesAgo.add(Calendar.MINUTE, -30);
		if (person.getTokenDate().before(calTenMinutesAgo.getTime())) {
			/* expirou sessao */
			throw new WebApplicationException("token expirado", 401);
		}
		person.setTokenDate(new Date());
		person = persistence.updatePerson(person);

		Collection<Profile> profiles = persistence.getProfilesFromPerson(person).stream()
				.filter((Profile p) -> !p.getClass().getSimpleName().equals("Coach")).collect(Collectors.toList());

		return EntityDTOConverter.parse(person, profiles);
	}

	@POST
	@Path("/person/logout")
	public void logout() {
		String token = AccessFilter.getToken(ResteasyProviderFactory.getContextData(HttpServletRequest.class));
		if (token == null) {
			throw new WebApplicationException("token invalido", 401);
		}
		Person person = persistence.getPersonByToken(token);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -24);
		person.setTokenDate(cal.getTime());
		persistence.updatePerson(person);

		Cookie cookie = new Cookie("authorization", person.getToken());
		cookie.setDomain("www.ajafit.com.br");
		cookie.setPath("/");
		cookie.setMaxAge(0);
		logger.info("create cookie");
		ResteasyProviderFactory.getContextData(HttpServletResponse.class).addCookie(cookie);

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/person/validation")
	public EntityDTO validation() {

		EntityDTO dto = new EntityDTO();
		Collection<Icon> icons = HashHelper.shuffle(5);

		String codes[] = icons.stream().map((Icon i) -> i.getCode()).collect(Collectors.toList())
				.toArray(new String[0]);
		String label[] = icons.stream().map((Icon i) -> i.getLabel()).collect(Collectors.toList())
				.toArray(new String[0]);

		dto.setIcons(codes);
		int random = new Random().nextInt(label.length - 1);
		dto.setIconTips(label[random]);
		dto.setValidationHash(HashHelper.hash(codes[random]));
		return dto;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/person/register")
	public Response register(EntityDTO dto) {
		try {
			required(dto.getEmail(), dto.getPassword(), dto.getValidationHash(), dto.getValidationChoose());
		} catch (Exception e) {
			EntityDTO resp = new EntityDTO();
			resp.setDescriptions("Parametros invalidos..");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
		String choose = HashHelper.hash(dto.getValidationChoose());
		if (!choose.equals(dto.getValidationHash())) {
			EntityDTO resp = new EntityDTO();
			resp.setDescriptions("Ops.. " + dto.getValidationChoose() + " n�o era a op��o correta..");
			return Response.status(Response.Status.UNAUTHORIZED).entity(resp).build();
		}

		Person person = persistence.getPersonByEmail(dto.getEmail());
		if (person != null) {
			EntityDTO resp = new EntityDTO();
			resp.setDescriptions("Ja existe um usuario cadastrado com este E-mail!");
			return Response.status(Response.Status.UNAUTHORIZED).entity(resp).build();
		}

		person = new Person();
		person.setEmail(dto.getEmail());
		person.setPassword(dto.getPassword());

		person.setEmailValidation(false);

		person = this.persistence.createPerson(person);
		String token = HashHelper.generateToken(person.getId() + "");
		person.setToken(token);
		person.setTokenDate(new Date());

		/* create coachee profile */
		Coachee coachee = new Coachee();
		coachee.setPerson(person);
		coachee = this.persistence.createCouchee(coachee);

		StringBuilder ss = new StringBuilder();
		ss.append("<HTML><BODY>");
		ss.append("Obrigado por se registrar na AjaFit");
		ss.append("<br/>");
		ss.append("Você acaba de dar um grande passo na busca por uma qualidade de vida melhor.");
		ss.append("<br/><br/>");
		ss.append("Agora falta pouco..<br/>Clique no link abaixo e complete a ativação de sua conta..");
		ss.append("<br/>");
		ss.append("<br/>");
		ss.append("<div>");
		ss.append(generateActivationLink(person));
		ss.append("</div>");
		ss.append("<br/>");
		ss.append("<br/>");
		ss.append("<br/>");
		ss.append("AjaFit - Por Uma Vida Saudável");
		ss.append("</BODY></HTML>");
		try {
			SendMailIntegration.send(new MailMessage(person.getEmail(), "AjaFit - Ativação de conta", ss.toString()));
			EntityDTO resp = new EntityDTO();
			resp.setDescriptions("Enviamos um e-mail para: " + person.getEmail()
					+ " com as informações necessárias para a ativação de sua conta!");
			resp.setEmail(person.getEmail());
			return Response.status(Response.Status.ACCEPTED).entity(resp).build();

		} catch (Exception e) {

			EntityDTO resp = new EntityDTO();
			resp.setDescriptions("Tivemos alguns problemas ao enviar o link de ativação para este email: "
					+ person.getEmail()
					+ "\n Você pode tentar repetir o registro ou pode tentar regitrar com uma outra conta de e-mail..");
			resp.setEmail(person.getEmail());
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();

		}

	}

	private String generateActivationLink(Person person) {

		String link = "<a href=\"http://www.ajafit.com.br/ajafit/platform/service/profile/activate/" + person.getToken()
				+ "\">Clique aqui para ativar sua conta AjaFit</a>";
		return link;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/person/login")
	public Response login(EntityDTO dto) {
		try {
			required(dto.getEmail(), dto.getPassword(), dto.getValidationHash(), dto.getValidationChoose());
		} catch (Exception e) {
			EntityDTO resp = new EntityDTO();
			resp.setDescriptions("Parametros invalidos..");
			return Response.status(Response.Status.BAD_REQUEST).entity(resp).build();
		}
		String choose = HashHelper.hash(dto.getValidationChoose());
		if (!choose.equals(dto.getValidationHash())) {
			EntityDTO resp = new EntityDTO();
			resp.setDescriptions("Ops.. " + dto.getValidationChoose() + " n�o era a op��o correta..");
			return Response.status(Response.Status.UNAUTHORIZED).entity(resp).build();
		}
		Person person = persistence.getPersonByEmailAndPassword(dto.getEmail(), dto.getPassword());
		if (person == null || (person.getTemp() != null) && person.getTemp() == true) {
			// throw new WebApplicationException("login or password invalid!!", 401);
			EntityDTO resp = new EntityDTO();
			resp.setDescriptions("E-mail ou Senha Invalidos!!");
			return Response.status(Response.Status.UNAUTHORIZED).entity(resp).build();
		}

		/* caso houver,,,troca carrinho para recem autenticado */
		try {
			String token = AccessFilter.getToken(ResteasyProviderFactory.getContextData(HttpServletRequest.class));
			changeCart(person, token);
		} catch (Exception e) {
			/* nao tem carrinho pra trocar */
			logger.error(e);
		}

		String token = HashHelper.generateToken(person.getId() + "");
		person.setToken(token);
		person.setTokenDate(new Date());
		persistence.updatePerson(person);
		Collection<Profile> profiles = persistence.getProfilesFromPerson(person).stream()
				.filter((Profile p) -> !p.getClass().getSimpleName().equals("Coach")).collect(Collectors.toList());

		Cookie cookie = new Cookie("authorization", person.getToken());
		cookie.setDomain("www.ajafit.com.br");
		cookie.setPath("/");
		cookie.setMaxAge(-1);
		logger.info("create cookie");
		ResteasyProviderFactory.getContextData(HttpServletResponse.class).addCookie(cookie);

		EntityDTO entity = EntityDTOConverter.parse(person, profiles);
		return Response.status(Response.Status.ACCEPTED).entity(entity).build();
	}

	private void changeCart(Person person, String token) {

		/* verifica se tem tmpPerson */
		Person tmpPerson = persistence.getPersonByToken(token);
		if (tmpPerson != null) {
			/* se tmpPErson for igual a person,, nao troca carrinho.. */
			if (tmpPerson.equals(person)) {
				return;
			}

			/* se tem,, verifica se person atual tem perfil de coachee */
			Collection<Profile> col = persistence.getProfilesFromPerson(person);

			Coachee coachee = null;
			try {
				coachee = (Coachee) col.stream().filter((Profile p) -> p.getClass().getSimpleName().equals("Coachee"))
						.findFirst().get();
			} catch (Exception e) {
				/* nao tem coachee! ignorar carrinho atual */
				logger.warn("person: " + person.getEmail()
						+ " nao tem perfil de coachee. descartando carrinho de compras e tmpperson");
			}

			/* coachee assume carrinho de tmpperson */
			Collection<Profile> tmpCol = persistence.getProfilesFromPerson(tmpPerson);
			Coachee tmpCoachee = (Coachee) tmpCol.stream()
					.filter((Profile p) -> p.getClass().getSimpleName().equals("Coachee")).findFirst().get();

			Cart tmpCart = this.persistence.getCartByCoachee(tmpCoachee);
			Collection<Order> tmpOrders = this.persistence.getOrdersByCart(tmpCart);
			Collection<CouponUsage> oldCouponUsages = tmpOrders.stream().map((Order o) -> o.getCouponUsage())
					.collect(Collectors.toList());

			if (coachee != null) {

				/* limpar se houve, cart not done e orders de authperson */
				Cart cart = persistence.getCartByCoachee(coachee);
				if (cart != null) {
					Collection<Order> orders = this.persistence.getOrdersByCart(cart);
					Set<CouponUsage> set = orders.stream().map((Order o) -> o.getCouponUsage())
							.collect(Collectors.toSet());
					orders.stream().forEach((Order o) -> persistence.removeOrder(o));

					set.stream().filter((CouponUsage c) -> persistence.getOrdersByCouponUsage(c).isEmpty())
							.forEach((CouponUsage c) -> persistence.removeCouponUsage(c));

					/* limpa o delivery do cart */
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
					// persistence.getCouponUsageById(id)
				}

				/* troca carrinnho de tmpperson pra auth person */

				final Coachee finalCoachee = coachee;
				tmpOrders.stream().forEach((Order o) -> o
						.setCouponUsage(persistence.createOrGetCouponUsage(mountCouponUsage(o, finalCoachee))));
				oldCouponUsages.stream().forEach((CouponUsage c) -> persistence.removeCouponUsage(c));
				tmpOrders.stream().forEach((Order o) -> persistence.updateOrder(o));
			} else {
				tmpOrders.stream().forEach((Order o) -> persistence.removeOrder(o));
				persistence.removeCart(tmpCart);
			}

			this.persistence.removeCoachee(tmpCoachee);
			this.persistence.removePerson(tmpPerson);

			logger.info("carrinho trocado..");

		}

	}

	private static CouponUsage mountCouponUsage(Order o, Coachee coachee) {
		CouponUsage c = new CouponUsage(new CouponUsagePK(o.getCouponUsage().getId().getCoupon(), coachee),
				o.getCouponUsage().getDate(), new ArrayList<>());
		return c;
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/person/update")
	public EntityDTO personUpdate(EntityDTO dto) {
		required(dto.getId(), dto.getAccessToken());
		validatePermissions(dto.getId(), dto.getAccessToken());

		Person person = persistence.getPersonById(dto.getId());
		person.setName(dto.getName() != null ? dto.getName() : person.getName());
		person.setEmail(dto.getEmail() != null ? dto.getEmail() : person.getEmail());

		person = persistence.updatePerson(person);

		Collection<Profile> profiles = persistence.getProfilesFromPerson(person);
		return EntityDTOConverter.parse(person, profiles);

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/persons")
	public Collection<EntityDTO> persons() {
		ArrayList<EntityDTO> list = new ArrayList<>();
		persistence.filterPersons()
				.forEach((Person p) -> list.add(EntityDTOConverter.parse(p, new ArrayList<Profile>())));
		return list;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/persons/{filter}")
	public Collection<EntityDTO> persons(@PathParam(value = "filter") String filter) {
		ArrayList<EntityDTO> list = new ArrayList<>();
		persistence.filterPersons(filter)
				.forEach((Person p) -> list.add(EntityDTOConverter.parse(p, new ArrayList<Profile>())));
		return list;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/coachee/create")
	public EntityDTO createCoachee(EntityDTO dto) {
		// System.out.println("bateu aki.." + filter);

		Person person = new Person();
		person.setRegister(new Date());
		person.setAuthType(PersonAuthType.LOCAL);
		person.setBirth(new Date());
		person.setEmail("alexandre.deassis@yahoo.com");
		person.setGender(PersonGender.M);
		person.setName("Alexandre de Assis");
		person.setPassword("alexandre123");
		person.setEmailValidation(true);

		person = persistence.createPerson(person);
		Region region = persistence.findRegionById(7);

		Manager manager = new Manager();
		manager.setPerson(person);
		manager.setManager(null);
		// return persistence.createManager(manager);
		return null;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/manager/{filter}")
	public Collection<Manager> filterManagers(@PathParam("filter") String filter) {

		return persistence.filterManager(filter);

	}
}
