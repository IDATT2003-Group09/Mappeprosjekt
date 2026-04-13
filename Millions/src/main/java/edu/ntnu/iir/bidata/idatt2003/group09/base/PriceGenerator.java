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

        double baseNoise = 0.01;
        double scaledNoise = baseNoise * (stock.getRisk() / 5.0);

        double rand = java.util.concurrent.ThreadLocalRandom.current()
                .nextDouble(-scaledNoise * 0.7, scaledNoise);

        eventImpact = eventImpact.add(BigDecimal.valueOf(rand));

        if (newsPaper != null) {
            BigDecimal marketImpact = newsPaper.getGlobalEvent().getImpactForSector(stock.getSector());

            BigDecimal stockImpact = newsPaper.getImpactForStock(stock);

            eventImpact = eventImpact.add(marketImpact).add(stockImpact);
        }

        BigDecimal riskFactor = BigDecimal.valueOf(1)
                .add(BigDecimal.valueOf(stock.getRisk()).multiply(BigDecimal.valueOf(0.1)));

        eventImpact = eventImpact.multiply(riskFactor);

        BigDecimal newPrice = currentPrice.multiply(BigDecimal.ONE.add(eventImpact))
                .setScale(6, RoundingMode.HALF_UP);

        if (newPrice.compareTo(MINIMUM_STOCK_PRICE) < 0) {
            return MINIMUM_STOCK_PRICE;
        }

        return newPrice;
    }
}
