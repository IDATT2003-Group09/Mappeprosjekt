package edu.ntnu.iir.bidata.idatt2003.group09.io;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.List;

import edu.ntnu.iir.bidata.idatt2003.group09.model.Exchange;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Player;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.model.game.GameProgress;

import static org.junit.jupiter.api.Assertions.*;

public class SaveManagerTest {

  private File tmpDir;

  @AfterEach
  void tearDown() {
    if (tmpDir != null && tmpDir.exists()) {
      for (File f : tmpDir.listFiles()) f.delete();
      tmpDir.delete();
    }
    SaveManager.overrideSaveDir = null;
  }

  @Test
  void normalizeSaveFileName_nullOrBlank_returnsDefaultPath() throws Exception {
    tmpDir = Files.createTempDirectory("millions-save-test").toFile();
    SaveManager.overrideSaveDir = tmpDir;

    String path = SaveManager.normalizeSaveFileName(null);
    assertTrue(path.endsWith("savegame.dat"));
    assertTrue(new File(path).getParentFile().equals(tmpDir));

    String path2 = SaveManager.normalizeSaveFileName("   ");
    assertTrue(path2.endsWith("savegame.dat"));
  }

  @Test
  void normalizeSaveFileName_sanitizesAndAddsPrefixSuffix() throws Exception {
    tmpDir = Files.createTempDirectory("millions-save-test").toFile();
    SaveManager.overrideSaveDir = tmpDir;

    String input = "my*weird:name?.txt";
    String normalized = SaveManager.normalizeSaveFileName(input);
    assertTrue(normalized.contains("savegame-"));
    assertTrue(normalized.endsWith(".dat"));
    assertFalse(normalized.contains("*"));
  }

  @Test
  void saveAndLoad_roundtrip() throws Exception {
    tmpDir = Files.createTempDirectory("millions-save-test").toFile();
    SaveManager.overrideSaveDir = tmpDir;

    Stock s = new Stock("TST", "Test", new BigDecimal("10"), "Tech", 2);
    Exchange ex = new Exchange("E", List.of(s));
    Player p = new Player("P", new BigDecimal("1000"), "Easy");

    GameProgress gp = new GameProgress(new BigDecimal("0.04"), p.getStartingMoney(), 1);
    GameState state = new GameState(p, ex, p.getNetWorth(), 1, p.getDifficulty(), false, gp);

    SaveManager.save(state, "unittest-save");

    GameState loaded = SaveManager.load("unittest-save");
    assertNotNull(loaded);
    assertEquals(p.getName(), loaded.getPlayer().getName());
    assertEquals(ex.getWeek(), loaded.getExchange().getWeek());
  }

  @Test
  void listAndDeleteSaveFiles() throws Exception {
    tmpDir = Files.createTempDirectory("millions-save-test").toFile();
    SaveManager.overrideSaveDir = tmpDir;

    Stock s = new Stock("TST", "Test", new BigDecimal("10"), "Tech", 2);
    Exchange ex = new Exchange("E", List.of(s));
    Player p = new Player("P", new BigDecimal("1000"), "Easy");

    GameProgress gp = new GameProgress(new BigDecimal("0.04"), p.getStartingMoney(), 1);
    GameState state = new GameState(p, ex, p.getNetWorth(), 1, p.getDifficulty(), false, gp);

    SaveManager.save(state, "a");
    SaveManager.save(state, "b");

    List<String> saves = SaveManager.listSaveFiles();
    assertTrue(saves.stream().anyMatch(n -> n.contains("savegame-a")));
    assertTrue(saves.stream().anyMatch(n -> n.contains("savegame-b")));

    SaveManager.deleteSaveFile("a");
    List<String> after = SaveManager.listSaveFiles();
    assertFalse(after.stream().anyMatch(n -> n.contains("savegame-a")));
  }
}
package edu.ntnu.iir.bidata.idatt2003.group09.io;

import static org.junit.jupiter.api.Assertions.*;

