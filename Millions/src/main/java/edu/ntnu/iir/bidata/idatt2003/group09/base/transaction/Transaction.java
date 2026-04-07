package edu.ntnu.iir.bidata.idatt2003.group09.base.transaction;

import edu.ntnu.iir.bidata.idatt2003.group09.base.Player;
import edu.ntnu.iir.bidata.idatt2003.group09.base.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.base.calculator.TransactionCalculator;

import java.io.Serializable;
import java.util.Objects;

public abstract class Transaction implements Serializable {

  private final Share share;
  private final int week;
  private final TransactionCalculator calculator;
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
    return calculator;
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
