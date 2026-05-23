package edu.ntnu.iir.bidata.idatt2003.group09.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Share implements Serializable {

  private Stock stock;
  private BigDecimal quantity;
  private BigDecimal purchasePrice;

  /***
   * uses set methods to set the argumets
   */
  public Share(Stock stock, BigDecimal quantity, BigDecimal purchasePrice) {
    setStock(stock);
    setQuantity(quantity);
    setPurchasePrice(purchasePrice);
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

  /***
   * Setters validates if values are empty or invalid
   */
  public void setStock(Stock stock) {
    if (stock == null) {
      throw new IllegalArgumentException("stock can not be null");
    } else {
      this.stock = stock;
    }
  }

  public void setQuantity(BigDecimal quantity) {
    if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("quantity can not be or be less than zero");
    } else {
      this.quantity = quantity;
    }
  }

  public void setPurchasePrice(BigDecimal purchasePrice) {
    if (purchasePrice.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("purchasePrice can not be or be less than zero");
    } else {
      this.purchasePrice = purchasePrice;
    }
  }
}
