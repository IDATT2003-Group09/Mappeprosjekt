package edu.ntnu.iir.bidata.idatt2003.group09.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.iir.bidata.idatt2003.group09.io.SaveManager;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Exchange;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Player;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.model.transaction.Sale;
import edu.ntnu.iir.bidata.idatt2003.group09.model.transaction.Transaction;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class GameControllerTest {

    private static final String SAVE_FILE = "test-game-controller-sell-all";

    @AfterEach
    void cleanUp() {
        SaveManager.deleteSaveFile(SAVE_FILE);
    }

    @Test
    void sellAllShares_recordsSalesInTransactionArchive() {
        Player player = createPlayerWithShares();
        GameController controller = createController(player);

        controller.sellAllShares("AAPL");

        List<Transaction> transactions = player.getTransactionArchive().getAllTransactions();
        assertEquals(2, transactions.size());
        assertTrue(transactions.stream().allMatch(Sale.class::isInstance));
        assertTrue(player.getPortfolio().getShares("AAPL").isEmpty());
        assertEquals(1, player.getPortfolio().getShares("MSFT").size());
    }

    @Test
    void sellAllShares_recordsSalesForAllOwnedShares() {
        Player player = createPlayerWithShares();
        GameController controller = createController(player);

        controller.sellAllShares();

        List<Transaction> transactions = player.getTransactionArchive().getAllTransactions();
        assertEquals(3, transactions.size());
        assertTrue(transactions.stream().allMatch(Sale.class::isInstance));
        assertTrue(player.getPortfolio().getShares().isEmpty());
    }

    private static GameController createController(Player player) {
        Stock apple = player.getPortfolio().getShares().stream()
            .filter(share -> share.getStock().getSymbol().equals("AAPL"))
            .findFirst()
            .orElseThrow()
            .getStock();
        Stock microsoft = player.getPortfolio().getShares().stream()
            .filter(share -> share.getStock().getSymbol().equals("MSFT"))
            .findFirst()
            .orElseThrow()
            .getStock();
        Exchange exchange = new Exchange("Test Exchange", List.of(apple, microsoft));
        return new GameController(exchange, player, SAVE_FILE);
    }

    private static Player createPlayerWithShares() {
        Player player = new Player("Alice", BigDecimal.valueOf(1000), "Easy");
        Stock apple = new Stock("AAPL", "Apple", new BigDecimal("150.00"), "Technology", 4);
        Stock microsoft = new Stock("MSFT", "Microsoft", new BigDecimal("300.00"), "Technology", 4);

        player.getPortfolio().addShare(new Share(apple, new BigDecimal("2"), new BigDecimal("120.00")));
        player.getPortfolio().addShare(new Share(apple, new BigDecimal("1"), new BigDecimal("130.00")));
        player.getPortfolio().addShare(new Share(microsoft, new BigDecimal("1"), new BigDecimal("250.00")));
        return player;
    }
}