package edu.ntnu.iir.bidata.idatt2003.group09;

import java.math.BigDecimal;

public interface TransactionCalculator {

  public BigDecimal calculateGross();

  public BigDecimal calculateCommision();

  public BigDecimal calculateTax();

  public BigDecimal calculateTotal();
}
