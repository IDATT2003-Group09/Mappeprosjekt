package edu.ntnu.iir.bidata.idatt2003.group09.model.screen;

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
     * Updates observable rows from transaction history.
     */
    public void setTransactions(List<Transaction> transactionHistory) {
        List<Transaction> reversed = new ArrayList<>(transactionHistory);
        Collections.reverse(reversed);
        transactions.setAll(reversed);
    }
}