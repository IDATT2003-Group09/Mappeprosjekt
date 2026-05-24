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