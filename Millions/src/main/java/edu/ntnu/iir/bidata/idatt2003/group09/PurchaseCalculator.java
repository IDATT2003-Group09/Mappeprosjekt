package edu.ntnu.iir.bidata.idatt2003.group09;

import java.math.BigDecimal;

public class PurchaseCalculator implements TransactionCalculator {
  private BigDecimal purchasePrice;
  private BigDecimal quantity;

  public PurchaseCalculator(Share share) {
    purchasePrice = share.getPurchasePrice();
    quantity = share.getQuantity();
  }

  @Override
  public BigDecimal calculateGross() {
    return purchasePrice.multiply(quantity);
  }

  @Override
  public BigDecimal calculateCommission() {
    BigDecimal gross = calculateGross();
    BigDecimal commissionRate = new BigDecimal("0.05");
    return gross.multiply(commissionRate);
  }

  @Override
  public BigDecimal calculateTax() {
    return new BigDecimal(0);
  }

  @Override
  public BigDecimal calculateTotal() {
    return calculateGross().add(calculateCommission());
  }
}
