package edu.ntnu.iir.bidata.idatt2003.group09.base.news;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class StockSpecificEvent extends Event {

  private final String partialHeadline;
  private final String partialDescription;
  private final Map<String, BigDecimal> stockImpactData;
  private BigDecimal defaultImpact;

	public StockSpecificEvent(String partialHeadline, String partialDescription) {
		this(partialHeadline, partialDescription, BigDecimal.ZERO);
	}

	public StockSpecificEvent(String partialHeadline, String partialDescription, BigDecimal defaultImpact) {
		super(partialHeadline, partialDescription);
		this.partialHeadline = partialHeadline;
		this.partialDescription = partialDescription;
		this.stockImpactData = new HashMap<>();
		setDefaultImpact(defaultImpact);
	}

  public void setDefaultImpact(BigDecimal defaultImpact) {
    if (defaultImpact == null) {
      throw new IllegalArgumentException("Default impact cannot be null");
    }
    this.defaultImpact = defaultImpact;
  }

  public BigDecimal getDefaultImpact() {
    return defaultImpact;
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
      return defaultImpact;
    }
    return stockImpactData.getOrDefault(symbol, defaultImpact);
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
