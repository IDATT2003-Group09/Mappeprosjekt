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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

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
}
