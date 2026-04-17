package edu.ntnu.iir.bidata.idatt2003.group09.view.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.controller.GameController;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.model.calculator.PurchaseCalculator;
import edu.ntnu.iir.bidata.idatt2003.group09.model.screen.TradeScreenModel;
import edu.ntnu.iir.bidata.idatt2003.group09.view.elements.StockGraph;
import edu.ntnu.iir.bidata.idatt2003.group09.view.elements.StockListView;
import edu.ntnu.iir.bidata.idatt2003.group09.view.elements.TransactionOverview;
import edu.ntnu.iir.bidata.idatt2003.group09.view.sound.UiSoundEffects;
import edu.ntnu.iir.bidata.idatt2003.group09.view.tutorial.TutorialOverlay;
import javafx.collections.FXCollections;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.Set;
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
import javafx.collections.ObservableList;

import edu.ntnu.iir.bidata.idatt2003.group09.controller.screen.TradeScreenController;

public class TradeScreenView extends StackPane {

    private final GameController controller;
    private final TradeScreenController tradeScreenController;
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

    private final Label quarterLabel;
    private final Label requirementOverlayLabel;
    private final Label netWorthOverlayLabel;
    private final StackPane progressBarStack;
    private final ProgressBar progressBar;
    private final Label deadlineLabel;

    private TextField searchField;
    private ObservableList<Stock> filteredStocks;
    private final HBox sectorButtonContainer;
    private final TradeScreenModel tradeScreenModel;


    public TradeScreenView(GameController controller, List<Stock> stocks, Runnable onSaveAndQuit) {
        this(controller, stocks, onSaveAndQuit, false, null);
    }

    public TradeScreenView(GameController controller, List<Stock> stocks, Runnable onSaveAndQuit, boolean tutorialMode) {
        this(controller, stocks, onSaveAndQuit, tutorialMode, null);
    }

    private final StackPane overlayPane = new StackPane();
    private TransactionOverview transactionOverviewOverlay = null;
    private QuarterLevelUpOverlay quarterLevelUpOverlay = null;
    private VBox headerBox;
    private GridPane contentGrid;

