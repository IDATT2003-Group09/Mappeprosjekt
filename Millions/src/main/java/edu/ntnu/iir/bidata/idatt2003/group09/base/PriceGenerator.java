package edu.ntnu.iir.bidata.idatt2003.group09.base;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class PriceGenerator {

	private static final Random RANDOM = new Random();

	private PriceGenerator() {
	}

	public static BigDecimal nextWeekPrice(Stock stock) {
		if (stock == null) {
			throw new IllegalArgumentException("Stock cannot be null");
		}

		BigDecimal currentPrice = stock.getSalesPrice();
		double riskScale = Math.max(1, stock.getRisk()) / 7.0;
		double randomChange = RANDOM.nextGaussian() * (0.015 + (0.035 * riskScale));

		if (randomChange > 0.25) {
			randomChange = 0.25;
		} else if (randomChange < -0.25) {
			randomChange = -0.25;
		}

		BigDecimal multiplier = BigDecimal.ONE.add(BigDecimal.valueOf(randomChange));
		BigDecimal newPrice = currentPrice.multiply(multiplier).setScale(6, RoundingMode.HALF_UP);

		if (newPrice.compareTo(BigDecimal.valueOf(0.01)) < 0) {
			return BigDecimal.valueOf(0.01);
		}

		return newPrice;
	}
}
