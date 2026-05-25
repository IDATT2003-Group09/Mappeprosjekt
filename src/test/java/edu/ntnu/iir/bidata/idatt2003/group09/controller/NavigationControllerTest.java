package edu.ntnu.iir.bidata.idatt2003.group09.controller;

import edu.ntnu.iir.bidata.idatt2003.group09.view.screen.LoadGameScreen;
import edu.ntnu.iir.bidata.idatt2003.group09.view.screen.StartScreen;
import edu.ntnu.iir.bidata.idatt2003.group09.view.tutorial.TutorialOverlay;
import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import edu.ntnu.iir.bidata.idatt2003.group09.view.screen.CreateGameScreen;

import static org.junit.jupiter.api.Assertions.*;

public class NavigationControllerTest {

  @BeforeAll
  public static void initToolkit() throws Exception {
    try {
      Platform.startup(() -> {});
    } catch (IllegalStateException e) {
      // already started
    }
  }

  @Test
  void showStartScreen_stopsTutorial_and_setsCenter() throws Exception {
    BorderPane root = new BorderPane();

    AtomicBoolean stopped = new AtomicBoolean(false);
    TutorialOverlay overlay = new TutorialOverlay() {
      @Override public void stopTutorial() { stopped.set(true); }
    };

    NavigationController nav = new NavigationController(root, overlay);

    CountDownLatch latch = new CountDownLatch(1);
    Platform.runLater(() -> {
      nav.showStartScreen();
      latch.countDown();
    });

    assertTrue(latch.await(2, TimeUnit.SECONDS));
    assertTrue(stopped.get(), "tutorialOverlay.stopTutorial should be called");
    assertTrue(root.getCenter() instanceof StartScreen, "center should be StartScreen");
  }

  @Test
  void loadGame_whenSessionMissing_showsLoadScreen() throws Exception {
    BorderPane root = new BorderPane();
    TutorialOverlay overlay = new TutorialOverlay();

    NavigationController nav = new NavigationController(root, overlay);

    // inject a stub GameSessionService that returns empty for loadSession
    GameSessionService stubService = new GameSessionService() {
      @Override public Optional<GameSession> loadSession(String fileName) {
        return Optional.empty();
      }
    };

    // replace private field via reflection
    Field svcField = NavigationController.class.getDeclaredField("gameSessionService");
    svcField.setAccessible(true);
    svcField.set(nav, stubService);

    CountDownLatch latch = new CountDownLatch(1);
    Platform.runLater(() -> {
      try {
        // invoke private loadGame via reflection
        var m = NavigationController.class.getDeclaredMethod("loadGame", String.class);
        m.setAccessible(true);
        m.invoke(nav, "does-not-exist");
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      latch.countDown();
    });

    assertTrue(latch.await(2, TimeUnit.SECONDS));
    assertTrue(root.getCenter() instanceof LoadGameScreen, "center should be LoadGameScreen when load missing");
  }

  @Test
  void startTutorialGame_invokesShowGame() throws Exception {
    BorderPane root = new BorderPane();

    AtomicBoolean shown = new AtomicBoolean(false);
    TutorialOverlay overlay = new TutorialOverlay();

    NavigationController nav = new NavigationController(root, overlay);

    // stub GameSessionService to return a session
    GameSessionService stubSvc = new GameSessionService() {
      @Override
      public GameSession createTutorialSession(String playerName, String startingMoney) {
        GameController ctrl = new GameController(new edu.ntnu.iir.bidata.idatt2003.group09.model.Exchange("E", List.of(new edu.ntnu.iir.bidata.idatt2003.group09.model.Stock("TST","Test", new java.math.BigDecimal("10"), "Tech", 2))), new edu.ntnu.iir.bidata.idatt2003.group09.model.Player("P", new java.math.BigDecimal("1000"), "Easy"));
        return new GameSession(ctrl, List.of(), true);
      }
    };

    // stub coordinator
    GameViewCoordinator stubCoord = new GameViewCoordinator(root, overlay, () -> {}) {
      @Override public void showGame(GameSessionService.GameSession session) { shown.set(true); }
    };

    // inject
    var f1 = NavigationController.class.getDeclaredField("gameSessionService");
    var f2 = NavigationController.class.getDeclaredField("gameViewCoordinator");
    f1.setAccessible(true); f2.setAccessible(true);
    f1.set(nav, stubSvc); f2.set(nav, stubCoord);

    var m = NavigationController.class.getDeclaredMethod("startTutorialGame", String.class, String.class);
    m.setAccessible(true);

    CountDownLatch latch = new CountDownLatch(1);
    Platform.runLater(() -> {
      try { m.invoke(nav, "player", "1000"); } catch (Exception e) { throw new RuntimeException(e); }
      latch.countDown();
    });

    assertTrue(latch.await(2, TimeUnit.SECONDS));
    assertTrue(shown.get(), "gameViewCoordinator.showGame should have been called");
  }

  @Test
  void startNewGame_invokesShowGame_and_handlesCustomCsvError() throws Exception {
    BorderPane root = new BorderPane();
    TutorialOverlay overlay = new TutorialOverlay();

    NavigationController nav = new NavigationController(root, overlay);

    AtomicBoolean shown = new AtomicBoolean(false);
    // stub svc: createNewSession returns normally for normal flow
    GameSessionService stubSvc = new GameSessionService() {
      @Override
      public GameSession createNewSession(String playerName, String experienceLevel, String exchangeChoice, String startingMoney) throws java.io.IOException {
        if (exchangeChoice != null && exchangeChoice.startsWith("custom:")) {
          throw new java.io.IOException("bad csv");
        }
        GameController ctrl = new GameController(new edu.ntnu.iir.bidata.idatt2003.group09.model.Exchange("E", List.of(new edu.ntnu.iir.bidata.idatt2003.group09.model.Stock("TST","Test", new java.math.BigDecimal("10"), "Tech", 2))), new edu.ntnu.iir.bidata.idatt2003.group09.model.Player("P", new java.math.BigDecimal("1000"), "Easy"));
        return new GameSession(ctrl, List.of(), false);
      }
    };

    GameViewCoordinator stubCoord = new GameViewCoordinator(root, overlay, () -> {}) {
      @Override public void showGame(GameSessionService.GameSession session) { shown.set(true); }
    };

    var f1 = NavigationController.class.getDeclaredField("gameSessionService");
    var f2 = NavigationController.class.getDeclaredField("gameViewCoordinator");
    f1.setAccessible(true); f2.setAccessible(true);
    f1.set(nav, stubSvc); f2.set(nav, stubCoord);

    var m = NavigationController.class.getDeclaredMethod("startNewGame", String.class, String.class, String.class, String.class);
    m.setAccessible(true);

    // normal path -> should call showGame
    CountDownLatch latch1 = new CountDownLatch(1);
    Platform.runLater(() -> {
      try { m.invoke(nav, "p", "Easy", "sp500", "1000"); } catch (Exception e) { throw new RuntimeException(e); }
      latch1.countDown();
    });
    assertTrue(latch1.await(2, TimeUnit.SECONDS));
    assertTrue(shown.get());

    // custom CSV error -> should show CreateGameScreen
    CountDownLatch latch2 = new CountDownLatch(1);
    Platform.runLater(() -> {
      try { m.invoke(nav, "p2", "Easy", "custom:/nope.csv", "1000"); } catch (Exception e) { throw new RuntimeException(e); }
      latch2.countDown();
    });
    assertTrue(latch2.await(2, TimeUnit.SECONDS));
    assertTrue(root.getCenter() instanceof CreateGameScreen, "center should be CreateGameScreen after custom csv error");
  }
}
