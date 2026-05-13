package edu.ntnu.iir.bidata.idatt2003.group09.model.calculator;

import java.math.BigDecimal;

import edu.ntnu.iir.bidata.idatt2003.group09.model.Share;

/**
 * Calculator for sale transactions, including gross, commission, tax, and net proceeds.
 */
public class SaleCalculator implements TransactionCalculator {

  private BigDecimal purchasePrice;
  private BigDecimal salesPrice;
  private BigDecimal quantity;

  /**
   * Constructs a SaleCalculator for the given share.
   *
   * @param share the Share being sold
   */
  public SaleCalculator(Share share) {
    this.purchasePrice = share.getPurchasePrice();
    this.salesPrice = share.getStock().getSalesPrice();
    this.quantity = share.getQuantity();
  }

  @Override
  /**
   * Calculates the gross proceeds (sales price × quantity).
   *
   * @return the gross proceeds as BigDecimal
   */
  public BigDecimal calculateGross() {
    return salesPrice.multiply(quantity);
  }

  @Override
  /**
   * Calculates the commission for the sale (1% of gross).
   *
   * @return the commission as BigDecimal
   */
  public BigDecimal calculateCommission() {
    return calculateGross().multiply(new BigDecimal("0.01"));
  }

  @Override
  /**
   * Calculates the tax on the sale (30% of profit, if any).
   *
   * @return the tax as BigDecimal (zero if no profit)
   */
  public BigDecimal calculateTax() {
    BigDecimal originalPrice = purchasePrice.multiply(quantity);
    BigDecimal taxRate = new BigDecimal("0.3");

    BigDecimal profit = calculateGross()
            .subtract(originalPrice)
            .subtract(calculateCommission());

    if (profit.compareTo(BigDecimal.ZERO) <= 0) {
        return BigDecimal.ZERO;
    }

    return profit.multiply(taxRate);
  }

  @Override
  /**
   * Calculates the total net proceeds (gross - commission - tax).
   *
   * @return the total net proceeds as BigDecimal
   */
  public BigDecimal calculateTotal() {
    BigDecimal gross = calculateGross();
    BigDecimal commission = calculateCommission();
    BigDecimal tax = calculateTax();

    return gross.subtract(commission).subtract(tax);
  }
}
