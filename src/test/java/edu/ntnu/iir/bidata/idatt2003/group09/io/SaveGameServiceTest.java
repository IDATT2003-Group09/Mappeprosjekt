package edu.ntnu.iir.bidata.idatt2003.group09.io;

import edu.ntnu.iir.bidata.idatt2003.group09.model.Exchange;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Player;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.model.game.GameProgress;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SaveGameServiceTest {

    private Path tmpDir;

    @BeforeEach
    void setUp() throws Exception {
        tmpDir = Files.createTempDirectory("millions-test-saves-");
        SaveManager.overrideSaveDir = tmpDir.toFile();
    }

    @AfterEach
    void tearDown() throws Exception {
        SaveManager.overrideSaveDir = null;
        if (tmpDir != null && Files.exists(tmpDir)) {
            Files.walk(tmpDir)
                .map(Path::toFile)
                .sorted((a, b) -> -a.compareTo(b))
                .forEach(File::delete);
        }
    }

    @Test
    void listSaveGames_returnsSavedInfo() {
        Stock stock = new Stock("TST", "TestCo", new BigDecimal("10"), "Tech", 1);
        List<Stock> stocks = List.of(stock);
        Exchange exchange = new Exchange("TestExchange", stocks);
        Player player = new Player("Alice", new BigDecimal("1000"), "Medium");
        GameProgress progress = new GameProgress(new BigDecimal("0.03"), player.getStartingMoney(), 5);

        GameState state = new GameState(player, exchange, player.getNetWorth(), 5, "Medium", false, progress);
        SaveManager.save(state, "alice");

        SaveGameService svc = new SaveGameService();
        List<SaveGameInfo> list = svc.listSaveGames();

        assertEquals(1, list.size());
        SaveGameInfo info = list.get(0);
        assertEquals("alice", info.displayName());
        assertEquals("Week: 5", info.weekText());
        assertEquals("Medium", info.difficultyText());
        assertFalse(info.lost());
    }

    @Test
    void listSaveGames_handlesCorruptFileGracefully() throws Exception {
        // create a file with the normalized save name but non-serialized content
        String fileName = SaveManager.normalizeSaveFileName("corrupt");
        Path p = Path.of(fileName);
        Files.writeString(p, "this is not a serialized object");

        SaveGameService svc = new SaveGameService();
        List<SaveGameInfo> list = svc.listSaveGames();

        assertEquals(1, list.size());
        SaveGameInfo info = list.get(0);
        assertEquals("Could not read save", info.weekText());
    }
}
