package edu.ntnu.iir.bidata.idatt2003.group09;

import java.math.BigDecimal;
import java.util.List;

import com.sun.nio.sctp.IllegalUnbindException;

import java.util.ArrayList;

public class Stock {

  private String symbol;
  private String company;
  private List<BigDecimal> prices;

  public Stock(String symbol, String company, BigDecimal salesPrice) {
    setSymbol(symbol);
    setCompany(company);
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

}
