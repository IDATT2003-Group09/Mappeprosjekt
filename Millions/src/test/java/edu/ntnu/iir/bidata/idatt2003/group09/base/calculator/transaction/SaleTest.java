package edu.ntnu.iir.bidata.idatt2003.group09.base.calculator.transaction;

import edu.ntnu.iir.bidata.idatt2003.group09.base.Player;
import edu.ntnu.iir.bidata.idatt2003.group09.base.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.base.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.base.transaction.Sale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class SaleTest {

	private Stock stock;
	private Share share;
	private Sale sale;
	private Player player;

	@BeforeEach
	void setUp() {
		stock = new Stock("AAPL", "Apple", new BigDecimal("150.00"), "Technology", 4);
		share = new Share(stock, new BigDecimal("2"), new BigDecimal("10.00"));
		sale = new Sale(share, 1);
		player = new Player("Alice", new BigDecimal("1000.00"), "Easy");
	}

	@Test
	void commitShouldRemoveShareFromPortfolio() {
		player.getPortfolio().addShare(share);
		sale.commit(player);
		assertTrue(player.getPortfolio().getShares().isEmpty());
	}

	@Test
	void commitShouldMarkTransactionAsCommitted() {
		player.getPortfolio().addShare(share);
		sale.commit(player);
		assertTrue(sale.isCommited());
	}

	@Test
	void commitShouldThrowWhenCommittedTwice() {
		player.getPortfolio().addShare(share);
		sale.commit(player);
		assertThrows(IllegalStateException.class, () -> sale.commit(player));
	}

	@Test
	void commitShouldThrowWhenPlayerIsNull() {
		assertThrows(NullPointerException.class, () -> sale.commit(null));
	}

	@Test
	void commitShouldThrowWhenShareNotOwned() {
		assertThrows(IllegalStateException.class, () -> sale.commit(player));
	}
}
