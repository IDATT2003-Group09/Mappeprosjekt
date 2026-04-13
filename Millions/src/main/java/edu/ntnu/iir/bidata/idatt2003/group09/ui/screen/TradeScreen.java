package edu.ntnu.iir.bidata.idatt2003.group09.ui.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.base.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.base.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.controller.GameController;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.StockGraph;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.StockListView;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.TutorialOverlay;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.UiSoundEffects;
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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;

public class TradeScreen extends BorderPane {

    private final GameController controller;
    private final Runnable onSaveAndQuit;
    private final boolean tutorialMode;
    private final TutorialOverlay tutorialOverlay;

    private final ListView<Stock> stockList;
    private final StockGraph graph;

    private final Label statusLabel;
    private final Label cashLabel;
    private final Label holdingsLabel;
    private final Label netWorthLabel;
    private final Label weekLabel;

    private final TextField quantityField;
    private final NumberFormat currencyFormat;

    // Removed levelLabel; Quartal label will reflect current quarter
    private final Label quarterLabel;
    private final Label requirementOverlayLabel;
    private final StackPane progressBarStack;
    private final ProgressBar progressBar;
    private final Label levelUpLabel;
    private final Label deadlineLabel;
    private int lastLevel = 1;

    public TradeScreen(GameController controller, List<Stock> stocks, Runnable onSaveAndQuit) {
        this(controller, stocks, onSaveAndQuit, false, null);
    }

    public TradeScreen(GameController controller, List<Stock> stocks, Runnable onSaveAndQuit, boolean tutorialMode) {
        this(controller, stocks, onSaveAndQuit, tutorialMode, null);
    }

