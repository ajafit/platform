package br.com.ajafit.platform.core.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
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

import org.jboss.logging.Logger;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import br.com.ajafit.platform.core.domain.Manager;
import br.com.ajafit.platform.core.domain.Person;
import br.com.ajafit.platform.core.domain.PersonAuthType;
import br.com.ajafit.platform.core.domain.PersonGender;
import br.com.ajafit.platform.core.domain.Profile;
import br.com.ajafit.platform.core.domain.Region;
import br.com.ajafit.platform.core.service.dto.EntityDTO;
import br.com.ajafit.platform.core.service.dto.EntityDTOConverter;
import br.com.ajafit.platform.core.service.dto.HashHelper;

@Path("/service/profile")
@Stateless
public class ProfileService extends ServiceValidation {

	private Logger logger = Logger.getLogger(ProfileService.class);

	@Inject
	private ServletContext context;

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

	@POST
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/person/register")
	public EntityDTO personRegister(EntityDTO dto) {

		required(dto.getName(), dto.getEmail(), dto.getValidation());
		captcha(dto.getValidation(), dto.getValidationIMGReference());

		Person person = new Person();
		person.setName(dto.getName());
		person.setEmail(dto.getEmail());
		person.setRegister(new Date());
		person.setAuthType(PersonAuthType.LOCAL);

		person = persistence.createPerson(person);
		logger.info("register " + person);

		dto.setId(person.getId());

		return dto;
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
		cookie.setDomain("localhost");
		cookie.setPath("/");
		cookie.setMaxAge(0);
		logger.info("create cookie");
		ResteasyProviderFactory.getContextData(HttpServletResponse.class).addCookie(cookie);

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/person/login")
	public EntityDTO login(EntityDTO dto) {
		required(dto.getEmail(), dto.getPassword());
		Person person = persistence.getPersonByEmailAndPassword(dto.getEmail(), dto.getPassword());
		if (person == null || (person.getTemp() != null) && person.getTemp() == true) {
			throw new WebApplicationException("login or password invalid!!", 401);
		}
		String token = HashHelper.generateToken(person.getId() + "");
		person.setToken(token);
		person.setTokenDate(new Date());
		persistence.updatePerson(person);
		Collection<Profile> profiles = persistence.getProfilesFromPerson(person).stream()
				.filter((Profile p) -> !p.getClass().getSimpleName().equals("Coach")).collect(Collectors.toList());

		Cookie cookie = new Cookie("authorization", person.getToken());
		cookie.setDomain("localhost");
		cookie.setPath("/");
		cookie.setMaxAge(-1);
		logger.info("create cookie");
		ResteasyProviderFactory.getContextData(HttpServletResponse.class).addCookie(cookie);

		return EntityDTOConverter.parse(person, profiles);
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
		manager.setRegion(region);
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
