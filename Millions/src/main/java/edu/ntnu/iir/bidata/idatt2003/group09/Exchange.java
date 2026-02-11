package edu.ntnu.iir.bidata.idatt2003.group09;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Exchange {

  private String name;
  private int week;
  private Map<String, Stock> stockMap;
  private Random random;

  public Exchange(String name, List<Stock> stocks) {
    setName(name);
    stockMap = new Map<>();
  }

  public String getName() {
    return name;
  }

  public int getWeek() {
    return week;
  }

  public void setName(String name) {
    if (name.trim().isEmpty() || name == null) {
      throw new IllegalArgumentException("""
            arg cant be empty or null
          """);
    } else {
      this.name = name;
    }
  }

  public void setStockMap(List<Stock> stocks) {
    if (stocks.isEmpty()) {
      throw new IllegalArgumentException("arg cant be empty");
    } else {
      for (Stock stock : stocks) {

      }
    }
  }

  public boolean hasStock(String symbol) {

  }

  public Stock getStock(String symbol) {

  }

  public Stock findStocks(String searchTerm) {

  }

  public Transaction buy(String symbol,
      Player player, BigDecimal quantity) {

  }

  public Transaction sell(Share share, Player player) {

  }

  public void Advance() {

  }
}
