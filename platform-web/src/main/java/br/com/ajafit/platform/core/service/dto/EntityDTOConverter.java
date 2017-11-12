package br.com.ajafit.platform.core.service.dto;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;

import br.com.ajafit.platform.core.domain.Coupon;
import br.com.ajafit.platform.core.domain.Item;
import br.com.ajafit.platform.core.domain.Order;
import br.com.ajafit.platform.core.domain.Person;
import br.com.ajafit.platform.core.domain.Product;
import br.com.ajafit.platform.core.domain.ProductNutrition;
import br.com.ajafit.platform.core.domain.Profile;
import br.com.ajafit.platform.core.domain.Region;
import br.com.ajafit.platform.core.domain.Review;
import br.com.ajafit.platform.core.domain.Saleable;
import br.com.ajafit.platform.core.domain.ScreenConfig;

public class EntityDTOConverter {
	private static Logger logger = Logger.getLogger(EntityDTOConverter.class);

	public static EntityDTO parse(Person person, Collection<Profile> profiles) {
		EntityDTO dto = new EntityDTO();
		dto.setId(person.getId());
		dto.setName(person.getName());
		dto.setDate(person.getRegister());
		dto.setEmail(person.getEmail());
		if (!profiles.isEmpty()) {
			List<String> list = profiles.stream().map((Profile p) -> p.getClass().getSimpleName())
					.collect(Collectors.toList());
			dto.setProfiles(list.toArray(new String[0]));
		}

		return dto;
	}

	public static RegionDTO parse(Region region, boolean fillInner, int level) {

		RegionDTO dto = new RegionDTO();
		dto.setDescriptions(region.getDescriptions());
		dto.setRegionId(region.getId());
		dto.setLevel(level);
		if (region.getOuter() != null) {
			dto.setOuter(parse(region.getOuter(), false, level - 1));
		}

		if (fillInner) {
			region.getInner().sort(new Comparator<Region>() {
				public int compare(Region o1, Region o2) {
					return o1.getDescriptions().compareTo(o2.getDescriptions());
				}

			});
			for (Region r : region.getInner()) {
				dto.getInner().add(parse(r, true, level + 1));
			}
		}

		return dto;

	}

	public static ScreenItemDTO parse(Order order) {
		ScreenItemDTO dto = new ScreenItemDTO();
		dto.setItemId(order.getId());
		Coupon coupon = order.getCouponUsage().getId().getCoupon();
		dto = fillImagesFromCoupon(coupon, dto);
		dto.setName(getNameFromCoupon(coupon));
		dto.setDescriptions(getDescriptionsFromCoupon(coupon));
		dto.setAmount(order.getAmount());
		int value[] = MoneyHelper.calculate(coupon);
		dto.setValue(MoneyHelper.toString(value[0]));
		dto.setValueFinal(value.length == 2 ? MoneyHelper.toString(value[1]) : null);
		return dto;

	}

	public static ScreenItemDTO parse(ScreenConfig screenConfig) {

		Coupon coupon = screenConfig.getId().getCoupon();

		ScreenItemDTO dto = new ScreenItemDTO();

		/* se fizer isso vai ser baca!! */
		// dto.setItemId(coupon.getKit().getItems().iterator().next().getId().getSaleable().getId());

		dto.setCouponId(coupon.getId());
		dto.setScreenId(screenConfig.getId().getScreen().getId());
		dto.setAmountToGetOneFree(coupon.getAmountToGetOneFree() == 0 ? null : coupon.getAmountToGetOneFree());

		/* setting name and descriptions */
		if (coupon.getKit().getName() == null) {
			String name = getNameFromCoupon(coupon);
			String desc = getDescriptionsFromCoupon(coupon);
			dto.setName(name);
			dto.setDescriptions(desc);

		} else {
			dto.setName(coupon.getKit().getName());
			dto.setDescriptions(coupon.getKit().getDescriptions());
		}

		/* setting images and video */
		dto = fillImagesFromCoupon(coupon, dto);

		/* setting value and priority */
		int value[] = MoneyHelper.calculate(coupon);
		dto.setValue(MoneyHelper.toString(value[0]));
		dto.setValueFinal(value.length == 2 ? MoneyHelper.toString(value[1]) : null);
		dto.setPriority(screenConfig.getPriority());

		/* setting items */
		LinkedList<ScreenItemDTO> items = new LinkedList<>();
		coupon.getKit().getItems().stream().forEach((Item i) -> items.add(parse(i)));
		dto.setItems(items.toArray(new ScreenItemDTO[0]));

		return dto;
	}

