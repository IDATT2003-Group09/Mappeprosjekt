package edu.ntnu.iir.bidata.idatt2003.group09.base;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import static java.lang.Math.*;
import edu.ntnu.iir.bidata.idatt2003.group09.base.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.base.MarketNews;

public class PriceGenerator {
	private static final double BASE_VOLATILITY = 0.05; // baseline weekly stddev for risk=1
	private static final double VOLATILITY_STEP = 0.05; // extra stddev per risk level
	private static final double BASE_EXPECTED_GROWTH = 0.05; // baseline weekly expected growth (0.1%)
	private static final Random RANDOM = new Random();

	/**
	 * Generates the next week's price for a stock using a lognormal model.
	 * @param stock The stock to generate the price for
	 * @return The next week's price as BigDecimal
	 */
	public static BigDecimal nextWeekPrice(Stock stock) {
		BigDecimal lastPrice = stock.getSalesPrice();
		int risk = stock.getRisk();

		// Volatility (stddev) increases with risk
		double volatility = BASE_VOLATILITY + (risk - 1) * VOLATILITY_STEP;
		// Expected growth, can be tuned per your model
		double expectedGrowth = BASE_EXPECTED_GROWTH;

		// Volatility drag: geometric Brownian motion drift adjustment
		double drift = expectedGrowth - 0.5 * volatility * volatility;

		// Generate a standard normal random value
		double z = RANDOM.nextGaussian();

		// Calculate next price using lognormal model
		double last = lastPrice.doubleValue();
		double next = last * exp(drift + volatility * z);

		// Ensure price is at least 0.01
		next = max(next, 0.01);

		return BigDecimal.valueOf(next).setScale(2, RoundingMode.HALF_UP);
	}

	public static BigDecimal nextWeekPriceWithNews(Stock stock, MarketNews news) {
		nextWeekPrice(stock); // Get base price without news
		if (news != null) {
			BigDecimal impact = news.getImpactForSector(stock.getSector());
			if (impact != null) {
				BigDecimal lastPrice = stock.getSalesPrice();
				BigDecimal newsAdjustedPrice = lastPrice.multiply(BigDecimal.ONE.add(impact));
				return newsAdjustedPrice.setScale(2, RoundingMode.HALF_UP);
			}
		}
		return nextWeekPrice(stock); // Return base price if no news impact
	}

}
