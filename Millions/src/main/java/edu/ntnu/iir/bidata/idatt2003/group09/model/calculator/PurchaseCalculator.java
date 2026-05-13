package edu.ntnu.iir.bidata.idatt2003.group09.model.calculator;

import java.math.BigDecimal;

import edu.ntnu.iir.bidata.idatt2003.group09.model.Share;


/**
 * Calculator for purchase transactions, including gross, commission, and total cost.
 */
public class PurchaseCalculator implements TransactionCalculator {
  private BigDecimal purchasePrice;
  private BigDecimal quantity;
  private BigDecimal commissionRate;

  /**
   * Constructs a PurchaseCalculator with a default commission rate (0.5%).
   *
   * @param share the Share being purchased
   */
  public PurchaseCalculator(Share share) {
    this(share, new BigDecimal("0.005"));
  }

  /**
   * Constructs a PurchaseCalculator with a specified commission rate.
   *
   * @param share      
   * @param commissionRate the commission rate to use (if null, defaults to 0.5%)
   */
  public PurchaseCalculator(Share share, BigDecimal commissionRate) {
    this.purchasePrice = share.getPurchasePrice();
    this.quantity = share.getQuantity();
    this.commissionRate = commissionRate != null ? commissionRate : new BigDecimal("0.005");
  }

  @Override
  /**
   * Calculates the gross cost (purchase price × quantity).
   *
   * @return the gross cost as BigDecimal
   */
  public BigDecimal calculateGross() {
    return purchasePrice.multiply(quantity);
  }

  @Override
  /**
   * Calculates the commission for the purchase.
   *
   * @return the commission as BigDecimal
   */
  public BigDecimal calculateCommission() {
    BigDecimal gross = calculateGross();
    return gross.multiply(commissionRate);
  }

  @Override
  /**
   * Calculates the tax for the purchase (always zero for purchases).
   *
   * @return the tax (zero)
   */
  public BigDecimal calculateTax() {
    return new BigDecimal(0);
  }

  @Override
  /**
   * Calculates the total cost (gross + commission).
   *
   * @return the total cost as BigDecimal
   */
  public BigDecimal calculateTotal() {
    return calculateGross().add(calculateCommission());
  }
}
