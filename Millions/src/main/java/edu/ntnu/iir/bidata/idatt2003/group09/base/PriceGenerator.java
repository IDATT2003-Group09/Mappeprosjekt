package edu.ntnu.iir.bidata.idatt2003.group09.base;

import java.math.BigDecimal;
import java.math.RoundingMode;

import edu.ntnu.iir.bidata.idatt2003.group09.base.news.NewsPaper;

public class PriceGenerator {

	private static final BigDecimal MINIMUM_STOCK_PRICE = BigDecimal.valueOf(0.01);

	private PriceGenerator() {
	}

	public static BigDecimal nextWeekPrice(Stock stock, NewsPaper newsPaper) {
		if (stock == null) {
			throw new IllegalArgumentException("Stock cannot be null");
		}

		BigDecimal currentPrice = stock.getSalesPrice();
		BigDecimal eventImpact = BigDecimal.ZERO;

		if (newsPaper != null) {
			eventImpact = newsPaper.getImpactForStock(stock);
		}

		if (eventImpact.compareTo(BigDecimal.ZERO) != 0) {
			BigDecimal riskFactor = BigDecimal.valueOf(stock.getRisk())
					.divide(BigDecimal.valueOf(4), 2, RoundingMode.HALF_UP);
			eventImpact = eventImpact.multiply(riskFactor);
		}

		BigDecimal newPrice = currentPrice.multiply(BigDecimal.ONE.add(eventImpact))
				.setScale(6, RoundingMode.HALF_UP);

		if (newPrice.compareTo(MINIMUM_STOCK_PRICE) < 0) {
			return MINIMUM_STOCK_PRICE;
		}

		return newPrice;
	}
}
