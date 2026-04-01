package edu.ntnu.iir.bidata.idatt2003.group09.base.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ntnu.iir.bidata.idatt2003.group09.base.Exchange;
import edu.ntnu.iir.bidata.idatt2003.group09.base.Stock;

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

        exchange.Advance();

        assertEquals(oldWeek + 1, exchange.getWeek());
        assertNotEquals(oldPrice, apple.getSalesPrice());
    }

    @Test
    void advance_addsNewPriceToHistory() {
      int oldSize = apple.getHistoricalPrices().size();

      exchange.Advance();

      assertEquals(oldSize + 1, apple.getHistoricalPrices().size());
    }
}