    public TradeScreenView(
        GameController controller,
        List<Stock> stocks,
        Runnable onSaveAndQuit,
        boolean tutorialMode,
        TutorialOverlay tutorialOverlay
    ) {
        this.controller = controller;
        this.tradeScreenController = new TradeScreenController(controller);
        this.tradeScreenModel = new TradeScreenModel(stocks);
        this.onSaveAndQuit = onSaveAndQuit;
        this.tutorialMode = tutorialMode;
        this.tutorialOverlay = tutorialOverlay;
        this.currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

        getStylesheets().add(getClass().getResource("/styling/tradescreen.css").toExternalForm());
        getStyleClass().add("trade-screen");

        stockList = new StockListView().createStockList(controller.getPlayer());
        sectorButtonContainer = new HBox(10);
        sectorButtonContainer.getStyleClass().add("trade-sector-container");
        createSectorFilters();

        this.filteredStocks = FXCollections.observableArrayList(tradeScreenModel.getAllStocks());
        stockList.setItems(filteredStocks);

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

        quarterLabel = new Label();
        requirementOverlayLabel = new Label();
        netWorthOverlayLabel = new Label();
        quarterLabel.getStyleClass().add("trade-progress-bar-label");
        requirementOverlayLabel.getStyleClass().add("trade-progress-bar-label");
        netWorthOverlayLabel.getStyleClass().add("trade-progress-bar-label");
        quarterLabel.setStyle("-fx-text-fill: white;");
        requirementOverlayLabel.setStyle("-fx-text-fill: white;");
        netWorthOverlayLabel.setStyle("-fx-text-fill: white;");
        progressBar = new ProgressBar(0);
        progressBar.getStyleClass().add("trade-progress-bar");
        progressBarStack = new StackPane();
        HBox progressOverlay = new HBox();
        progressOverlay.getStyleClass().add("trade-progress-bar-overlay");
        progressOverlay.setPadding(new Insets(0, 10, 0, 10));
        progressOverlay.setSpacing(10);
        progressOverlay.setAlignment(Pos.CENTER);
        HBox.setHgrow(quarterLabel, Priority.ALWAYS);
        HBox.setHgrow(netWorthOverlayLabel, Priority.ALWAYS);
        HBox.setHgrow(requirementOverlayLabel, Priority.ALWAYS);
        quarterLabel.setMaxWidth(Double.MAX_VALUE);
        netWorthOverlayLabel.setMaxWidth(Double.MAX_VALUE);
        requirementOverlayLabel.setMaxWidth(Double.MAX_VALUE);
        quarterLabel.setAlignment(Pos.CENTER_LEFT);
        netWorthOverlayLabel.setAlignment(Pos.CENTER);
        requirementOverlayLabel.setAlignment(Pos.CENTER_RIGHT);
        progressOverlay.getChildren().addAll(quarterLabel, netWorthOverlayLabel, requirementOverlayLabel);
        progressBarStack.getChildren().addAll(progressBar, progressOverlay);
        deadlineLabel = new Label();

        buildLayout();
        refreshInfo();

        overlayPane.setPickOnBounds(false);
        overlayPane.setMouseTransparent(true);
        BorderPane mainPane = new BorderPane();
        mainPane.setTop(headerBox);
        mainPane.setCenter(contentGrid);
        getChildren().addAll(mainPane, overlayPane);
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

        UiSoundEffects.installHoverSound(buyButton);
        UiSoundEffects.installHoverSound(sellButton);
        UiSoundEffects.installHoverSound(nextWeekButton);
        UiSoundEffects.installClickSound(buyButton);
        UiSoundEffects.installClickSound(sellButton);
        UiSoundEffects.installClickSound(nextWeekButton);


        buyButton.setOnAction(e -> {
            if (tutorialMode && tutorialOverlay != null) {
                tutorialOverlay.onBuyButtonClicked();
            }
            tradeScreenController.handleBuy(
                stockList,
                quantityField,
                statusLabel,
                (action, stockSymbol, quantity, price, commission, tax, total, onConfirm) ->
                    showTransactionOverlay(action, stockSymbol, quantity, price, commission, tax, total, onConfirm),
                this::onTutorialBuySuccess,
                this::refreshInfo
            );
        });
        sellButton.setOnAction(e -> {
            if (tutorialMode && tutorialOverlay != null) {
                tutorialOverlay.onSellButtonClicked();
            }
            tradeScreenController.handleSell(
                stockList,
                quantityField,
                statusLabel,
                (action, stockSymbol, quantity, price, commission, tax, total, onConfirm) ->
                    showTransactionOverlay(action, stockSymbol, quantity, price, commission, tax, total, onConfirm),
                this::onTutorialSellSuccess,
                this::refreshInfo
            );
        });

        nextWeekButton.setOnAction(e -> {
            GameController.WeekAdvanceResult result = controller.nextWeek();
            if (result.gameOver()) {
                return;
            }
            stockList.refresh();
            refreshInfo();
            updateSelectedStockGraph();
            onTutorialNextWeek();
            if (result.quarterAdvanced()) {
                showQuarterLevelUpOverlay(result);
            }
        });

        searchField = new TextField();
        searchField.setPromptText("Search stocks by symbol or name...");
        searchField.getStyleClass().add("trade-search-field");
        setupSearchFilter();


        Button maxButton = new Button("Max");
        maxButton.getStyleClass().addAll("trade-button", "trade-max-button");
        maxButton.setOnAction(e -> {
            Stock selectedStock = stockList.getSelectionModel().getSelectedItem();
            if (selectedStock != null) {
                BigDecimal cash = controller.getMoney();
                BigDecimal commissionRate = controller.getExchange().getCommissionRate();
                String maxQty = tradeScreenModel.calculateMaxBuyQuantity(selectedStock, cash, commissionRate);
                quantityField.setText(maxQty);
            }
        });

        HBox buysell = new HBox(10, quantityLabel, quantityField, maxButton, buyButton, sellButton);
        buysell.getStyleClass().add("trade-buysell");
        buysell.setPadding(new Insets(10, 0, 0, 0));

        HBox infoBox = new HBox(20, deadlineLabel, weekLabel, cashLabel, statusLabel);
        infoBox.getStyleClass().add("trade-info");
        infoBox.setPadding(new Insets(0, 0, 10, 0));

        progressBar.setPrefHeight(18);
        progressBar.setPrefWidth(900);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(progressBarStack, Priority.ALWAYS);
        HBox progressAndNextWeek = new HBox(12, progressBarStack, nextWeekButton);
        progressAndNextWeek.setAlignment(Pos.CENTER_LEFT);
        progressAndNextWeek.setPadding(new Insets(0, 0, 0, 0));

        headerBox = new VBox(
            8,
            progressAndNextWeek,
            infoBox,
            searchField,
            sectorButtonContainer
        );
        headerBox.getStyleClass().add("trade-header");
        headerBox.setPadding(new Insets(10));

        contentGrid = new GridPane();
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
        VBox graphAndControls = new VBox(10, graph, buysell);
        graphAndControls.setFillWidth(true);
        graphAndControls.setPadding(new Insets(0));
        contentGrid.add(graphAndControls, 1, 0);
    }

