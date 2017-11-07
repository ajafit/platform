package br.com.ajafit.platform.core.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

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
import br.com.ajafit.platform.core.persistence.RatePersistence;
import br.com.ajafit.platform.core.service.dto.EntityDTOConverter;
import br.com.ajafit.platform.core.service.dto.MoneyHelper;
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

		int mainTotalRate = 0;
		int mainTotalReview = 0;
		for (ScreenItemDTO item : dto.getItems()) {
			Collection<Review> rvs = persistence.getReviews(item.getItemId());
			if (!rvs.isEmpty()) {
				item.setReviews(EntityDTOConverter.parse(rvs));

				mainTotalRate += rvs.stream().map((Review r) -> r.getRate()).reduce(Integer::sum).get();
				mainTotalReview += rvs.size();
			}

		}
		

		if (mainTotalRate > 0) {
			int evaluate = ((mainTotalRate * 10) / mainTotalReview);
			dto.setRate(MoneyHelper.toString(evaluate, 1));
			dto.setTotalReviews(mainTotalReview);
		}

		Saleable saleable = screenConfig.getId().getCoupon().getKit().getItems().iterator().next().getId()
				.getSaleable();
		if (saleable instanceof Product) {
			Product prod = (Product) saleable;
			Collection<ScreenConfig> col = this.persistence.findScreenConfigByProductType(prod.getType());

			LinkedList<ScreenItemDTO> list = new LinkedList<>();
			col.stream().filter((ScreenConfig c) -> !(c.equals(screenConfig)))
					.forEach((ScreenConfig c) -> list.add(EntityDTOConverter.parse(c)));

			// list.stream().forEach((ScreenItemDTO ss) -> fillRates(ss, persistence));
			for (ScreenItemDTO screenItemDTO : list) {

				int totalRate = 0;
				int totalReview = 0;
				for (ScreenItemDTO item : screenItemDTO.getItems()) {
					Collection<Review> rvs = persistence.getReviews(item.getItemId());
					if (!rvs.isEmpty()) {
						item.setReviews(EntityDTOConverter.parse(rvs));

						totalRate += rvs.stream().map((Review r) -> r.getRate()).reduce(Integer::sum).get();
						totalReview += rvs.size();
					}
				}
				if (totalRate > 0) {
					int evaluate = ((totalRate * 10) / totalReview);
					screenItemDTO.setRate(MoneyHelper.toString(evaluate, 1));
				}

			}

			dto.setRelated(list.toArray(new ScreenItemDTO[0]));

		}

		return dto;

	}

	public static void fillRates(ScreenItemDTO dto, RatePersistence persistence) {
		Collection<Review> reviews;
		if (dto.getItemId() == null) {
			Collection<Long> col = Arrays.asList(dto.getItems()).stream().map((ScreenItemDTO ss) -> ss.getItemId())
					.collect(Collectors.toList());
			reviews = persistence.getReviews(col);
		} else {

			reviews = persistence.getReviews(dto.getItemId());
		}

		if (!reviews.isEmpty()) {
			int totalRate = reviews.stream().map((Review r) -> r.getRate()).reduce(Integer::sum).get();
			int evaluate = ((totalRate * 10) / (reviews.size()));
			dto.setRate(MoneyHelper.toString(evaluate, 1));
		}
	}

}
