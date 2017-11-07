package br.com.ajafit.platform.core.service;

import java.util.Collection;
import java.util.LinkedList;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

import br.com.ajafit.platform.core.domain.Product;
import br.com.ajafit.platform.core.domain.Review;
import br.com.ajafit.platform.core.domain.Saleable;
import br.com.ajafit.platform.core.domain.ScreenConfig;
import br.com.ajafit.platform.core.service.dto.EntityDTOConverter;
import br.com.ajafit.platform.core.service.dto.ScreenItemDTO;

@Path("/service/screen")
@Stateless
public class ScreenService extends ServiceValidation {

	private Logger logger = Logger.getLogger(ScreenService.class);

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/list/{code}")
	public Collection<ScreenItemDTO> list(@PathParam("code") String code) {
		required(code);
		LinkedList<ScreenItemDTO> list = new LinkedList<>();
		Collection<ScreenConfig> screenConfigs = this.persistence.findCouponsByScreenCode(code);
		screenConfigs.stream().forEach((ScreenConfig c) -> list.add(EntityDTOConverter.parse(c)));
		return list;

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/detail/{screen}/{coupon}")
	public ScreenItemDTO detail(@PathParam("screen") Long screen, @PathParam("coupon") Long coupon) {
		required(screen, coupon);
		ScreenConfig screenConfig = this.persistence.getScreenConfigById(screen, coupon);
		ScreenItemDTO dto = EntityDTOConverter.parse(screenConfig);

		for (ScreenItemDTO item : dto.getItems()) {
			Collection<Review> rvs = persistence.getReviews(item.getItemId());
			item.setReviews(EntityDTOConverter.parse(rvs));
		}
		Saleable saleable = screenConfig.getId().getCoupon().getKit().getItems().iterator().next().getId()
				.getSaleable();
		if (saleable instanceof Product) {
			Product prod = (Product) saleable;
			Collection<ScreenConfig> col = this.persistence.findScreenConfigByProductType(prod.getType());
			LinkedList<ScreenItemDTO> list = new LinkedList<>();
			col.stream().forEach((ScreenConfig c) -> list.add(EntityDTOConverter.parse(c)));
			dto.setRelated(list.toArray(new ScreenItemDTO[0]));
			for (ScreenItemDTO i : dto.getRelated()) {
				logger.info("pqp:" + i.getName());
			}

		}
	
		return dto;

	}

}
