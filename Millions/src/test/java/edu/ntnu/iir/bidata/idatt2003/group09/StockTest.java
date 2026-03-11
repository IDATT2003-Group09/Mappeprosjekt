package edu.ntnu.iir.bidata.idatt2003.group09;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class StockTest {

  private Stock stock;

  @BeforeEach
  void setUp() {
    stock = new Stock("AAPL", "Apple", new BigDecimal("150.00"));
  }

  /**
   * Verifies that the constructor correctly sets the stock symbol
   */
  @Test
  void constructorShouldSetSymbolCorrectly() {
    String expectedSymbol = "AAPL";
    String actualSymbol = stock.getSymbol();
    assertEquals(expectedSymbol, actualSymbol);
  }

  /**
   * Verifies that the constructor correctly sets the company name.
   */
  @Test
  void constructorShouldSetCompanyCorrectly() {
    String expectedCompany = "Apple";
    String actualCompany = stock.getCompany();
    assertEquals(expectedCompany, actualCompany);
  }

  /**
   * Verifies that the initial sales price is returned correctly.
   */
  @Test
  void getSalesPriceShouldReturnInitialPrice() {
    BigDecimal expectedPrice = new BigDecimal("150.00");
    BigDecimal actualPrice = stock.getSalesPrice();
    assertEquals(expectedPrice, actualPrice);
  }

  /**
   * Verifies that adding a new sales price updates the latest price.
   */
  @Test
  void addNewSalesPriceShouldUpdateLatestPrice() {
    BigDecimal newPrice = new BigDecimal("155.50");
    stock.addNewSalesPrice(newPrice);
    assertEquals(newPrice, stock.getSalesPrice());
  }

  /**
   * Verifies that the constructor throws an exception when the symbol is null.
   */
  @Test
  void constructorShouldThrowExceptionWhenSymbolIsNull() {
    BigDecimal price = new BigDecimal("100.00");
    assertThrows(IllegalArgumentException.class, () -> new Stock(null, "Apple", price));
  }

  /**
   * Verifies that the constructor throws an exception when the company name is
   * null.
   */
  @Test
  void constructorShouldThrowExceptionWhenCompanyIsNull() {
    BigDecimal price = new BigDecimal("100.00");
    assertThrows(IllegalArgumentException.class, () -> new Stock("AAPL", null, price));
  }

  /**
   * Verifies that the constructor throws an exception when the price is null.
   */
  @Test
  void constructorShouldThrowExceptionWhenPriceIsNull() {
    assertThrows(IllegalArgumentException.class, () -> new Stock("AAPL", "Apple", null));
  }

  /**
   * Verifies that the constructor throws an exception when the price is negative.
   */
  @Test
  void constructorShouldThrowExceptionWhenPriceIsNegative() {
    BigDecimal negativePrice = new BigDecimal("-10.00");
    assertThrows(IllegalArgumentException.class, () -> new Stock("AAPL", "Apple", negativePrice));
  }

  /**
   * Verifies that adding a null price results in an exception.
   */
  @Test
  void addNewSalesPriceShouldThrowExceptionWhenPriceIsNull() {
    assertThrows(IllegalArgumentException.class, () -> stock.addNewSalesPrice(null));
  }

  /**
   * Verifies that adding a negative price results in an exception.
   */
  @Test
  void addNewSalesPriceShouldThrowExceptionWhenPriceIsNegative() {
    BigDecimal negativePrice = new BigDecimal("-5.00");
    assertThrows(IllegalArgumentException.class, () -> stock.addNewSalesPrice(negativePrice));
  }

    @Test
    void getHistoricalPricesShouldReturnAllPrices() {
        stock.addNewSalesPrice(new BigDecimal("160.00"));
        stock.addNewSalesPrice(new BigDecimal("170.00"));

        assertEquals(3, stock.getHistoricalPrices().size());
    }

    @Test
    void getHistoricalPricesShouldReturnUnmodifiableList() {
        var prices = stock.getHistoricalPrices();

        assertThrows(UnsupportedOperationException.class,
                () -> prices.add(new BigDecimal("200.00")));
    }

    @Test
    void getHighestPriceShouldReturnHighestPrice() {
        stock.addNewSalesPrice(new BigDecimal("180.00"));
        stock.addNewSalesPrice(new BigDecimal("170.00"));

        assertEquals(new BigDecimal("180.00"), stock.getHighestPrice());
    }

    @Test
    void getLowestPriceShouldReturnLowestPrice() {
        stock.addNewSalesPrice(new BigDecimal("120.00"));
        stock.addNewSalesPrice(new BigDecimal("170.00"));

        assertEquals(new BigDecimal("120.00"), stock.getLowestPrice());
    }

    @Test
    void getLatestPriceChangeShouldReturnPositiveDifference() {
        stock.addNewSalesPrice(new BigDecimal("170.00"));

        assertEquals(new BigDecimal("20.00"), stock.getLatestPriceChange());
    }

    @Test
    void getLatestPriceChangeShouldReturnNegativeDifference() {
        stock.addNewSalesPrice(new BigDecimal("140.00"));

        assertEquals(new BigDecimal("-10.00"), stock.getLatestPriceChange());
    }

    @Test
    void getLatestPriceChangeShouldReturnZeroWhenOnlyOnePriceExists() {
        assertEquals(BigDecimal.ZERO, stock.getLatestPriceChange());
    }
}
