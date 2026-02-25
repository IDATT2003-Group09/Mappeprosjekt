package edu.ntnu.iir.bidata.idatt2003.group09;

import java.math.BigDecimal;

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
    return calculateGross().multiply(new BigDecimal(0.01));
  }

  @Override
  public BigDecimal calculateTax() {
    BigDecimal originalPrice = purchasePrice.multiply(quantity);
    BigDecimal taxRate = new BigDecimal(0.3);
    return calculateGross().subtract(originalPrice).subtract(calculateCommission()).multiply(taxRate);
  }

  @Override
  public BigDecimal calculateTotal() {
    BigDecimal originalPrice = purchasePrice.multiply(quantity);
    BigDecimal earnings = calculateGross().subtract(originalPrice);
    return earnings.subtract(calculateCommission()).subtract(calculateTax());
  }
}
