package edu.ntnu.iir.bidata.idatt2003.group09.base.calculator;

import java.io.Serializable;
import java.math.BigDecimal;

import edu.ntnu.iir.bidata.idatt2003.group09.base.Share;

/**
 * Interface for calculating the current value of a share in a portfolio.
 * Allows different valuation strategies to be plugged in.
 */
public interface PortfolioValueCalculator extends Serializable {
    /**
     * Calculates the current value of a share after taxes and commissions.
     *
     * @param share the share to calculate the value for
     * @return the net value of the share
     */
    BigDecimal calculateShareValue(Share share);
}
