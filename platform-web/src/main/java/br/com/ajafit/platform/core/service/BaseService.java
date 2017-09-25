package br.com.ajafit.platform.core.service;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.ajafit.platform.core.domain.Region;
import br.com.ajafit.platform.core.persistence.RatePersistence;

@Path("/service")
@Stateless
public class BaseService {

	@EJB
	protected RatePersistence persistence;

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/check")
	public String check() {

		return "ok";
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/regions/{filter}")
	public Collection<Region> regions(@PathParam("filter") String filter) {
		System.out.println("bateu aki.." + filter);
		return persistence.filterRegion(filter);
	}
}
