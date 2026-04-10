package edu.ntnu.iir.bidata.idatt2003.group09.ui.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.base.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.base.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.controller.GameController;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.StockGraph;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.StockTable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TradeScreen extends BorderPane {

    private final GameController controller;
    private final Runnable onSaveAndQuit;

    private final TableView<Stock> stockTable;
    private final StockGraph graph;

    private final Label statusLabel;
    private final Label cashLabel;
    private final Label holdingsLabel;
    private final Label netWorthLabel;
    private final Label weekLabel;

    private final TextField quantityField;
    private final NumberFormat currencyFormat;

    private final Label levelLabel;
    private final Label requirementLabel;
    private final ProgressBar progressBar;
    private final Label levelUpLabel;
    private final Label deadlineLabel;
    private int lastLevel = 1;

    public TradeScreen(GameController controller, List<Stock> stocks, Runnable onSaveAndQuit) {
        this.controller = controller;
        this.onSaveAndQuit = onSaveAndQuit;
        this.currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

        getStylesheets().add(getClass().getResource("/styling/tradescreen.css").toExternalForm());
        getStyleClass().add("trade-screen");

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

        levelLabel = new Label();
        requirementLabel = new Label();
        progressBar = new ProgressBar();
        levelUpLabel = new Label();
        deadlineLabel = new Label();

        buildLayout();
        refreshInfo();
    }

    private void buildLayout() {

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

        HBox infoBox = new HBox(20, weekLabel, cashLabel, netWorthLabel, holdingsLabel);
        infoBox.setPadding(new Insets(0, 0, 10, 0));

        VBox headerBox = new VBox(
                8,
                levelLabel,
                requirementLabel,
                progressBar,
                levelUpLabel,
                deadlineLabel,

                infoBox,
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

        var progress = controller.getProgress();
        var player = controller.getPlayer();

        int currentLevel = progress.getCurrentLevelNumber(
                player.getNetWorth(),
                player.getStartingMoney()
        );

        deadlineLabel.setText("Deadline in: " +
                progress.getWeeksUntilDeadline() + " weeks");

        BigDecimal growth = player.getNetWorth()
                .subtract(player.getStartingMoney())
                .divide(player.getStartingMoney(), 4, RoundingMode.HALF_UP);

        BigDecimal required = progress.getBaseRequirement()
                .multiply(BigDecimal.valueOf(currentLevel));

        levelLabel.setText("Level " + currentLevel);

        requirementLabel.setText("Next Target: : " +
                currencyFormat.format(progress.getCurrentTarget()));

        double progressValue = 0;
        if (required.compareTo(BigDecimal.ZERO) > 0) {
            progressValue = growth
                    .divide(required, 4, RoundingMode.HALF_UP)
                    .doubleValue();
        }
        progressBar.setProgress(Math.min(progressValue, 1.0));

        if (currentLevel > lastLevel) {
            levelUpLabel.setText("Level Up! Now level " + currentLevel);

            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ignored) {}

                Platform.runLater(() -> levelUpLabel.setText(""));
            }).start();

            lastLevel = currentLevel;
        }
    }
}