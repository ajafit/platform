package br.com.ajafit.platform.core.service;

import java.util.Collection;
import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

import br.com.ajafit.platform.core.domain.Manager;
import br.com.ajafit.platform.core.domain.Person;
import br.com.ajafit.platform.core.domain.PersonAuthType;
import br.com.ajafit.platform.core.domain.PersonGender;
import br.com.ajafit.platform.core.domain.Region;
import br.com.ajafit.platform.core.persistence.RatePersistence;
import br.com.ajafit.platform.core.service.dto.EntityDTO;

@Path("/service/profile")
@Stateless
public class ProfileService extends ServiceValidation {

	private Logger logger = Logger.getLogger(ProfileService.class);

	@EJB
	protected RatePersistence persistence;

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
	@Path("/manager/create")
	public Manager createManager() {
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
		return persistence.createManager(manager);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/manager/{filter}")
	public Collection<Manager> filterManagers(@PathParam("filter") String filter) {

		return persistence.filterManager(filter);

	}
}
