package edu.ntnu.iir.bidata.idatt2003.group09.base.calculator.transaction;

import edu.ntnu.iir.bidata.idatt2003.group09.base.Player;
import edu.ntnu.iir.bidata.idatt2003.group09.base.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.base.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.base.transaction.Purchase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class PurchaseTest {

  private Stock stock;
  private Share share;
  private Purchase purchase;
  private Player player;

  @BeforeEach
  void setUp() {
    stock = new Stock("AAPL", "Apple", new BigDecimal("150.00"), "tech", 4);
    share = new Share(stock, new BigDecimal("2.5"), new BigDecimal("10.00"));
    purchase = new Purchase(share, 1);
    player = new Player("Alice", new BigDecimal("1000.00"));
  }

  @Test
  void commitShouldAddShareToPortfolio() {
    assertDoesNotThrow(() -> purchase.commit(player));
    assertEquals(1, player.getPortfolio().getShares().size());
    assertSame(share, player.getPortfolio().getShares().getFirst());
  }

  @Test
  void commitShouldMarkTransactionAsCommitted() {
    purchase.commit(player);
    assertTrue(purchase.isCommited());
  }

  @Test
  void commitShouldThrowWhenCommittedTwice() {
    purchase.commit(player);
    assertThrows(IllegalStateException.class, () -> purchase.commit(player));
  }

  @Test
  void commitShouldThrowWhenPlayerIsNull() {
    assertThrows(NullPointerException.class, () -> purchase.commit(null));
  }

  @Test
  void calculatorShouldComputeTotalWithCommission() {
    BigDecimal total = purchase.getCalculator().calculateTotal();
    assertEquals(new BigDecimal("26.25000"), total);
  }
}
