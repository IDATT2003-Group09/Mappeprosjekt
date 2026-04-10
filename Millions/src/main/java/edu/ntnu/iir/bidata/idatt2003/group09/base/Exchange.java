package edu.ntnu.iir.bidata.idatt2003.group09.base;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import edu.ntnu.iir.bidata.idatt2003.group09.base.news.EventFactory;
import edu.ntnu.iir.bidata.idatt2003.group09.base.news.NewsPaper;
import edu.ntnu.iir.bidata.idatt2003.group09.base.news.StockSpecificEvent;
import edu.ntnu.iir.bidata.idatt2003.group09.base.transaction.Transaction;
import edu.ntnu.iir.bidata.idatt2003.group09.base.transaction.TransactionFactory;

public class Exchange implements Serializable {

  private String name;
  private int week;
  private Map<String, Stock> stockMap;
  private MarketNews pendingNews;
  private NewsPaper pendingNewsPaper;
  private final EventFactory eventFactory = new EventFactory();
  private final Random random = new Random();

  /**
   * uses setters to validate input
   * @param name
   * @param stocks
   */
  public Exchange(String name, List<Stock> stocks) {
    setName(name);
    stockMap = new HashMap<>();
    setStockMap(stocks);
    generatePendingNews();
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
     * Get method for stockmap
     * @return stockmap
     */
    public Map<String, Stock> getStockMap() {
        return stockMap;
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
     * Method to get a list of the stockmap
     *
     * @return list of stocks
     */
  public List<Stock> getStocks() {
      return List.copyOf(stockMap.values());
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
     * Getter for news
     * @return news
     */
    public MarketNews getPendingNews() {
        return pendingNews;
    }

    public NewsPaper getPendingNewsPaper() {
      return pendingNewsPaper;
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
    Transaction purchase = TransactionFactory.createPurchase(share, week);
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
    Transaction sale = TransactionFactory.createSale(share, week);
    sale.commit(player);
    return sale;
  }

  /**
   * Advances the market by one week
   * Applies currently pending news to this week's prices
   * Generates new pending news that will affect next week
   */
  public void advance() {
      NewsPaper activeNewsPaper = pendingNewsPaper;

      if (activeNewsPaper == null) {
        generatePendingNews();
        activeNewsPaper = pendingNewsPaper;
      }

      for (Stock stock : stockMap.values()) {
        BigDecimal newPrice = PriceGenerator.nextWeekPrice(stock, activeNewsPaper);
          stock.addNewSalesPrice(newPrice);
      }

      generatePendingNews();

      if (pendingNews != null) {
        System.out.println("Week " + week + " UPCOMING NEWS: " + pendingNews.getHeadline());
        for (StockSpecificEvent event : pendingNewsPaper.getStockSpecificEvents()) {
          System.out.println(" - " + event.getGeneratedHeadline());
        }
      }

      week++;
  }

  private void generatePendingNews() {
    pendingNewsPaper = NewsPaper.create(eventFactory, getStocks(), random);
    pendingNews = new MarketNews(
        pendingNewsPaper.getGlobalEvent().getHeadline(),
        pendingNewsPaper.getGlobalEvent().getDescription(),
        "ALL",
        pendingNewsPaper.getGlobalEvent().getAverageImpact()
    );
  }
}