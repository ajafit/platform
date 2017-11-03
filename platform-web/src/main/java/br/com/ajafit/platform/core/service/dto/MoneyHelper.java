package br.com.ajafit.platform.core.service.dto;

import org.jboss.logging.Logger;

import br.com.ajafit.platform.core.domain.Coupon;
import br.com.ajafit.platform.core.domain.Item;

public class MoneyHelper {

	private static Logger logger = Logger.getLogger(MoneyHelper.class);

	public static String toString(int value) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		sb.append(value);
		if (sb.length() < 3) {
			throw new RuntimeException("invalid money value: " + value);
		}

		sb.insert(sb.length() - 2, ',');
		return sb.toString();
	}

	public static int[] calculate(Coupon coupon) {

		int sumItemsCost = coupon.getKit().getItems().stream().mapToInt((Item i) -> i.getId().getSaleable().getCost() * i.getAmount())
				.sum();
		int sumMaxItemsRevenueShare = coupon.getKit().getItems().stream()
				.mapToInt((Item i) -> i.getId().getSaleable().getRevenueShare() * i.getAmount()).sum();

		int resp[];

		if (coupon.getDiscount() == 0) {
			resp = new int[1];
			resp[0] = sumItemsCost;
		} else if (coupon.getDiscount() < sumMaxItemsRevenueShare) {
			resp = new int[2];
			resp[0] = sumItemsCost;
			resp[1] = (sumItemsCost - coupon.getDiscount());
		} else {
			resp = new int[1];
			resp[0] = sumItemsCost;
			logger.error("coupon configurado com desconto maior que revenue share.. nao aplicando desconto algum: "
					+ coupon);
		}

		return resp;

	}

	public static void main(String[] args) {

		for (int i = 145; i < 10000; i += 1090)
			System.err.println(toString(i));
	}
}
