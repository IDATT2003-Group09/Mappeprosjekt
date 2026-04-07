package edu.ntnu.iir.bidata.idatt2003.group09.ui.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.base.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.base.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.controller.GameController;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.StockGraph;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.StockTable;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class tradeScreen extends BorderPane {

    private final GameController controller;
    private final Runnable onSaveAndQuit;

    private final TableView<Stock> stockTable;
    private final StockGraph graph;

    private final Label statusLabel;
    private final Label cashLabel;
    private final Label holdingsLabel;
    private final Label netWorthLabel;
    private final Label weekLabel;
    private final Label newsLabel;

    private final TextField quantityField;
    private final NumberFormat currencyFormat;

    public tradeScreen(GameController controller, List<Stock> stocks, Runnable onSaveAndQuit) {
        this.controller = controller;
        this.onSaveAndQuit = onSaveAndQuit;
        this.currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

        stockTable = new StockTable().createStockTable(controller.getPlayer());
        stockTable.setItems(FXCollections.observableArrayList(stocks));
        graph = new StockGraph(stocks);
        stockTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldStock, newStock) -> {
                    if (newStock != null) {
                        graph.updateChart(newStock);
                    }
                }
        );

        quantityField = new TextField("1");
        quantityField.setPrefWidth(100);

        statusLabel = new Label("Select a stock, then buy or sell.");
        cashLabel = new Label();
        holdingsLabel = new Label();
        netWorthLabel = new Label();
        weekLabel = new Label();
        newsLabel = new Label();

        buildLayout();
        refreshInfo();
    }

    private void buildLayout() {
        Label titleLabel = new Label("Trade Stocks");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label quantityLabel = new Label("Quantity:");

        Button buyButton = new Button("Buy");
        Button sellButton = new Button("Sell");
        Button nextWeekButton = new Button("Next Week");

        Button saveButton = new Button("Save and Quit");
        saveButton.setOnAction(e -> {
            controller.saveGame();
            if (onSaveAndQuit != null) {
                onSaveAndQuit.run();
            }
        });

        buyButton.setOnAction(e -> buySelectedStock());
        sellButton.setOnAction(e -> sellSelectedStock());

        nextWeekButton.setOnAction(e -> {
            controller.nextWeek();
            stockTable.refresh();
            refreshInfo();
            updateSelectedStockGraph();
        });

        HBox controls = new HBox(10, quantityLabel, quantityField, buyButton, sellButton, nextWeekButton, saveButton);
        controls.setPadding(new Insets(0, 0, 10, 0));

        VBox headerBox = new VBox(
                8,
                titleLabel,
                weekLabel,
                cashLabel,
                netWorthLabel,
                holdingsLabel,
                newsLabel,
                controls,
                statusLabel
        );

        headerBox.setPadding(new Insets(10));
        setTop(headerBox);
        setPadding(new Insets(10));

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(stockTable, graph);
        splitPane.setDividerPositions(0.3);
        setCenter(splitPane);
    }

    private void buySelectedStock() {
        Stock selectedStock = stockTable.getSelectionModel().getSelectedItem();

        if (selectedStock == null) {
            statusLabel.setText("Please select a stock first.");
            return;
        }

        try {
            BigDecimal quantity = parseQuantity();

            controller.buy(selectedStock.getSymbol(), quantity);

            statusLabel.setText("Bought " + quantity + " of " + selectedStock.getSymbol());

            stockTable.refresh();
            refreshInfo();

        } catch (Exception e) {
            statusLabel.setText("Buy failed: " + e.getMessage());
        }
    }

    private void sellSelectedStock() {
        Stock selectedStock = stockTable.getSelectionModel().getSelectedItem();

        if (selectedStock == null) {
            statusLabel.setText("Please select a stock first.");
            return;
        }

        List<Share> shares = controller.getPortfolio().getShares(selectedStock.getSymbol());

        if (shares.isEmpty()) {
            statusLabel.setText("You do not own this stock.");
            return;
        }

        try {
            controller.sell(shares.getFirst());

            statusLabel.setText("Sold " + selectedStock.getSymbol());

            stockTable.refresh();
            refreshInfo();

        } catch (Exception e) {
            statusLabel.setText("Sell failed: " + e.getMessage());
        }
    }

    private BigDecimal parseQuantity() {
        try {
            BigDecimal quantity = new BigDecimal(quantityField.getText().trim());

            if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Quantity must be > 0");
            }

            return quantity;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number");
        }
    }

    private void updateSelectedStockGraph() {
        Stock selected = stockTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            graph.updateChart(selected);
        }
    }

    private void refreshInfo() {
        cashLabel.setText("Cash: " + currencyFormat.format(controller.getMoney()));
        netWorthLabel.setText("Net Worth: " + currencyFormat.format(controller.getNetWorth()));
        holdingsLabel.setText("Positions: " + controller.getPortfolio().getShares().size());
        weekLabel.setText("Week: " + controller.getWeek());
        newsLabel.setText("News: " + controller.getLatestNews());
    }
}