package edu.ntnu.iir.bidata.idatt2003.group09.io;

import edu.ntnu.iir.bidata.idatt2003.group09.controller.GameController;
import edu.ntnu.iir.bidata.idatt2003.group09.controller.GameSessionService;
import edu.ntnu.iir.bidata.idatt2003.group09.model.*;
import edu.ntnu.iir.bidata.idatt2003.group09.model.game.GameProgress;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    private Path tmpDir;

    @BeforeEach
    void setUp() throws Exception {
        tmpDir = Files.createTempDirectory("millions-gc-saves-");
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
    void nextWeek_advancesQuarter_whenRequirementMet() {
        Stock stock = new Stock("TST", "TestCo", new BigDecimal("10"), "Tech", 1);
        List<Stock> stocks = List.of(stock);
        Exchange exchange = new Exchange("TestEx", stocks);
        Player player = new Player("Alice", new BigDecimal("1000"), "Easy");

        GameProgress progress = new GameProgress(BigDecimal.ZERO, player.getStartingMoney(), 12);
        GameController gc = new GameController(exchange, player, "gc-test", progress);

        GameController.WeekAdvanceResult res = gc.nextWeek();

        assertFalse(gc.isLost());
        assertTrue(res.quarterAdvanced());
        assertNotNull(res.nextTarget());
    }

    @Test
    void nextWeek_setsGameOver_whenRequirementNotMet_and_runsCallback() {
        Stock stock = new Stock("TST", "TestCo", new BigDecimal("10"), "Tech", 1);
        List<Stock> stocks = List.of(stock);
        Exchange exchange = new Exchange("TestEx", stocks);
        Player player = new Player("Bob", new BigDecimal("1000"), "Easy");

        // make requirement very high so Bob fails
        GameProgress progress = new GameProgress(new BigDecimal("1.0"), player.getStartingMoney(), 12);
        GameController gc = new GameController(exchange, player, "gc-test", progress);

        AtomicBoolean called = new AtomicBoolean(false);
        gc.setOnGameOver(() -> called.set(true));

        GameController.WeekAdvanceResult res = gc.nextWeek();

        assertTrue(res.gameOver());
        assertTrue(gc.isLost());
        assertTrue(called.get());
    }

    @Test
    void sellAllShares_sellsPortfolioAndRecordsTransactions() {
        Stock stock = new Stock("TST", "TestCo", new BigDecimal("10"), "Tech", 1);
        List<Stock> stocks = List.of(stock);
        Exchange exchange = new Exchange("TestEx", stocks);
        Player player = new Player("Cara", new BigDecimal("1000"), "Easy");

        Share s = new Share(stock, new BigDecimal("5"), new BigDecimal("10"));
        boolean added = player.getPortfolio().addShare(s);
        assertTrue(added);

        GameController gc = new GameController(exchange, player, "gc-test");
        gc.sellAllShares();

        assertTrue(player.getPortfolio().getShares().isEmpty());
        assertFalse(player.getTransactionArchive().isEmpty());
        assertTrue(player.getMoney().compareTo(player.getStartingMoney()) > 0);
    }
}
