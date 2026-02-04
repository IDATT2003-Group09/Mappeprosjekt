package edu.ntnu.iir.bidata.idatt2003.group09;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

public class Stock {

  private String symbol;
  private String company;
  private List<BigDecimal> prices;

  public Stock(String symbol, String company, BigDecimal salesPrice) {
      if (symbol == null || company == null || salesPrice == null){
          throw new IllegalArgumentException("Arguments must not be null");
      }
      if (salesPrice.compareTo(BigDecimal.ZERO) <= 0){
          throw new IllegalArgumentException("Price must be greater than zero");
      }
    this.symbol = symbol;
    this.company = company;
    prices = new ArrayList<>();
    prices.add(salesPrice);
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
      if (price == null) {
          throw new IllegalArgumentException("price cannot be null");
      }
      if (price.compareTo(BigDecimal.ZERO) <= 0){
          throw new IllegalArgumentException("Price must be greater than zero");
      }
    prices.add(price);
  }
}