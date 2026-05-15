package edu.ntnu.iir.bidata.idatt2003.group09.view.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.controller.GameController;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.model.screen.TradeScreenModel;
import edu.ntnu.iir.bidata.idatt2003.group09.view.elements.StockGraph;
import edu.ntnu.iir.bidata.idatt2003.group09.view.elements.StockListView;
import edu.ntnu.iir.bidata.idatt2003.group09.view.elements.TransactionOverview;
import edu.ntnu.iir.bidata.idatt2003.group09.view.elements.BuyOverview;
import edu.ntnu.iir.bidata.idatt2003.group09.view.elements.SellOverview;
import edu.ntnu.iir.bidata.idatt2003.group09.view.sound.UiSoundEffects;
import edu.ntnu.iir.bidata.idatt2003.group09.view.tutorial.TutorialOverlay;
import javafx.collections.FXCollections;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.scene.Node;

import java.util.function.Consumer;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
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
    private final Runnable onGameOver;
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
    private final HBox sectorButtonContainer;
    private Map<String, Button> sectorButtons = new HashMap<>();
    private final TradeScreenModel tradeScreenModel;
    private final Consumer<TradeScreenModel.TradeEvent> tradeEventListener;


    public TradeScreenView(GameController controller, List<Stock> stocks, Runnable onSaveAndQuit) {
        this(controller, stocks, onSaveAndQuit, null, false, null);
    }

    public TradeScreenView(GameController controller, List<Stock> stocks, Runnable onSaveAndQuit, boolean tutorialMode) {
        this(controller, stocks, onSaveAndQuit, null, tutorialMode, null);
    }

    private final StackPane overlayPane = new StackPane();
    private TransactionOverview transactionOverviewOverlay = null;
    private QuarterLevelUpOverlay quarterLevelUpOverlay = null;
    private VBox headerBox;
    private GridPane contentGrid;

    private ToggleButton allSectorsToggle;
    public TradeScreenView(
        GameController controller,
        List<Stock> stocks,
        Runnable onSaveAndQuit,
        Runnable onGameOver,
        boolean tutorialMode,
        TutorialOverlay tutorialOverlay
    ) {
        this.controller = controller;
        this.tradeScreenModel = new TradeScreenModel(stocks);
        this.tradeScreenController = new TradeScreenController(controller, tradeScreenModel);
        this.onSaveAndQuit = onSaveAndQuit;
        this.onGameOver = onGameOver;
        this.tutorialMode = tutorialMode;
        this.tutorialOverlay = tutorialOverlay;
        this.currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        this.tradeEventListener = event -> {
            if (event == TradeScreenModel.TradeEvent.BUY_SUCCESS) {
                onTutorialBuySuccess();
            } else if (event == TradeScreenModel.TradeEvent.SELL_SUCCESS) {
                onTutorialSellSuccess();
            }
        };

        getStylesheets().add(getClass().getResource("/styling/tradescreen.css").toExternalForm());
        getStyleClass().add("trade-screen");

        stockList = new StockListView().createStockList(controller.getPlayer());
        sectorButtonContainer = new HBox(10);
        sectorButtonContainer.getStyleClass().add("trade-sector-container");
        createSectorFilters();

        stockList.setItems(tradeScreenModel.getFilteredStocks());
        tradeScreenModel.addTradeEventListener(tradeEventListener);

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
        bindModelToView();

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

        Button maxBuyButton = new Button("Max Buy");
        maxBuyButton.getStyleClass().addAll("trade-button", "trade-max-button");
        maxBuyButton.setOnAction(e -> {
            Stock selectedStock = stockList.getSelectionModel().getSelectedItem();
            if (selectedStock != null) {
                String maxQty = tradeScreenController.calculateMaxBuyQuantity(selectedStock);
                quantityField.setText(maxQty);
            }
        });

        Button maxSellButton = new Button("Max Sell");
        maxSellButton.getStyleClass().addAll("trade-button", "trade-max-button");
        maxSellButton.setOnAction(e -> {
            Stock selectedStock = stockList.getSelectionModel().getSelectedItem();
            if (selectedStock != null) {
                String maxQty = tradeScreenController.calculateMaxSellQuantity(selectedStock.getSymbol());
                quantityField.setText(maxQty);
            }
        });

        UiSoundEffects.installHoverSound(buyButton);
        UiSoundEffects.installHoverSound(sellButton);
        UiSoundEffects.installHoverSound(nextWeekButton);
        UiSoundEffects.installHoverSound(maxBuyButton);
        UiSoundEffects.installHoverSound(maxSellButton);
        UiSoundEffects.installClickSound(buyButton);
        UiSoundEffects.installClickSound(sellButton);
        UiSoundEffects.installClickSound(nextWeekButton);
        UiSoundEffects.installClickSound(maxBuyButton);
        UiSoundEffects.installClickSound(maxSellButton);

        buyButton.setOnAction(e -> {
            if (tutorialMode && tutorialOverlay != null) {
                tutorialOverlay.onBuyButtonClicked();
            }
            tradeScreenController.handleBuy(
                stockList,
                quantityField,
                statusLabel,
                (action, stockSymbol, quantity, price, commission, tax, total, onConfirm) ->
                    showTransactionOverlay(action, stockSymbol, quantity, price, commission, tax, total, onConfirm)
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
                    showTransactionOverlay(action, stockSymbol, quantity, price, commission, tax, total, onConfirm)
            );
        });

        nextWeekButton.setOnAction(e -> {
            GameController.WeekAdvanceResult result = tradeScreenController.advanceWeek();
            if (result.gameOver()) {
                if (onGameOver != null) {
                    onGameOver.run();
                }
                return;
            }
            stockList.refresh();
            updateSelectedStockGraph();
            onTutorialNextWeek();
            if (result.quarterAdvanced()) {
                showQuarterLevelUpOverlay(result);
            }
        });


        searchField = new TextField();
        searchField.setPromptText("Search stocks by symbol or name...");
        searchField.getStyleClass().add("trade-search-field");
        searchField.setPrefWidth(350);
        searchField.setMinWidth(250);
        searchField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(searchField, Priority.ALWAYS);
        setupSearchFilter();

        HBox filterBar = new HBox(10, searchField, allSectorsToggle, ownedToggleButton, winnersToggleButton, losersToggleButton);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.setPadding(new Insets(0, 0, 0, 0));
        filterBar.getStyleClass().add("trade-filter-bar");

        HBox buysell = new HBox(5, quantityLabel, quantityField, maxBuyButton, buyButton, sellButton, maxSellButton);
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
            filterBar,
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
        boolean cancelEnabled =
            !tutorialMode
            || tutorialOverlay == null
            || !tutorialOverlay.isAtConfirmationStep();
        if (transactionOverviewOverlay != null) overlayPane.getChildren().remove(transactionOverviewOverlay);
        Runnable onCancel = () -> {
            overlayPane.getChildren().remove(transactionOverviewOverlay);
            transactionOverviewOverlay = null;
            updateOverlayInterception();
        };
        if (action.equalsIgnoreCase("buy")) {
            transactionOverviewOverlay = new BuyOverview(stockSymbol, quantity, price, commission, tax, total, () -> {
                overlayPane.getChildren().remove(transactionOverviewOverlay);
                transactionOverviewOverlay = null;
                updateOverlayInterception();
                onConfirm.run();
            }, onCancel
            , cancelEnabled
            );
        } else if (action.equalsIgnoreCase("sell")) {
            transactionOverviewOverlay = new SellOverview(stockSymbol, quantity, price, commission, tax, total, () -> {
                overlayPane.getChildren().remove(transactionOverviewOverlay);
                transactionOverviewOverlay = null;
                updateOverlayInterception();
                onConfirm.run();
            }, onCancel, cancelEnabled);
        } else {
            throw new IllegalArgumentException("Unknown transaction action: " + action);
        }
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
        Set<String> ownedSymbols = tradeScreenController.getOwnedSymbols();

        tradeScreenModel.applyFilters(
            searchText,
            allSectorsToggle != null && allSectorsToggle.isSelected(),
            filterOwned,
            ownedSymbols,
            winnersToggleButton != null && winnersToggleButton.isSelected(),
            losersToggleButton != null && losersToggleButton.isSelected()
        );
    }

    private void filterBySectors() {
        filterStockList(searchField.getText());
    }

    private ToggleButton winnersToggleButton;
    private ToggleButton losersToggleButton;

    private void createSectorFilters() {
        Set<String> sectors = tradeScreenModel.getAllSectors();

        // Owned toggle button
        ownedToggleButton = new ToggleButton("Owned");
        ownedToggleButton.getStyleClass().add("trade-owned-toggle");
        ownedToggleButton.setOnAction(e -> {
            filterOwned = ownedToggleButton.isSelected();
            filterBySectorsAndOwned();
        });
        // owned toggle is placed in the filter bar, not in the sector container

        // All toggle: selects/deselects all sector filters (use controller to adhere to MVC)
        allSectorsToggle = new ToggleButton("All");
        allSectorsToggle.getStyleClass().add("trade-all-toggle");
        allSectorsToggle.setOnAction(e -> {
            boolean on = allSectorsToggle.isSelected();
            if (on) {
                tradeScreenController.selectAllSectors();
                // mark all sector buttons active
                for (Button b : sectorButtons.values()) {
                    if (!b.getStyleClass().contains("trade-sector-active")) {
                        b.getStyleClass().add("trade-sector-active");
                    }
                }
            } else {
                tradeScreenController.clearSelectedSectors();
                for (Button b : sectorButtons.values()) {
                    b.getStyleClass().remove("trade-sector-active");
                }
            }
            filterBySectorsAndOwned();
        });

        winnersToggleButton = new ToggleButton("Winners");
        winnersToggleButton.getStyleClass().add("trade-winners-toggle");
        winnersToggleButton.setOnAction(e -> {
            if (winnersToggleButton.isSelected()) {
                losersToggleButton.setSelected(false);
            }
            filterBySectorsAndOwned();
        });
        // winners toggle is placed in the filter bar

        losersToggleButton = new ToggleButton("Losers");
        losersToggleButton.getStyleClass().add("trade-losers-toggle");
        losersToggleButton.setOnAction(e -> {
            if (losersToggleButton.isSelected()) {
                winnersToggleButton.setSelected(false);
            }
            filterBySectorsAndOwned();
        });
        // losers toggle is placed in the filter bar

        for (String sector : sectors) {
            Button sectorButton = new Button(sector);
            sectorButton.getStyleClass().add("trade-sector-button");
            sectorButtons.put(sector, sectorButton);
            sectorButton.setOnAction(e -> {
                if (tradeScreenController.getSelectedSectors().contains(sector)) {
                    tradeScreenController.deselectSector(sector);
                    sectorButton.getStyleClass().remove("trade-sector-active");
                } else {
                    tradeScreenController.selectSector(sector);
                    sectorButton.getStyleClass().add("trade-sector-active");
                }
                // update All toggle state: if all sectors are selected, mark All as selected
                ToggleButton allToggle = null;
                for (Node n : sectorButtonContainer.getChildren()) {
                    if (n instanceof ToggleButton tb && "All".equals(tb.getText())) {
                        allToggle = tb;
                        break;
                    }
                }
                if (allToggle != null) {
                    boolean allSelected = tradeScreenController.getSelectedSectors().containsAll(sectors) && !sectors.isEmpty();
                    allToggle.setSelected(allSelected);
                }

                filterBySectorsAndOwned();
            });
            sectorButtonContainer.getChildren().add(sectorButton);
        }
    }

    private boolean filterOwned = false;
    private ToggleButton ownedToggleButton;

    private void filterBySectorsAndOwned() {
        String searchText = searchField != null ? searchField.getText() : "";
        Set<String> ownedSymbols = tradeScreenController.getOwnedSymbols();

        tradeScreenModel.applyFilters(
            searchText,
            allSectorsToggle != null && allSectorsToggle.isSelected(), // was: filterOwned
            filterOwned,
            ownedSymbols,
            winnersToggleButton != null && winnersToggleButton.isSelected(),
            losersToggleButton != null && losersToggleButton.isSelected()
        );
    }

    private void bindModelToView() {
        // Cash
        tradeScreenModel.cashProperty().addListener((obs, oldVal, newVal) -> {
            cashLabel.setText("Cash: " + currencyFormat.format(newVal == null ? BigDecimal.ZERO : newVal));
        });

        // Net worth
        tradeScreenModel.netWorthProperty().addListener((obs, oldVal, newVal) -> {
            netWorthLabel.setText("Net Worth: " + currencyFormat.format(newVal == null ? BigDecimal.ZERO : newVal));
        });

        // Holdings
        tradeScreenModel.holdingsProperty().addListener((obs, oldVal, newVal) -> {
            holdingsLabel.setText("Positions: " + newVal);
        });

        // Week
        tradeScreenModel.weekProperty().addListener((obs, oldVal, newVal) -> {
            weekLabel.setText("Week: " + newVal);
        });

        // Progress and overlays
        tradeScreenModel.quarterLabelProperty().addListener((obs, oldVal, newVal) -> {
            quarterLabel.setText(newVal == null ? "" : newVal);
        });
        tradeScreenModel.requirementOverlayValueProperty().addListener((obs, oldVal, newVal) -> {
            BigDecimal val = newVal == null ? BigDecimal.ZERO : newVal;
            requirementOverlayLabel.setText("Requirement: " + currencyFormat.format(val));
        });
        tradeScreenModel.netWorthOverlayValueProperty().addListener((obs, oldVal, newVal) -> {
            BigDecimal val = newVal == null ? BigDecimal.ZERO : newVal;
            netWorthOverlayLabel.setText(currencyFormat.format(val));
        });
        tradeScreenModel.deadlineLabelProperty().addListener((obs, oldVal, newVal) -> {
            deadlineLabel.setText(newVal == null ? "" : newVal);
        });

        progressBar.progressProperty().bind(tradeScreenModel.progressProperty());
        BigDecimal initCash = tradeScreenModel.cashProperty().get();
        cashLabel.setText("Cash: " + currencyFormat.format(initCash == null ? BigDecimal.ZERO : initCash));

        BigDecimal initNet = tradeScreenModel.netWorthProperty().get();
        netWorthLabel.setText("Net Worth: " + currencyFormat.format(initNet == null ? BigDecimal.ZERO : initNet));

        holdingsLabel.setText("Positions: " + tradeScreenModel.holdingsProperty().get());
        weekLabel.setText("Week: " + tradeScreenModel.weekProperty().get());

        String q = tradeScreenModel.quarterLabelProperty().get();
        quarterLabel.setText(q == null ? "" : q);

        BigDecimal req = tradeScreenModel.requirementOverlayValueProperty().get();
        requirementOverlayLabel.setText("Requirement: " + currencyFormat.format(req == null ? BigDecimal.ZERO : req));

        BigDecimal nw = tradeScreenModel.netWorthOverlayValueProperty().get();
        netWorthOverlayLabel.setText(currencyFormat.format(nw == null ? BigDecimal.ZERO : nw));

        deadlineLabel.setText(tradeScreenModel.deadlineLabelProperty().get());
    }
}