package edu.ntnu.iir.bidata.idatt2003.group09.base.calculator.transaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ntnu.iir.bidata.idatt2003.group09.model.Player;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.model.transaction.Purchase;

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
    player = new Player("Alice", new BigDecimal("1000.00"), "Easy");
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
    assertEquals(new BigDecimal("25.125000"), total);
  }
}
