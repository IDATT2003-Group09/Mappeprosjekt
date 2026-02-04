package edu.ntnu.iir.bidata.idatt2003.group09;

import java.math.BigDecimal;

public class Share {

  private Stock stock;
  private BigDecimal quantity;
  private BigDecimal purchasePrice;

  public Share(Stock stock, BigDecimal quantity, BigDecimal purchasePrice) {
    this.stock = stock;
    this.quantity = quantity;
    this.purchasePrice = purchasePrice;
  }

  /***
   * Getters
   */
  public Stock getStock() {
    return stock;
  }

  public BigDecimal getQuantity() {
    return quantity;
  }

  public BigDecimal getPurchasePrice() {
    return purchasePrice;
  }
}
