package edu.ntnu.iir.bidata.idatt2003.group09.model.transaction;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import edu.ntnu.iir.bidata.idatt2003.group09.model.Player;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.model.calculator.TransactionCalculator;

public abstract class Transaction implements Serializable {

  private final Share share;
  private final int week;
  private transient TransactionCalculator calculator;
  private boolean committed;

  protected Transaction(Share share, int week, TransactionCalculator calculator) {
    this.share = Objects.requireNonNull(share, "Share cannot be null");
    if (week < 0) {
      throw new IllegalArgumentException("Week must be a positive number");
    }
    this.week = week;
    this.calculator = Objects.requireNonNull(calculator, "Calculator cannot be null");
    this.committed = false;
  }

  public Share getShare() {
    return share;
  }

  public int getWeek() {
    return week;
  }

  public TransactionCalculator getCalculator() {
    if (calculator == null) {
      calculator = createCalculator(share);
    }
    return calculator;
  }

  protected abstract TransactionCalculator createCalculator(Share share);

    public BigDecimal getFees() {
        return getCalculator().calculateCommission()
                .add(getCalculator().calculateTax());
    }

  public boolean isCommited() {
    return committed;
  }

  protected void markCommited() {
    this.committed = true;
  }

  public abstract void commit(Player player);

  protected void validateCommit(Player player) {
    Objects.requireNonNull(player, "player cannot be null");
    if (committed) {
      throw new IllegalStateException("Transaction has already been committed");
    }
  }
}
