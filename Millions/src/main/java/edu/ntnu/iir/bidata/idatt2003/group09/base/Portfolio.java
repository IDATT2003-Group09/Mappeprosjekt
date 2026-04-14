package edu.ntnu.iir.bidata.idatt2003.group09.base;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.ntnu.iir.bidata.idatt2003.group09.base.calculator.PortfolioValueCalculator;
import edu.ntnu.iir.bidata.idatt2003.group09.base.calculator.SaleBasedValueCalculator;

public class Portfolio implements Serializable {

  private final List<Share> shares;
  private final PortfolioValueCalculator valueCalculator;
  private List<BigDecimal> values;

  /**
   * Initializes the shares list with the default sale-based value calculator
   */
  public Portfolio() {
    this(new SaleBasedValueCalculator());
  }

  /**
   * Initializes the shares list with a custom value calculator
   * 
   * @param valueCalculator the calculator to use for determining share values
   */
  public Portfolio(PortfolioValueCalculator valueCalculator) {
    shares = new ArrayList<>();
    this.valueCalculator = valueCalculator;
    this.values = new ArrayList<>();
  }

  /**
   * Returns true if adding share was successful
   */
  public boolean addShare(Share share) {
    return shares.add(share);
  }

  /**
   * Returns true if removing share was successful
   */
  public boolean removeShare(Share share) {
    return shares.remove(share);
  }

  /**
   * Returns a unmodifiable version of the shares list
   */
  public List<Share> getShares() {
    return Collections.unmodifiableList(shares);
  }

  /**
   * Returns a list containing all shares of a specific symbol
   */
  public List<Share> getShares(String symbol) {
    List<Share> matchingShares = new ArrayList<>();

    for (Share share : shares) {
      if (share.getStock().getSymbol().equalsIgnoreCase(symbol)) {
        matchingShares.add(share);
      }
    }

    return matchingShares;
  }

    /**
     * Calculates the total networth of the portfolio
     *
     * @return the total value of all shares after tax and commisions
     */
  public BigDecimal getNetWorth() {
      return shares.stream()
          .map(valueCalculator::calculateShareValue)
          .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public void addThisWeekValues() {
    for (Share share : shares) {
      values.add(valueCalculator.calculateShareValue(share));
    }
  }

  public List<BigDecimal> getValues() {
    return Collections.unmodifiableList(values);
  }

  /**
   * Appends a net worth value to the internal values list for graphing.
   * @param value the net worth to add
   */
  public void addNetWorthValue(BigDecimal value) {
    values.add(value);
  }
  

}