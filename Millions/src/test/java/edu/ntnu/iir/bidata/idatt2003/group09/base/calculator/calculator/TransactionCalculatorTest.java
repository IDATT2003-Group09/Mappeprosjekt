package edu.ntnu.iir.bidata.idatt2003.group09.base.calculator.calculator;

import edu.ntnu.iir.bidata.idatt2003.group09.base.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.base.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.base.calculator.PurchaseCalculator;
import edu.ntnu.iir.bidata.idatt2003.group09.base.calculator.SaleCalculator;
import edu.ntnu.iir.bidata.idatt2003.group09.base.calculator.TransactionCalculator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionCalculatorTest {

    /**
     * Provides test instances of TransactionCalculator implementations
     */
    static Stream<TransactionCalculator> provideCalculators() {
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("150.00"), "tech", 4);
        Share purchaseShare = new Share(stock, new BigDecimal("2.5"), new BigDecimal("10.00"));
        Share saleShare = new Share(stock, new BigDecimal("2"), new BigDecimal("100.00"));
        
        return Stream.of(
            new PurchaseCalculator(purchaseShare),
            new SaleCalculator(saleShare)
        );
    }

    @ParameterizedTest
    @MethodSource("provideCalculators")
    void calculateGrossShouldReturnNonNull(TransactionCalculator calculator) {
        assertNotNull(calculator.calculateGross());
    }

    @ParameterizedTest
    @MethodSource("provideCalculators")
    void calculateGrossShouldReturnPositiveValue(TransactionCalculator calculator) {
        BigDecimal gross = calculator.calculateGross();
        assertTrue(gross.compareTo(BigDecimal.ZERO) > 0);
    }

    @ParameterizedTest
    @MethodSource("provideCalculators")
    void calculateCommissionShouldReturnNonNull(TransactionCalculator calculator) {
        assertNotNull(calculator.calculateCommission());
    }

    @ParameterizedTest
    @MethodSource("provideCalculators")
    void calculateCommissionShouldReturnNonNegativeValue(TransactionCalculator calculator) {
        BigDecimal commission = calculator.calculateCommission();
        assertTrue(commission.compareTo(BigDecimal.ZERO) >= 0);
    }

    @ParameterizedTest
    @MethodSource("provideCalculators")
    void calculateTaxShouldReturnNonNull(TransactionCalculator calculator) {
        assertNotNull(calculator.calculateTax());
    }

    @ParameterizedTest
    @MethodSource("provideCalculators")
    void calculateTaxShouldReturnNonNegativeValue(TransactionCalculator calculator) {
        BigDecimal tax = calculator.calculateTax();
        assertTrue(tax.compareTo(BigDecimal.ZERO) >= 0);
    }

    @ParameterizedTest
    @MethodSource("provideCalculators")
    void calculateTotalShouldReturnNonNull(TransactionCalculator calculator) {
        assertNotNull(calculator.calculateTotal());
    }

    // Specific tests for PurchaseCalculator
    @Test
    void purchaseCalculatorShouldCalculateGrossCorrectly() {
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("150.00"), "tech", 4);
        Share share = new Share(stock, new BigDecimal("3"), new BigDecimal("50.00"));
        PurchaseCalculator calculator = new PurchaseCalculator(share);
        
        BigDecimal gross = calculator.calculateGross();
        assertEquals(new BigDecimal("150.00"), gross);
    }

    @Test
    void purchaseCalculatorShouldCalculateCommissionAsFivePercent() {
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("150.00"), "tech", 4);
        Share share = new Share(stock, new BigDecimal("2"), new BigDecimal("100.00"));
        PurchaseCalculator calculator = new PurchaseCalculator(share);
        
        BigDecimal commission = calculator.calculateCommission();
        assertEquals(0, new BigDecimal("10.00").compareTo(commission));
    }

    @Test
    void purchaseCalculatorShouldHaveZeroTax() {
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("150.00"), "tech", 4);
        Share share = new Share(stock, new BigDecimal("2"), new BigDecimal("100.00"));
        PurchaseCalculator calculator = new PurchaseCalculator(share);
        
        BigDecimal tax = calculator.calculateTax();
        assertEquals(new BigDecimal("0"), tax);
    }

    @Test
    void purchaseCalculatorTotalShouldBeGrossPlusCommission() {
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("150.00"), "tech", 4);
        Share share = new Share(stock, new BigDecimal("2"), new BigDecimal("100.00"));
        PurchaseCalculator calculator = new PurchaseCalculator(share);
        
        BigDecimal total = calculator.calculateTotal();
        BigDecimal expected = calculator.calculateGross().add(calculator.calculateCommission());
        assertEquals(expected, total);
    }

    // Specific tests for SaleCalculator
    @Test
    void saleCalculatorShouldCalculateGrossBasedOnSalesPrice() {
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("200.00"), "tech", 4);
        Share share = new Share(stock, new BigDecimal("3"), new BigDecimal("100.00"));
        SaleCalculator calculator = new SaleCalculator(share);
        
        BigDecimal gross = calculator.calculateGross();
        assertEquals(new BigDecimal("600.00"), gross);
    }

    @Test
    void saleCalculatorShouldCalculateCommissionAsOnePercent() {
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("150.00"), "tech", 4);
        Share share = new Share(stock, new BigDecimal("10"), new BigDecimal("100.00"));
        SaleCalculator calculator = new SaleCalculator(share);
        
        BigDecimal commission = calculator.calculateCommission();
        // Commission should be 1% of gross (1500), which is 15
        assertTrue(commission.compareTo(new BigDecimal("14.99")) > 0);
        assertTrue(commission.compareTo(new BigDecimal("15.01")) < 0);
    }

    @Test
    void saleCalculatorShouldCalculateTaxOnProfit() {
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("120.00"), "tech", 4);
        Share share = new Share(stock, new BigDecimal("10"), new BigDecimal("100.00"));
        SaleCalculator calculator = new SaleCalculator(share);
        
        BigDecimal tax = calculator.calculateTax();
        assertTrue(tax.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void saleCalculatorTotalShouldBeNetEarnings() {
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("120.00"), "tech", 4);
        Share share = new Share(stock, new BigDecimal("10"), new BigDecimal("100.00"));
        SaleCalculator calculator = new SaleCalculator(share);
        
        BigDecimal total = calculator.calculateTotal();
        assertNotNull(total);
        assertTrue(total.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void saleCalculatorTotalShouldBeLessThanGross() {
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("150.00"), "tech", 4);
        Share share = new Share(stock, new BigDecimal("10"), new BigDecimal("100.00"));
        SaleCalculator calculator = new SaleCalculator(share);
        
        BigDecimal total = calculator.calculateTotal();
        BigDecimal gross = calculator.calculateGross();
        assertTrue(total.compareTo(gross) < 0);
    }

    @Test
    void calculatorsShouldHandleSmallQuantities() {
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("150.00"), "tech", 4);
        Share share = new Share(stock, new BigDecimal("0.5"), new BigDecimal("100.00"));
        PurchaseCalculator calculator = new PurchaseCalculator(share);
        
        BigDecimal gross = calculator.calculateGross();
        assertEquals(0, new BigDecimal("50.0").compareTo(gross));
    }

    @Test
    void calculatorsShouldHandleLargeQuantities() {
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("150.00"), "tech", 4);
        Share share = new Share(stock, new BigDecimal("1000"), new BigDecimal("100.00"));
        PurchaseCalculator calculator = new PurchaseCalculator(share);
        
        BigDecimal gross = calculator.calculateGross();
        assertEquals(0, new BigDecimal("100000").compareTo(gross));
    }

    @Test
    void purchaseCalculatorConstructorShouldNotThrowForValidShare() {
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("150.00"), "tech", 4);
        Share share = new Share(stock, new BigDecimal("2"), new BigDecimal("100.00"));
        
        assertDoesNotThrow(() -> new PurchaseCalculator(share));
    }

    @Test
    void saleCalculatorConstructorShouldNotThrowForValidShare() {
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("150.00"), "tech", 4);
        Share share = new Share(stock, new BigDecimal("2"), new BigDecimal("100.00"));
        
        assertDoesNotThrow(() -> new SaleCalculator(share));
    }
}
