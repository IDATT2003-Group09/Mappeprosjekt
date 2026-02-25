package edu.ntnu.iir.bidata.idatt2003.group09;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class SaleCalculatorTest {

    private static BigDecimal bd(String value) {
        return new BigDecimal(value);
    }

    private static void assertBigDecimalEquals(BigDecimal expected, BigDecimal actual) {
        BigDecimal e = expected.setScale(10, RoundingMode.HALF_UP);
        BigDecimal a = actual.setScale(10, RoundingMode.HALF_UP);
        assertTrue(e.compareTo(a) == 0);
    }

    @Test
    void constructor_throwsException_whenShareIsNull() {
        assertThrows(NullPointerException.class, () -> new SaleCalculator(null));
    }

    @Test
    void calculateGross_returnsSalesPriceTimesQuantity() {
        Stock stock = new Stock("ACME", "Acme Inc", bd("120"));
        Share share = new Share(stock, bd("10"), bd("100"));
        SaleCalculator calculator = new SaleCalculator(share);

        BigDecimal gross = calculator.calculateGross();

        assertBigDecimalEquals(bd("1200"), gross);
    }

    @Test
    void calculateCommission_isOnePercentOfGross() {
        Stock stock = new Stock("ACME", "Acme Inc", bd("120"));
        Share share = new Share(stock, bd("10"), bd("100"));
        SaleCalculator calculator = new SaleCalculator(share);

        BigDecimal commission = calculator.calculateCommission();

        assertBigDecimalEquals(bd("12"), commission);
    }

    @Test
    void calculateTax_returnsThirtyPercentOfProfitBase() {
        Stock stock = new Stock("ACME", "Acme Inc", bd("120"));
        Share share = new Share(stock, bd("10"), bd("100"));
        SaleCalculator calculator = new SaleCalculator(share);

        BigDecimal tax = calculator.calculateTax();

        assertBigDecimalEquals(bd("56.4"), tax);
    }

    @Test
    void calculateTotal_returnsNetEarningsAfterCommissionAndTax() {
        Stock stock = new Stock("ACME", "Acme Inc", bd("120"));
        Share share = new Share(stock, bd("10"), bd("100"));
        SaleCalculator calculator = new SaleCalculator(share);

        BigDecimal total = calculator.calculateTotal();

        assertBigDecimalEquals(bd("131.6"), total);
    }
}