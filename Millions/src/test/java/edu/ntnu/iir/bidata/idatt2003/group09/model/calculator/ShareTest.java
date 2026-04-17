package edu.ntnu.iir.bidata.idatt2003.group09.model.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ntnu.iir.bidata.idatt2003.group09.model.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ShareTest {

    private Stock stock;
    private Share share;

    private static Stock validStock() {
        return new Stock("AAPL", "Apple", new BigDecimal("150.00"), "tech", 4);
    }

    /**
     * Sets up a valid instance before every test
     */
    @BeforeEach
    void setUp() {
        stock = validStock();
        share = new Share(stock, new BigDecimal("10"), new BigDecimal("25.50"));
    }

    /**
     * Verifies that the constructor accepts valid arguments
     */
    @Test
    void constructorShouldNotThrowForValidArguments() {
        assertDoesNotThrow(() ->
                new Share(stock, new BigDecimal("1"), new BigDecimal("0.01")));
    }

    /**
     * Verifies that the method accepts non null stocks
     */
    @Test
    void setStockShouldAcceptNonNullStock() {
        Stock other = new Stock("MSFT", "Microsoft", new BigDecimal("300.00"), "tech", 6);
        assertDoesNotThrow(() -> share.setStock(other));
    }

    /**
     * Verifies that the set method accepts a positive value
     */
    @Test
    void setQuantityShouldAcceptPositiveValue() {
        assertDoesNotThrow(() -> share.setQuantity(new BigDecimal("0.0001")));
    }

    /**
     * Verifies that the set method accepts a positive value
     */
    @Test
    void setPurchasePriceShouldAcceptPositiveValue() {
        assertDoesNotThrow(() -> share.setPurchasePrice(new BigDecimal("999999.99")));
    }

    /**
     * Verifies that the constructor throws when stock is null
     */
    @Test
    void constructorShouldThrowWhenStockIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Share(null, new BigDecimal("1"), new BigDecimal("10")));
        assertTrue(ex.getMessage().toLowerCase().contains("stock"));
    }

    /**
     * Verifies that the constructor throws when the quantity is zero
     */
    @Test
    void constructorShouldThrowWhenQuantityIsZero() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Share(stock, BigDecimal.ZERO, new BigDecimal("10")));
        assertTrue(ex.getMessage().toLowerCase().contains("quantity"));
    }

    /**
     * Verifies that the constructor throws when the quantity is negative
     */
    @Test
    void constructorShouldThrowWhenQuantityIsNegative() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Share(stock, new BigDecimal("-1"), new BigDecimal("10")));
        assertTrue(ex.getMessage().toLowerCase().contains("quantity"));
    }

    /**
     * Verifies that the constructor throws when price is zero
     */
    @Test
    void constructorShouldThrowWhenPurchasePriceIsZero() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Share(stock, new BigDecimal("1"), BigDecimal.ZERO));
        assertTrue(ex.getMessage().toLowerCase().contains("purchaseprice"));
    }

    /**
     * Verifies that the constructor throws when the price is negative
     */
    @Test
    void constructorShouldThrowWhenPurchasePriceIsNegative() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Share(stock, new BigDecimal("1"), new BigDecimal("-0.01")));
        assertTrue(ex.getMessage().toLowerCase().contains("purchaseprice"));
    }

    /**
     * Verifies that the method throws when null
     */
    @Test
    void setStockShouldThrowWhenNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                share.setStock(null));
        assertTrue(ex.getMessage().toLowerCase().contains("stock"));
    }

    /**
     * Verifies that the method throws when the quantity is zero
     */
    @Test
    void setQuantityShouldThrowWhenZero() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                share.setQuantity(BigDecimal.ZERO));
        assertTrue(ex.getMessage().toLowerCase().contains("quantity"));
    }

    /**
     * Verifies that the method throws when the quantity is negative
     */
    @Test
    void setQuantityShouldThrowWhenNegative() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                share.setQuantity(new BigDecimal("-0.0001")));
        assertTrue(ex.getMessage().toLowerCase().contains("quantity"));
    }

    /**
     * Verifies that the method throws when the price is zero
     */
    @Test
    void setPurchasePriceShouldThrowWhenZero() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                share.setPurchasePrice(BigDecimal.ZERO));
        assertTrue(ex.getMessage().toLowerCase().contains("purchaseprice"));
    }

    /**
     * Verifies that the method throws when the price is negative
     */
    @Test
    void setPurchasePriceShouldThrowWhenNegative() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                share.setPurchasePrice(new BigDecimal("-1"))
        );
        assertTrue(ex.getMessage().toLowerCase().contains("purchaseprice"));
    }
}