    private void showTransactionOverlay(String action, String stockSymbol, BigDecimal quantity, BigDecimal price, BigDecimal commission, BigDecimal tax, BigDecimal total, Runnable onConfirm) {
        if (transactionOverviewOverlay != null) overlayPane.getChildren().remove(transactionOverviewOverlay);
        transactionOverviewOverlay = new TransactionOverview(action, stockSymbol, quantity, price, commission, tax, total, () -> {
            overlayPane.getChildren().remove(transactionOverviewOverlay);
            transactionOverviewOverlay = null;
            updateOverlayInterception();
            onConfirm.run();
        });
        overlayPane.getChildren().add(transactionOverviewOverlay);
        updateOverlayInterception();
    }

    private void showQuarterLevelUpOverlay(GameController.WeekAdvanceResult result) {
        if (quarterLevelUpOverlay != null) {
            overlayPane.getChildren().remove(quarterLevelUpOverlay);
        }

        quarterLevelUpOverlay = new QuarterLevelUpOverlay(
            result.completedQuarter(),
            result.unlockedQuarter(),
            result.clearedNetWorth(),
            result.clearedTarget(),
            result.nextTarget(),
            this::hideQuarterLevelUpOverlay
        );
        overlayPane.getChildren().add(quarterLevelUpOverlay);
        updateOverlayInterception();
    }

    private void hideQuarterLevelUpOverlay() {
        if (quarterLevelUpOverlay == null) {
            return;
        }

        overlayPane.getChildren().remove(quarterLevelUpOverlay);
        quarterLevelUpOverlay = null;
        updateOverlayInterception();
    }

    private void updateOverlayInterception() {
        boolean hasOverlay = !overlayPane.getChildren().isEmpty();
        overlayPane.setPickOnBounds(hasOverlay);
        overlayPane.setMouseTransparent(!hasOverlay);
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
        int week = controller.getWeek();
        weekLabel.setText("Week: " + week);

        var progress = controller.getProgress();
        var player = controller.getPlayer();

        int currentQuarter = progress.getCheckpointLevel();
        deadlineLabel.setText("Deadline in: " + progress.getWeeksUntilDeadline() + " weeks");

        BigDecimal requirement = progress.getCurrentTarget();
        BigDecimal netWorth = player.getNetWorth();

        quarterLabel.setText("Q" + currentQuarter);
        requirementOverlayLabel.setText("Requirement: " + currencyFormat.format(requirement));
        netWorthOverlayLabel.setText(currencyFormat.format(netWorth));

        double progressValue = 0;
        if (requirement.compareTo(BigDecimal.ZERO) > 0) {
            progressValue = netWorth
                .divide(requirement, 4, RoundingMode.HALF_UP)
                .doubleValue();
        }
        progressBar.setProgress(Math.max(0, Math.min(progressValue, 1.0)));
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

    private void setupSearchFilter() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterStockList(newValue);
        });
    }


    private void filterStockList(String searchText) {
        filteredStocks.setAll(tradeScreenModel.filterStocks(searchText));
    }

    private void filterBySectors() {
        filterStockList(searchField.getText());
    }

    private void createSectorFilters() {
        Set<String> sectors = tradeScreenModel.getAllSectors();
        Set<String> allSectorNames = new HashSet<>(sectors);

        Button allButton = new Button("Switch");
        allButton.getStyleClass().addAll("trade-sector-button", "trade-sector-all");
        allButton.setOnAction(e -> {
            if (tradeScreenModel.getSelectedSectors().size() < allSectorNames.size()) {
                tradeScreenModel.selectAllSectors();
            } else {
                tradeScreenModel.clearSelectedSectors();
            }
            updateSectorButtonStyles(allButton);
            filterBySectors();
        });
        sectorButtonContainer.getChildren().add(allButton);

        for (String sector : sectors) {
            Button sectorButton = new Button(sector);
            sectorButton.getStyleClass().add("trade-sector-button");
            sectorButton.setOnAction(e -> {
                if (tradeScreenModel.getSelectedSectors().contains(sector)) {
                    tradeScreenModel.deselectSector(sector);
                    sectorButton.getStyleClass().remove("trade-sector-active");
                } else {
                    tradeScreenModel.selectSector(sector);
                    sectorButton.getStyleClass().add("trade-sector-active");
                }
                updateSectorButtonStyles(allButton);
                filterBySectors();
            });
            sectorButtonContainer.getChildren().add(sectorButton);
        }
    }

    private void updateSectorButtonStyles(Button allButton) {
        if (tradeScreenModel.getSelectedSectors().isEmpty()) {
            allButton.getStyleClass().add("trade-sector-active");
        } else {
            allButton.getStyleClass().remove("trade-sector-active");
        }
    }

}
