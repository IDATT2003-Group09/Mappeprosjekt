package edu.ntnu.iir.bidata.idatt2003.group09;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Exchange {

  private String name;
  private int week;
  private Map<String, Stock> stockMap;
  private Random random;

  public Exchange(String name, List<Stock> stocks) {
    random = new Random();
    setName(name);
    stockMap = new HashMap<>();
    setStockMap(stocks);
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
        stockMap.put(stock.getSymbol(), stock);
      }
    }
  }

  public boolean hasStock(String symbol) {
    return stockMap.containsKey(symbol);
  }

  public Stock getStock(String symbol) {
    if (hasStock(symbol)) {
      return stockMap.get(symbol);
    } else {
      throw new IllegalArgumentException("stock not found");
    }
  }

  public Stock findStocks(String searchTerm) {
    for (Stock stock : stockMap.values()) {
      if (stock.getSymbol().equalsIgnoreCase(searchTerm) ||
          stock.getCompany().equalsIgnoreCase(searchTerm)) {
        return stock;
      }
    }
    throw new IllegalArgumentException("stock not found");
  }

  public Transaction buy(String symbol,
      Player player, BigDecimal quantity) {
    Stock stock = getStock(symbol);
    BigDecimal salesPrice = stock.getSalesPrice();
    Share share = new Share(stock, quantity, salesPrice);
    Purchase purchase = new Purchase(share, week);
    purchase.commit(player);
    return purchase;
  }

  public Transaction sell(Share share, Player player) {
    Sale sale = new Sale(share, week);
    sale.commit(player);
    return sale;
  }

  public void Advance() {
    week++;
  }
}