	private static ScreenItemDTO fillImagesFromCoupon(Coupon coupon, ScreenItemDTO dto) {
		if (coupon.getKit().getImage1() == null) {
			dto.setImageLarge(coupon.getKit().getItems().iterator().next().getId().getSaleable().getImageLarge());
			dto.setImage1(coupon.getKit().getItems().iterator().next().getId().getSaleable().getImage1());
			dto.setImage2(coupon.getKit().getItems().iterator().next().getId().getSaleable().getImage2());
			dto.setImage3(coupon.getKit().getItems().iterator().next().getId().getSaleable().getImage3());
			dto.setVideo(coupon.getKit().getItems().iterator().next().getId().getSaleable().getVideo());
		} else {

			dto.setImageLarge(coupon.getKit().getImageLarge());
			dto.setImage1(coupon.getKit().getImage1());
			dto.setImage2(coupon.getKit().getImage2());
			dto.setImage3(coupon.getKit().getImage3());
			dto.setVideo(coupon.getKit().getVideo());
		}
		return dto;
	}

	private static Collection<NutritionInfoDTO> parseNutritionInfos(Saleable saleable) {
		LinkedList<NutritionInfoDTO> col = new LinkedList<>();
		Product prod = (Product) saleable;
		if (!prod.getProductNutritions().isEmpty()) {
			prod.getProductNutritions().stream().forEach((ProductNutrition p) -> col.add(mount(p)));
		}

		return col;
	}

	private static NutritionInfoDTO mount(ProductNutrition productNutrition) {
		NutritionInfoDTO dto = new NutritionInfoDTO();
		dto.setKey(productNutrition.getNutritionTable().getName());
		dto.setValue(productNutrition.getValue() + productNutrition.getNutritionTable().getUnity());
		dto.setVd((productNutrition.getVd() == null) ? null : MoneyHelper.toString(productNutrition.getVd()) + "%");
		return dto;

	}

	private static String getNameFromCoupon(Coupon coupon) {
		Collection<Item> items = coupon.getKit().getItems();

		StringBuilder bf = new StringBuilder();
		String delimiter = items.size() > 1 ? "|" : "";
		// long count = items.stream().map((Item i) ->
		// i.getId().getSaleable().getName()).count();
		long distinct = items.stream().map((Item i) -> i.getId().getSaleable().getName()).distinct().count();
		if (distinct > 1) {
			items.stream().forEach((Item i) -> bf.append(i.getId().getSaleable().getName() + delimiter));
		} else {
			String name = items.stream().findFirst().get().getId().getSaleable().getName();
			// bf.append(items.stream().findFirst().get().getAmount());
			// bf.append(" ");
			bf.append(name);
		}

		return bf.toString();
	}

	public static void main(String[] args) {
		String[] vet = { "alexandre", "de", "alexandre", "eh", "foda" };
		Arrays.asList(vet).stream().forEach((String s) -> System.out.println(s));
		long c = Arrays.asList(vet).stream().map((String s) -> s).count();
		long d = Arrays.asList(vet).stream().map((String s) -> s).distinct().count();
		System.err.println("test" + c + d);
	}

	private static String getDescriptionsFromCoupon(Coupon coupon) {
		Collection<Item> items = coupon.getKit().getItems();
		StringBuilder bf = new StringBuilder();
		String delimiter = items.size() > 1 ? "|" : "";
		items.stream().forEach((Item i) -> bf.append(i.getId().getSaleable().getDescriptions() + delimiter));
		return bf.toString();
	}

	public static ScreenItemDTO parse(Item item) {
		ScreenItemDTO dto = new ScreenItemDTO();
		dto.setItemId(item.getId().getSaleable().getId());
		dto.setAmount(item.getAmount());
		dto.setValue(MoneyHelper.toString(item.getId().getSaleable().getCost()));
		dto.setName(item.getId().getSaleable().getName());
		dto.setDescriptions(item.getId().getSaleable().getDescriptions());

		dto.setImageLarge(item.getId().getSaleable().getImageLarge());
		dto.setImage1(item.getId().getSaleable().getImage1());
		dto.setImage2(item.getId().getSaleable().getImage2());
		dto.setImage3(item.getId().getSaleable().getImage3());
		dto.setVideo(item.getId().getSaleable().getVideo());

		/* nutrition info */
		if (item.getId().getSaleable() instanceof Product) {
			Product prod = (Product) item.getId().getSaleable();
			dto.setNutritionInfo(parseNutritionInfos(prod).toArray(new NutritionInfoDTO[0]));
			dto.setIngredients(prod.getIngredients());
		}

		return dto;
	}

	public static ReviewDTO[] parse(Collection<Review> rvs) {

		LinkedList<ReviewDTO> col = new LinkedList<>();
		rvs.stream().forEach((Review r) -> col.add(mount(r)));
		return col.toArray(new ReviewDTO[0]);
	}

	private static ReviewDTO mount(Review r) {
		ReviewDTO dto = new ReviewDTO();
		dto.setProfileId(r.getId().getProfile().getId());
		dto.setSaleableId(r.getId().getSaleable().getId());
		dto.setRate(r.getRate());
		dto.setValue(r.getValue());
		dto.setProfileName(r.getId().getProfile().getPerson().getName());
		dto.setProfileImage(r.getId().getProfile().getPerson().getImage());
		dto.setDate(new SimpleDateFormat("dd/MMM/YYYY").format(r.getDate()));
		return dto;
	}

}
