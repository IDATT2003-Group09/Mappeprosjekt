package edu.ntnu.iir.bidata.idatt2003.group09.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.iir.bidata.idatt2003.group09.model.Exchange;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Player;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.model.game.GameProgress;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class SaveManagerTest {

	@TempDir
	Path tempDir;

	@Test
	void normalizeSaveFileNameShouldHandleNullBlankUnsafeAndExtensions() {
		assertEquals("savegame.dat", SaveManager.normalizeSaveFileName(null));
		assertEquals("savegame.dat", SaveManager.normalizeSaveFileName("   "));
		assertEquals("savegame-my_save.dat", SaveManager.normalizeSaveFileName("my save"));
		assertEquals("savegame-custom.dat", SaveManager.normalizeSaveFileName("custom"));
		assertEquals("savegame-custom.dat", SaveManager.normalizeSaveFileName("savegame-custom.dat"));
	}

	@Test
	void saveAndLoadShouldRoundTripGameState() throws IOException {
		withWorkingDirectory(tempDir, () -> {
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
		withWorkingDirectory(tempDir, () -> {
			GameState loaded = SaveManager.load("missing-save");
			assertNull(loaded);
		});
	}

	@Test
	void doesSaveFileExistAndDeleteShouldReflectFileState() throws IOException {
		withWorkingDirectory(tempDir, () -> {
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
		withWorkingDirectory(tempDir, () -> {
			int baselineCount = SaveManager.numberOfSaveFiles();
			String suffix = String.valueOf(System.nanoTime());
			String firstSaveName = "one-" + suffix;
			String secondSaveName = "two-" + suffix;
			String normalizedFirst = SaveManager.normalizeSaveFileName(firstSaveName);
			String normalizedSecond = SaveManager.normalizeSaveFileName(secondSaveName);

			SaveManager.save(createGameState("One"), firstSaveName);
			SaveManager.save(createGameState("Two"), secondSaveName);
			Files.writeString(Path.of("notes.txt"), "ignore me");

			List<String> saves = SaveManager.listSaveFiles();

			assertEquals(baselineCount + 2, SaveManager.numberOfSaveFiles());
			assertTrue(saves.contains(normalizedFirst));
			assertTrue(saves.contains(normalizedSecond));
			assertFalse(saves.contains("notes.txt"));
		});
	}

	@Test
	void saveExistsShouldTrackDefaultFileLifecycle() throws IOException {
		withWorkingDirectory(tempDir, () -> {
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

		return new GameState(
				player,
				exchange,
				new BigDecimal("2500.00"),
				3,
				"Normal",
				false,
				progress);
	}

	private void withWorkingDirectory(Path dir, ThrowingRunnable task) throws IOException {
		String previousDir = System.getProperty("user.dir");
		System.setProperty("user.dir", dir.toAbsolutePath().toString());
		try {
			task.run();
		} catch (Exception e) {
			if (e instanceof IOException ioException) {
				throw ioException;
			}
			throw new RuntimeException(e);
		} finally {
			System.setProperty("user.dir", previousDir);
		}
	}

	@FunctionalInterface
	private interface ThrowingRunnable {
		void run() throws Exception;
	}
}