    public TradeScreen(
        GameController controller,
        List<Stock> stocks,
        Runnable onSaveAndQuit,
        boolean tutorialMode,
        TutorialOverlay tutorialOverlay
    ) {
        this.controller = controller;
        this.onSaveAndQuit = onSaveAndQuit;
        this.tutorialMode = tutorialMode;
        this.tutorialOverlay = tutorialOverlay;
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
                        onTutorialStockSelected();
                    }
                }
        );

        quantityField = new TextField("1");
        quantityField.setPrefWidth(100);
        quantityField.getStyleClass().add("trade-quantity-field");

        statusLabel = new Label(tutorialMode
            ? "Tutorial mode: use S&P 500 stocks and practice buying/selling."
            : "Select a stock, then buy or sell.");
        statusLabel.getStyleClass().add("trade-status");
        cashLabel = new Label();
        holdingsLabel = new Label();
        netWorthLabel = new Label();
        weekLabel = new Label();

        // Removed levelLabel initialization
        quarterLabel = new Label();
        requirementOverlayLabel = new Label();
        progressBar = new ProgressBar();
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBarStack = new StackPane();
        HBox progressOverlay = new HBox();
        progressOverlay.setPadding(new Insets(0, 10, 0, 10));
        progressOverlay.setSpacing(10);
        progressOverlay.setAlignment(Pos.CENTER);
        HBox.setHgrow(quarterLabel, Priority.ALWAYS);
        HBox.setHgrow(requirementOverlayLabel, Priority.ALWAYS);
        quarterLabel.setMaxWidth(Double.MAX_VALUE);
        requirementOverlayLabel.setMaxWidth(Double.MAX_VALUE);
        quarterLabel.setAlignment(Pos.CENTER_LEFT);
        requirementOverlayLabel.setAlignment(Pos.CENTER_RIGHT);
        progressOverlay.getChildren().addAll(quarterLabel, requirementOverlayLabel);
        progressBarStack.getChildren().addAll(progressBar, progressOverlay);
        levelUpLabel = new Label();
        deadlineLabel = new Label();

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

        UiSoundEffects.installHoverSound(buyButton);
        UiSoundEffects.installHoverSound(sellButton);
        UiSoundEffects.installHoverSound(nextWeekButton);
        UiSoundEffects.installHoverSound(saveButton);
        UiSoundEffects.installClickSound(buyButton);
        UiSoundEffects.installClickSound(sellButton);
        UiSoundEffects.installClickSound(nextWeekButton);
        UiSoundEffects.installClickSound(saveButton);

        buyButton.setOnAction(e -> buySelectedStock());
        sellButton.setOnAction(e -> sellSelectedStock());

        nextWeekButton.setOnAction(e -> {
            controller.nextWeek();
            stockList.refresh();
            refreshInfo();
            updateSelectedStockGraph();
            onTutorialNextWeek();
        });

        HBox controls = new HBox(10, quantityLabel, quantityField, buyButton, sellButton, nextWeekButton, saveButton);
        controls.getStyleClass().add("trade-controls");
        controls.setPadding(new Insets(0, 0, 10, 0));

        HBox infoBox = new HBox(20, weekLabel, cashLabel, netWorthLabel, holdingsLabel);
        infoBox.getStyleClass().add("trade-info");
        infoBox.setPadding(new Insets(0, 0, 10, 0));

        VBox headerBox = new VBox(
            8,
            progressBarStack,
            levelUpLabel,
            deadlineLabel,

            infoBox,
            controls,
            statusLabel
        );

        headerBox.getStyleClass().add("trade-header");
        headerBox.setPadding(new Insets(10));
        setTop(headerBox);
        setPadding(new Insets(10));

        GridPane contentGrid = new GridPane();
        contentGrid.getStyleClass().add("trade-content-grid");

        ColumnConstraints leftColumn = new ColumnConstraints();
        leftColumn.setPercentWidth(50);
        leftColumn.setHgrow(Priority.ALWAYS);

        ColumnConstraints rightColumn = new ColumnConstraints();
        rightColumn.setPercentWidth(50);
        rightColumn.setHgrow(Priority.ALWAYS);

        RowConstraints row = new RowConstraints();
        row.setPercentHeight(100);
        row.setVgrow(Priority.ALWAYS);

        contentGrid.getColumnConstraints().addAll(leftColumn, rightColumn);
        contentGrid.getRowConstraints().add(row);

        stockList.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        graph.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        contentGrid.add(stockList, 0, 0);
        contentGrid.add(graph, 1, 0);

        setCenter(contentGrid);
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
            onTutorialBuySuccess();

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
            BigDecimal quantity = parseQuantity();
            controller.sell(selectedStock.getSymbol(), quantity);

            statusLabel.setText("Sold " + quantity.stripTrailingZeros().toPlainString()
                + " of " + selectedStock.getSymbol());
            onTutorialSellSuccess();

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

        var progress = controller.getProgress();
        var player = controller.getPlayer();



        int currentQuarter = (progress.getCurrentWeek() / 13) + 1;
        deadlineLabel.setText("Deadline in: " + progress.getWeeksUntilDeadline() + " weeks");

        BigDecimal growth = player.getNetWorth()
            .subtract(player.getStartingMoney())
            .divide(player.getStartingMoney(), 4, RoundingMode.HALF_UP);

        BigDecimal required = progress.getBaseRequirement()
            .multiply(BigDecimal.valueOf(progress.getCheckpointLevel()));

        quarterLabel.setText("Q" + currentQuarter);
        requirementOverlayLabel.setText("Requirement: " + currencyFormat.format(progress.getCurrentTarget()));

        double progressValue = 0;
        if (required.compareTo(BigDecimal.ZERO) > 0) {
            progressValue = growth
                .divide(required, 4, RoundingMode.HALF_UP)
                .doubleValue();
        }
        progressBar.setProgress(Math.min(progressValue, 1.0));

        int checkpointLevel = progress.getCheckpointLevel();
        if (checkpointLevel > lastLevel) {
            levelUpLabel.setText("Quarter Up! Now Q" + checkpointLevel);

            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ignored) {}

                Platform.runLater(() -> levelUpLabel.setText(""));
            }).start();

            lastLevel = checkpointLevel;
        }
    }

    private void onTutorialStockSelected() {
        if (!tutorialMode || tutorialOverlay == null) {
            return;
        }
        tutorialOverlay.onStockSelected();
    }

    private void onTutorialBuySuccess() {
        if (!tutorialMode || tutorialOverlay == null) {
            return;
        }
        tutorialOverlay.onBuySuccess();
    }

    private void onTutorialNextWeek() {
        if (!tutorialMode || tutorialOverlay == null) {
            return;
        }
        tutorialOverlay.onNextWeek();
    }

    private void onTutorialSellSuccess() {
        if (!tutorialMode || tutorialOverlay == null) {
            return;
        }
        tutorialOverlay.onSellSuccess();
    }
}