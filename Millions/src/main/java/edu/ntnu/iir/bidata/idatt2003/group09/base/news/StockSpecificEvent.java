package edu.ntnu.iir.bidata.idatt2003.group09.base.news;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class StockSpecificEvent {

	private final Event baseEvent;
	private final Map<String, BigDecimal> stockImpactData;

	public StockSpecificEvent(String headline, String description) {
		this.baseEvent = new Event(headline, description);
		this.stockImpactData = new HashMap<>();
	}

	public void addStockImpact(String symbol, BigDecimal impact) {
		if (symbol == null || symbol.isBlank()) {
			throw new IllegalArgumentException("Symbol cannot be null or empty");
		}
		if (impact == null) {
			throw new IllegalArgumentException("Impact cannot be null");
		}

		stockImpactData.put(symbol, impact);
	}

	public BigDecimal getImpactForStock(String symbol) {
		if (symbol == null || symbol.isBlank()) {
			return BigDecimal.ZERO;
		}
		return stockImpactData.getOrDefault(symbol, BigDecimal.ZERO);
	}

	public boolean affectsStock(String symbol) {
		return symbol != null && !symbol.isBlank() && stockImpactData.containsKey(symbol);
	}

	public Map<String, BigDecimal> getStockImpactData() {
		return Collections.unmodifiableMap(stockImpactData);
	}

	public Event getBaseEvent() {
		return baseEvent;
	}
}
