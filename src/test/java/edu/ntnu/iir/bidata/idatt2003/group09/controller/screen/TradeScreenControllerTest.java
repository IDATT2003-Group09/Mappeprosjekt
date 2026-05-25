package edu.ntnu.iir.bidata.idatt2003.group09.controller.screen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import edu.ntnu.iir.bidata.idatt2003.group09.controller.GameController;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Exchange;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Player;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.model.screen.TradeScreenModel;
import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TradeScreenControllerTest {

  private Stock aapl;
  private Stock jpm;
  private Stock jnj;
  private Player player;
  private TradeScreenModel model;
  private TradeScreenController controller;

  @BeforeEach
  void setUp() {
    aapl = new Stock("AAPL", "Apple", new BigDecimal("150.00"), "Technology", 4);
    jpm = new Stock("JPM", "JPMorgan", new BigDecimal("200.00"), "Finance", 4);
    jnj = new Stock("JNJ", "Johnson & Johnson", new BigDecimal("100.00"), "Health", 4);

    Exchange exchange = new Exchange("Test Exchange", List.of(aapl, jpm, jnj));
    player = new Player("Test", new BigDecimal("10000.00"), "Normal");
    GameController gameController = new GameController(exchange, player);

    model = new TradeScreenModel(List.of(aapl, jpm, jnj));
    controller = new TradeScreenController(gameController, model);

    aapl.addNewSalesPrice(new BigDecimal("180.00"));
    jpm.addNewSalesPrice(new BigDecimal("180.00"));
    jnj.addNewSalesPrice(new BigDecimal("105.00"));
  }

  @Nested
  @DisplayName("applyFilters")
  public class ApplyFiltersTests {

    @Test
    @DisplayName("Owned and winners returns only owned positive movers in order")
    void applyFilters_ownedAndWinners_filtersAndSortsCorrectly() {
      player.getPortfolio().addShare(new Share(aapl, new BigDecimal("2"), new BigDecimal("150.00")));
      player.getPortfolio().addShare(new Share(jnj, new BigDecimal("1"), new BigDecimal("100.00")));

      controller.applyFilters(new TradeFilterRequest("", true, true, false));

      List<String> symbols = model.getFilteredStocks().stream().map(Stock::getSymbol).collect(Collectors.toList());
      assertEquals(List.of("AAPL", "JNJ"), symbols);
    }

    @Test
    @DisplayName("Owned and losers returns only owned negative movers")
    void applyFilters_ownedAndLosers_returnsOwnedLosersOnly() {
      player.getPortfolio().addShare(new Share(aapl, new BigDecimal("1"), new BigDecimal("150.00")));
      player.getPortfolio().addShare(new Share(jpm, new BigDecimal("1"), new BigDecimal("200.00")));

      controller.applyFilters(new TradeFilterRequest("", true, false, true));

      List<String> symbols = model.getFilteredStocks().stream().map(Stock::getSymbol).collect(Collectors.toList());
      assertEquals(List.of("JPM"), symbols);
    }

    @Test
    @DisplayName("Null search text is treated as empty")
    void applyFilters_nullSearchText_treatedAsEmpty() {
      controller.applyFilters(new TradeFilterRequest(null, false, false, false));

      assertEquals(3, model.getFilteredStocks().size());
    }
  }

  @Nested
  @DisplayName("handleAdvanceWeek")
  public class HandleAdvanceWeekTests {

    @Test
    @DisplayName("Runs only game-over callback when game ends")
    void handleAdvanceWeek_gameOver_runsOnlyGameOverCallback() {
      TradeScreenController localController = createControllerWithQueuedResult(GameController.WeekAdvanceResult.gameOverResult());

      AtomicInteger gameOverCalls = new AtomicInteger();
      AtomicInteger weekAdvancedCalls = new AtomicInteger();
      AtomicInteger quarterAdvancedCalls = new AtomicInteger();

      localController.handleAdvanceWeek(
          gameOverCalls::incrementAndGet,
          weekAdvancedCalls::incrementAndGet,
          result -> quarterAdvancedCalls.incrementAndGet());

      assertEquals(1, gameOverCalls.get());
      assertEquals(0, weekAdvancedCalls.get());
      assertEquals(0, quarterAdvancedCalls.get());
    }

    @Test
    @DisplayName("Runs week and quarter callbacks when quarter advances")
    void handleAdvanceWeek_quarterAdvanced_runsWeekAndQuarterCallbacks() {
      GameController.WeekAdvanceResult quarterResult =
          new GameController.WeekAdvanceResult(
              false,
              true,
              1,
              2,
              new BigDecimal("12000.00"),
              new BigDecimal("11000.00"),
              new BigDecimal("12500.00"));
      TradeScreenController localController = createControllerWithQueuedResult(quarterResult);

      AtomicInteger gameOverCalls = new AtomicInteger();
      AtomicInteger weekAdvancedCalls = new AtomicInteger();
      AtomicReference<GameController.WeekAdvanceResult> quarterCallbackResult = new AtomicReference<>();

      localController.handleAdvanceWeek(
          gameOverCalls::incrementAndGet,
          weekAdvancedCalls::incrementAndGet,
          quarterCallbackResult::set);

      assertEquals(0, gameOverCalls.get());
      assertEquals(1, weekAdvancedCalls.get());
      assertSame(quarterResult, quarterCallbackResult.get());
    }
  }

  private TradeScreenController createControllerWithQueuedResult(GameController.WeekAdvanceResult result) {
    Exchange exchange = new Exchange("Advance Test Exchange", List.of(aapl, jpm, jnj));
    Player advancePlayer = new Player("Advance", new BigDecimal("10000.00"), "Normal");
    StubGameController stubGameController = new StubGameController(exchange, advancePlayer);
    stubGameController.enqueue(result);

    TradeScreenModel localModel = new TradeScreenModel(List.of(aapl, jpm, jnj));
    return new TradeScreenController(stubGameController, localModel);
  }

  private static class StubGameController extends GameController {
    private final Deque<WeekAdvanceResult> queuedResults = new ArrayDeque<>();

    StubGameController(Exchange exchange, Player player) {
      super(exchange, player);
    }

    void enqueue(WeekAdvanceResult result) {
      queuedResults.addLast(result);
    }

    @Override
    public WeekAdvanceResult nextWeek() {
      return queuedResults.isEmpty() ? WeekAdvanceResult.noChange() : queuedResults.removeFirst();
    }
  }
}