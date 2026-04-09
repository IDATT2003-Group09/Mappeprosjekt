package edu.ntnu.iir.bidata.idatt2003.group09.base.news;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class StockSpecificEvent extends Event {

  private final String partialHeadline;
  private final String partialDescription;
  private final Map<String, BigDecimal> stockImpactData;

	public StockSpecificEvent(String partialHeadline, String partialDescription) {
		super(partialHeadline, partialDescription);
		this.partialHeadline = partialHeadline;
		this.partialDescription = partialDescription;
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

  public Map<String, BigDecimal> getStockImpactData() {
    return Collections.unmodifiableMap(stockImpactData);
  }

  public String generateFullHeadline(String stockName) {
    return partialHeadline.replace("{stock}", stockName);
  }

  public String generateFullDescription(String stockName) {
    return partialDescription.replace("{stock}", stockName);
  }

}
