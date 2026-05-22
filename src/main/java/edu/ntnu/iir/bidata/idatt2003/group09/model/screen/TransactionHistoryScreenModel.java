package edu.ntnu.iir.bidata.idatt2003.group09.model.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.controller.GameController;
import edu.ntnu.iir.bidata.idatt2003.group09.model.transaction.Transaction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Model for TransactionHistoryScreen, exposing observable transaction rows.
 */
public class TransactionHistoryScreenModel {

    private final ObservableList<Transaction> transactions = FXCollections.observableArrayList();

    public ObservableList<Transaction> transactionsProperty() {
        return transactions;
    }

    /**
     * Pulls transaction history from game state and updates observable rows.
     */
    public void updateFromGameState(GameController controller) {
        List<Transaction> reversed = new ArrayList<>(
            controller.getPlayer().getTransactionArchive().getAllTransactions()
        );
        Collections.reverse(reversed);
        transactions.setAll(reversed);
    }
}