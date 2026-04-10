package edu.ntnu.iir.bidata.idatt2003.group09.ui.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.base.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.base.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.controller.GameController;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.StockGraph;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.StockListView;

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

public class TradeScreen extends BorderPane {

    private final GameController controller;
    private final Runnable onSaveAndQuit;

    private final ListView<Stock> stockList;
    private final StockGraph graph;

    private final Label statusLabel;
    private final Label cashLabel;
    private final Label holdingsLabel;
    private final Label netWorthLabel;
    private final Label weekLabel;

    private final TextField quantityField;
    private final NumberFormat currencyFormat;

    public TradeScreen(GameController controller, List<Stock> stocks, Runnable onSaveAndQuit) {
        this.controller = controller;
        this.onSaveAndQuit = onSaveAndQuit;
        this.currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

        getStylesheets().add(getClass().getResource("/styling/tradescreen.css").toExternalForm());
        getStyleClass().add("trade-screen");

        stockList = new StockListView().createStockList(controller.getPlayer());
        stockList.setItems(FXCollections.observableArrayList(stocks));
        graph = new StockGraph(stocks);
        graph.getStyleClass().add("trade-graph");
        stockList.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldStock, newStock) -> {
                    if (newStock != null) {
                        graph.updateChart(newStock);
                    }
                }
        );

        quantityField = new TextField("1");
        quantityField.setPrefWidth(100);
        quantityField.getStyleClass().add("trade-quantity-field");

        statusLabel = new Label("Select a stock, then buy or sell.");
        statusLabel.getStyleClass().add("trade-status");
        cashLabel = new Label();
        holdingsLabel = new Label();
        netWorthLabel = new Label();
        weekLabel = new Label();

        buildLayout();
        refreshInfo();
    }

    private void buildLayout() {

        Label quantityLabel = new Label("Quantity:");
        quantityLabel.getStyleClass().add("trade-quantity-label");

        Button buyButton = new Button("Buy");
        buyButton.getStyleClass().addAll("trade-button", "trade-buy-button");
        Button sellButton = new Button("Sell");
        sellButton.getStyleClass().addAll("trade-button", "trade-sell-button");
        Button nextWeekButton = new Button("Next Week");
        nextWeekButton.getStyleClass().addAll("trade-button", "trade-next-button");

        Button saveButton = new Button("Save and Quit");
        saveButton.getStyleClass().addAll("trade-button", "trade-save-button");
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
            stockList.refresh();
            refreshInfo();
            updateSelectedStockGraph();
        });

        HBox controls = new HBox(10, quantityLabel, quantityField, buyButton, sellButton, nextWeekButton, saveButton);
        controls.getStyleClass().add("trade-controls");
        controls.setPadding(new Insets(0, 0, 10, 0));

        HBox infoBox = new HBox(20, weekLabel, cashLabel, netWorthLabel, holdingsLabel);
        infoBox.getStyleClass().add("trade-info");
        infoBox.setPadding(new Insets(0, 0, 10, 0));

        VBox headerBox = new VBox(
                8,
                infoBox,
                controls,
                statusLabel
        );

        headerBox.getStyleClass().add("trade-header");
        headerBox.setPadding(new Insets(10));
        setTop(headerBox);
        setPadding(new Insets(10));

        SplitPane splitPane = new SplitPane();
        splitPane.getStyleClass().add("trade-split-pane");
        splitPane.getItems().addAll(stockList, graph);
        splitPane.setDividerPositions(0.3);

        setCenter(splitPane);
    }

    private void buySelectedStock() {
        Stock selectedStock = stockList.getSelectionModel().getSelectedItem();

        if (selectedStock == null) {
            statusLabel.setText("Please select a stock first.");
            return;
        }

        try {
            BigDecimal quantity = parseQuantity();

            controller.buy(selectedStock.getSymbol(), quantity);

            statusLabel.setText("Bought " + quantity + " of " + selectedStock.getSymbol());

            stockList.refresh();
            refreshInfo();

        } catch (Exception e) {
            statusLabel.setText("Buy failed: " + e.getMessage());
        }
    }

    private void sellSelectedStock() {
        Stock selectedStock = stockList.getSelectionModel().getSelectedItem();

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

            stockList.refresh();
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
        Stock selected = stockList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            graph.updateChart(selected);
        }
    }

    private void refreshInfo() {
        cashLabel.setText("Cash: " + currencyFormat.format(controller.getMoney()));
        netWorthLabel.setText("Net Worth: " + currencyFormat.format(controller.getNetWorth()));
        holdingsLabel.setText("Positions: " + controller.getPortfolio().getShares().size());
        weekLabel.setText("Week: " + controller.getWeek());
    }
}