package edu.ntnu.iir.bidata.idatt2003.group09.base.calculator;

import org.junit.jupiter.api.Test;

import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;

import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class StockTest {

    private Stock stock;

    @BeforeEach
    void setUp() {
        stock = new Stock("AAPL", "Apple", new BigDecimal("150.00"), "Technology", 4);
    }

    @Test
    void constructorShouldSetSymbolCorrectly() {
        assertEquals("AAPL", stock.getSymbol());
    }

    @Test
    void constructorShouldSetCompanyCorrectly() {
        assertEquals("Apple", stock.getCompany());
    }

    @Test
    void constructorShouldSetSectorCorrectly() {
        assertEquals("Technology", stock.getSector());
    }

    @Test
    void constructorShouldSetRiskCorrectly() {
        assertEquals(4, stock.getRisk());
    }

    @Test
    void getSalesPriceShouldReturnInitialPrice() {
        assertEquals(new BigDecimal("150.00"), stock.getSalesPrice());
    }

    @Test
    void addNewSalesPriceShouldUpdateLatestPrice() {
        BigDecimal newPrice = new BigDecimal("155.50");
        stock.addNewSalesPrice(newPrice);
        assertEquals(newPrice, stock.getSalesPrice());
    }


    @Test
    void constructorShouldThrowExceptionWhenSymbolIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new Stock(null, "Apple", new BigDecimal("100.00"), "Technology", 4));
    }

    @Test
    void constructorShouldThrowExceptionWhenCompanyIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new Stock("AAPL", null, new BigDecimal("100.00"), "Technology", 4));
    }

    @Test
    void constructorShouldThrowExceptionWhenPriceIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new Stock("AAPL", "Apple", null, "Technology", 4));
    }

    @Test
    void constructorShouldThrowExceptionWhenPriceIsNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> new Stock("AAPL", "Apple", new BigDecimal("-10.00"), "Technology", 4));
    }

    @Test
    void constructorShouldThrowExceptionWhenSectorIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new Stock("AAPL", "Apple", new BigDecimal("100.00"), null, 4));
    }

    @Test
    void constructorShouldThrowExceptionWhenRiskIsInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> new Stock("AAPL", "Apple", new BigDecimal("100.00"), "Technology", 10));
    }


    @Test
    void addNewSalesPriceShouldThrowExceptionWhenPriceIsNull() {
        assertThrows(IllegalArgumentException.class, () -> stock.addNewSalesPrice(null));
    }

    @Test
    void addNewSalesPriceShouldThrowExceptionWhenPriceIsNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> stock.addNewSalesPrice(new BigDecimal("-5.00")));
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