import edu.ntnu.iir.bidata.idatt2003.group09.model.Exchange;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Player;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.model.game.GameProgress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class SaveManagerTest {

    @TempDir
    Path tempDir;

    @AfterEach
    void resetSaveDir() {
        SaveManager.overrideSaveDir = null;
    }

    private void withTempSaveDir(ThrowingRunnable task) throws IOException {
        SaveManager.overrideSaveDir = tempDir.toFile();
        try {
            task.run();
        } catch (Exception e) {
            if (e instanceof IOException ioException) throw ioException;
            throw new RuntimeException(e);
        }
    }

    @Test
    void normalizeSaveFileNameShouldHandleNullBlankUnsafeAndExtensions() throws IOException {
        withTempSaveDir(() -> {
            assertTrue(SaveManager.normalizeSaveFileName(null).endsWith("savegame.dat"));
            assertTrue(SaveManager.normalizeSaveFileName("   ").endsWith("savegame.dat"));
            assertTrue(SaveManager.normalizeSaveFileName("my save").endsWith("savegame-my_save.dat"));
            assertTrue(SaveManager.normalizeSaveFileName("custom").endsWith("savegame-custom.dat"));
            assertTrue(SaveManager.normalizeSaveFileName("savegame-custom.dat").endsWith("savegame-custom.dat"));
        });
    }

    @Test
    void saveAndLoadShouldRoundTripGameState() throws IOException {
        withTempSaveDir(() -> {
            GameState original = createGameState("RoundTrip");

            SaveManager.save(original, "roundtrip");
            GameState loaded = SaveManager.load("roundtrip");

            assertNotNull(loaded);
            assertEquals("RoundTrip", loaded.getPlayer().getName());
            assertEquals("Normal", loaded.getDifficulty());
            assertEquals(0, new BigDecimal("2500.00").compareTo(loaded.getNetWorth()));
            assertEquals(3, loaded.getWeek());
            assertFalse(loaded.isLost());
            assertNotNull(loaded.getExchange());
            assertNotNull(loaded.getProgress());
        });
    }

    @Test
    void loadShouldReturnNullWhenFileDoesNotExist() throws IOException {
        withTempSaveDir(() -> {
            GameState loaded = SaveManager.load("missing-save");
            assertNull(loaded);
        });
    }

    @Test
    void loadShouldReturnNullWhenSerializedContentIsNotGameState() throws IOException {
        withTempSaveDir(() -> {
            String normalizedPath = SaveManager.normalizeSaveFileName("invalid-content");
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(normalizedPath))) {
                out.writeObject("not a game state");
            }

            GameState loaded = SaveManager.load("invalid-content");
            assertNull(loaded);
        });
    }

    @Test
    void doesSaveFileExistAndDeleteShouldReflectFileState() throws IOException {
        withTempSaveDir(() -> {
            String fileName = "to-delete";

            assertFalse(SaveManager.doesSaveFileExist(fileName));

            SaveManager.save(createGameState("DeleteMe"), fileName);
            assertTrue(SaveManager.doesSaveFileExist(fileName));

            SaveManager.deleteSaveFile(fileName);
            assertFalse(SaveManager.doesSaveFileExist(fileName));
        });
    }

    @Test
    void listSaveFilesAndNumberOfSaveFilesShouldTrackOnlySavegameDatFiles() throws IOException {
        withTempSaveDir(() -> {
            int baselineCount = SaveManager.numberOfSaveFiles();
            String suffix = String.valueOf(System.nanoTime());
            String firstSaveName = "one-" + suffix;
            String secondSaveName = "two-" + suffix;
            String normalizedFirst = SaveManager.normalizeSaveFileName(firstSaveName);
            String normalizedSecond = SaveManager.normalizeSaveFileName(secondSaveName);

            SaveManager.save(createGameState("One"), firstSaveName);
            SaveManager.save(createGameState("Two"), secondSaveName);
            Files.writeString(tempDir.resolve("notes.txt"), "ignore me");

            List<String> saves = SaveManager.listSaveFiles();

            assertEquals(baselineCount + 2, SaveManager.numberOfSaveFiles());
			String nameFirst = new File(normalizedFirst).getName();
			String nameSecond = new File(normalizedSecond).getName();
			assertTrue(saves.contains(nameFirst));
			assertTrue(saves.contains(nameSecond));
			assertFalse(saves.contains("notes.txt"));
        });
    }

    @Test
    void saveExistsShouldTrackDefaultFileLifecycle() throws IOException {
        withTempSaveDir(() -> {
            assertFalse(SaveManager.saveExists());

            SaveManager.save(createGameState("DefaultSave"));
            assertTrue(SaveManager.saveExists());

            SaveManager.deleteSaveFile("savegame");
            assertFalse(SaveManager.saveExists());
        });
    }

    private GameState createGameState(String playerName) {
        Player player = new Player(playerName, new BigDecimal("1000.00"), "Normal");
        Stock stock = new Stock("AAPL", "Apple Inc", new BigDecimal("100.00"), "Technology", 4);
        Exchange exchange = new Exchange("Test Exchange", List.of(stock));
        GameProgress progress = new GameProgress(new BigDecimal("0.10"), new BigDecimal("1000.00"));

        return new GameState(player, exchange, new BigDecimal("2500.00"), 3, "Normal", false, progress);
    }

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Exception;
    }
}