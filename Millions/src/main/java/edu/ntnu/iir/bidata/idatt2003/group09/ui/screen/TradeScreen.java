package edu.ntnu.iir.bidata.idatt2003.group09.ui.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.base.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.base.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.controller.GameController;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.StockGraph;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.StockListView;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.TutorialOverlay;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.UiSoundEffects;
import edu.ntnu.iir.bidata.idatt2003.group09.base.calculator.PurchaseCalculator;
import javafx.collections.FXCollections;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
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
import javafx.collections.ObservableList;

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
    private final Label netWorthOverlayLabel;
    private final StackPane progressBarStack;
    private final ProgressBar progressBar;
    private final Label levelUpLabel;
    private final Label deadlineLabel;
    private int lastLevel = 1;

    private TextField searchField;
    private ObservableList<Stock> allStocks;
    private ObservableList<Stock> filteredStocks;

    private ToggleGroup sectorToggleGroup;
    private final HBox sectorButtonContainer;
    private Set<String> selectedSectors;

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

        this.allStocks = FXCollections.observableArrayList(stocks);
        sectorButtonContainer = new HBox(10);
        sectorButtonContainer.getStyleClass().add("trade-sector-container");
        createSectorFilters(stocks);

        this.filteredStocks = FXCollections.observableArrayList(allStocks);
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

        // Removed levelLabel initialization
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

        UiSoundEffects.installHoverSound(buyButton);
        UiSoundEffects.installHoverSound(sellButton);
        UiSoundEffects.installHoverSound(nextWeekButton);
        UiSoundEffects.installClickSound(buyButton);
        UiSoundEffects.installClickSound(sellButton);
        UiSoundEffects.installClickSound(nextWeekButton);

        buyButton.setOnAction(e -> buySelectedStock());
        sellButton.setOnAction(e -> sellSelectedStock());

        nextWeekButton.setOnAction(e -> {
            controller.nextWeek();
            stockList.refresh();
            refreshInfo();
            updateSelectedStockGraph();
            onTutorialNextWeek();
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
                BigDecimal price = selectedStock.getSalesPrice();
                BigDecimal cash = controller.getMoney();
                if (price.compareTo(BigDecimal.ZERO) > 0 && cash.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal low = BigDecimal.ZERO;
                    BigDecimal high = cash.divide(price, 0, RoundingMode.FLOOR).add(BigDecimal.ONE);
                    BigDecimal best = BigDecimal.ZERO;
                    while (low.compareTo(high) < 0) {
                        BigDecimal mid = low.add(high).divide(new BigDecimal("2"), 0, RoundingMode.FLOOR);
                        if (mid.compareTo(BigDecimal.ZERO) <= 0) {
                            low = mid.add(BigDecimal.ONE);
                            continue;
                        }
                        Share tempShare = new Share(selectedStock, mid, price);
                        PurchaseCalculator calc =
                                new PurchaseCalculator(tempShare);
                        BigDecimal totalCost = calc.calculateTotal();
                        if (totalCost.compareTo(cash) <= 0) {
                            best = mid;
                            low = mid.add(BigDecimal.ONE);
                        } else {
                            high = mid;
                        }
                    }
                    quantityField.setText(best.toPlainString());
                }
            }
        });

        HBox buysell = new HBox(10, quantityLabel, quantityField, maxButton, buyButton, sellButton);
        buysell.getStyleClass().add("trade-buysell");
        buysell.setPadding(new Insets(10, 0, 0, 0));

        HBox infoBox = new HBox(20, deadlineLabel, weekLabel, cashLabel, holdingsLabel, levelUpLabel);
        infoBox.getStyleClass().add("trade-info");
        infoBox.setPadding(new Insets(0, 0, 10, 0));

        HBox controls = new HBox(10, nextWeekButton);
        controls.getStyleClass().add("trade-controls");
        controls.setPadding(new Insets(10, 0, 0, 0));

        // Make progress bar shorter and place next week button next to it
        progressBar.setPrefHeight(18);
        progressBar.setPrefWidth(900);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(progressBarStack, Priority.ALWAYS);
        HBox progressAndNextWeek = new HBox(12, progressBarStack, nextWeekButton);
        progressAndNextWeek.setAlignment(Pos.CENTER_LEFT);
        progressAndNextWeek.setPadding(new Insets(0, 0, 0, 0));

        VBox headerBox = new VBox(
            8,
            progressAndNextWeek,
            infoBox,
            controls,
            statusLabel,
            searchField,
            sectorButtonContainer
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
        // Add graph and controls in a VBox in the right cell
        VBox graphAndControls = new VBox(10, graph, buysell);
        graphAndControls.setFillWidth(true);
        graphAndControls.setPadding(new Insets(0));
        contentGrid.add(graphAndControls, 1, 0);

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

    private void setupSearchFilter() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterStockList(newValue);
        });
    }


    private void filterStockList(String searchText) {
        // First filter by sectors if any are selected
        List<Stock> filteredBySector = allStocks.stream()
            .filter(stock -> {
                if (selectedSectors == null || selectedSectors.isEmpty()) {
                    return true;
                }
                String stockSector = stock.getSector();
                return stockSector != null && selectedSectors.contains(stockSector);
            })
            .collect(Collectors.toList());
        
        // Then filter by search text
        if (searchText == null || searchText.trim().isEmpty()) {
            filteredStocks.setAll(filteredBySector);
            return;
        }

        String lowerCaseSearch = searchText.toLowerCase().trim();
        List<Stock> filtered = filteredBySector.stream()
            .filter(stock -> 
                stock.getSymbol().toLowerCase().contains(lowerCaseSearch) ||
                stock.getCompany().toLowerCase().contains(lowerCaseSearch)
            )
            .collect(Collectors.toList());
        
        filteredStocks.setAll(filtered);
    }

    private Set<String> getAllSectors(List<Stock> stocks) {
        return stocks.stream()
            .map(Stock::getSector)
            .filter(sector -> sector != null && !sector.isEmpty())
            .collect(Collectors.toSet());
    }

    private void filterBySectors() {
        String searchText = searchField.getText();

        // First filter by sectors
        List<Stock> filteredBySector = allStocks.stream()
            .filter(stock -> {
                if (selectedSectors.isEmpty()) {
                    return true; // No sector filter active
                }
                String stockSector = stock.getSector();
                return stockSector != null && selectedSectors.contains(stockSector);
            })
            .collect(Collectors.toList());
        
        // Then filter by search text
        if (searchText != null && !searchText.trim().isEmpty()) {
            String lowerCaseSearch = searchText.toLowerCase().trim();
            filteredBySector = filteredBySector.stream()
                .filter(stock -> 
                    stock.getSymbol().toLowerCase().contains(lowerCaseSearch) ||
                    stock.getCompany().toLowerCase().contains(lowerCaseSearch)
                )
                .collect(Collectors.toList());
        }

        filteredStocks.setAll(filteredBySector);
    }

    private void createSectorFilters(List<Stock> stocks) {
        Set<String> sectors = getAllSectors(stocks);
        selectedSectors = new HashSet<>();
        Set<String> allSectorNames = new HashSet<>(sectors);

        // Add "All" button to reverse all sector filters
        Button allButton = new Button("Switch");
        allButton.getStyleClass().addAll("trade-sector-button", "trade-sector-all");
        allButton.setOnAction(e -> {
            if (selectedSectors.size() < allSectorNames.size()) {
                // Select all
                selectedSectors.clear();
                selectedSectors.addAll(allSectorNames);
            } else {
                // Deselect all
                selectedSectors.clear();
            }
            // Update all sector button styles visually
            for (javafx.scene.Node node : sectorButtonContainer.getChildren()) {
                if (node instanceof Button && node != allButton) {
                    Button btn = (Button) node;
                    String sector = btn.getText();
                    if (selectedSectors.contains(sector)) {
                        if (!btn.getStyleClass().contains("trade-sector-active")) {
                            btn.getStyleClass().add("trade-sector-active");
                        }
                    } else {
                        btn.getStyleClass().remove("trade-sector-active");
                    }
                }
            }
            updateSectorButtonStyles(allButton);
            filterBySectors();
        });
        sectorButtonContainer.getChildren().add(allButton);

        // Create a button for each sector
        for (String sector : sectors) {
            Button sectorButton = new Button(sector);
            sectorButton.getStyleClass().add("trade-sector-button");
            sectorButton.setOnAction(e -> {
                if (selectedSectors.contains(sector)) {
                    selectedSectors.remove(sector);
                    sectorButton.getStyleClass().remove("trade-sector-active");
                } else {
                    selectedSectors.add(sector);
                    sectorButton.getStyleClass().add("trade-sector-active");
                }
                updateSectorButtonStyles(allButton);
                filterBySectors();
            });
            sectorButtonContainer.getChildren().add(sectorButton);
        }
    }

    private void updateSectorButtonStyles(Button allButton) {
        // Update All button style
        if (selectedSectors.isEmpty()) {
            allButton.getStyleClass().add("trade-sector-active");
        } else {
            allButton.getStyleClass().remove("trade-sector-active");
        }
    }

}