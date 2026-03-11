package edu.ntnu.iir.bidata.idatt2003.group09;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    apple = new Stock("AAPL", "Apple", new BigDecimal("150.00"));
    microsoft = new Stock("MSFT", "Microsoft", new BigDecimal("300.00"));
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
  void advanceShouldIncrementWeek() {
    exchange.Advance();
    assertEquals(1, exchange.getWeek());
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
}
