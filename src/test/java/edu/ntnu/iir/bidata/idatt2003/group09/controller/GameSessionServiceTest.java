package edu.ntnu.iir.bidata.idatt2003.group09.controller;

import edu.ntnu.iir.bidata.idatt2003.group09.io.SaveManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameSessionServiceTest {

  private File tmpDir;

  @BeforeAll
  public static void init() {
    try {
      Class.forName("javafx.application.Platform");
    } catch (ClassNotFoundException ignored) {}
  }

  @AfterEach
  public void tearDown() throws Exception {
    if (tmpDir != null && tmpDir.exists()) {
      for (File f : tmpDir.listFiles()) f.delete();
      tmpDir.delete();
    }
    // clear overrideSaveDir via reflection
    try {
      Field f = SaveManager.class.getDeclaredField("overrideSaveDir");
      f.setAccessible(true);
      f.set(null, null);
    } catch (NoSuchFieldException ignored) {}
  }

  private void setOverrideSaveDir(File d) throws Exception {
    Field f = SaveManager.class.getDeclaredField("overrideSaveDir");
    f.setAccessible(true);
    f.set(null, d);
  }

  @Test
  void createTutorialSession_and_createNewSession_basic() throws Exception {
    tmpDir = Files.createTempDirectory("millions-session-test").toFile();
    setOverrideSaveDir(tmpDir);

    GameSessionService svc = new GameSessionService();
    var session = svc.createTutorialSession("Tester", "1000");
    assertNotNull(session);
    assertTrue(session.tutorialMode());

    var session2 = svc.createNewSession("Tester2", "Easy", "sp500", "5000");
    assertNotNull(session2);
    assertFalse(session2.tutorialMode());
  }

  @Test
  void loadSession_returnsEmpty_forMissingFile() throws Exception {
    tmpDir = Files.createTempDirectory("millions-session-test").toFile();
    setOverrideSaveDir(tmpDir);

    GameSessionService svc = new GameSessionService();
    var loaded = svc.loadSession("no-such-file-xyz");
    assertTrue(loaded.isEmpty());
  }
}
