package edu.ntnu.iir.bidata.idatt2003.group09.calculator;

import edu.ntnu.iir.bidata.idatt2003.group09.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.calculator.PurchaseCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class PurchaseCalculatorTest {

  private PurchaseCalculator calculator;

  @BeforeEach
  void setUp() {
    Stock stock = new Stock("AAPL", "Apple", new BigDecimal("150.00"));
    Share share = new Share(stock, new BigDecimal("2.5"), new BigDecimal("10.00"));
    calculator = new PurchaseCalculator(share);
  }

  @Test
  void calculateGrossShouldReturnPriceTimesQuantity() {
    assertEquals(new BigDecimal("25.000"), calculator.calculateGross());
  }

  @Test
  void calculateCommissionShouldReturnFivePercentOfGross() {
    assertEquals(new BigDecimal("1.25000"), calculator.calculateCommission());
  }

  @Test
  void calculateTaxShouldReturnZero() {
    assertEquals(new BigDecimal("0"), calculator.calculateTax());
  }

  @Test
  void calculateTotalShouldReturnGrossPlusCommission() {
    assertEquals(new BigDecimal("26.25000"), calculator.calculateTotal());
  }
}
