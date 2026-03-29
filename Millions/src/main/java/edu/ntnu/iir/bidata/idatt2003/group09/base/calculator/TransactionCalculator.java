package edu.ntnu.iir.bidata.idatt2003.group09.base.calculator;

import java.math.BigDecimal;

public interface TransactionCalculator {

  public BigDecimal calculateGross();

  public BigDecimal calculateCommission();

  public BigDecimal calculateTax();

  public BigDecimal calculateTotal();
}
