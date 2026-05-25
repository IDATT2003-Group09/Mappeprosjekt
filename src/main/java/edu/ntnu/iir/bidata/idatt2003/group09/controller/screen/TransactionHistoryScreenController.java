package edu.ntnu.iir.bidata.idatt2003.group09.controller.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.controller.GameController;
import edu.ntnu.iir.bidata.idatt2003.group09.model.screen.TransactionHistoryScreenModel;
import edu.ntnu.iir.bidata.idatt2003.group09.model.transaction.Transaction;
import java.util.List;
import java.util.Objects;

/**
 * Controller for the transaction history screen.
 * Pulls domain data from game state and updates the screen model.
 */
public class TransactionHistoryScreenController {

    private final GameController gameController;
    private final TransactionHistoryScreenModel model;

    public TransactionHistoryScreenController(GameController gameController) {
        this.gameController = Objects.requireNonNull(gameController, "GameController cannot be null");
        this.model = new TransactionHistoryScreenModel();
    }

    public TransactionHistoryScreenModel getModel() {
        return model;
    }

    /**
     * Syncs latest transactions from game state into the view model.
     */
    public void refresh() {
        List<Transaction> history = gameController
            .getPlayer()
            .getTransactionArchive()
            .getAllTransactions();
        model.setTransactions(history);
    }
}
