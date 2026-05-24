package edu.ntnu.iir.bidata.idatt2003.group09.controller;

import edu.ntnu.iir.bidata.idatt2003.group09.io.SaveManager;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Exchange;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Player;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.view.tutorial.TutorialOverlay;
import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TabPane;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

public class GameViewCoordinatorTest {

  @BeforeAll
  public static void initToolkit() throws Exception {
    try {
      Platform.startup(() -> {});
    } catch (IllegalStateException e) {
      // already started
    }
  }

  @AfterEach
  public void cleanup() {
    SaveManager.overrideSaveDir = null;
  }

  @Test
  void showGame_tutorialMode_startsTutorial_and_newspaperSelectionTriggersTutorial() throws Exception {
    BorderPane root = new BorderPane();

    AtomicBoolean started = new AtomicBoolean(false);
    AtomicBoolean newspaperViewed = new AtomicBoolean(false);

    TutorialOverlay overlay = new TutorialOverlay() {
      @Override public void startTutorial() { started.set(true); }
      @Override public void stopTutorial() { }
      @Override public void onNewspaperViewed() { newspaperViewed.set(true); }
      @Override public void onTransactionHistoryViewed() { }
      @Override public void onStockSelected() { }
      @Override public void onBuyButtonClicked() { }
      @Override public void onTradeScreenViewed() { }
      @Override public boolean isActive() { return true; }
    };

    AtomicBoolean returned = new AtomicBoolean(false);

    // prepare model/controller/session
    Stock s = new Stock("TST", "Test", new BigDecimal("10"), "Tech", 2);
    Exchange ex = new Exchange("E", List.of(s));
    Player p = new Player("P", new BigDecimal("1000"), "Easy");
    GameController controller = new GameController(ex, p, "testsave");
    GameSessionService.GameSession session = new GameSessionService.GameSession(controller, List.of(s), true);

    GameViewCoordinator coordinator = new GameViewCoordinator(root, overlay, () -> returned.set(true));

    CountDownLatch latch = new CountDownLatch(1);
    Platform.runLater(() -> {
      coordinator.showGame(session);
      latch.countDown();
    });

    assertTrue(latch.await(2, TimeUnit.SECONDS));
    assertTrue(started.get(), "tutorial should have started");

    // find TabPane and select Newspaper tab
    assertTrue(root.getCenter() instanceof TabPane);
    TabPane tp = (TabPane) root.getCenter();

    Optional.ofNullable(tp.getTabs().stream().filter(t -> "Newspaper".equals(t.getText())).findFirst().orElse(null))
        .ifPresent(tab -> {
          CountDownLatch latch2 = new CountDownLatch(1);
          Platform.runLater(() -> {
            tp.getSelectionModel().select(tab);
            latch2.countDown();
          });
          try { latch2.await(2, TimeUnit.SECONDS); } catch (InterruptedException ignored) {}
        });

    // allow listener to run
    Thread.sleep(100);
    assertTrue(newspaperViewed.get(), "onNewspaperViewed should have been called");
  }

  @Test
  void selectingSaveQuit_callsSave_and_runsReturnCallback() throws Exception {
    // set override save dir to temp
    File tmp = Files.createTempDirectory("millions-test-save").toFile();
    SaveManager.overrideSaveDir = tmp;

    BorderPane root = new BorderPane();
    TutorialOverlay overlay = new TutorialOverlay() {
      @Override public void startTutorial() { }
      @Override public void stopTutorial() { }
      @Override public void onNewspaperViewed() { }
      @Override public void onTransactionHistoryViewed() { }
      @Override public void onStockSelected() { }
      @Override public void onBuyButtonClicked() { }
      @Override public void onTradeScreenViewed() { }
      @Override public boolean isActive() { return false; }
    };

    AtomicBoolean returned = new AtomicBoolean(false);

    Stock s = new Stock("TST", "Test", new BigDecimal("10"), "Tech", 2);
    Exchange ex = new Exchange("E", List.of(s));
    Player p = new Player("P", new BigDecimal("1000"), "Easy");
    GameController controller = new GameController(ex, p, "my-player-save");
    GameSessionService.GameSession session = new GameSessionService.GameSession(controller, List.of(s), false);

    GameViewCoordinator coordinator = new GameViewCoordinator(root, overlay, () -> returned.set(true));

    CountDownLatch latch = new CountDownLatch(1);
    Platform.runLater(() -> { coordinator.showGame(session); latch.countDown(); });
    assertTrue(latch.await(2, TimeUnit.SECONDS));

    TabPane tp = (TabPane) root.getCenter();
    // find Save & Quit tab
    var saveTab = tp.getTabs().stream().filter(t -> t.getText() != null && t.getText().startsWith("Save & Quit")).findFirst().orElse(null);
    assertNotNull(saveTab);

    CountDownLatch latch2 = new CountDownLatch(1);
    Platform.runLater(() -> { tp.getSelectionModel().select(saveTab); latch2.countDown(); });
    assertTrue(latch2.await(2, TimeUnit.SECONDS));

    // wait for save to happen and callback
    Thread.sleep(200);
    assertTrue(returned.get(), "onReturnToStart should have been called");

    // check save file was created in override dir
    assertTrue(tmp.listFiles((d,n)->n.startsWith("savegame")).length > 0);
  }
}
