package edu.ntnu.iir.bidata.idatt2003.group09.model.calculator;

import java.math.BigDecimal;

import edu.ntnu.iir.bidata.idatt2003.group09.model.Share;


public class PurchaseCalculator implements TransactionCalculator {
  private BigDecimal purchasePrice;
  private BigDecimal quantity;
  private BigDecimal commissionRate;

  public PurchaseCalculator(Share share) {
    this(share, new BigDecimal("0.005"));
  }

  public PurchaseCalculator(Share share, BigDecimal commissionRate) {
    this.purchasePrice = share.getPurchasePrice();
    this.quantity = share.getQuantity();
    this.commissionRate = commissionRate != null ? commissionRate : new BigDecimal("0.005");
  }

  @Override
  public BigDecimal calculateGross() {
    return purchasePrice.multiply(quantity);
  }

  @Override
  public BigDecimal calculateCommission() {
    BigDecimal gross = calculateGross();
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
