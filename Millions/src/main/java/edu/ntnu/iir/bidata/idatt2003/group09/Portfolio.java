package edu.ntnu.iir.bidata.idatt2003.group09;

import edu.ntnu.iir.bidata.idatt2003.group09.calculator.SaleCalculator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Portfolio {

  private final List<Share> shares;

  /**
   * Intitalizes the shares list
   */
  public Portfolio() {
    shares = new ArrayList<>();
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

  public BigDecimal getNetWorth() {
      BigDecimal total = BigDecimal.ZERO;

      for (Share share : shares) {
          SaleCalculator calculator = new SaleCalculator(share);
          total = total.add(calculator.calculateTotal());
      }
      return total;
  }

}