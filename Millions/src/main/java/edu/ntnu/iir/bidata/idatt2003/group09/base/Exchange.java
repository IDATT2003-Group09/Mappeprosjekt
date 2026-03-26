package edu.ntnu.iir.bidata.idatt2003.group09.base;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import edu.ntnu.iir.bidata.idatt2003.group09.base.transaction.Purchase;
import edu.ntnu.iir.bidata.idatt2003.group09.base.transaction.Sale;
import edu.ntnu.iir.bidata.idatt2003.group09.base.transaction.Transaction;

public class Exchange {

  private String name;
  private int week;
  private Map<String, Stock> stockMap;
  private Random random;

  /**
   * uses setters to validate input
   * @param name
   * @param stocks
   */
  public Exchange(String name, List<Stock> stocks) {
    random = new Random();
    setName(name);
    stockMap = new HashMap<>();
    setStockMap(stocks);
  }

  /**
   * getname
   * @return
   */
  public String getName() {
    return name;
  }

  /**
   * get week
   * @return
   */
  public int getWeek() {
    return week;
  }

  /**
   * set name, validates input
   * @param name
   */
  public void setName(String name) {
    if (name.trim().isEmpty() || name == null) {
      throw new IllegalArgumentException("""
            arg cant be empty or null
          """);
    } else {
      this.name = name;
    }
  }

  /**
   * set stockmap, validates input
   * @param stocks
   */
  public void setStockMap(List<Stock> stocks) {
    if (stocks.isEmpty()) {
      throw new IllegalArgumentException("arg cant be empty");
    } else {
      for (Stock stock : stocks) {
        stockMap.put(stock.getSymbol(), stock);
      }
    }
  }

  /**
   * checks if stockmap contains stock with symbol
   * @param symbol
   * @return
   */
  public boolean hasStock(String symbol) {
    return stockMap.containsKey(symbol);
  }

  /**
   * 
   * @param symbol
   * @return
   */
  public Stock getStock(String symbol) {
    if (hasStock(symbol)) {
      return stockMap.get(symbol);
    } else {
      throw new IllegalArgumentException("stock not found");
    }
  }

  /**
   * gets all stocks with positive price change
   * @return a list of gaining stocks
   */
  public List<Stock> getGainers() {
    return stockMap.values().stream()
        .filter(stock -> stock.getLatestPriceChange().compareTo(BigDecimal.ZERO) > 0)
        .toList();
  }

  /**
   * gets all stocks with negative price change
   * @return a list of losing stocks
   */
  public List<Stock> getLosers() {
    return stockMap.values().stream()
        .filter(stock -> stock.getLatestPriceChange().compareTo(BigDecimal.ZERO) < 0)
        .toList();
  }

  /**
   * searches for stock with symbol or company name matching searchterm
   * @param searchTerm
   * @return
   */
  public Stock findStocks(String searchTerm) {
    for (Stock stock : stockMap.values()) {
      if (stock.getSymbol().equalsIgnoreCase(searchTerm) ||
          stock.getCompany().equalsIgnoreCase(searchTerm)) {
        return stock;
      }
    }
    throw new IllegalArgumentException("stock not found");
  }

  /**
   * creates a purchase transaction
   * @param symbol
   * @param player
   * @param quantity
   * @return
   */
  public Transaction buy(String symbol,
      Player player, BigDecimal quantity) {
    Stock stock = getStock(symbol);
    BigDecimal salesPrice = stock.getSalesPrice();
    Share share = new Share(stock, quantity, salesPrice);
    Purchase purchase = new Purchase(share, week);
    purchase.commit(player);
    return purchase;
  }

  /**
   * creates a sale transaction
   * @param share
   * @param player
   * @return
   */
  public Transaction sell(Share share, Player player) {
    Sale sale = new Sale(share, week);
    sale.commit(player);
    return sale;
  }

  /**
   * Increasing the week counter
   */
  public void Advance() {
    week++;
  }
}