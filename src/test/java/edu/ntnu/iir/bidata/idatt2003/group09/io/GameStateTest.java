package edu.ntnu.iir.bidata.idatt2003.group09.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.iir.bidata.idatt2003.group09.model.Exchange;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Player;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.model.game.GameProgress;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

public class GameStateTest {

	@Test
	void constructorShouldExposeAllValuesThroughGetters() {
		Player player = createPlayer();
		Exchange exchange = createExchange();
		GameProgress progress = createProgress();
		BigDecimal netWorth = new BigDecimal("12500.50");
		int week = 7;
		String difficulty = "Hard";
		boolean lost = true;

		GameState state = new GameState(player, exchange, netWorth, week, difficulty, lost, progress);

		assertSame(player, state.getPlayer());
		assertSame(exchange, state.getExchange());
		assertSame(progress, state.getProgress());
		assertEquals(0, netWorth.compareTo(state.getNetWorth()));
		assertEquals(week, state.getWeek());
		assertEquals(difficulty, state.getDifficulty());
		assertTrue(state.isLost());
	}

	@Test
	void constructorShouldAllowNullReferencesAndKeepPrimitiveValues() {
		GameState state = new GameState(null, null, null, 1, null, false, null);

		assertNull(state.getPlayer());
		assertNull(state.getExchange());
		assertNull(state.getNetWorth());
		assertNull(state.getDifficulty());
		assertNull(state.getProgress());
		assertEquals(1, state.getWeek());
		assertFalse(state.isLost());
	}

	@Test
	void gameStateShouldBeSerializable() {
		GameState state =
				new GameState(
						createPlayer(),
						createExchange(),
						BigDecimal.TEN,
						2,
						"Easy",
						false,
						createProgress());

		assertTrue(state instanceof Serializable);
	}

	private Player createPlayer() {
		return new Player("TestPlayer", new BigDecimal("1000.00"), "Normal");
	}

	private Exchange createExchange() {
		Stock stock = new Stock("AAPL", "Apple Inc", new BigDecimal("100.00"), "Technology", 4);
		return new Exchange("Test Exchange", List.of(stock));
	}

	private GameProgress createProgress() {
		return new GameProgress(new BigDecimal("0.10"), new BigDecimal("1000.00"));
	}
}
