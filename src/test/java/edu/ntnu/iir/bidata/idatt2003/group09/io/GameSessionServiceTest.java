package edu.ntnu.iir.bidata.idatt2003.group09.io;

import edu.ntnu.iir.bidata.idatt2003.group09.model.Exchange;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Player;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.model.game.GameProgress;
import edu.ntnu.iir.bidata.idatt2003.group09.controller.GameSessionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GameSessionServiceTest {

    private Path tmpDir;

    @BeforeEach
    void setUp() throws Exception {
        tmpDir = Files.createTempDirectory("millions-session-saves-");
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
    void loadSession_returnsEmptyWhenNoFile() {
        GameSessionService svc = new GameSessionService();
        Optional<GameSessionService.GameSession> session = svc.loadSession("doesnotexist");
        assertTrue(session.isEmpty());
    }

    @Test
    void loadSession_returnsSessionWhenValidSave() throws Exception {
        Stock stock = new Stock("TST", "TestCo", new BigDecimal("10"), "Tech", 1);
        List<Stock> stocks = List.of(stock);
        Exchange exchange = new Exchange("TestExchange", stocks);
        Player player = new Player("Bob", new BigDecimal("1000"), "Easy");
        GameProgress progress = new GameProgress(new BigDecimal("0.03"), player.getStartingMoney(), 2);

        GameState state = new GameState(player, exchange, player.getNetWorth(), 2, "Easy", false, progress);
        SaveManager.save(state, "bob");

        GameSessionService svc = new GameSessionService();
        Optional<GameSessionService.GameSession> session = svc.loadSession("bob");

        assertTrue(session.isPresent());
        GameSessionService.GameSession gs = session.get();
        assertNotNull(gs.controller());
        assertEquals(1, gs.stocks().size());
        assertEquals("TST", gs.stocks().get(0).getSymbol());
    }
}
