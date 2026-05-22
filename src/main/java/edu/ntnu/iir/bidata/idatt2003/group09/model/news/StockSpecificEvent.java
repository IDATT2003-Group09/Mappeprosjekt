package edu.ntnu.iir.bidata.idatt2003.group09.model.news;

import java.math.BigDecimal;

import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;

public class StockSpecificEvent extends Event {

  private final String partialHeadline;
  private final String partialDescription;
  private BigDecimal defaultImpact;
  private String targetSymbol;
  private String generatedHeadline;
  private String generatedDescription;

	public StockSpecificEvent(String partialHeadline, String partialDescription) {
		this(partialHeadline, partialDescription, BigDecimal.ZERO);
	}

	public StockSpecificEvent(String partialHeadline, String partialDescription, BigDecimal defaultImpact) {
		super(partialHeadline, partialDescription);
		this.partialHeadline = partialHeadline;
		this.partialDescription = partialDescription;
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
  
  public void addStock(Stock stock) {
    if (stock == null) {
      throw new IllegalArgumentException("Stock cannot be null");
    }
    this.targetSymbol = stock.getSymbol();
    this.generatedHeadline = generateFullHeadline(stock.getCompany());
    this.generatedDescription = generateFullDescription(stock.getCompany());
  }

  public String getTargetSymbol() {
    return targetSymbol;
  }

  public String getGeneratedHeadline() {
    return generatedHeadline != null ? generatedHeadline : getHeadline();
  }

  public String getGeneratedDescription() {
    return generatedDescription != null ? generatedDescription : getDescription();
  }

  public BigDecimal getImpactForStock(Stock stock) {
    if (stock == null) {
      return BigDecimal.ZERO;
    }
    if (targetSymbol == null || targetSymbol.isBlank()) {
      return defaultImpact;
    }
    return targetSymbol.equalsIgnoreCase(stock.getSymbol()) ? defaultImpact : BigDecimal.ZERO;
  }

  public StockSpecificEvent createForStock(Stock stock) {
    StockSpecificEvent eventForStock =
        new StockSpecificEvent(partialHeadline, partialDescription, defaultImpact);
    eventForStock.addStock(stock);
    return eventForStock;
  }

  public String generateFullHeadline(String stockName) {
    return partialHeadline.replace("{stock}", stockName);
  }

  public String generateFullDescription(String stockName) {
    return partialDescription.replace("{stock}", stockName);
  }

}
