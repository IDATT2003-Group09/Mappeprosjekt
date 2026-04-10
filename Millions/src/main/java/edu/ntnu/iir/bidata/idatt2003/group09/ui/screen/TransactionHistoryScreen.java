package edu.ntnu.iir.bidata.idatt2003.group09.ui.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.base.transaction.Transaction;
import edu.ntnu.iir.bidata.idatt2003.group09.controller.GameController;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

import java.text.NumberFormat;
import java.util.Locale;

public class TransactionHistoryScreen extends BorderPane {

    private final GameController controller;
    private final TableView<Transaction> table;
    private final NumberFormat currencyFormat;

    public TransactionHistoryScreen(GameController controller) {
        this.controller = controller;
        this.currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

        table = new TableView<>();

        buildTable();
        refresh();

        setCenter(table);
    }
}
