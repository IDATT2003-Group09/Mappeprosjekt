package edu.ntnu.iir.bidata.idatt2003.group09.base.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ntnu.iir.bidata.idatt2003.group09.base.Exchange;
import edu.ntnu.iir.bidata.idatt2003.group09.base.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.base.news.GlobalEvent;
import edu.ntnu.iir.bidata.idatt2003.group09.base.news.NewsPaper;
import edu.ntnu.iir.bidata.idatt2003.group09.base.news.StockSpecificEvent;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExchangeTest {

  private Exchange exchange;
  private Stock apple;
  private Stock microsoft;

  @BeforeEach
  void setUp() {
    apple = new Stock("AAPL", "Apple", new BigDecimal("150.00"), "tech", 4);
    microsoft = new Stock("MSFT", "Microsoft", new BigDecimal("300.00"), "tech", 6);
    exchange = new Exchange("OsloBors", List.of(apple, microsoft));
  }

  @Test
  void constructorShouldSetName() {
    assertEquals("OsloBors", exchange.getName());
  }

  @Test
  void nullNameShouldThrow() {
    assertThrows(IllegalArgumentException.class, () -> new Exchange("", List.of(apple, microsoft)));
  }

  @Test
  void constructorShouldInitializeWeekToZero() {
    assertEquals(0, exchange.getWeek());
  }

  @Test
  void hasStockShouldReturnTrueWhenSymbolExists() {
    assertTrue(exchange.hasStock("AAPL"));
  }

  @Test
  void hasStockShouldReturnFalseWhenSymbolDoesNotExist() {
    assertFalse(exchange.hasStock("TSLA"));
  }

  @Test
  void getStockShouldReturnMatchingStock() {
    assertSame(apple, exchange.getStock("AAPL"));
  }

  @Test
  void getStockShouldThrowWhenSymbolNotFound() {
    assertThrows(IllegalArgumentException.class, () -> exchange.getStock("TSLA"));
  }

  @Test
  void findStocksShouldMatchBySymbolIgnoringCase() {
    assertSame(apple, exchange.findStocks("aapl"));
  }

  @Test
  void findStocksShouldMatchByCompanyIgnoringCase() {
    assertSame(microsoft, exchange.findStocks("microsoft"));
  }

  @Test
  void findStocksShouldThrowWhenNoMatch() {
    assertThrows(IllegalArgumentException.class, () -> exchange.findStocks("UnknownCorp"));
  }

  @Test
  void setNameShouldThrowWhenBlank() {
    assertThrows(IllegalArgumentException.class, () -> exchange.setName("   "));
  }

  @Test
  void setStockMapShouldThrowWhenEmptyList() {
    Exchange newExchange = new Exchange("Test", List.of(apple));
    assertThrows(IllegalArgumentException.class, () -> newExchange.setStockMap(new ArrayList<>()));
  }

  @Test
  void getGainersShouldReturnStocksWithPriceIncrease() {
    apple.addNewSalesPrice(new BigDecimal("160.00"));
    microsoft.addNewSalesPrice(new BigDecimal("290.00"));

    List<Stock> gainers = exchange.getGainers();
    assertTrue(gainers.contains(apple));
    assertFalse(gainers.contains(microsoft));
  }

  @Test
  void getLosersShouldReturnStocksWithPriceDecrease() {
    apple.addNewSalesPrice(new BigDecimal("140.00"));
    microsoft.addNewSalesPrice(new BigDecimal("300.00"));

    List<Stock> losers = exchange.getLosers();
    assertTrue(losers.contains(apple));
    assertFalse(losers.contains(microsoft));
  }

    @Test
    void advance_updatesWeekAndStockPrices() {

        BigDecimal oldPrice = apple.getSalesPrice();
        int oldWeek = exchange.getWeek();

        exchange.advance();

        assertEquals(oldWeek + 1, exchange.getWeek());
        assertNotEquals(oldPrice, apple.getSalesPrice());
    }

    @Test
    void advance_addsNewPriceToHistory() {
      int oldSize = apple.getHistoricalPrices().size();

      exchange.advance();

      assertEquals(oldSize + 1, apple.getHistoricalPrices().size());
    }

    @Test
    void advance_clampsPriceToMinimumWhenCombinedImpactIsTooNegative() throws Exception {
      Stock riskyStock = new Stock("RISK", "Risky Inc", new BigDecimal("100.00"), "tech", 7);
      Exchange extremeExchange = new Exchange("TestExchange", List.of(riskyStock));

      GlobalEvent crashEvent = new GlobalEvent("Market Crash", "Extreme downturn");
      crashEvent.addEventData("tech", new BigDecimal("-0.90"));
      GlobalEvent secondCrashEvent = new GlobalEvent("Banking Panic", "Credit markets freeze");
      secondCrashEvent.addEventData("tech", new BigDecimal("-0.20"));

      StockSpecificEvent specificEvent1 =
        new StockSpecificEvent("A", "A", new BigDecimal("-0.25")).createForStock(riskyStock);
      StockSpecificEvent specificEvent2 =
        new StockSpecificEvent("B", "B", new BigDecimal("-0.25")).createForStock(riskyStock);
      StockSpecificEvent specificEvent3 =
        new StockSpecificEvent("C", "C", new BigDecimal("-0.25")).createForStock(riskyStock);

      NewsPaper extremeNewsPaper =
        new NewsPaper(List.of(crashEvent, secondCrashEvent), List.of(specificEvent1, specificEvent2, specificEvent3));

      Field pendingNewsPaperField = Exchange.class.getDeclaredField("pendingNewsPaper");
      pendingNewsPaperField.setAccessible(true);
      pendingNewsPaperField.set(extremeExchange, extremeNewsPaper);

      assertDoesNotThrow(extremeExchange::advance);
      assertTrue(riskyStock.getSalesPrice().compareTo(new BigDecimal("0.01")) >= 0);
    }
}
