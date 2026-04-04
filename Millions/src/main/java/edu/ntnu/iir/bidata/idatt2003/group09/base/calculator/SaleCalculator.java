package edu.ntnu.iir.bidata.idatt2003.group09.base.calculator;

import java.math.BigDecimal;

import edu.ntnu.iir.bidata.idatt2003.group09.base.Share;

public class SaleCalculator implements TransactionCalculator {

  private BigDecimal purchasePrice;
  private BigDecimal salesPrice;
  private BigDecimal quantity;

  public SaleCalculator(Share share) {
    this.purchasePrice = share.getPurchasePrice();
    this.salesPrice = share.getStock().getSalesPrice();
    this.quantity = share.getQuantity();
  }

  @Override
  public BigDecimal calculateGross() {
    return salesPrice.multiply(quantity);
  }

  @Override
  public BigDecimal calculateCommission() {
    return calculateGross().multiply(new BigDecimal("0.01"));
  }

  @Override
  public BigDecimal calculateTax() {
    BigDecimal originalPrice = purchasePrice.multiply(quantity);
    BigDecimal taxRate = new BigDecimal("0.3");

    BigDecimal profit = calculateGross()
            .subtract(originalPrice)
            .subtract(calculateCommission());

    if (profit.compareTo(BigDecimal.ZERO) <= 0) {
        return BigDecimal.ZERO;
    }

    return profit.multiply(new BigDecimal("0.3"));
  }

  @Override
  public BigDecimal calculateTotal() {
    BigDecimal gross = calculateGross();
    BigDecimal commission = calculateCommission();
    BigDecimal tax = calculateTax();

    return gross.subtract(commission).subtract(tax);
  }
}
