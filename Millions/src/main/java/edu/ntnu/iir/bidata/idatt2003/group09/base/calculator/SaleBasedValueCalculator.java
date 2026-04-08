package edu.ntnu.iir.bidata.idatt2003.group09.base.calculator;

import java.math.BigDecimal;

import edu.ntnu.iir.bidata.idatt2003.group09.base.Share;

/**
 * Calculates the value of a share by treating it as if it were sold at current market price.
 * This strategy accounts for sales commissions and capital gains tax.
 */
public class SaleBasedValueCalculator implements PortfolioValueCalculator {

    @Override
    public BigDecimal calculateShareValue(Share share) {
        SaleCalculator calculator = new SaleCalculator(share);
        return calculator.calculateTotal();
    }
}
