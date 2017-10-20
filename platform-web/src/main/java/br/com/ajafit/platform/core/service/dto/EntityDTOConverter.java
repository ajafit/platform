package br.com.ajafit.platform.core.service.dto;

import java.util.Collection;
import java.util.LinkedList;

import org.jboss.logging.Logger;

import br.com.ajafit.platform.core.domain.Coupon;
import br.com.ajafit.platform.core.domain.Item;
import br.com.ajafit.platform.core.domain.Person;
import br.com.ajafit.platform.core.domain.ScreenConfig;

public class EntityDTOConverter {
	private static Logger logger = Logger.getLogger(EntityDTOConverter.class);

	public static EntityDTO parse(Person person) {
		EntityDTO dto = new EntityDTO();
		dto.setId(person.getId());
		dto.setName(person.getName());
		dto.setDate(person.getRegister());
		dto.setEmail(person.getEmail());
		return dto;
	}

	public static ScreenItemDTO parse(ScreenConfig screenConfig) {

		Coupon coupon = screenConfig.getId().getCoupon();

		ScreenItemDTO dto = new ScreenItemDTO();
		dto.setId(coupon.getId());
		if (coupon.getKit().getDescriptions() == null) {
			String desc = getNameFromItems(coupon.getKit().getItems());
			dto.setDescriptions(desc);

		} else {
			dto.setDescriptions(coupon.getKit().getDescriptions());
		}
		if (coupon.getKit().getImage() == null) {
			dto.setImage(coupon.getKit().getItems().iterator().next().getId().getSaleable().getImage());
		} else {
			dto.setImage(coupon.getKit().getImage());
		}

		LinkedList<ScreenItemDTO> items = new LinkedList<>();

		int value[] = MoneyHelper.calculate(coupon);
		dto.setValue(MoneyHelper.toString(value[0]));
		dto.setValueFinal(value.length == 2 ? MoneyHelper.toString(value[1]) : null);

		dto.setPriority(screenConfig.getPriority());
		coupon.getKit().getItems().stream().forEach((Item i) -> items.add(parse(i)));

		dto.setItems(items.toArray(new ScreenItemDTO[0]));
		return dto;
	}

	private static String getNameFromItems(Collection<Item> items) {
		StringBuilder bf = new StringBuilder();
		String delimiter = items.size() > 1 ? "|" : "";
		items.stream().forEach((Item i) -> bf.append(i.getId().getSaleable().getName() + delimiter));
		return bf.toString();
	}

	public static ScreenItemDTO parse(Item item) {
		ScreenItemDTO dto = new ScreenItemDTO();
		dto.setId(item.getId().getSaleable().getId());
		dto.setAmount(item.getAmount());
		dto.setValue(MoneyHelper.toString(item.getId().getSaleable().getCost()));
		dto.setDescriptions(item.getId().getSaleable().getDescriptions());
		dto.setImage(item.getId().getSaleable().getImage());
		return dto;
	}
}
