package edu.ntnu.iir.bidata.idatt2003.group09.view.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.controller.GameController;
import edu.ntnu.iir.bidata.idatt2003.group09.model.transaction.Transaction;
import javafx.collections.FXCollections;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
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

                getStylesheets().add(getClass().getResource("/styling/transactionHistory.css").toExternalForm());
                getStyleClass().add("transaction-history-screen");

        table = new TableView<>();
                table.getStyleClass().add("transaction-history-table");

        buildTable();
        refresh();

        setCenter(table);
    }

    private void buildTable() {

        TableColumn<Transaction, Integer> weekCol = new TableColumn<>("Week");
        weekCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getWeek()).asObject()
        );

        TableColumn<Transaction, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getClass().getSimpleName().replace("Purchase", "Buy").replace("Sale", "Sell")
                )
        );

        typeCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                                if (!getStyleClass().contains("transaction-type-cell")) {
                                        getStyleClass().add("transaction-type-cell");
                                }

                if (empty || item == null) {
                    setText(null);
                                        getStyleClass().removeAll("positive", "negative");
                    return;
                }

                setText(item);

                if (item.equals("Buy")) {
                                        getStyleClass().remove("negative");
                                        if (!getStyleClass().contains("positive")) {
                                                getStyleClass().add("positive");
                                        }
                } else {
                                        getStyleClass().remove("positive");
                                        if (!getStyleClass().contains("negative")) {
                                                getStyleClass().add("negative");
                                        }
                }
            }
        });

        TableColumn<Transaction, String> tickerCol = new TableColumn<>("Ticker");
        tickerCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getShare().getStock().getSymbol()
                )
        );

        TableColumn<Transaction, String> qtyCol = new TableColumn<>("Qty");
        qtyCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getShare().getQuantity().toString()
                )
        );

        TableColumn<Transaction, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(data -> {
            var transaction = data.getValue();
            var share = transaction.getShare();

            if (transaction instanceof edu.ntnu.iir.bidata.idatt2003.group09.model.transaction.Sale) {
                return new javafx.beans.property.SimpleStringProperty(
                        currencyFormat.format(share.getStock().getSalesPrice())
                );
            } else {
                return new javafx.beans.property.SimpleStringProperty(
                        currencyFormat.format(share.getPurchasePrice())
                );
            }
        });

        TableColumn<Transaction, String> feesCol = new TableColumn<>("Total Fees");

        feesCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        currencyFormat.format(data.getValue().getFees()))
        );

        TableColumn<Transaction, String> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        currencyFormat.format(
                                data.getValue().getCalculator().calculateTotal()
                        )
                )
        );

        table.getColumns().addAll(weekCol, typeCol, tickerCol, qtyCol, priceCol, feesCol, totalCol);

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    }

    public void refresh() {
        table.setItems(FXCollections.observableArrayList(
                controller.getPlayer().getTransactionArchive().getAllTransactions()
        ));
    }
}