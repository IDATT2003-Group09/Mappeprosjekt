package edu.ntnu.iir.bidata.idatt2003.group09.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class Stock implements Serializable {

  private String symbol;
  private String company;
  private List<BigDecimal> prices;
  private String sector;
  private int risk;

  public Stock(String symbol, String company, BigDecimal salesPrice, String sector, int risk) {
    setSymbol(symbol);
    setCompany(company);
    setSector(sector);
    setRisk(risk);

    prices = new ArrayList<>();
    addNewSalesPrice(salesPrice);
  }

  /***
   * Getters
   */
  public String getSymbol() {
    return symbol;
  }

  public String getCompany() {
    return company;
  }

  public String getSector() {
      return sector;
  }

  public int getRisk() {
      return risk;
  }

  /***
   * settters
   * @return
   */
  public void setSymbol(String symbol) {
    if (symbol == null || symbol.isEmpty()) {
      throw new IllegalArgumentException("""
          symbol cant be
          empty or null
          """);
    } else {
      this.symbol = symbol;
    }
  }
  public void setCompany(String company) {
    if (company == null || company.isEmpty()) {
      throw new IllegalArgumentException("""
          company cant be
          empty or null
          """);
    } else {
      this.company = company;
    }
  }

  public void setSector(String sector) {
      if (sector == null || sector.isBlank()) {
          throw new IllegalArgumentException("Sector cannot be null or empty");
      }
      this.sector = sector;
  }

  public void setRisk(int risk) {
      if (risk < 1 || risk > 7) {
          throw new IllegalArgumentException("Risk must be between 1 and 7");
      }
      this.risk = risk;
  }

  /***
   * returns the latest price from the prices
   * list
   */
  public BigDecimal getSalesPrice() {
    return prices.getLast();
  }

  /***
   * Add the argumetn price to the last index
   * in the prices list
   */
  public void addNewSalesPrice(BigDecimal price) {
    if (price == null ||
        price.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("""
          price cant be
          empty and must be more than zero
          """);
    } else {
      prices.add(price);
    }
  }

    /**
     * Returns all the historical prices
     *
     * @return an unmodifable list of historical prices
     */
  public List<BigDecimal> getHistoricalPrices() {
      return Collections.unmodifiableList(prices);
  }

    /**
     * Returns the highest price registered
     *
     * @return the highest price
     */
  public BigDecimal getHighestPrice() {
      return Collections.max(prices);
  }

    /**
     * Returns the lowest price registered
     *
     * @return the lowst price
     */
  public BigDecimal getLowestPrice() {
      return Collections.min(prices);
  }

    /**
     * Returns the diffrence between the latest price and the previous one.
     * If there is only one price, the change is zero
     *
     * @return the latest price change
     */
  public BigDecimal getLatestPriceChange() {
      if (prices.size() < 2) {
          return BigDecimal.ZERO;
      }

      BigDecimal latestPrice = prices.get(prices.size() -1 );
      BigDecimal previousPrice = prices.get(prices.size() -2);

      return latestPrice.subtract(previousPrice);
  }
}